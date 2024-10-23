package de.ait.chat.exceptions;

public class UserAlreadyActiveException extends Exception {
    public UserAlreadyActiveException(String message) {
        super(message);
    }
}
