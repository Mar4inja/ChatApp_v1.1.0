// Pārliecinies, ka kods tiek izpildīts pēc HTML ielādes
document.addEventListener('DOMContentLoaded', () => {
    // Ielādē navigācijas joslu no navbar.html
    fetch('navbar.html')
        .then(response => {
            if (!response.ok) {
                throw new Error('Tīkla kļūda: ' + response.statusText);
            }
            return response.text();
        })
        .then(data => {
            document.getElementById('navbar-placeholder').innerHTML = data;
        })
        .catch(error => console.error('Kļūda, ielādējot navigācijas joslu:', error));

    // Funkcija, lai iegūtu e-pasta adresi no cookies
    function getEmail() {
        const value = `; ${document.cookie}`; // Pievieno semikolu priekšā
        const parts = value.split(`; accessToken=`); // Maini "accessToken" uz jūsu faktisko cookie nosaukumu
        if (parts.length === 2) {
            const token = parts.pop().split(';').shift(); // Atgriež tokenu
            const decoded = decodeToken(token); // Dekodē tokenu
            return decoded.sub; // Atgriež e-pasta adresi no dekodētā tokena
        }
        return null; // Ja nav atrasts, atgriež null
    }

    // Funkcija, lai iegūtu accessToken no cookies
    function getAccessToken() {
        const value = `; ${document.cookie}`; // Pievieno semikolu priekšā
        const parts = value.split(`; accessToken=`); // Maini "accessToken" uz jūsu faktisko cookie nosaukumu
        if (parts.length === 2) {
            return parts.pop().split(';').shift(); // Atgriež tokenu
        }
        return null; // Ja nav atrasts, atgriež null
    }

    // Funkcija, lai dekodētu JWT tokenu
    function decodeToken(token) {
        if (!token) {
            console.log('Token is empty or null');
            return null;
        }
        const payload = token.split('.')[1]; // Ņem otrajā daļā, kas satur payload
        const decodedPayload = JSON.parse(atob(payload)); // Dekodē un parsē JSON
        console.log('Token decoded:', decodedPayload); // Parāda dekodēto saturu
        return decodedPayload; // Atgriež dekodēto saturu
    }

    // Ielādē lietotāja datus no servera
    const email = getEmail(); // Iegūst e-pasta adresi
    console.log('Iegūtais e-pasts:', email); // Pārbaudiet, vai e-pasts ir pieejams

    const token = getAccessToken(); // Iegūst accessToken

    if (email) {
        fetch(`http://localhost:8080/api/users/by-email?email=${encodeURIComponent(email)}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}` // Pievieno tokenu kā autorizācijas galveni
            }
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Tīkla kļūda: ' + response.statusText);
            }
            return response.json(); // Atgriež JSON datus
        })
        .then(userData => {
            console.log('User data:', userData); // Pārbauda, kas tiek atgriezts
            // Aizvieto HTML saturu ar iegūtajiem datiem
            const fullName = `${userData.firstName} ${userData.lastName}`; // Apvieno vārdu un uzvārdu
            document.querySelector('.card-body h4').textContent = fullName; // Ieliek pilnu vārdu un uzvārdu
            document.querySelector('.text-secondary.mb-1').textContent = userData.title; // Ieliek amatu
            document.querySelector('.text-muted.font-size-sm').textContent = userData.location; // Ieliek atrašanās vietu
        })
        .catch(error => console.error('Kļūda, ielādējot lietotāja datus:', error));
    } else {
        console.error('E-pasta adrese nav pieejama.'); // Ja e-pasta adrese nav pieejama
    }

    // Aizvieto vārdu un uzvārdu pēc dekodētā tokena
    if (token) {
        const decodedToken = decodeToken(token); // Dekodē tokenu
        if (decodedToken && decodedToken.name) {
            document.querySelector('#user-name').textContent = decodedToken.name; // Aizvieto ar vārdu un uzvārdu
        } else {
            console.error('Name not found in decoded token'); // Ja nav vārda
        }
    } else {
        console.error('No token found'); // Ja nav tokena
    }
});
