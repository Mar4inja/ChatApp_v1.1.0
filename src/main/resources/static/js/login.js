'use strict';

document.addEventListener('DOMContentLoaded', function() {
    var loginForm = document.querySelector('#loginForm');
    var usernameInput = document.querySelector('#username');
    var passwordInput = document.querySelector('#password');
    var logoutButton = document.querySelector('#logout-button');

    // Pievieno notikumu klausītāju pieslēgšanās formai
    loginForm.addEventListener('submit', login);

    // Pievieno notikumu klausītāju izrakstīšanās pogai
    if (logoutButton) {
        logoutButton.addEventListener('click', logout);
    }

    function login(event) {
        event.preventDefault();

        var email = usernameInput.value.trim();
        var password = passwordInput.value;

        if (!validateEmail(email)) {
            alert('Please enter a valid email address.');
            return;
        }

        console.log('Submitting form with email:', email);

        fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',  // Iekļauj sīkdatnes pieprasījumos
            body: JSON.stringify({ email: email, password: password }),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            alert('Login successful!');
            window.location.href = 'profile.html';
        })
        .catch(error => {
            console.error('Login error:', error);
        });
    }

    function logout(event) {
        event.preventDefault(); // Novērš noklikšķināšanas notikumu
        
        fetch('http://localhost:8080/api/users/logout', {
            method: 'POST',
            credentials: 'include',  // Iekļauj sīkdatnes pieprasījumos
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network error');
            }
            return response.json();
        })
        .then(data => {
            alert(data.message); // Parāda ziņu no servera
            window.location.href = "login.html"; // Novirza uz pieteikšanās lapu
        })
        .catch(error => {
            console.error('Logout error:', error);
        });
    }

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }
});

// Ielādē navigācijas joslu no navbar.html
fetch('navbar.html')
    .then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        return response.text();
    })
    .then(data => {
        document.getElementById('navbar-placeholder').innerHTML = data; // Ievieto navigācijas joslu HTML
    })
    .catch(error => console.error('Error loading navbar:', error)); // Apstrādā kļūdas
