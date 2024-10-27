'use strict';

function logout(event) {
    event.preventDefault(); // Novērš noklikšķināšanas notikumu
    console.log('Logout function called'); // Pievieno ziņojumu, kad funkcija tiek izsaukta

    // Noņem token un access token no sīkdatnēm
    document.cookie = "token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "accessToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";

    // Novirza uz pieteikšanās lapu
    window.location.href = "login.html"; 
}

document.addEventListener("DOMContentLoaded", () => {
    const logoutButton = document.getElementById('logout-button');
    const signInButton = document.getElementById('signin-button');

    const token = document.cookie.split('; ').find(row => row.startsWith('token='));

    if (token) {
        if (signInButton) {
            signInButton.style.display = 'none';
        }
    }

    if (logoutButton) {
        logoutButton.addEventListener('click', logout);
    } else {
        console.error('Logout button not found!');
    }
});
