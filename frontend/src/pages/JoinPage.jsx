import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { joinContest } from '../services/api.jsx';

function JoinPage() {
  const [formData, setFormData] = useState({
    contestId: '',
    username: '',
    email: '',
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      const user = await joinContest(formData.username, formData.email);
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('contestId', formData.contestId);
      navigate(`/contest/${formData.contestId}`);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to join contest. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-slate-900 via-slate-800 to-slate-900 px-4">
      <div className="max-w-md w-full">
        <div className="text-center mb-8">
          <h1 className="text-5xl font-bold mb-4 bg-gradient-to-r from-blue-400 to-cyan-400 bg-clip-text text-transparent">
            CodeArena
          </h1>
          <p className="text-slate-400 text-lg">Join the coding challenge</p>
        </div>

        <div className="bg-slate-800 rounded-2xl shadow-2xl p-8 border border-slate-700">
          <form onSubmit={handleSubmit} className="space-y-6">
            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">
                Contest ID
              </label>
              <input
                type="text"
                name="contestId"
                value={formData.contestId}
                onChange={handleChange}
                required
                className="w-full px-4 py-3 bg-slate-900 border border-slate-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-slate-100 transition duration-200"
                placeholder="Enter contest ID"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">
                Username
              </label>
              <input
                type="text"
                name="username"
                value={formData.username}
                onChange={handleChange}
                required
                className="w-full px-4 py-3 bg-slate-900 border border-slate-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-slate-100 transition duration-200"
                placeholder="Choose a username"
              />
            </div>

            <div>
              <label className="block text-sm font-medium text-slate-300 mb-2">
                Email
              </label>
              <input
                type="email"
                name="email"
                value={formData.email}
                onChange={handleChange}
                required
                className="w-full px-4 py-3 bg-slate-900 border border-slate-600 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent text-slate-100 transition duration-200"
                placeholder="your@email.com"
              />
            </div>

            {error && (
              <div className="bg-red-900/30 border border-red-500 text-red-400 px-4 py-3 rounded-lg">
                {error}
              </div>
            )}

            <button
              type="submit"
              disabled={loading}
              className="w-full bg-gradient-to-r from-blue-500 to-cyan-500 text-white font-semibold py-3 px-6 rounded-lg hover:from-blue-600 hover:to-cyan-600 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-slate-800 transition duration-200 disabled:opacity-50 disabled:cursor-not-allowed shadow-lg"
            >
              {loading ? 'Joining...' : 'Join Contest'}
            </button>
          </form>
        </div>

        <div className="mt-6 text-center text-slate-500 text-sm">
          <p>Ready to showcase your coding skills?</p>
        </div>
      </div>
    </div>
  );
}

export default JoinPage;
