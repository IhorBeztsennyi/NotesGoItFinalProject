package ua.goit.users;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ua.goit.users.exception.UserEmailAlreadyExistException;
import ua.goit.users.exception.UsernameAlreadyExistException;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping(path = "/list")
    public String getListUsers(Model model) {
        List<UserDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "listUsers";
    }

    @GetMapping(path = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createUserForm(Model model) {
        List<UserRole> userRoles = Arrays.asList(UserRole.values());
        model.addAttribute("userRoles", userRoles);
        return "createUser";
    }

    @PostMapping(path = "/create")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createUser(@Valid UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addAttribute("userRoles", userRoles);
            return "createUser";
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addOrUpdate(user);
        } catch (UsernameAlreadyExistException | UserEmailAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addAttribute("userRoles", userRoles);
            return "createUser";
        }
        return "redirect:/users/list";
    }

    @ModelAttribute("user")
    public UserDto getDefaultUserDto() {
        return new UserDto();
    }

    @GetMapping(path = "/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUserForm(@PathVariable("id") UUID id, Model model) {
        UserDto user = userService.findById(id);
        model.addAttribute("user", user);
        List<UserRole> userRoles = Arrays.asList(UserRole.values());
        model.addAttribute("userRoles", userRoles);
        return "editUser";
    }

    @PostMapping(path = "/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUser(@PathVariable("id") UUID id, @Valid UserDto userDto,
                           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addAttribute("userRoles", userRoles);
            return "editUser";
        }
        try {
            UserDto user = userService.findById(id);
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setUserRole(userDto.getUserRole());
            user.setId(userDto.getId());
            if (user.getEmail().equals(userDto.getEmail())) {
                userService.delete(userDto);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addOrUpdate(user);
            return "redirect:/users/list";
        } catch (UsernameAlreadyExistException | UserEmailAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addAttribute("userRoles", userRoles);
            return  "editUser";
        }
    }

    @GetMapping(path = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") UUID id) {
        UserDto user = userService.findById(id);
        userService.delete(user);
        return "redirect:/users/list";
    }
}
