'use strict';

document.addEventListener('DOMContentLoaded', function () {
    var registrationForm = document.querySelector('#registrationForm');

    registrationForm.addEventListener('submit', function (event) {
        event.preventDefault(); // Prevent default form submission

        var firstName = document.querySelector('#firstName').value;
        var lastName = document.querySelector('#lastName').value;
        var birthdate = document.querySelector('#birthdate').value;
        var email = document.querySelector('#email').value;
        var password = document.querySelector('#password').value;

        // Prepare registration data object
        var registrationData = {
            firstName: firstName,
            lastName: lastName,
            birthdate: birthdate,
            email: email,
            password: password
        };

        // Send registration request to the server
        fetch('http://localhost:8080/api/users/register', { // Ensure this is the correct URL
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registrationData)
        })
        .then(response => {
            if (response.ok) {
                // If registration is successful
           alert('Registration successful! A confirmation email has been sent to the address you provided.');
                window.location.href = 'login.html'; // Redirect to login page
            } else {
                // If there is an error during registration
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
