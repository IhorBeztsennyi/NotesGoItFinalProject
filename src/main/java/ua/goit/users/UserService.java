package ua.goit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.goit.users.exception.UserEmailAlreadyExistException;
import ua.goit.users.exception.UsernameAlreadyExistException;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserConverter userConverter;

    @Autowired
    public UserService(UserRepository userRepository, UserConverter userConverter) {
        this.userConverter = userConverter;
        this.userRepository = userRepository;
    }

    public void save(UserDto userDto) {
        validateUsername(userDto);
        validateEmail(userDto);

        userDto.setUserRole(UserRole.ROLE_USER);
        userRepository.save(userConverter.toDao(userDto));
    }

    public List<UserDto> findAll() {
        return StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(userConverter::toDto)
                .collect(Collectors.toList());
    }

    public void addOrUpdate(UserDto userDto) {
        validateUsername(userDto);
        validateEmail(userDto);

        userRepository.save(userConverter.toDao(userDto));
    }

    public void delete(UserDto userDto) {
        userRepository.delete(userConverter.toDao(userDto));
    }

    public void validateUsername(UserDto dto) {
        userRepository.findByUsername(dto.getUsername())
                .ifPresent((username) -> {
                    throw new UsernameAlreadyExistException("User with username " + username.getUsername() +
                            " already exists!");
                });
    }

    public void validateEmail(UserDto dto) {
        userRepository.findByEmail(dto.getEmail())
                .ifPresent((email) -> {
                    throw new UserEmailAlreadyExistException("User with email " + email.getEmail() +
                            " already exists!");
                });
    }


}
