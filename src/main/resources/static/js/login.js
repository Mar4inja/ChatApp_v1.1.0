'use strict';

// Событие загрузки контента страницы
document.addEventListener('DOMContentLoaded', function() {
    var loginForm = document.querySelector('#loginForm');
    var usernameInput = document.querySelector('#username');
    var passwordInput = document.querySelector('#password');
    var logoutButton = document.querySelector('#logout-button');

    // Добавление обработчика события для формы входа
    loginForm.addEventListener('submit', login);

    // Добавление обработчика события для кнопки выхода
    if (logoutButton) {
        logoutButton.addEventListener('click', logout);
    }

    function login(event) {
        event.preventDefault(); // Отменяем стандартное поведение формы

        var email = usernameInput.value.trim();
        var password = passwordInput.value;

        // Валидация email
        if (!validateEmail(email)) {
            alert('Пожалуйста, введите корректный адрес электронной почты.');
            return;
        }

        console.log('Отправка формы с email:', email);

        // Выполнение запроса входа
        fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',  // Включает cookies в запросах
            body: JSON.stringify({ email: email, password: password }), // Отправляем email и пароль
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Сетевая ошибка: ' + response.statusText);
            }
            return response.json();  // Возвращаем ответ в формате JSON
        })
        .then(data => {
            console.log('Вход успешен, полученные данные:', data);  // Логируем данные
            
            // Устанавливаем accessToken в cookies
            document.cookie = `accessToken=${data.accessToken}; path=/; secure; SameSite=Strict`; // Сохраняем accessToken

            alert('Вход успешен!');  // Показываем сообщение об успехе
            window.location.href = 'profile.html';  // Переходим на страницу профиля
        })
        .catch(error => {
            console.error('Ошибка входа:', error);  // Логируем ошибку
            alert('Ошибка входа: ' + error.message); // Показываем сообщение об ошибке
        });
    }

    function logout(event) {
        event.preventDefault(); // Отменяем стандартное поведение кнопки выхода
        
        fetch('http://localhost:8080/api/users/logout', {
            method: 'POST',
            credentials: 'include',  // Включает cookies в запросах
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Сетевая ошибка');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message); // Показываем сообщение от сервера
            window.location.href = "login.html"; // Перенаправляем на страницу входа
        })
        .catch(error => {
            console.error('Ошибка выхода:', error);
        });
    }

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Регулярное выражение для валидации email
        return re.test(String(email).toLowerCase());
    }
});

// Загрузка навигационной панели из navbar.html
fetch('navbar.html')
    .then(response => {
        if (!response.ok) {
            throw new Error('Сетевая ошибка');
        }
        return response.text();
    })
    .then(data => {
        document.getElementById('navbar-placeholder').innerHTML = data; // Вставляем навигационную панель в HTML
    })
    .catch(error => console.error('Ошибка загрузки навигационной панели:', error)); // Обработка ошибок
