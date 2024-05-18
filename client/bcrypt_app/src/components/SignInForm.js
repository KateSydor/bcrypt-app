import React, { useState } from 'react';
import './RegisterForm.css';
import { useNavigate } from 'react-router-dom';
import CustomAlert from "./CustomAlert";

function SignIn(callback) {
    const [formData, setFormData] = useState({
        username: '',
        password: '',
    });

    const navigate = useNavigate();

    const [showAlert, setShowAlert] = useState(false);

    const [message, setMessage] = useState("");


    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };


    const handleSubmit = (e) => {
        e.preventDefault();

        if (formData.username === '' || formData.password === '') {
            setMessage('Поля не можуть бути порожніми!');
            setShowAlert(true);
            return;
        }

        fetch('http://localhost:8080/user/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log('Sign in response:', data);
                if (data.message === 'User sign in successfully') {
                    sessionStorage.setItem('username', formData.username);
                    sessionStorage.setItem('password', formData.password);
                    navigate('/home');
                }else {
                    setMessage("Некоректні дані");
                    setShowAlert(true);
                }
            })
            .catch((error) => {
                console.error('Sign in error:', error);
            });
    };
    const handleRegister = () => {
        navigate('/register');
    }

    const handleCloseAlert = () => {
        setShowAlert(false);
    };

    return (
        <div className="container">
            <h2>Вхід</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="username"
                    placeholder="Ім я користувача"
                    value={formData.username}
                    onChange={handleChange}
                    className="input-field"
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Пароль"
                    value={formData.password}
                    onChange={handleChange}
                    className="input-field"
                />
                <button type="submit" className="confirm-button">
                    Увійти
                </button>
                {showAlert && (
                    <CustomAlert message={message} onClose={handleCloseAlert} />
                )}
                <button className="have-account" onClick={handleRegister}>В мене немає акаунта</button>
            </form>
        </div>
    );
}

export default SignIn;
