package de.ait.chat.exceptions;

public class ConfirmationCodeExpiredException extends IllegalStateException {
    public ConfirmationCodeExpiredException(String message) {
        super(message);
    }
}
