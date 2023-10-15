import React, { useState } from 'react';
import './BCryptPage.css';
import {useNavigate} from "react-router-dom";
import CustomAlert from "./CustomAlert";


function YourPage() {
    const [passwords, setPasswords] = useState(['']);
    const [rounds, setRounds] = useState(10);
    const [showAlert, setShowAlert] = useState(false);
    const [message, setMessage] = useState("");

    const addPasswordField = () => {
        if (passwords.length < 10) {
            setPasswords([...passwords, '']);
        } else {
            setMessage("You can\'t add more then 10 passwords!");
            setShowAlert(true);
        }
    };

    const handlePasswordChange = (index, value) => {
        const updatedPasswords = [...passwords];
        updatedPasswords[index] = value;
        setPasswords(updatedPasswords);
    };

    const user_name = sessionStorage.getItem('username');
    const password = sessionStorage.getItem('password');

    const base64Credentials = btoa(user_name + ':' + password);
    const handleConfirm = () => {

        for (let p of passwords){
            if (p === ''){
                setMessage('Fields cannot be empty');
                setShowAlert(true);
                return;
            }
        }
        const requestBody = {
            originalPasswords: passwords,
            rounds: rounds, // Ensure rounds is an integer
        };

        const requestOptions = {
            method: 'POST',
            headers: {
                Authorization: 'Basic '+base64Credentials,
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody),
        };

        fetch('http://localhost:8080/task/res', requestOptions)
            .then((response) => response.json())
            .then((result) => {
                if(result.message === "Request limit exceeded!"){
                    setMessage('Request limit exceeded! Wait for finishing one of your previous request');
                    setShowAlert(true);
                    return;
                }
                console.log('Fetch result:', result);
                sessionStorage.setItem('taskId', result.taskId);
                navigate('/progress');
            })
            .catch((error) => console.error('Fetch error:', error));
    };
    const navigate = useNavigate();

    const handleHistory = () => {
        navigate('/history');
    }

    const handleBCrypt = () => {
        navigate('/home');
    }

    const handleLogOut = () => {
        sessionStorage.setItem("username", null);
        sessionStorage.setItem("password", null);
        navigate('/signin');
    }

    const handleCloseAlert = () => {
        setShowAlert(false);
    };

    return (
        <div className="bcrypt-page">
            <div>
                <div className="test">
                    <br />
                    <span className="title-text">Bcrypt Hash Generator & Verifier</span>
                    <br />
                    <span className="title-text-description">Generate password hashes with bcrypt algorithm</span>
                </div>
            </div>

            <header>
                <div className="header-content">
                    <button className="header-button" onClick={handleBCrypt}>BCrypt</button>
                    <button className="header-button" onClick={handleHistory}>History</button>
                    <button className="header-button" onClick={handleLogOut}>Log Out</button>
                    <span className="username">Username: {user_name}</span>
                </div>
            </header>

            <main className="home-page">
                <div>
                    <p className="main-text">Bcrypt Hash Generator</p>
                </div>
                <div className="password-inputs">
                    {passwords.map((password, index) => (
                        <input
                            key={index}
                            type="text"
                            placeholder="Password"
                            value={password}
                            onChange={(e) => handlePasswordChange(index, e.target.value)}
                        />
                    ))}
                    <button className="confirm-button" onClick={addPasswordField}>
                        Add Password
                    </button>
                </div>
                <div className="rounds-input">
                    <label htmlFor="rounds">Rounds:</label>
                    <select
                        id="rounds"
                        value={rounds}
                        onChange={(e) => setRounds(parseInt(e.target.value))}
                        onInput={(e) => e.preventDefault()}
                    >
                        {Array.from({ length: 27 }, (_, i) => i + 4).map((round) => (
                            <option key={round} value={round}>
                                {round}
                            </option>
                        ))}
                    </select>
                </div>

                <button className="confirm-button" onClick={handleConfirm}>
                    Confirm
                </button>
                {showAlert && (
                    <CustomAlert message={message} onClose={handleCloseAlert} />
                )}
            </main>
        </div>
    );
}

export default YourPage;
