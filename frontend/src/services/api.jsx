import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

export const joinContest = async (username, email) => {
  const response = await api.post('/users/join', { username, email });
  return response.data;
};

export const getContest = async (contestId) => {
  const response = await api.get(`/contests/${contestId}`);
  return response.data;
};

export const submitCode = async (submissionData) => {
  const response = await api.post('/submissions', submissionData);
  return response.data;
};

export const getSubmission = async (submissionId) => {
  const response = await api.get(`/submissions/${submissionId}`);
  return response.data;
};

export const getLeaderboard = async (contestId) => {
  const response = await api.get(`/contests/${contestId}/leaderboard`);
  return response.data;
};

export default api;
