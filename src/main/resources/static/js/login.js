'use strict';

document.addEventListener('DOMContentLoaded', function() {
    var loginForm = document.querySelector('#loginForm');
    var usernameInput = document.querySelector('#username');
    var passwordInput = document.querySelector('#password');
    var logoutButton = document.querySelector('#logout-button'); // Pievieno logout pogu

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
                document.cookie = `token=${data.accessToken}; path=/;`; // Iestata token
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

    function logout(event) {
        event.preventDefault(); // Novērš noklikšķināšanas notikumu
        // Noņem token un access token no sīkdatnēm
        document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
        document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // Pievienojiet šo rindu
        window.location.href = "login.html"; // Novirza uz pieteikšanās lapu
    }

    document.addEventListener("DOMContentLoaded", () => {
        // Pievieno izrakstīšanās saitei notikumu klausītāju
        document.getElementById('logout-button').addEventListener('click', logout);
    });
    

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