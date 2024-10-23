document.addEventListener("DOMContentLoaded", () => {
    try {
        const userEmail = getUserEmailFromToken(); // Получаем email из токена
        fetchUserProfile(userEmail);
    } catch (error) {
        console.error("Ошибка получения email пользователя:", error);
        document.getElementById('user-info').innerText = "Не удалось загрузить данные пользователя.";
    }
});

function fetchUserProfile(userEmail) {
    const url = `http://localhost:8080/api/users/by-email?email=${encodeURIComponent(userEmail)}`; // Используем email в запросе
    console.log("Загрузка профиля пользователя с URL:", url);
    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error("Пользователь не найден");
            }
            return response.json();
        })
        .then(user => {
            displayUserInfo(user);
        })
        .catch(error => {
            console.error("Ошибка загрузки профиля пользователя:", error);
            document.getElementById('user-info').innerText = "Не удалось загрузить данные пользователя.";
        });
}

function displayUserInfo(user) {
    const userInfoDiv = document.getElementById('user-info');
    userInfoDiv.innerHTML = `
        <p><strong>Имя:</strong> ${user.firstName}</p>
        <p><strong>Фамилия:</strong> ${user.lastName}</p>
        <p><strong>Email:</strong> ${user.email}</p>
        <p><strong>Дата рождения:</strong> ${new Date(user.birthdate).toLocaleDateString()}</p>
    `;
}

function getUserEmailFromToken() {
    const token = getCookie('token'); // Получаем токен из cookies
    console.log("Токен из cookies:", token);
    if (!token) {
        throw new Error("Токен не найден");
    }

    const payload = token.split('.')[1]; // Извлекаем payload из токена
    const decodedPayload = JSON.parse(atob(payload)); // Декодируем base64
    return decodedPayload.sub; // Возвращаем email пользователя
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) return parts.pop().split(';').shift();
}
