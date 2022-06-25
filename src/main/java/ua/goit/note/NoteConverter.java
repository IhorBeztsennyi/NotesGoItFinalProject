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
        NoteDto dto = new NoteDto();
        dto.setId(dao.getId());
        dto.setName(dao.getName());
        dto.setContent(dao.getContent());
        dto.setAccessType(dao.getAccessType());
        dto.setUser(converter.toDto(dao.getUser()));
        return dto;
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
