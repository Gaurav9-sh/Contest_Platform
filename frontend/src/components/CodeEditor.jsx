import React, { useState, useEffect } from 'react';
import Editor from '@monaco-editor/react';
import { submitCode, getSubmission } from '../services/api.jsx';

const languageTemplates = {
  java: `import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        // Write your code here

    }
}`,
  python: `# Write your code here
`,
  cpp: `#include <iostream>
using namespace std;

int main() {
    // Write your code here

    return 0;
}`,
  c: `#include <stdio.h>

int main() {
    // Write your code here

    return 0;
}`
};

function CodeEditor({ problem, contestId, user }) {
  const [language, setLanguage] = useState('java');
  const [code, setCode] = useState(languageTemplates.java);
  const [submitting, setSubmitting] = useState(false);
  const [submission, setSubmission] = useState(null);
  const [polling, setPolling] = useState(false);

  useEffect(() => {
    setCode(languageTemplates[language]);
    setSubmission(null);
  }, [language, problem]);

  useEffect(() => {
    let interval;
    if (polling && submission) {
      interval = setInterval(async () => {
        try {
          const updatedSubmission = await getSubmission(submission.id);
          setSubmission(updatedSubmission);

          if (
            updatedSubmission.status !== 'PENDING' &&
            updatedSubmission.status !== 'RUNNING'
          ) {
            setPolling(false);
          }
        } catch (error) {
          console.error('Error polling submission:', error);
        }
      }, 2000);
    }

    return () => {
      if (interval) clearInterval(interval);
    };
  }, [polling, submission]);

  const handleSubmit = async () => {
    if (!user) {
      alert('Please login to submit code');
      return;
    }

    setSubmitting(true);
    setSubmission(null);

    try {
      const submissionData = {
        userId: user.id,
        username: user.username,
        contestId: contestId,
        problemId: problem.id,
        code: code,
        language: language,
      };

      const newSubmission = await submitCode(submissionData);
      setSubmission(newSubmission);
      setPolling(true);
    } catch (error) {
      console.error('Error submitting code:', error);
      alert('Failed to submit code. Please try again.');
    } finally {
      setSubmitting(false);
    }
  };

  const getStatusColor = (status) => {
    switch (status) {
      case 'PENDING':
        return 'text-yellow-400';
      case 'RUNNING':
        return 'text-blue-400';
      case 'ACCEPTED':
        return 'text-green-400';
      case 'WRONG_ANSWER':
        return 'text-red-400';
      case 'ERROR':
      case 'COMPILATION_ERROR':
      case 'TIME_LIMIT_EXCEEDED':
        return 'text-red-400';
      default:
        return 'text-slate-400';
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'PENDING':
      case 'RUNNING':
        return '⏳';
      case 'ACCEPTED':
        return '✓';
      case 'WRONG_ANSWER':
      case 'ERROR':
      case 'COMPILATION_ERROR':
      case 'TIME_LIMIT_EXCEEDED':
        return '✗';
      default:
        return '';
    }
  };

  return (
    <div className="bg-slate-800 rounded-xl shadow-xl border border-slate-700 overflow-hidden">
      <div className="bg-slate-700 px-6 py-3 flex items-center justify-between border-b border-slate-600">
        <div className="flex items-center gap-4">
          <h3 className="text-sm font-semibold text-slate-200">Code Editor</h3>
          <select
            value={language}
            onChange={(e) => setLanguage(e.target.value)}
            className="bg-slate-600 text-slate-200 px-3 py-1.5 rounded-lg text-sm border border-slate-500 focus:outline-none focus:ring-2 focus:ring-blue-500"
          >
            <option value="java">Java</option>
            <option value="python">Python</option>
            <option value="cpp">C++</option>
            <option value="c">C</option>
          </select>
        </div>

        <button
          onClick={handleSubmit}
          disabled={submitting || polling}
          className="bg-gradient-to-r from-green-500 to-emerald-500 text-white px-6 py-2 rounded-lg font-semibold hover:from-green-600 hover:to-emerald-600 focus:outline-none focus:ring-2 focus:ring-green-500 disabled:opacity-50 disabled:cursor-not-allowed transition duration-200 shadow-lg"
        >
          {submitting ? 'Submitting...' : polling ? 'Judging...' : 'Submit Code'}
        </button>
      </div>

      <div className="h-96">
        <Editor
          height="100%"
          language={language === 'cpp' ? 'cpp' : language === 'c' ? 'c' : language}
          value={code}
          onChange={(value) => setCode(value)}
          theme="vs-dark"
          options={{
            minimap: { enabled: false },
            fontSize: 14,
            lineNumbers: 'on',
            scrollBeyondLastLine: false,
            automaticLayout: true,
            tabSize: 4,
          }}
        />
      </div>

      {submission && (
        <div className="p-6 border-t border-slate-700 bg-slate-750">
          <h4 className="text-sm font-semibold text-slate-200 mb-3">
            Submission Result
          </h4>

          <div className="bg-slate-900 rounded-lg p-4 border border-slate-700">
            <div className="flex items-center justify-between mb-3">
              <div className="flex items-center gap-2">
                <span className="text-2xl">{getStatusIcon(submission.status)}</span>
                <span className={`font-semibold ${getStatusColor(submission.status)}`}>
                  {submission.status.replace(/_/g, ' ')}
                </span>
              </div>
              <div className="text-slate-400 text-sm">
                Score: <span className="text-blue-400 font-semibold">{submission.score}</span> / {problem.marks}
              </div>
            </div>

            {submission.result && (
              <p className="text-slate-300 text-sm mb-3">{submission.result}</p>
            )}

            {submission.testCaseResults && submission.testCaseResults.length > 0 && (
              <div className="mt-4">
                <p className="text-xs font-semibold text-slate-400 mb-2">
                  Test Cases: {submission.passedTestCases} / {submission.totalTestCases} passed
                </p>
                <div className="space-y-2 max-h-48 overflow-y-auto">
                  {submission.testCaseResults.map((result, index) => (
                    <div
                      key={index}
                      className={`p-3 rounded-lg text-sm ${
                        result.passed
                          ? 'bg-green-900/20 border border-green-700'
                          : 'bg-red-900/20 border border-red-700'
                      }`}
                    >
                      <div className="flex items-center justify-between mb-2">
                        <span className={result.passed ? 'text-green-400' : 'text-red-400'}>
                          Test Case #{result.testCaseNumber}
                        </span>
                        <span className={result.passed ? 'text-green-400' : 'text-red-400'}>
                          {result.passed ? '✓ Passed' : '✗ Failed'}
                        </span>
                      </div>
                      {!result.passed && (
                        <div className="text-xs space-y-1">
                          {result.error && (
                            <p className="text-red-300">Error: {result.error}</p>
                          )}
                          {result.actualOutput && (
                            <p className="text-slate-400">
                              Your output: <span className="text-red-300">{result.actualOutput}</span>
                            </p>
                          )}
                          {result.expectedOutput && (
                            <p className="text-slate-400">
                              Expected: <span className="text-green-300">{result.expectedOutput}</span>
                            </p>
                          )}
                        </div>
                      )}
                    </div>
                  ))}
                </div>
              </div>
            )}
          </div>
        </div>
      )}
    </div>
  );
}

export default CodeEditor;
