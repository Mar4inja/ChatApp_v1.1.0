'use strict';

document.addEventListener('DOMContentLoaded', function() {
    var usernamePage = document.querySelector('#username-page');
    var chatPage = document.querySelector('#chat-page');
    var usernameForm = document.querySelector('#usernameForm');
    var messageForm = document.querySelector('#messageForm');
    var messageInput = document.querySelector('#message');
    var messageArea = document.querySelector('#messageArea');
    var connectingElement = document.querySelector('.connecting');
    var stompClient = null;
    var username = null;
    var currentRoom = 'public'; // Variable to store the current room

    var colors = [
        '#2196F3', '#32c787', '#00BCD4', '#ff5652',
        '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
    ];

    function connect(event) {
        event.preventDefault(); // Prevent default form submission
        username = document.querySelector('#name').value.trim();

        if (username) {
            usernamePage.classList.add('hidden');
            chatPage.classList.remove('hidden');

            var socket = new SockJS('http://localhost:8080/ws');
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
        event.preventDefault(); // Prevent default action
        const roomName = prompt("Enter room name:");
        if (roomName) {
            currentRoom = roomName; // Update current room
            stompClient.send("/app/chat.createRoom", {}, JSON.stringify({ sender: username, content: roomName, type: 'ROOM_CREATE' }));
        }
    }

    function joinRoom(roomName) {
        currentRoom = roomName; // Update current room
        stompClient.subscribe(`/topic/${roomName}`, onMessageReceived); // Subscribe to new room
        stompClient.send("/app/chat.joinRoom", {}, JSON.stringify({ sender: username, type: 'JOIN_ROOM' }));
    }

    function sendMessage(event) {
        event.preventDefault(); // Prevent default form submission
        var messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            var chatMessage = {
                sender: username,
                content: messageContent,
                type: 'CHAT',
                room: currentRoom // Include room name in the message
            };
            stompClient.send(`/app/chat.sendMessage`, {}, JSON.stringify(chatMessage));
            messageInput.value = ''; // Clear input after sending
        }
    }

    function onMessageReceived(payload) {
        var message = JSON.parse(payload.body);
        var messageElement = document.createElement('li');

        if (message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' joined!';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' left!';
        } else {
            messageElement.classList.add('chat-message');

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(message.sender[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(message.sender);
            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(message.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);
        messageElement.appendChild(textElement);
        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight; // Scroll to the bottom
    }

    function getAvatarColor(messageSender) {
        var hash = 0;
        for (var i = 0; i < messageSender.length; i++) {
            hash = 31 * hash + messageSender.charCodeAt(i);
        }
        var index = Math.abs(hash % colors.length);
        return colors[index];
    }

    // Event listeners
    usernameForm.addEventListener('submit', connect);
    messageForm.addEventListener('submit', sendMessage);
    document.querySelector('#createRoomButton').addEventListener('click', createRoom); // Add event to create a new room
});
