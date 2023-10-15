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
            setMessage('Fields cannot be empty');
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
                    setMessage("Incorrect user credentials");
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
            <h2>Sign In Form</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={formData.username}
                    onChange={handleChange}
                    className="input-field"
                />
                <input
                    type="password"
                    name="password"
                    placeholder="Password"
                    value={formData.password}
                    onChange={handleChange}
                    className="input-field"
                />
                <button type="submit" className="confirm-button">
                    Sign In
                </button>
                {showAlert && (
                    <CustomAlert message={message} onClose={handleCloseAlert} />
                )}
                <button className="have-account" onClick={handleRegister}>I haven't an account</button>
            </form>
        </div>
    );
}

export default SignIn;
