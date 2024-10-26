document.addEventListener("DOMContentLoaded", () => {
    try {
        const userEmail = getUserEmailFromToken(); // Saņem e-pastu no token
        fetchUserProfile(userEmail); // Izsauc funkciju, lai iegūtu lietotāja profilu
    } catch (error) {
        console.error("Error fetching user email:", error); // Ievieto kļūdas ziņojumu konsolē
        document.getElementById('user-info').innerText = "Failed to load user data."; // Parāda kļūdas ziņojumu lietotāja informācijas vietā
    }
});

function fetchUserProfile(userEmail) {
    const url = `http://localhost:8080/api/users/by-email?email=${encodeURIComponent(userEmail)}`; // Izveido URL, lai iegūtu lietotāja profilu pēc e-pasta
    console.log("Loading user profile with URL:", url); // Ievieto URL konsolē
    
    const token = getCookie('token'); // Saņem token no sīkdatnēm

    fetch(url, {
        method: 'GET', // Izmanto GET metodi
        headers: {
            'Authorization': `Bearer ${token}` // Pievieno token pieprasījuma galvenēm
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("User not found"); // Ja atbilde nav veiksmīga, izm throws kļūda
        }
        return response.json(); // Atgriež atbildi kā JSON
    })
    .then(user => {
        displaySimpleUserInfo(user); // Attēlo vienkāršo informāciju
        displayFullUserInfo(user); // Attēlo pilno informāciju
    })
    .catch(error => {
        console.error("Error loading user profile:", error); // Ievieto kļūdas ziņojumu konsolē
        document.getElementById('user-info').innerText = "Failed to load user data."; // Parāda kļūdas ziņojumu lietotāja informācijas vietā
    });
}

function displaySimpleUserInfo(user) {
    const simpleProfileDiv = document.getElementById('simple-profile'); // Iegūst elementu ar vienkāršo profilu
    simpleProfileDiv.querySelector('.name').innerText = `${user.firstName} ${user.lastName}`; // Ievieto vārdu un uzvārdu

    // Pārliecinieties, ka pievienojat dzimšanas datumu un e-pastu šeit, ja nepieciešams
    const birthdateDiv = simpleProfileDiv.querySelector('#full-birthdate'); // Iegūst dzimšanas datuma elementu
    birthdateDiv.innerText = `Birth Date: ${user.birthdate || "Not available"}`; // Ievieto dzimšanas datumu

    const emailDiv = simpleProfileDiv.querySelector('#full-email'); // Iegūst e-pasta elementu
    emailDiv.innerText = `Email: ${user.email || "Not available"}`; // Ievieto e-pastu
}

function displayFullUserInfo(user) {
    const fullProfileDiv = document.getElementById('full-profile'); // Iegūst elementu ar pilno profilu
    fullProfileDiv.querySelector('#full-name').innerText = `${user.firstName} ${user.lastName}`; // Ievieto vārdu un uzvārdu
    fullProfileDiv.querySelector('#full-about').innerText = user.about || "No information available."; // Ievieto informāciju par sevi

    // Nav nepieciešams šeit pievienot dzimšanas datumu un e-pastu, jo tie jau tiek pievienoti vienkāršajā profilā
}


function getUserEmailFromToken() {
    const token = getCookie('token'); // Saņem token no sīkdatnēm
    console.log("Token from cookies:", token); // Ievieto token konsolē
    if (!token) {
        throw new Error("Token not found"); // Ja token nav atrasts, izm throws kļūda
    }

    const payload = token.split('.')[1]; // Izvelk payload no token
    const decodedPayload = JSON.parse(atob(payload)); // Dekodē base64 payload
    return decodedPayload.sub; // Atgriež lietotāja e-pastu
}

function getCookie(name) {
    const value = `; ${document.cookie}`; // Saņem visas sīkdatnes
    const parts = value.split(`; ${name}=`); // Izsplēš sīkdatni pēc nosaukuma
    if (parts.length === 2) return parts.pop().split(';').shift(); // Atgriež sīkdatnes vērtību
}
