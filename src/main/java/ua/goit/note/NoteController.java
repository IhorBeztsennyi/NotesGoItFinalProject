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
    public Page<NoteDto> getNotes(@PageableDefault(sort = "id", direction= Sort.Direction.DESC,size = 5) Pageable page,
                                  Principal currentlyLoggedUser, Model model){
        String username = currentlyLoggedUser.getName();
        UserDto user = userService.findByName(username);
        Page<NoteDto> notes = noteService.viewNotes(page, user.getId());
        model.addAttribute("pageNumber", page.getPageNumber());
        model.addAttribute("pageSize", page.getPageSize());
        model.addAttribute("notes", notes);
        return notes;
    }
    @PostMapping("/createNote")
    public String saveNote(NoteDto note, Principal currentlyLoggedUser){
        try {
            String userName = currentlyLoggedUser.getName();
            UserDto user = userService.findByName(userName);
            note.setUser(user);
            noteService.createNote(note);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        return "note/list";
    }

    @GetMapping("/editNote")
    public String editNote(Model m, @RequestParam("id") UUID id){
        try {
            NoteDto note = noteService.findById(id);
            m.addAttribute("note", note);
        }catch (RuntimeException e){
            return e.getMessage();

        }
        return "note/updateNote";
    }

    @PutMapping("/updateNote")
    public String updateNote(@ModelAttribute NoteDto note, Principal currentlyLoggedUser){
        try {
            String userName = currentlyLoggedUser.getName();
            UserDto user = userService.findByName(userName);
            note.setUser(user);
            noteService.editNote(note);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        return "note/list";
    }
    @DeleteMapping("/deleteNote")
    public String deleteNote(@RequestParam UUID id){
        try {
            noteService.deleteNoteById(id);
        }catch (RuntimeException e){
            return e.getMessage();
        }
        return "note/list";
    }

    public NoteDto findNoteByName(String name){
        return noteService.findByName(name);
    }

}
