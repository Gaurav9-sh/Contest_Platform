import React from 'react';

function ProblemList({ problems, selectedProblem, onSelectProblem }) {
  const getDifficultyColor = (difficulty) => {
    switch (difficulty?.toLowerCase()) {
      case 'easy':
        return 'text-green-400';
      case 'medium':
        return 'text-yellow-400';
      case 'hard':
        return 'text-red-400';
      default:
        return 'text-slate-400';
    }
  };

  return (
    <div className="bg-slate-800 rounded-xl shadow-xl border border-slate-700 overflow-hidden">
      <div className="bg-gradient-to-r from-blue-600 to-cyan-600 px-4 py-3">
        <h2 className="text-lg font-semibold text-white">Problems</h2>
      </div>

      <div className="p-2">
        {problems.map((problem, index) => (
          <button
            key={problem.id}
            onClick={() => onSelectProblem(problem)}
            className={`w-full text-left px-4 py-3 rounded-lg mb-2 transition duration-200 ${
              selectedProblem?.id === problem.id
                ? 'bg-blue-600 text-white shadow-lg'
                : 'bg-slate-700 text-slate-300 hover:bg-slate-600'
            }`}
          >
            <div className="flex items-start justify-between">
              <div className="flex-1">
                <div className="flex items-center gap-2">
                  <span className="font-semibold text-sm">
                    {index + 1}. {problem.title}
                  </span>
                </div>
                <div className="flex items-center gap-3 mt-1">
                  <span className={`text-xs font-medium ${getDifficultyColor(problem.difficulty)}`}>
                    {problem.difficulty}
                  </span>
                  <span className="text-xs text-slate-400">
                    {problem.marks} pts
                  </span>
                </div>
              </div>
            </div>
          </button>
        ))}
      </div>
    </div>
  );
}

export default ProblemList;
