package com.codeplatform.service;

import com.codeplatform.model.Problem;
import com.codeplatform.model.Submission;
import com.codeplatform.model.TestCase;
import com.codeplatform.model.TestCaseResult;
import com.codeplatform.repository.ProblemRepository;
import com.codeplatform.repository.SubmissionRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import com.github.dockerjava.core.DefaultDockerClientConfig;
// Import the new required classes
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
// Import the new required class
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class JudgeService {

    private final SubmissionRepository submissionRepository;
    private final ProblemRepository problemRepository;
    private final DockerClient dockerClient;

    @Value("${judge.docker.image}")
    private String dockerImage;

    @Value("${judge.timeout.seconds}")
    private int timeoutSeconds;

    @Value("${judge.memory.limit}")
    private String memoryLimit;

    @Value("${judge.temp.dir}")
    private String tempDir;

    public JudgeService(SubmissionRepository submissionRepository, ProblemRepository problemRepository) {
        this.submissionRepository = submissionRepository;
        this.problemRepository = problemRepository;

        // --- START DOCKER CLIENT FIX ---

        // 1. Get the default config (finds npipe on Windows or sock on Linux)
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder().build();

        // 2. Build an Apache HttpClient-based transport
        // This client supports npipe:// on Windows and /var/run/docker.sock on Linux
        ApacheDockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        // 3. Build the DockerClient with the new transport
        this.dockerClient = DockerClientImpl.getInstance(config, httpClient);

        // --- END DOCKER CLIENT FIX ---
    }

    @Async
    public void judgeSubmission(String submissionId) {
        log.info("Starting judgment for submission: {}", submissionId);

        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

        Problem problem = problemRepository.findById(submission.getProblemId())
                .orElseThrow(() -> new RuntimeException("Problem not found"));

        submission.setStatus(Submission.SubmissionStatus.RUNNING);
        submissionRepository.save(submission);

        try {
            List<TestCaseResult> results = runTests(submission, problem);
            submission.setTestCaseResults(results);

            long passedTests = results.stream().filter(TestCaseResult::isPassed).count();
            submission.setPassedTestCases((int) passedTests);

            if (passedTests == results.size()) {
                submission.setStatus(Submission.SubmissionStatus.ACCEPTED);
                submission.setScore(problem.getMarks());
                submission.setResult("All test cases passed!");
            } else {
                submission.setStatus(Submission.SubmissionStatus.WRONG_ANSWER);
                submission.setScore((int) ((passedTests * problem.getMarks()) / results.size()));
                submission.setResult(String.format("Passed %d out of %d test cases", passedTests, results.size()));
            }
        } catch (Exception e) {
            log.error("Error during judgment: ", e);
            submission.setStatus(Submission.SubmissionStatus.ERROR);
            submission.setResult("Error: " + e.getMessage());
            submission.setScore(0);
        }

        submission.setCompletedAt(LocalDateTime.now());
        submissionRepository.save(submission);

        log.info("Judgment completed for submission: {}", submissionId);
    }

    private List<TestCaseResult> runTests(Submission submission, Problem problem) throws Exception {
        List<TestCaseResult> results = new ArrayList<>();
        String language = submission.getLanguage().toLowerCase();

        for (int i = 0; i < problem.getTestCases().size(); i++) {
            TestCase testCase = problem.getTestCases().get(i);
            TestCaseResult result = runSingleTest(submission.getCode(), language, testCase, i + 1);
            results.add(result);
        }

        return results;
    }

    private TestCaseResult runSingleTest(String code, String language, TestCase testCase, int testNumber) {
        TestCaseResult result = new TestCaseResult();
        result.setTestCaseNumber(testNumber);
        result.setExpectedOutput(testCase.getExpectedOutput().trim());

        String executionId = UUID.randomUUID().toString();
        Path workDir = null;

        try {
            workDir = Files.createTempDirectory(Path.of(tempDir), "judge-" + executionId);
            String fileName = getFileName(language);
            Path codeFile = workDir.resolve(fileName);
            Files.writeString(codeFile, code);

            if (testCase.getInput() != null && !testCase.getInput().isEmpty()) {
                Path inputFile = workDir.resolve("input.txt");
                Files.writeString(inputFile, testCase.getInput());
            }

            String containerId = createAndStartContainer(workDir, language);

            boolean completed = dockerClient.waitContainerCmd(containerId)
                    .exec(new WaitContainerResultCallback())
                    .awaitCompletion(timeoutSeconds, TimeUnit.SECONDS);

            if (!completed) {
                result.setPassed(false);
                result.setError("Time Limit Exceeded");
                dockerClient.killContainerCmd(containerId).exec();
            } else {
                String output = getContainerOutput(containerId);
                result.setActualOutput(output.trim());
                result.setPassed(output.trim().equals(testCase.getExpectedOutput().trim()));
            }

            dockerClient.removeContainerCmd(containerId).withForce(true).exec();

        } catch (Exception e) {
            log.error("Error executing test case: ", e);
            result.setPassed(false);
            result.setError("Execution error: " + e.getMessage());
        } finally {
            if (workDir != null) {
                try {
                    deleteDirectory(workDir.toFile());
                } catch (Exception e) {
                    log.error("Error cleaning up work directory: ", e);
                }
            }
        }

        return result;
    }

    private String createAndStartContainer(Path workDir, String language) throws Exception {
        Volume volume = new Volume("/code");
        HostConfig hostConfig = HostConfig.newHostConfig()
                .withBinds(new Bind(workDir.toString(), volume))
                .withMemory(parseMemoryLimit(memoryLimit))
                .withNetworkMode("none");

        String[] cmd = getExecutionCommand(language);

        CreateContainerResponse container = dockerClient.createContainerCmd(dockerImage)
                .withHostConfig(hostConfig)
                .withVolumes(volume)
                .withWorkingDir("/code")
                .withCmd(cmd)
                .withAttachStdout(true)
                .withAttachStderr(true)
                .exec();

        dockerClient.startContainerCmd(container.getId()).exec();

        return container.getId();
    }

    private String getContainerOutput(String containerId) {
        StringBuilder output = new StringBuilder();

        try {
            dockerClient.logContainerCmd(containerId)
                    .withStdOut(true)
                    .withStdErr(true)
                    .withFollowStream(true)
                    .exec(new com.github.dockerjava.api.async.ResultCallback.Adapter<Frame>() {
                        @Override
                        public void onNext(Frame frame) {
                            output.append(new String(frame.getPayload()));
                        }
                    }).awaitCompletion();
        } catch (Exception e) {
            log.error("Error getting container output: ", e);
        }

        return output.toString();
    }

    private String getFileName(String language) {
        return switch (language) {
            case "java" -> "Solution.java";
            case "python" -> "solution.py";
            case "cpp" -> "solution.cpp";
            case "c" -> "solution.c";
            default -> "solution.txt";
        };
    }

    private String[] getExecutionCommand(String language) {
        return switch (language) {
            case "java" -> new String[]{"sh", "-c", "javac Solution.java && java Solution < input.txt"};
            case "python" -> new String[]{"python3", "solution.py < input.txt"};
            case "cpp" -> new String[]{"sh", "-c", "g++ solution.cpp -o solution && ./solution < input.txt"};
            case "c" -> new String[]{"sh", "-c", "gcc solution.c -o solution && ./solution < input.txt"};
            default -> throw new RuntimeException("Unsupported language: " + language);
        };
    }

    private long parseMemoryLimit(String memoryLimit) {
        String value = memoryLimit.replaceAll("[^0-9]", "");
        long bytes = Long.parseLong(value);
        if (memoryLimit.toLowerCase().contains("m")) bytes *= 1024 * 1024;
        else if (memoryLimit.toLowerCase().contains("g")) bytes *= 1024 * 1024 * 1024;
        return bytes;
    }

    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) deleteDirectory(file);
                else file.delete();
            }
        }
        directory.delete();
    }
}