function updateAuthButtons() {
    const accessToken = getCookie('Access-Token');
    console.log('Access Token:', accessToken); // Pārbaudiet, vai accessToken ir pieejams

    const loginButton = document.getElementById('login-button');
    const registerButton = document.getElementById('register-button');
    const logoutButton = document.getElementById('logout-button');

    if (accessToken) {
        // Ja accessToken eksistē, slēpj "Sign In" un "Sign Up" pogas
        console.log("User is logged in"); // Paziņojums
        loginButton.style.display = 'none'; // Slēpj "Sign In"
        registerButton.style.display = 'none'; // Slēpj "Sign Up"
        logoutButton.style.display = 'block'; // Rāda "Logout"
    } else {
        // Ja accessToken neeksistē, rāda "Sign In" un "Sign Up" pogas
        console.log("User is logged out"); // Paziņojums
        loginButton.style.display = 'block'; // Rāda "Sign In"
        registerButton.style.display = 'block'; // Rāda "Sign Up"
        logoutButton.style.display = 'none'; // Slēpj "Logout"
    }
}

function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        console.log('Cookie found:', parts.pop().split(';').shift()); // Pārbaudiet, ko iegūstat
        return parts.pop().split(';').shift(); // Atgriež sīkdatnes vērtību
    }
    console.log('Cookie not found'); // Paziņojums, ja sīkdatne nav atrasta
    return null; // Atgriež null, ja sīkdatne nav atrasta
}

document.addEventListener('DOMContentLoaded', updateAuthButtons); // Pārbaudiet, vai šis notikums darbojas



// Pārējais kods
const menuButton = document.querySelector('#hamburger-btn'); // Ikonas pogas elements
const navLinks = document.querySelector('.links'); // Navigācijas links

// Iestata notikumu klausītāju uz klikšķi ikonā
menuButton.addEventListener('click', () => {
    navLinks.classList.toggle('active'); // Pārslēdz klasi active, lai parādītu navigāciju
});

// Iegūst visus apakšizvēlnes elementus
const subMenus = document.querySelectorAll('.sub-menu');

// Pārbauda visus apakšizvēlnes elementus un pievieno notikumu klausītāju uz klikšķi
subMenus.forEach(subMenu => {
    const arrow = subMenu.previousElementSibling.querySelector('.arrow'); // Ikona bultiņa

    subMenu.previousElementSibling.addEventListener('click', () => {
        subMenu.classList.toggle('active'); // Pārslēdz klasi active, lai parādītu apakšizvēlni
        arrow.classList.toggle('rotate'); // Pagriež bultiņu
    });
});
