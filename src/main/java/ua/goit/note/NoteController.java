package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class NoteController {

    private final NoteService noteService;

    @Autowired
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    public Page<NoteDto> getNotes(@PageableDefault(sort = "id", direction= Sort.Direction.DESC,size = 5) Pageable p){
        return noteService.viewNotes(p);
    }

    public void saveNote(NoteDto n){
        noteService.createNote(n);
    }

    public void updateNote(NoteDto note){
        noteService.editNote(note);
    }

    public void deleteNote(@RequestParam UUID id){
        noteService.deleteNoteById(id);
    }

    public NoteDto findNoteByName(String name){
        return noteService.findByName(name);
    }

}
