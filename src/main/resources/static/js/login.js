'use strict';

document.addEventListener('DOMContentLoaded', function() {
    var loginForm = document.querySelector('#loginForm');
    var usernameInput = document.querySelector('#username');
    var passwordInput = document.querySelector('#password');

    loginForm.addEventListener('submit', login);

    function login(event) {
        event.preventDefault();

        var email = usernameInput.value.trim();
        var password = passwordInput.value;

        if (!validateEmail(email)) {
            alert('Please enter a valid email address.');
            return;
        }

        console.log('Submitting form with email:', email); // Debugging line

        fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email: email, password: password }),
        })
        .then(response => {
            console.log('Response status:', response.status); // Debugging line
            if (!response.ok) {
                if (response.status === 401) {
                    alert('Login failed: Invalid credentials.');
                } else {
                    alert('Login failed: ' + response.statusText);
                }
                throw new Error('Network response was not ok: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            console.log('Login data:', data); // Debugging line
            if (data && data.accessToken) {
                alert('Login successful!');
                document.cookie = `token=${data.accessToken}; path=/;`;
                window.location.href = 'profile.html';
            } else {
                alert('Login failed: Unknown error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Login failed: ' + error.message);
        });
    }

    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }
});
