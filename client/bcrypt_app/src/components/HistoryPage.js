import React, {useEffect, useState} from 'react';
import './HistoryPage.css';
import {useNavigate} from "react-router-dom";

function HistoryPage() {
    const [userTasks, setUserTasks] = useState([]);
    const user_name = sessionStorage.getItem('username');;
    const password = sessionStorage.getItem('password');;

    const base64Credentials = btoa(user_name + ':' + password);

    const requestOptions = {
        method: 'GET',
        headers: {
            Authorization: 'Basic '+base64Credentials,
            'Content-Type': 'application/json',
        },
    };

    // Make the fetch request
    useEffect(() => {fetch('http://localhost:8080/task/history', requestOptions)
        .then((response) => response.json())
        .then((result) => {
            setUserTasks(result);
            console.log('Fetch result:', result);
        })
        .catch((error) => console.error('Fetch error:', error));}, []);
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

    const parseDatabaseDate = (dateString) => {
        const dateParts = dateString.split(',').map(Number);
        const [year, month, day, hour, minute, second, milliseconds] = dateParts;

        return new Date(year, month - 1, day, hour+3, minute, second, milliseconds / 1000000);
    };

    return (
        <div className="history_page">
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

            <main className="main-content">
                <div className="App">
                    <h1>Entities Table</h1>
                    <div className="table-container">
                    <table>
                        <thead>
                        <tr>
                            <th>Task ID</th>
                            <th>Started</th>
                            <th>Finished</th>
                            <th>Bcrypt Response List</th>
                        </tr>
                        </thead>
                        <tbody>
                        {userTasks.map((entity, index) => (
                            <tr key={index}>
                                <td>{entity.taskId}</td>
                                <td>{parseDatabaseDate(entity.started.toString()).toLocaleString()}</td>
                                <td>{parseDatabaseDate(entity.finished.toString()).toLocaleString()}</td>
                                <td><ul key={entity.taskId} >
                                    {entity.bcryptResponseList.map((bcryptResponse) => (
                                        <li key={bcryptResponse.id}>
                                            <div>ID: {bcryptResponse.id}</div>
                                            <div>Original Password: {bcryptResponse.originalPassword}</div>
                                            <div>Hashed Password: {bcryptResponse.hashedPassword}</div>
                                            <div>Verify Result: {bcryptResponse.verifyResult.toString()}</div>
                                            <div>Rounds: {bcryptResponse.rounds}</div>
                                        </li>))}
                                </ul>
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                    </div>
                </div>
            </main>
        </div>
    );
}

export default HistoryPage;