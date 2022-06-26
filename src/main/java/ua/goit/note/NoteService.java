package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NoteService {

    private final NoteRepository repository;

    private final NoteConverter converter;

@Autowired
    public NoteService(NoteRepository repository, NoteConverter converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public void createNote(NoteDto note){
    repository.save(converter.toDao(note));
    }

    public void editNote(NoteDto note){
    repository.save(converter.toDao(note));
    }

    public void deleteNoteById(UUID id){
    repository.deleteById(id);
    }


    public NoteDto findById(UUID id){
    return converter.toDto(repository.findById(id).get());
    }

    public List<NoteDto> viewNotes(){
        return repository.findAll().stream()
                .map(converter::toDto)
                .collect(Collectors.toList());
    }

}
