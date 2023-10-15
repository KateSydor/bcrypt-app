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
            setMessage('Fields cannot be empty');
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
                    setMessage("Incorrect data input");
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
            <h2>Registration Form</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={formData.email}
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
                <input
                    type="text"
                    name="username"
                    placeholder="Username"
                    value={formData.username}
                    onChange={handleChange}
                    className="input-field"
                />
                <button type="submit" className="confirm-button">
                    Register
                </button>
                {showAlert && (
                    <CustomAlert message={message} onClose={handleCloseAlert} />
                )}
                <button className="have-account" onClick={handleSignIn}>I already have an account</button>
            </form>
        </div>
    );
}

export default RegisterForm;
