package ua.goit.note.exeptions;

public class NoteNameIsAlreadyExistException extends RuntimeException {

    public NoteNameIsAlreadyExistException(String message) {
        super(message);
    }
}
