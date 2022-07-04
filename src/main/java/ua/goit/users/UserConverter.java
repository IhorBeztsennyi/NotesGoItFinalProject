package ua.goit.users;

import org.springframework.stereotype.Service;

@Service
public class UserConverter {

    public UserDto toDto(UserDao dao) {
        return new UserDto.UserBuilder()
                .withId(dao.getId())
                .withUsername(dao.getUsername())
                .withEmail(dao.getEmail())
                .withPassword(dao.getPassword())
                .withUserRole(dao.getUserRole())
                .build();
    }

    public UserDao toDao(UserDto dto) {
        UserDao dao = new UserDao();
        dao.setId(dto.getId());
        dao.setUsername(dto.getUsername());
        dao.setEmail(dto.getEmail());
        dao.setPassword(dto.getPassword());
        dao.setUserRole(dto.getUserRole());
        return dao;
    }
}
