package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.goit.users.UserDto;
import ua.goit.users.UserService;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }
    @GetMapping("/list")
    public String getNotes(Model model){
        List<NoteDto> notes = noteService.viewNotes();
        model.addAttribute("notes", notes);
        return "list_notes";
    }

    @GetMapping("/create")
    public String createNoteForm(Model model){

        model.addAttribute("note", new NoteDto());
        return "create_note";
    }

    @PostMapping("/createNote")
    public String saveNote(NoteDto note){
        try {
            noteService.createNote(note);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        return "redirect:/note/list";
    }

    @GetMapping("/editNote")
    public String editNote(Model m, @RequestParam("id") UUID id){
        try {
            NoteDto note = noteService.findById(id);
            m.addAttribute("note", note);
        }catch (RuntimeException e){
            return e.getMessage();

        }
        return "updateNote";
    }

    @PutMapping("/updateNote")
    public String updateNote(@ModelAttribute NoteDto note){
        try {
            noteService.editNote(note);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        return "redirect:/note/list";
    }
    @DeleteMapping("/deleteNote")
    public String deleteNote(@RequestParam UUID id){
        try {
            noteService.deleteNoteById(id);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        return "redirect:/note/list";
    }


}
