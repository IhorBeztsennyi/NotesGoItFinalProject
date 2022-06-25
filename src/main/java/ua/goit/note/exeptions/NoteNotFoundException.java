package ua.goit.note.exeptions;

public class NoteNotFoundException extends RuntimeException {

    public NoteNotFoundException(String message) {
        super(message);
    }
}
