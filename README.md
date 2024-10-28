#TalkTown Social-chat application
This is a chat application built with Spring Boot that enables users to communicate in real time. It includes user registration, email confirmation, and authentication for secure interaction.

Features
User Registration: Users can register with a username and password. After registering, email confirmation is required.
Email Confirmation: Users receive an email to confirm their registration.
User Authentication: Only registered and confirmed users can log in to the application.
Real-Time Chat: Users can send and receive messages in real time through WebSocket.
Database Management: User data and chat messages are stored in a database.


Technologies Used
Spring Boot: Simplifies Java application development.
Spring Data JPA: Eases database interactions.
Spring Security: Provides authentication and authorization.
WebSocket: Supports real-time communication.
Liquibase: Manages database version control.
MySQL/PostgreSQL: Stores user and chat data.
Thymeleaf: Template engine for rendering web pages.
JUnit & Spring Security Test: Used for application testing.
Lombok: Reduces boilerplate code.


Getting Started
Database Setup: Configure MySQL/PostgreSQL and update the settings in application.properties.
Running the Application: Use mvn spring-boot:run to start the application.
Register and Confirm Email: Register as a user and confirm your email to access the chat.
