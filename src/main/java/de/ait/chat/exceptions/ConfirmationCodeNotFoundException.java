package de.ait.chat.exceptions;

public class ConfirmationCodeNotFoundException extends IllegalArgumentException {
    public ConfirmationCodeNotFoundException(String message) {
        super(message);
    }
}