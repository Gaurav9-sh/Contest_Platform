import React from 'react';

function Leaderboard({ leaderboard, currentUser }) {
  const getMedalEmoji = (rank) => {
    switch (rank) {
      case 1:
        return '🥇';
      case 2:
        return '🥈';
      case 3:
        return '🥉';
      default:
        return null;
    }
  };

  return (
    <div className="bg-slate-800 rounded-xl shadow-xl border border-slate-700 overflow-hidden sticky top-6">
      <div className="bg-gradient-to-r from-yellow-600 to-orange-600 px-4 py-3">
        <h2 className="text-lg font-semibold text-white">Leaderboard</h2>
      </div>

      <div className="p-2 max-h-[calc(100vh-200px)] overflow-y-auto">
        {leaderboard.length === 0 ? (
          <div className="text-center py-8 text-slate-400">
            <p>No submissions yet</p>
            <p className="text-sm mt-2">Be the first to submit!</p>
          </div>
        ) : (
          <div className="space-y-2">
            {leaderboard.map((entry) => {
              const isCurrentUser = currentUser && entry.userId === currentUser.id;
              const medal = getMedalEmoji(entry.rank);

              return (
                <div
                  key={entry.userId}
                  className={`p-3 rounded-lg transition duration-200 ${
                    isCurrentUser
                      ? 'bg-blue-600 shadow-lg border-2 border-blue-400'
                      : 'bg-slate-700 hover:bg-slate-600'
                  }`}
                >
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-3">
                      <div
                        className={`w-8 h-8 rounded-full flex items-center justify-center font-bold text-sm ${
                          isCurrentUser
                            ? 'bg-blue-500 text-white'
                            : entry.rank <= 3
                            ? 'bg-gradient-to-br from-yellow-400 to-orange-500 text-white'
                            : 'bg-slate-600 text-slate-300'
                        }`}
                      >
                        {medal || entry.rank}
                      </div>
                      <div>
                        <p
                          className={`font-semibold text-sm ${
                            isCurrentUser ? 'text-white' : 'text-slate-200'
                          }`}
                        >
                          {entry.username}
                          {isCurrentUser && (
                            <span className="ml-2 text-xs bg-blue-500 px-2 py-0.5 rounded-full">
                              You
                            </span>
                          )}
                        </p>
                        <p className="text-xs text-slate-400">
                          {entry.problemsSolved} solved
                        </p>
                      </div>
                    </div>
                    <div className="text-right">
                      <p
                        className={`font-bold ${
                          isCurrentUser ? 'text-white' : 'text-blue-400'
                        }`}
                      >
                        {entry.totalScore}
                      </p>
                      <p className="text-xs text-slate-400">points</p>
                    </div>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>

      <div className="px-4 py-3 bg-slate-700 border-t border-slate-600">
        <p className="text-xs text-slate-400 text-center">
          Updates every 20 seconds
        </p>
      </div>
    </div>
  );
}

export default Leaderboard;
