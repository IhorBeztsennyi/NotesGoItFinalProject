package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.goit.note.exeptions.NoteNameIsAlreadyExistException;
import ua.goit.note.exeptions.NoteNotFoundException;
import ua.goit.users.UserDao;
import ua.goit.users.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteConverter noteConverter;

    @Autowired
    public NoteService(NoteRepository noteRepository, UserRepository userRepository, NoteConverter noteConverter) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.noteConverter = noteConverter;
    }

    public void saveOrUpdate(NoteDto note) {
        validateNoteName(note);
        noteRepository.save(noteConverter.toDao(note));
    }

    public void validateNoteName(NoteDto dto) {
        Set<NoteDao> noteSet = userRepository.findByUsername(dto.getUser().getUsername()).get().getNotes();
        for (NoteDao note : noteSet) {
            if (note.getName().equals(dto.getName())) {
                throw new NoteNameIsAlreadyExistException("Note with name \"" + note.getName() +
                        "\" already exists!");
            }
        }
    }

    public List<NoteDto> findAll(UUID id) {
        Optional<UserDao> user = userRepository.findById(id);
        return StreamSupport.stream(user.get().getNotes().spliterator(), false)
                .map(noteConverter::toDto)
                .collect(Collectors.toList());

    }

    public List<NoteDto> findAllPublicNotes() {
        return StreamSupport.stream(noteRepository.findAllPublicNotes(Access.ACCESS_PUBLIC).spliterator(), false)
                .map(noteConverter::toDto)
                .collect(Collectors.toList());
    }

    public void delete(NoteDto noteDto) {
        noteRepository.delete(noteConverter.toDao(noteDto));
    }

    public NoteDto findById(UUID id) {
        return noteConverter.toDto(noteRepository.findById(id)
                .orElseThrow(() -> new NoteNotFoundException("Note does not exist")));
    }

    public NoteDto findByName(String noteName, String username) {
        Set<NoteDao> noteSet = userRepository.findByUsername(username).get().getNotes();
        for (NoteDao note : noteSet) {
            if (note.getName().equals(noteName.trim())) {
                return noteConverter.toDto(note);
            }
        }
        throw new NoteNameIsAlreadyExistException(" . . . . . Note with name \"" + noteName +
                "\" doesn't exists!");
    }
}
