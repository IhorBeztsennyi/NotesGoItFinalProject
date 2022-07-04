package ua.goit.note;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.goit.users.UserConverter;

@Service
public class NoteConverter {

    private final UserConverter converter;

    @Autowired
    public NoteConverter(UserConverter converter) {
        this.converter = converter;
    }

    public NoteDto toDto(NoteDao dao) {

        return new NoteDto.NoteBuilder()
                .withId(dao.getId())
                .withName(dao.getName())
                .withContent(dao.getContent())
                .withAccessType(dao.getAccessType())
                .withUser(converter.toDto(dao.getUser()))
                .build();
    }

    public NoteDao toDao(NoteDto dto) {
        NoteDao dao = new NoteDao();
        dao.setId(dto.getId());
        dao.setName(dto.getName());
        dao.setContent(dto.getContent());
        dao.setAccessType(dto.getAccessType());
        dao.setUser(converter.toDao(dto.getUser()));
        return dao;
    }
}
