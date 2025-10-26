import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import JoinPage from './pages/JoinPage.jsx';
import ContestPage from './pages/ContestPage.jsx';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<JoinPage />} />
        <Route path="/contest/:contestId" element={<ContestPage />} />
      </Routes>
    </Router>
  );
}

export default App;
