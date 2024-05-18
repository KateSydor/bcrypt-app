import React, {useEffect, useState} from 'react';
import './HistoryPage.css';
import './ProgressResultPage.css';
import {useNavigate} from "react-router-dom";
import CustomAlert from "./CustomAlert";

function ProgressResultPage() {
    const [userTaskProgress, setUserTaskProgress] = useState([]);
    const user_name = sessionStorage.getItem('username');
    const password = sessionStorage.getItem('password');
    const taskId = sessionStorage.getItem('taskId');
    const [taskResult, setTaskResult] = useState(null);

    const [showAlert, setShowAlert] = useState(false);

    const [message, setMessage] = useState("");

    const base64Credentials = btoa(user_name + ':' + password);

    const requestOptions = {
        method: 'GET',
        headers: {
            Authorization: 'Basic '+base64Credentials,
            'Content-Type': 'application/json',
        },
    };

    const fetchData = () => {
        fetch('http://localhost:8080/task/progress?taskId=' + taskId, requestOptions)
            .then((response) => response.json())
            .then((result) => {
                setUserTaskProgress(result);
                console.log('Fetch result:', result);
            })
            .catch((error) => console.error('Fetch error:', error));
    };

    useEffect(() => {
        fetchData();

        const intervalId = setInterval(fetchData, 10000);

        return () => clearInterval(intervalId);
    }, [taskId]);

    const fetchTaskResult = () => {
        fetch('http://localhost:8080/task/result?taskId=' + taskId, requestOptions)
            .then((response) => response.json())
            .then((result) => {
                setTaskResult(result);
                console.log('Результат:', result);
            })
            .catch((error) => console.error('Fetch error:', error));
    };

    const fetchCancelTask = () => {
        if (userTaskProgress.totalHash !== userTaskProgress.doneHash){
            fetch('http://localhost:8080/task/cancel?taskId=' + taskId, requestOptions)
                .then((response) => response.json())
                .then((result) => {
                    console.log('Скасовано:', result);
                    navigate('/home');
                })
                .catch((error) => console.error('Fetch error:', error));
        }
        else{
            setMessage('Ви не можете скасувати завершене завдання!');
            setShowAlert(true);
        }
    };



    const handleShowTaskResult = () => {
        fetchTaskResult();
    };

    const handleCancelTask = () => {
        fetchCancelTask();
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
                    <span className="username">Ім'я користувача: {user_name}</span>
                </div>
            </header>

            <main className="main">
                <div className="App">
                    <h1>Прогрес виконання завдання</h1>
                    <div className="table-container">
                        <table>
                            <thead>
                            <tr>
                                <th>Кількість паролів</th>
                                <th>Вже захешовано</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>{userTaskProgress.totalHash}</td>
                                <td>{userTaskProgress.doneHash}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <div>
                        <button className="confirm-button" onClick={handleShowTaskResult}>Показати результат завдання</button>
                        <button className="confirm-button cancel-button" onClick={handleCancelTask}>Скасувати завдання</button>
                        {showAlert && (
                            <CustomAlert message={message} onClose={handleCloseAlert}/>
                        )}
                    </div>
                    {taskResult && (
                        <div>
                            <h2>Результат виконання завдання:</h2>
                            <div className="table-container">
                                <table>
                                    <tbody>
                                    {taskResult.map((entity, index) => (
                                        <tr key={index}>
                                            <td>
                                                <ul>
                                                    <li key={entity.id}>
                                                        <div>Номер: {entity.id}</div>
                                                        <div>Початковий пароль: {entity.originalPassword}</div>
                                                        <div>Захешований пароль: {entity.hashedPassword}</div>
                                                        <div>Результат верифікації: {entity.verifyResult.toString()}</div>
                                                        <div>К-сть раундів: {entity.rounds}</div>
                                                    </li>
                                                </ul>
                                            </td>
                                        </tr>
                                    ))}
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    )}
                </div>
            </main>
        </div>
    );
}

export default ProgressResultPage;

