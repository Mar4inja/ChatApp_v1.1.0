'use strict';

// Gaida, kad tiks ielādēts DOM, lai sāktu strādāt ar elementiem
document.addEventListener("DOMContentLoaded", function() {
  // Iegūst access token no cookies
  const accessToken = getAccessTokenFromCookies();

  // Pārbauda, vai token ir pieejams
  if (!accessToken) {
      console.error('Toks neatrasts'); // Paziņo par kļūdu
      return; // Beidz izpildi, ja token neatrodas
  }

  // Dekodē token un iegūst payload
  const payload = parseJwt(accessToken);
  const email = payload.email; // Iegūst e-pastu no payload
  
  // Pievieno log, lai pārbaudītu, kurš e-pasts tiek sūtīts uz API
  console.log(`Sūtot pieprasījumu uz API ar e-pastu: ${email}`); 

  // Veic pieprasījumu uz serveri, lai iegūtu lietotāja datus
  fetch(`http://localhost:8080/api/users/by-email?email=${email}`, {
      method: 'GET',
      headers: {
          'Authorization': 'Bearer ' + accessToken // Nosūta token galvenes
      }
  })
  .then(response => {
      if (!response.ok) {
          throw new Error('Tīkla kļūda, iegūstot profila datus');
      }
      return response.json(); // Atgriež atbildi JSON formātā
  })
  .then(data => {
      // Aizpilda HTML elementus ar lietotāja datiem
      document.querySelector('.user-name').textContent = `${data.firstName} ${data.lastName}`; // Aizpilda vārdu un uzvārdu
      document.querySelector('.user-location').textContent = data.location || 'Nav norādīts'; // Aizpilda atrašanās vietu
      document.querySelector('.user-email').textContent = data.email || 'Nav norādīts'; // Aizpilda e-pastu
      document.querySelector('.user-birth-date').textContent = data.birthDate || 'Nav norādīta'; // Aizpilda dzimšanas datumu
  })
  .catch(error => console.error('Kļūda, ielādējot profilu:', error)); // Paziņo par kļūdām
});


// Funkcija token dekodēšanai (JWT)
function parseJwt(accessToken) {
    const base64Url = accessToken.split('.')[1]; // Iegūst payload
    const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
    const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
    return JSON.parse(jsonPayload); // Atgriež dekodētu payload
}

// Funkcija, lai iegūtu access token no cookies
function getAccessTokenFromCookies() {
    const name = 'accessToken=';
    const decodedCookie = decodeURIComponent(document.cookie); // Dekodē cookies
    const ca = decodedCookie.split(';'); // Sadala cookies masīvā
    for (let i = 0; i < ca.length; i++) {
        let c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1); // Noņem atstarpi
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length); // Atgriež token
        }
    }
    return null; // Token neatrasts
}

// Ielādē navigācijas joslu no navbar.html
fetch('navbar.html')
    .then(response => {
        if (!response.ok) {
            throw new Error('Tīkla kļūda: ' + response.statusText);
        }
        return response.text(); // Atgriež HTML navigācijas joslu
    })
    .then(data => {
        document.getElementById('navbar-placeholder').innerHTML = data; // Ieliek HTML elementā
    })
    .catch(error => console.error('Kļūda, ielādējot navigācijas joslu:', error)); // Paziņo par kļūdām
