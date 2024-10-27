'use strict';

$(document).ready(function() {
  // Переключение левого сайдбара
  $(".toggle-button, .user-profile").on("click", function() {
    $(".left-sidebar").toggleClass("minimize");
  });

  // Закрыть чат
  $(".close-chat-btn").on("click", function() {
    $(".direct-messaging").addClass("minimize");
  });

  // Открыть/закрыть чат
  $(".open-chat-btn").on("click", function() {
    $(".direct-messaging").toggleClass("minimize");
  });

  // Открыть/закрыть музыкальный плеер
  $(".open-music-btn").on("click", function() {
    $(".music-player").toggleClass("show");
  });

  // Открыть/закрыть таймер
  $(".open-timer-btn").on("click", function() {
    $(".timer-display").toggleClass("show");
  });
});

// Загружаем навигационную панель из navbar.html
fetch('navbar.html')
    .then(response => {
        if (!response.ok) {
            throw new Error('Сетевая ошибка: ' + response.statusText);
        }
        return response.text();
    })
    .then(data => {
        document.getElementById('navbar-placeholder').innerHTML = data; // Вставляем HTML навигационной панели
    })
    .catch(error => console.error('Ошибка загрузки навигационной панели:', error)); // Обработка ошибок
