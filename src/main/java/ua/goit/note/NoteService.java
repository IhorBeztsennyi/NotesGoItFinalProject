package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.goit.note.exeptions.NoteNameIsAlreadyExistException;
import ua.goit.users.UserDao;
import ua.goit.users.UserDto;
import ua.goit.users.UserRepository;
import ua.goit.users.exception.UsernameAlreadyExistException;

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
        noteRepository.findByName(dto.getName())
                .ifPresent((note) -> {
                    throw new NoteNameIsAlreadyExistException("Note with name " + note.getName() +
                            " already exists!");
                });
    }

//    public List<NoteDto> findAll(UUID id) {
//        return StreamSupport.stream(noteRepository.findAllByUserId(id).spliterator(), false)
//                .map(noteConverter::toDto)
//                .collect(Collectors.toList());
//    }

    public List<NoteDto> findAll(UUID id) {
        Optional<UserDao> user = userRepository.findById(id);
        return StreamSupport.stream(user.get().getNotes().spliterator(), false)
                .map(noteConverter::toDto)
                .collect(Collectors.toList());

    }
}
