package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NoteService {

    private final NoteRepository repository;

    private final NoteConverter converter;

@Autowired
    public NoteService(NoteRepository repository, NoteConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }
}
