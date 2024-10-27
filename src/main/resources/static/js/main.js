'use strict';

document.addEventListener('DOMContentLoaded', function() {
    const usernamePage = document.querySelector('#username-page');
    const chatPage = document.querySelector('#chat-page');
    const usernameForm = document.querySelector('#usernameForm');
    const messageForm = document.querySelector('#messageForm');
    const messageInput = document.querySelector('#message');
    const messageArea = document.querySelector('#messageArea');
    const connectingElement = document.querySelector('.connecting');
    let stompClient = null;
    let username = null;
    let currentRoom = 'public'; // Текущая комната

    const colors = [
        '#2196F3', '#32c787', '#00BCD4', '#ff5652',
        '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
    ];

    function connect(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение формы
        username = document.querySelector('#name').value.trim();

        if (username) {
            usernamePage.classList.add('hidden');
            chatPage.classList.remove('hidden');

            const socket = new SockJS('http://localhost:8080/ws');
            stompClient = Stomp.over(socket);

            stompClient.connect({}, function(frame) {
                console.log('Connected: ' + frame);
                stompClient.subscribe('/topic/public', onMessageReceived);
                stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
                connectingElement.classList.add('hidden');
            }, function(error) {
                console.error('Connection error: ', error);
                connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
                connectingElement.style.color = 'red';
            });
        } else {
            alert('You must enter a username to enter the chat.');
        }
    }

    function createRoom(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение
        const roomName = prompt("Enter room name:");
        if (roomName) {
            currentRoom = roomName;
            stompClient.send("/app/chat.createRoom", {}, JSON.stringify({ sender: username, content: roomName, type: 'ROOM_CREATE' }));
        }
    }

    function joinRoom(roomName) {
        currentRoom = roomName;
        stompClient.subscribe(`/topic/${roomName}`, onMessageReceived);
        stompClient.send("/app/chat.joinRoom", {}, JSON.stringify({ sender: username, type: 'JOIN_ROOM' }));
    }

    function sendMessage(event) {
        event.preventDefault(); // Предотвращаем стандартное поведение формы
        const messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            const chatMessage = {
                sender: username,
                content: messageContent,
                type: 'CHAT',
                room: currentRoom // Добавляем комнату в сообщение
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            messageInput.value = ''; // Очищаем поле ввода после отправки
        }
    }

    function onMessageReceived(payload) {
        const message = JSON.parse(payload.body);
        const messageElement = document.createElement('li');

        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' joined!';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' left!';
        } else {
            messageElement.classList.add('chat-message');

            const avatarElement = document.createElement('i');
            const avatarText = document.createTextNode(message.sender[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(message.sender);
            messageElement.appendChild(avatarElement);

            const usernameElement = document.createElement('span');
            const usernameText = document.createTextNode(message.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

        const textElement = document.createElement('p');
        const messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }

    function getAvatarColor(messageSender) {
        let hash = 0;
        for (let i = 0; i < messageSender.length; i++) {
            hash = 31 * hash + messageSender.charCodeAt(i);
        }
        const index = Math.abs(hash % colors.length);
        return colors[index];
    }

    // Обработчики событий
    if (usernameForm) usernameForm.addEventListener('submit', connect);
    if (messageForm) messageForm.addEventListener('submit', sendMessage);
    document.querySelector('#createRoomButton')?.addEventListener('click', createRoom);
});

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