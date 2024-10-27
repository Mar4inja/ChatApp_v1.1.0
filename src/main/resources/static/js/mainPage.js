'use strict'; // Aktivizē stingro režīmu

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

    let slideIndex = 0;
    const slides = document.querySelectorAll('.slide');
  
    function showSlides() {
        slides.forEach((slide, index) => {
            slide.classList.remove('active');
            if (index === slideIndex) {
                slide.classList.add('active');
            }
        });
        slideIndex = (slideIndex + 1) % slides.length;
    }
  
    setInterval(showSlides, 4200); // 3 секунды на смену слайда