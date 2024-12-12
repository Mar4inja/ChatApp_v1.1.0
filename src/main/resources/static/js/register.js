'use strict';

document.addEventListener('DOMContentLoaded', function () {
    var registrationForm = document.querySelector('#registrationForm');

    // Автоформатирование даты
    document.getElementById("birthdate").addEventListener("input", function(event) {
        let value = event.target.value.replace(/\D/g, "");
        if (value.length > 2 && value.length <= 4) {
            value = value.slice(0, 2) + "." + value.slice(2);
        } else if (value.length > 4) {
            value = value.slice(0, 2) + "." + value.slice(2, 4) + "." + value.slice(4); // Добавляем обе точки
        }
        event.target.value = value;
    });

    // Функция преобразования даты в формат yyyy-MM-dd
    function convertDateToISOFormat(dateString) {
        const [day, month, year] = dateString.split('.');
        return `${year}-${month}-${day}`;
    }

    registrationForm.addEventListener('submit', function (event) {
        event.preventDefault();

        var firstName = document.querySelector('#firstName').value;
        var lastName = document.querySelector('#lastName').value;
        var birthdate = document.querySelector('#birthdate').value;
        var email = document.querySelector('#email').value;
        var password = document.querySelector('#password').value;

        // Преобразуем дату перед отправкой
        var formattedBirthdate = convertDateToISOFormat(birthdate);

        var registrationData = {
            firstName: firstName,
            lastName: lastName,
            birthdate: formattedBirthdate,
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

fetch('navbar.html')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(data => {
        document.getElementById('navbar-placeholder').innerHTML = data;
    })
    .catch(error => console.error('Error loading navbar:', error));