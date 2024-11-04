'use strict';

// Событие загрузки контента страницы
document.addEventListener('DOMContentLoaded', function() {
    var loginForm = document.querySelector('#loginForm'); // Находим форму входа
    var usernameInput = document.querySelector('#username'); // Находим поле ввода email
    var passwordInput = document.querySelector('#password'); // Находим поле ввода пароля
    var logoutButton = document.querySelector('#logout-button'); // Находим кнопку выхода

    // Добавление обработчика события для формы входа
    loginForm.addEventListener('submit', login);

    // Добавление обработчика события для кнопки выхода
    if (logoutButton) {
        logoutButton.addEventListener('click', logout);
    }

    function login(event) {
        event.preventDefault(); // Отменяем стандартное поведение формы

        var email = usernameInput.value.trim(); // Получаем email
        var password = passwordInput.value; // Получаем пароль

        // Валидация email
        if (!validateEmail(email)) {
            alert('Пожалуйста, введите корректный адрес электронной почты.');
            return; // Завершаем выполнение функции, если email некорректный
        }

        console.log('Отправка формы с email:', email); // Логируем email в консоль

        fetch('http://localhost:8080/api/users/login', { // Отправляем запрос на сервер
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({
                email: email,   // Передаём email из формы
                password: password // Передаём пароль из формы
            })
        })
        .then(response => {
            if (!response.ok) { // Если ответ не успешный
                throw new Error(`Ошибка запроса: ${response.status} ${response.statusText}`);
            }
            return response.json(); // Возвращаем JSON-ответ
        })
        .then(data => {
            if (data.accessToken) { // Если токен получен
                setCookie("accessToken", data.accessToken, 1); // Сохраняем токен в cookies на 1 день
                if (data.refreshToken) { // Если refreshToken получен
                    setCookie("refreshToken", data.refreshToken, 1); // Сохраняем refresh token в cookies на 1 день
                }
                console.log("Успешный вход!");
                window.location.href = "profile.html"; // Перенаправляем на страницу профиля
            } else if (data.errorMessage) { // Если ошибка
                console.error("Ошибка:", data.errorMessage);
            }
        })
        .catch(error => console.error("Ошибка входа:", error)); // Логируем ошибку
    } // Закрываем функцию login

    function logout(event) {
        event.preventDefault(); // Отменяем стандартное поведение кнопки выхода
        
        fetch('http://localhost:8080/api/users/logout', { // Отправляем запрос на выход
            method: 'POST',
            credentials: 'include',  // Включает cookies в запросах
        })
        .then(response => {
            if (!response.ok) { // Если ответ не успешный
                throw new Error('Сетевая ошибка');
            }
            return response.json(); // Возвращаем JSON-ответ
        })
        .then(data => {
            alert(data.message); // Показываем сообщение от сервера
            window.location.href = "login.html"; // Перенаправляем на страницу входа
        })
        .catch(error => {
            console.error('Ошибка выхода:', error); // Логируем ошибку
        });
    } // Закрываем функцию logout

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; // Регулярное выражение для валидации email
        return re.test(String(email).toLowerCase()); // Проверяем соответствие email регулярному выражению
    }

    // Функция для установки куки
    function setCookie(name, value, days) {
        var expires = "";
        if (days) {
            var date = new Date();
            date.setTime(date.getTime() + (days * 24 * 60 * 60 * 1000)); // Устанавливаем срок действия куки
            expires = "; expires=" + date.toUTCString();
        }
        document.cookie = name + "=" + (value || "")  + expires + "; path=/"; // Устанавливаем куки
    }
});

// Загрузка навигационной панели из navbar.html
fetch('navbar.html')
    .then(response => {
        if (!response.ok) { // Если ответ не успешный
            throw new Error('Сетевая ошибка');
        }
        return response.text(); // Возвращаем текст ответа
    })
    .then(data => {
        document.getElementById('navbar-placeholder').innerHTML = data; // Вставляем навигационную панель в HTML
    })
    .catch(error => console.error('Ошибка загрузки навигационной панели:', error)); // Обработка ошибок
Z