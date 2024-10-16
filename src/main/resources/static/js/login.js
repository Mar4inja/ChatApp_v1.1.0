'use strict';

document.addEventListener('DOMContentLoaded', function() {
    // Your DOM elements
    var loginForm = document.querySelector('#loginForm');
    var usernameInput = document.querySelector('#username');
    var passwordInput = document.querySelector('#password');

    // Event listener for form submission
    loginForm.addEventListener('submit', login);

    function login(event) {
        event.preventDefault(); // Prevents the default form submission

        var email = usernameInput.value.trim(); // Assuming this is the email field
        var password = passwordInput.value;

        // Basic email validation
        if (!validateEmail(email)) {
            alert('Please enter a valid email address.');
            return;
        }

        // Login request to the server
        fetch('http://localhost:8080/api/users/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                email: email,
                password: password
            }),
        })
        .then(response => {
            console.log('Response status:', response.status);
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
            console.log('Login data:', data);
            if (data && data.accessToken) {
                alert('Login successful!');
                document.cookie = `token=${data.accessToken}; path=/;`;
                window.location.href = 'index.html';
            } else {
                alert('Login failed: Unknown error');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Login failed: ' + error.message);
        });
    }

    // Email validation function
    function validateEmail(email) {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    }
}); // Ensure this closing parenthesis is present and correctly placed
