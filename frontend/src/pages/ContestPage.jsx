import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { getContest, getLeaderboard } from '../services/api.jsx';
import ProblemList from '../components/ProblemList.jsx';
import ProblemView from '../components/ProblemView.jsx';
import CodeEditor from '../components/CodeEditor.jsx';
import Leaderboard from '../components/Leaderboard.jsx';

function ContestPage() {
  const { contestId } = useParams();
  const [contest, setContest] = useState(null);
  const [problems, setProblems] = useState([]);
  const [selectedProblem, setSelectedProblem] = useState(null);
  const [leaderboard, setLeaderboard] = useState([]);
  const [loading, setLoading] = useState(true);
  const [user, setUser] = useState(null);

  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
    fetchContestData();
    fetchLeaderboard();

    const leaderboardInterval = setInterval(() => {
      fetchLeaderboard();
    }, 20000);

    return () => clearInterval(leaderboardInterval);
  }, [contestId]);

  const fetchContestData = async () => {
    try {
      const data = await getContest(contestId);
      setContest(data.contest);
      setProblems(data.problems);
      if (data.problems.length > 0) {
        setSelectedProblem(data.problems[0]);
      }
      setLoading(false);
    } catch (error) {
      console.error('Error fetching contest:', error);
      setLoading(false);
    }
  };

  const fetchLeaderboard = async () => {
    try {
      const data = await getLeaderboard(contestId);
      setLeaderboard(data);
    } catch (error) {
      console.error('Error fetching leaderboard:', error);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-slate-900">
        <div className="text-center">
          <div className="animate-spin rounded-full h-16 w-16 border-t-2 border-b-2 border-blue-500 mx-auto mb-4"></div>
          <p className="text-slate-400">Loading contest...</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-slate-900">
      <header className="bg-slate-800 border-b border-slate-700 shadow-lg">
        <div className="max-w-7xl mx-auto px-4 py-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center">
            <div>
              <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-400 to-cyan-400 bg-clip-text text-transparent">
                {contest?.name}
              </h1>
              <p className="text-slate-400 text-sm mt-1">{contest?.description}</p>
            </div>
            {user && (
              <div className="text-right">
                <p className="text-sm text-slate-400">Logged in as</p>
                <p className="font-semibold text-blue-400">{user.username}</p>
              </div>
            )}
          </div>
        </div>
      </header>

      <div className="max-w-7xl mx-auto px-4 py-6 sm:px-6 lg:px-8">
        <div className="grid grid-cols-12 gap-6">
          <div className="col-span-3">
            <ProblemList
              problems={problems}
              selectedProblem={selectedProblem}
              onSelectProblem={setSelectedProblem}
            />
          </div>

          <div className="col-span-6">
            <div className="space-y-6">
              {selectedProblem && (
                <>
                  <ProblemView problem={selectedProblem} />
                  <CodeEditor
                    problem={selectedProblem}
                    contestId={contestId}
                    user={user}
                  />
                </>
              )}
            </div>
          </div>

          <div className="col-span-3">
            <Leaderboard leaderboard={leaderboard} currentUser={user} />
          </div>
        </div>
      </div>
    </div>
  );
}

export default ContestPage;
