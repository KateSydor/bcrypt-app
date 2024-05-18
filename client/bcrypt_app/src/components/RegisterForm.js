import React, { useState } from 'react';
import './RegisterForm.css';
import { useNavigate } from 'react-router-dom';
import CustomAlert from "./CustomAlert";

function RegisterForm(callback) {
    const [formData, setFormData] = useState({
        email: '',
        password: '',
        username: '',
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

        if (formData.username === '' || formData.password === '' || formData.email === '') {
            setMessage('Поля не можуть бути порожніми!');
            setShowAlert(true);
            return;
        }

        fetch('http://localhost:8080/user/signup', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(formData),
        })
            .then((response) => response.json())
            .then((data) => {
                console.log('Registration response:', data);
                if (data.message === 'User registered successfully') {
                    navigate('/signin');
                }else {
                    setMessage("Некоректні дані");
                    setShowAlert(true);
                }
            })
            .catch((error) => {
                console.log(formData);
                console.error('Registration error:', error);
            });
    };

    const handleSignIn = () => {
        navigate('/signin');
    }

    const handleCloseAlert = () => {
        setShowAlert(false);
    };

    return (
        <div className="container">
            <h2>Реєстрація</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    name="email"
                    placeholder="Пошта"
                    value={formData.email}
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
                <input
                    type="text"
                    name="username"
                    placeholder="Ім'я користувача"
                    value={formData.username}
                    onChange={handleChange}
                    className="input-field"
                />
                <button type="submit" className="confirm-button">
                    Зареєструваись
                </button>
                {showAlert && (
                    <CustomAlert message={message} onClose={handleCloseAlert} />
                )}
                <button className="have-account" onClick={handleSignIn}>В мене вже є акаунт</button>
            </form>
        </div>
    );
}

export default RegisterForm;
