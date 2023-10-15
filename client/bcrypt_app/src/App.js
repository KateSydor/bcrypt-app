import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import RegisterForm from './components/RegisterForm';
import SignInForm from "./components/SignInForm";
import BCryptPage from "./components/BCryptPage";
import HistoryPage from "./components/HistoryPage";
import ProgressResultPage from "./components/ProgressResultPage";

function App() {
    return (
        <Router>
            <div className="App">
                <Routes>
                    <Route path="/register" element={<RegisterForm />} />
                    <Route path="/signin" element={<SignInForm />} />
                    <Route path="/history" element={<HistoryPage />} />
                    <Route path="/home" element={<BCryptPage />} />
                    <Route path="/progress" element={<ProgressResultPage />} />
                    <Route path="/" element={<RegisterForm />} />
                </Routes>
            </div>
        </Router>
    );
}

export default App;
