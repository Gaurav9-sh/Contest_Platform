package com.codeplatform.config;

import com.codeplatform.model.Contest;
import com.codeplatform.model.Problem;
import com.codeplatform.model.TestCase;
import com.codeplatform.repository.ContestRepository;
import com.codeplatform.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final ContestRepository contestRepository;
    private final ProblemRepository problemRepository;

    @Override
    public void run(String... args) {
        if (contestRepository.count() == 0) {
            log.info("Initializing sample contest data...");
            initializeData();
            log.info("Sample data initialized successfully!");
        }
    }

    private void initializeData() {
        Contest contest = new Contest();
        contest.setName("Summer Coding Challenge 2024");
        contest.setDescription("Test your coding skills with these exciting problems!");
        contest.setStartTime(LocalDateTime.now().minusHours(1));
        contest.setEndTime(LocalDateTime.now().plusHours(3));
        contest.setProblemIds(new ArrayList<>());
        contest = contestRepository.save(contest);

        Problem problem1 = createProblem1(contest.getId());
        Problem problem2 = createProblem2(contest.getId());
        Problem problem3 = createProblem3(contest.getId());

        problem1 = problemRepository.save(problem1);
        problem2 = problemRepository.save(problem2);
        problem3 = problemRepository.save(problem3);

        contest.setProblemIds(Arrays.asList(problem1.getId(), problem2.getId(), problem3.getId()));
        contestRepository.save(contest);
    }

    private Problem createProblem1(String contestId) {
        Problem problem = new Problem();
        problem.setContestId(contestId);
        problem.setTitle("Sum of Two Numbers");
        problem.setDescription(
            "Write a program that reads two integers from input and prints their sum.\n\n" +
            "**Input Format:**\n" +
            "Two integers separated by space\n\n" +
            "**Output Format:**\n" +
            "Single integer representing the sum\n\n" +
            "**Example:**\n" +
            "Input: 5 3\n" +
            "Output: 8"
        );
        problem.setMarks(10);
        problem.setDifficulty("Easy");

        List<TestCase> testCases = new ArrayList<>();
        testCases.add(new TestCase("5 3", "8", true));
        testCases.add(new TestCase("10 20", "30", true));
        testCases.add(new TestCase("100 200", "300", false));
        testCases.add(new TestCase("-5 5", "0", false));
        testCases.add(new TestCase("0 0", "0", false));

        problem.setTestCases(testCases);
        return problem;
    }

    private Problem createProblem2(String contestId) {
        Problem problem = new Problem();
        problem.setContestId(contestId);
        problem.setTitle("Even or Odd");
        problem.setDescription(
            "Write a program that reads an integer and prints 'Even' if it's even, or 'Odd' if it's odd.\n\n" +
            "**Input Format:**\n" +
            "Single integer\n\n" +
            "**Output Format:**\n" +
            "'Even' or 'Odd'\n\n" +
            "**Example:**\n" +
            "Input: 4\n" +
            "Output: Even"
        );
        problem.setMarks(15);
        problem.setDifficulty("Easy");

        List<TestCase> testCases = new ArrayList<>();
        testCases.add(new TestCase("4", "Even", true));
        testCases.add(new TestCase("7", "Odd", true));
        testCases.add(new TestCase("0", "Even", false));
        testCases.add(new TestCase("100", "Even", false));
        testCases.add(new TestCase("999", "Odd", false));

        problem.setTestCases(testCases);
        return problem;
    }

    private Problem createProblem3(String contestId) {
        Problem problem = new Problem();
        problem.setContestId(contestId);
        problem.setTitle("Factorial");
        problem.setDescription(
            "Write a program that reads a non-negative integer N and prints its factorial.\n\n" +
            "**Input Format:**\n" +
            "Single non-negative integer N (0 <= N <= 12)\n\n" +
            "**Output Format:**\n" +
            "Factorial of N\n\n" +
            "**Example:**\n" +
            "Input: 5\n" +
            "Output: 120\n\n" +
            "Note: 5! = 5 × 4 × 3 × 2 × 1 = 120"
        );
        problem.setMarks(25);
        problem.setDifficulty("Medium");

        List<TestCase> testCases = new ArrayList<>();
        testCases.add(new TestCase("5", "120", true));
        testCases.add(new TestCase("0", "1", true));
        testCases.add(new TestCase("1", "1", false));
        testCases.add(new TestCase("3", "6", false));
        testCases.add(new TestCase("10", "3628800", false));

        problem.setTestCases(testCases);
        return problem;
    }
}
