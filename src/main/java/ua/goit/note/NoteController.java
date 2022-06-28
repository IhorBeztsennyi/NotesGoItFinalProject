package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.goit.note.exeptions.NoteNameIsAlreadyExistException;
import ua.goit.users.UserDto;
import ua.goit.users.UserService;

import javax.validation.Valid;
import java.util.*;

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
            noteService.save(note);
        } catch (NoteNameIsAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            return "createNote";
        }
        return "redirect:/notes/list";
    }

    @GetMapping(path = "/edit/{id}")
    public String editNoteForm(@PathVariable("id") UUID id, Model model) {
        NoteDto note = noteService.findById(id);
        model.addAttribute("note", note);
        List<Access> access = Arrays.asList(Access.values());
        model.addAttribute("access", access);
        return "editNotes";
    }

    @PostMapping("/edit/{id}")
    public String editNote(@PathVariable("id") UUID id, @ModelAttribute("note") NoteDto noteDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "editNotes";
        }
        try {
            NoteDto note = noteService.findById(id);
            note.setName(noteDto.getName());
            note.setContent(noteDto.getContent());
            note.setAccessType(noteDto.getAccessType());
            noteService.update(note);
            return "redirect:/notes/list";
        } catch (NoteNameIsAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            return "editNotes";
        }

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
        model.addAttribute("users", users);
        model.addAttribute("note", note);
        return "shareNote";
    }

    @GetMapping(path = "/share/note/{userId}/{noteId}")
    public String shareNote(@PathVariable("userId") UUID userId, @PathVariable("noteId") UUID noteId, Authentication authentication, Model model) {
        UserDto userReceiver = userService.findById(userId);
        UserDto userSender = userService.loadUserByUserName(authentication.getName());
        NoteDto sendersNote = noteService.findById(noteId);
        NoteDto receiversNote = new NoteDto.Builder()
                .withName(sendersNote.getName() + " from " + userSender.getUsername())
                .withContent(sendersNote.getContent())
                .withAccessType(Access.ACCESS_PRIVATE)
                .withUser(userReceiver)
                .build();
        noteService.saveOrUpdate(receiversNote);
        return "redirect:/notes/list";
    }

    @GetMapping(path = "/find")
    public String findNoteForm(Model model) {
        return "findNoteForm";
    }

    @RequestMapping(path = "/find/name")
    public String getVendor(@RequestParam(value = "name", required = false) String name, Model model, Authentication authentication) {
        try {
            UserDto user = userService.loadUserByUserName(authentication.getName());
            NoteDto note = noteService.findByName(name, user.getUsername());
            Set<NoteDto> notes = new HashSet<>();
            notes.add(note);
            model.addAttribute("notes", notes);
        } catch (NoteNameIsAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            return "findNoteForm";
        }
        return "findNote";
    }
}
