package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.goit.users.UserDto;
import ua.goit.users.UserService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/notes")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    @Autowired
    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping(path = "/list")
    public String getListNotes(Model model, Authentication authentication) {
        UserDto user = userService.loadUserByUserName(authentication.getName());
        List<NoteDto> notes = noteService.findAll(user.getId());
        model.addAttribute("notes", notes);
        return "listNotes";
    }

    @GetMapping(path = "/create")
    public String createNoteForm(Model model) {
        List<Access> access = Arrays.asList(Access.values());
        model.addAttribute("access", access);
        return "createNote";
    }

    @PostMapping(path = "/create")
    public String createNote(@Valid NoteDto note, Authentication authentication, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<Access> access = Arrays.asList(Access.values());
            model.addAttribute("access", access);
            return "createNote";
        }
        try {
            UserDto user = userService.loadUserByUserName(authentication.getName());
            note.setUser(user);
            noteService.saveOrUpdate(note);
        } catch (RuntimeException e) {
            return e.getMessage();
        }
        return "redirect:/notes/list";
    }

    @GetMapping(path = "/delete/{id}")
    public String deleteNote(@PathVariable("id") UUID id) {
        NoteDto note = noteService.findById(id);
        noteService.delete(note);
        return "redirect:/notes/list";
    }

    @GetMapping(path = "/listPublic")
    public String getListPublicNotes(Model model) {
        List<NoteDto> notes = noteService.findAllPublicNotes();
        model.addAttribute("notes", notes);
        return "listPublicNotes";
    }

    @GetMapping(path = "/share/{id}")
    public String shareNoteForm(@PathVariable("id") UUID id, Model model) {
        NoteDto note = noteService.findById(id);
        List<UserDto> users = userService.findAll();
        model.addAttribute("note", note);
        model.addAttribute("users", users);
        return "shareNote";
    }
}
