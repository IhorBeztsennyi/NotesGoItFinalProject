package ua.goit.users.exception;

public class UserEmailAlreadyExistException extends RuntimeException {

    public UserEmailAlreadyExistException(String message) {
        super(message);
    }
}
