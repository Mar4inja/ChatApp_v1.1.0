'use strict';

document.addEventListener('DOMContentLoaded', function () {
    var registrationForm = document.querySelector('#registrationForm');

    // Автоформатирование даты
    document.getElementById("birthdate").addEventListener("input", function(event) {
        let value = event.target.value.replace(/\D/g, ""); // Удаляем все, кроме чисел
        if (value.length > 2 && value.length <= 4) {
            value = value.slice(0, 2) + "." + value.slice(2); // Добавляем первую точку
        } else if (value.length > 4) {
            value = value.slice(0, 2) + "." + value.slice(2, 4) + "." + value.slice(4); // Добавляем обе точки
        }
        event.target.value = value;
    });

    registrationForm.addEventListener('submit', function (event) {
        event.preventDefault();

        var firstName = document.querySelector('#firstName').value;
        var lastName = document.querySelector('#lastName').value;
        var birthdate = document.querySelector('#birthdate').value;
        var email = document.querySelector('#email').value;
        var password = document.querySelector('#password').value;

        var registrationData = {
            firstName: firstName,
            lastName: lastName,
            birthdate: birthdate,
            email: email,
            password: password
        };

        fetch('http://localhost:8080/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registrationData)
        })
        .then(response => {
            if (response.ok) {
                alert('Registration successful! A confirmation email has been sent.');
                window.location.href = 'login.html';
            } else {
                return response.json().then(errorData => {
                    alert('Registration failed: ' + errorData.message);
                });
            }
        })
        .catch(error => {
            console.error('Error during registration:', error);
            alert('An error occurred. Please try again later.');
        });
    });
});
