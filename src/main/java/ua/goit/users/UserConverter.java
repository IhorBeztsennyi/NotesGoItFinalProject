package ua.goit.users;

import org.springframework.stereotype.Service;

@Service
public class UserConverter {

    public UserDto toDto(UserDao dao) {
        UserDto dto = new UserDto();
        dto.setUsername(dao.getUsername());
        dto.setEmail(dao.getEmail());
        dto.setPassword(dao.getPassword());
        dto.setUserRole(dao.getUserRole());
        return dto;
    }

    public UserDao toDao(UserDto dto) {
        UserDao dao = new UserDao();
        dao.setUsername(dto.getUsername());
        dao.setEmail(dao.getEmail());
        dao.setPassword(dto.getPassword());
        dao.setUserRole(dto.getUserRole());
        return dao;
    }
}
