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
