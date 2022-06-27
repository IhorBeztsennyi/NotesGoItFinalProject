package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.goit.note.exeptions.NoteNameIsAlreadyExistException;
import ua.goit.users.UserDto;
import ua.goit.users.UserRole;
import ua.goit.users.UserService;
import ua.goit.users.exception.UserEmailAlreadyExistException;
import ua.goit.users.exception.UsernameAlreadyExistException;

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
            noteService.saveOrUpdate(note);
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

//    @PostMapping(path = "/edit/{id}")
//    public String editNote(@PathVariable("id") UUID id, @Valid NoteDto noteDto, Authentication authentication,
//                           BindingResult bindingResult, Model model) {
//        if (bindingResult.hasErrors()) {
//            List<Access> access = Arrays.asList(Access.values());
//            model.addAttribute("access", access);
//            return "editNotes";
//        }
//        try {
//            NoteDto note = noteService.findById(id);
//            UserDto user = userService.loadUserByUserName(authentication.getName());
//            note.setName(noteDto.getName());
//            note.setContent(noteDto.getContent());
//            note.setAccessType(noteDto.getAccessType());
//            note.setUser(user);
//            note.setId(noteDto.getId());
//            if (note.getName().equals(noteDto.getName())) {
//                noteService.delete(noteDto);
//            }
//            noteService.update(note);
//            return "redirect:/notes/list";
//        } catch (NoteNameIsAlreadyExistException ex) {
//            model.addAttribute("message", ex.getMessage());
//            List<Access> access = Arrays.asList(Access.values());
//            model.addAttribute("access", access);
//            return "editNotes";
//        }
//    }

//    @GetMapping("/edit/{id}")
//    public String editNoteForm(@PathVariable("id") UUID id, Map<String, Object> model) {
//        NoteDto noteDto = noteService.findById(id);
//        model.put("note", noteDto);
//        return "editNotes";
//    }

    @PostMapping("/edit/{id}")
    public String editNote(@PathVariable("id") UUID id, @ModelAttribute("note") NoteDto noteDto) {
        NoteDto note = noteService.findById(id);
        note.setName(noteDto.getName());
        note.setContent(noteDto.getContent());
        note.setAccessType(noteDto.getAccessType());
        noteService.update(note);
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
        model.addAttribute("users", users);
        model.addAttribute("note", note);
        return "shareNote";
    }

    @GetMapping(path = "/share/note/{userId}/{noteId}")
    public String shareNote(@PathVariable("userId") UUID userId, @PathVariable("noteId") UUID noteId, Authentication authentication, Model model) {
        UserDto userReceiver = userService.findById(userId);
        UserDto userSender = userService.loadUserByUserName(authentication.getName());
        NoteDto sendersNote = noteService.findById(noteId);
        NoteDto receiversNote = new NoteDto();
        receiversNote.setName(sendersNote.getName() + " from " + userSender.getUsername());
        receiversNote.setContent(sendersNote.getContent());
        receiversNote.setAccessType(Access.ACCESS_PRIVATE);
        receiversNote.setUser(userReceiver);
        noteService.saveOrUpdate(receiversNote);
        return "redirect:/notes/list";
    }
}
