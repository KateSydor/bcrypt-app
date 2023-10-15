import React from 'react';
import './CustomAlert.css';

const CustomAlert = ({ message, onClose }) => {
    return (
        <div className="custom-alert">
            <div className="custom-alert-content">
                <p className="alert-text" >{message}</p>
                <button className="confirm-button" onClick={onClose}>OK</button>
            </div>
        </div>
    );
};

export default CustomAlert;
