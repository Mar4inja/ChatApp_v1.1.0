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

    var colors = [
        '#2196F3', '#32c787', '#00BCD4', '#ff5652',
        '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
    ];

    function connect(event) {
        username = document.querySelector('#name').value.trim();

        if (username) {
            if (isLoggedIn()) {
                usernamePage.classList.add('hidden');
                chatPage.classList.remove('hidden');

                var socket = new SockJS('http://localhost:8080/ws'); // Adjust to your WebSocket endpoint
                stompClient = Stomp.over(socket);

                stompClient.connect({}, function(frame) {
                    console.log('Connected: ' + frame);
                    onConnected(); // Call onConnected function
                }, onError);
            } else {
                alert('You must log in to access the chat.'); // Alert if not logged in
            }
        }
        event.preventDefault();
    }

    function isLoggedIn() {
        return document.cookie.split(';').some((item) => item.trim().startsWith('token='));
    }

    function onConnected() {
        stompClient.subscribe('/topic/public', onMessageReceived);
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
        connectingElement.classList.add('hidden');
    }

    function onError(error) {
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
    }

    function sendMessage(event) {
        var messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            var chatMessage = {
                sender: username,
                content: messageContent,
                type: 'CHAT'
            };
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
        event.preventDefault();
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
        messageArea.scrollTop = messageArea.scrollHeight;
    }

    function getAvatarColor(messageSender) {
        var hash = 0;
        for (var i = 0; i < messageSender.length; i++) {
            hash = 31 * hash + messageSender.charCodeAt(i);
        }
        var index = Math.abs(hash % colors.length);
        return colors[index];
    }

    document.querySelector('#leaveChatBtn').addEventListener('click', function() {
        if (stompClient) {
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
                sender: username,
                type: 'LEAVE'
            }));
            stompClient.disconnect();
        }
        chatPage.classList.add('hidden');
        usernamePage.classList.remove('hidden');
    });

    document.querySelector('#logoutBtn').addEventListener('click', function() {
        if (stompClient) {
            stompClient.send("/app/chat.sendMessage", {}, JSON.stringify({
                sender: username,
                type: 'LEAVE'
            }));
            stompClient.disconnect();
        }
        // Optionally, clear the token cookie or perform other cleanup
        document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:01 GMT;'; // Clear token cookie

        chatPage.classList.add('hidden'); // Hide chat page
        usernamePage.classList.remove('hidden'); // Show username page
    });

    usernameForm.addEventListener('submit', connect, true);
    messageForm.addEventListener('submit', sendMessage, true);

    // Hide chatPage initially if not logged in
    if (!isLoggedIn()) {
        chatPage.classList.add('hidden');
    }
});
