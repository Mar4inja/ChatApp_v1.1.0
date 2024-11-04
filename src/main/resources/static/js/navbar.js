'use strict';

// Funkcija, lai iegūtu sīkdatni pēc nosaukuma
function getCookie(name) {
    const value = `; ${document.cookie}`; // Iegūst visus sīkdatņu datus
    const parts = value.split(`; ${name}=`); // Sadala sīkdatnes pēc nosaukuma
    if (parts.length === 2) {
        return parts.pop().split(';').shift(); // Atgriež sīkdatnes vērtību
    }
    return null; // Atgriež null, ja sīkdatne nav atrasta
}

// Funkcija, lai pārbaudītu, vai lietotājs ir ielogojies
function isLoggedIn() {
    const accessToken = getCookie('accessToken'); // Iegūst sīkdatni ar nosaukumu "accessToken"
    const profileButton = document.getElementById('profile-button'); // Iegūst "Profile" pogu
    const loginButton = document.getElementById('login-button'); // Iegūst "Login" pogu
    const registerButton = document.getElementById('register-button'); // Iegūst "Register" pogu
    const logoutButton = document.getElementById('logout-button'); // Iegūst "Logout" pogu

    if (accessToken) {
        // Ja accessToken eksistē, slēpj "Login" un "Register" pogas
        profileButton.style.display = 'block'; // Rāda "Profile"
        loginButton.style.display = 'none'; // Slēpj "Login"
        registerButton.style.display = 'none'; // Slēpj "Register"
        logoutButton.style.display = 'block'; // Rāda "Logout"
    } else {
        // Ja accessToken neeksistē, rāda "Login" un "Register" pogas
        profileButton.style.display = 'none'; // Slēpj "Profile"
        loginButton.style.display = 'block'; // Rāda "Login"
        registerButton.style.display = 'block'; // Rāda "Register"
        logoutButton.style.display = 'none'; // Slēpj "Logout"
    }
}

// Kad lapa ir ielādēta, atjaunojiet autentifikācijas pogas
document.addEventListener('DOMContentLoaded', isLoggedIn);

// Izveidojiet izejošās pogas darbību
document.addEventListener('DOMContentLoaded', function() {
    const logoutButton = document.getElementById('logout-button');

    if (logoutButton) {
        logoutButton.addEventListener('click', function(event) {
            event.preventDefault(); // Atceļ noklusējuma darbību
            document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // Noņem sīkdatni
            isLoggedIn(); // Atjaunina pogas redzamību
            window.location.href = 'index.html'; // Pāradresē uz sākumlapu
        });
    }
});
