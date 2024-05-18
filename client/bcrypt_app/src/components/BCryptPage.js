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
            setMessage("Ви не можете додати більше 10 паролів!");
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
                setMessage('Поля не можуть бути порожніми');
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
                    setMessage('Ліміт запитів перевищено! Дочекайтеся завершення одного з ваших попередніх запитів');
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
                    <br/>
                    <span className="title-text">Генератор і верифікатор хешу Bcrypt</span>
                    <br/>
                    <span className="title-text-description">Генеруйте хеші паролів за допомогою алгоритму bcrypt</span>
                </div>
            </div>

            <header>
                <div className="header-content">
                    <button className="header-button" onClick={handleBCrypt}>BCrypt</button>
                    <button className="header-button" onClick={handleHistory}>Історія</button>
                    <button className="header-button" onClick={handleLogOut}>Вихід</button>
                    <span className="username">Ім'я користуваа: {user_name}</span>
                </div>
            </header>

            <main className="home-page">
                <div>
                    <p className="main-text">Bcrypt генератор</p>
                </div>
                <div className="password-inputs">
                    {passwords.map((password, index) => (
                        <input
                            key={index}
                            type="text"
                            placeholder="Пароль"
                            value={password}
                            onChange={(e) => handlePasswordChange(index, e.target.value)}
                        />
                    ))}
                    <button className="confirm-button" onClick={addPasswordField}>
                        Додати пароль
                    </button>
                </div>
                <div className="rounds-input">
                    <label htmlFor="rounds">Раунди:</label>
                    <select
                        id="rounds"
                        value={rounds}
                        onChange={(e) => setRounds(parseInt(e.target.value))}
                        onInput={(e) => e.preventDefault()}
                    >
                        {Array.from({length: 27}, (_, i) => i + 4).map((round) => (
                            <option key={round} value={round}>
                                {round}
                            </option>
                        ))}
                    </select>
                </div>

                <button className="confirm-button" onClick={handleConfirm}>
                    Підтвердити
                </button>
                {showAlert && (
                    <CustomAlert message={message} onClose={handleCloseAlert}/>
                )}
            </main>
        </div>
    );
}

export default YourPage;
