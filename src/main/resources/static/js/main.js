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

              var socket = new SockJS('http://localhost:8080/ws'); // Make sure this URL is correct
              stompClient = Stomp.over(socket);

              stompClient.connect({}, function(frame) {
                  console.log('Connected: ' + frame); // Added log message
                  // Get the selected room from the dropdown
                  var selectedRoom = document.querySelector('#room').value;
                  onConnected(selectedRoom); // Pass selectedRoom to onConnected
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

    function onConnected(selectedRoom) {
        console.log("Connected to WebSocket server");

        // Subscribe to the topic for the selected room
        stompClient.subscribe('/topic/room/' + selectedRoom, onMessageReceived);

        // Send the join message to the server
        stompClient.send("/app/chat.addUser", {}, JSON.stringify({ sender: username, type: 'JOIN' }));
        connectingElement.classList.add('hidden');
    }


    function onError(error) {
        connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
        connectingElement.style.color = 'red';
    }

  function sendMessage(event) {
      event.preventDefault();
      var messageContent = messageInput.value.trim();
      console.log("Sending message: ", messageContent);
      if (messageContent && stompClient) {
          var selectedRoom = document.querySelector('#room').value; // Get selected room ID

          var chatMessage = {
              sender: username,
              content: messageContent,
              type: 'CHAT',
              roomId: selectedRoom // Include roomId in the message
          };

          // Send the message to the correct destination
          stompClient.send(`/topic/room/${selectedRoom}`, {}, JSON.stringify(chatMessage));

          messageInput.value = ''; // Clear input field
      }
  }



    function onMessageReceived(payload) {
        console.log("Message received: ", payload);
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
