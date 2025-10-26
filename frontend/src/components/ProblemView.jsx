import React from 'react';

function ProblemView({ problem }) {
  return (
    <div className="bg-slate-800 rounded-xl shadow-xl border border-slate-700 overflow-hidden">
      <div className="bg-gradient-to-r from-blue-600 to-cyan-600 px-6 py-4">
        <h2 className="text-xl font-bold text-white">{problem.title}</h2>
        <div className="flex items-center gap-4 mt-2">
          <span className="text-sm text-blue-100">{problem.difficulty}</span>
          <span className="text-sm text-blue-100">{problem.marks} points</span>
        </div>
      </div>

      <div className="p-6">
        <div className="prose prose-invert max-w-none">
          <div className="text-slate-300 leading-relaxed whitespace-pre-wrap">
            {problem.description}
          </div>
        </div>

        {problem.testCases && problem.testCases.length > 0 && (
          <div className="mt-6">
            <h3 className="text-lg font-semibold text-slate-200 mb-3">
              Sample Test Cases
            </h3>
            <div className="space-y-4">
              {problem.testCases.map((testCase, index) => (
                <div
                  key={index}
                  className="bg-slate-900 rounded-lg p-4 border border-slate-700"
                >
                  <div className="mb-3">
                    <p className="text-xs font-semibold text-slate-400 mb-1">
                      INPUT
                    </p>
                    <pre className="text-sm text-green-400 font-mono">
                      {testCase.input}
                    </pre>
                  </div>
                  <div>
                    <p className="text-xs font-semibold text-slate-400 mb-1">
                      EXPECTED OUTPUT
                    </p>
                    <pre className="text-sm text-blue-400 font-mono">
                      {testCase.expectedOutput}
                    </pre>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default ProblemView;
