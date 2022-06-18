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

    @GetMapping(name = "/registration")
    public String getRegistrationForm() {
        return "registration";
    }

    @PostMapping(name = "registration")
    public String registrationUser(@ModelAttribute("userForm") @Valid UserDto user,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
        } catch (UsernameAlreadyExistException | UserEmailAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            return "registration";
        }
        return "login";
    }

    @GetMapping(path = "/list")
    public String getListUsers(Model model) {
        List<UserDto> users = userService.findAll();
        model.addAttribute("users", users);
        return "listUsers";
    }

    @GetMapping(path = "/create/form")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createUserForm(Model model) {
        List<UserRole> userRoles = Arrays.asList(UserRole.values());
        model.addAttribute("userRoles", userRoles);
        return "createUserForm";
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String createUser(@ModelAttribute("userForm") @Valid UserDto user,
                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addAttribute("userRoles", userRoles);
            return "createUserForm";
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addOrUpdate(user);
        } catch (UsernameAlreadyExistException | UserEmailAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addAttribute("userRoles", userRoles);
            return "createUserForm";
        }
        return "redirect:/users/list";
    }

    @ModelAttribute("userForm")
    public UserDto getDefaultUserDto() {
        return new UserDto();
    }

    @PostMapping(name = "/edit/form/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editUserForm(@PathVariable("id") UUID id, Model model) {
        UserDto user = userService.findById(id);
        model.addAttribute("user", user);
        List<UserRole> userRoles = Arrays.asList(UserRole.values());
        model.addAttribute("userRoles", userRoles);
        return "editUserForm";
    }

    @PostMapping(name = "/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editUser(@PathVariable("id") UUID id, @ModelAttribute("userForm") @Valid UserDto userDto,
                                 BindingResult bindingResult, ModelAndView model) {
        if (bindingResult.hasErrors()) {
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addObject("userRoles", userRoles);
            model.setViewName("editUserForm");
            model.setStatus(HttpStatus.BAD_REQUEST);
            return model;
        }
        try {
            UserDto user = userService.findById(id);
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            user.setUserRole(userDto.getUserRole());
            if (user.getEmail().equals(userDto.getEmail()) | user.getUsername().equals(userDto.getUsername())) {
                userService.delete(userDto);
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.addOrUpdate(user);
            model.setViewName("redirect:/users/list");
        } catch (UsernameAlreadyExistException | UserEmailAlreadyExistException ex) {
            model.addObject("message", ex.getMessage());
            List<UserRole> userRoles = Arrays.asList(UserRole.values());
            model.addObject("userRoles", userRoles);
            model.setViewName("editUserForm");
        }
        return model;
    }

    @PostMapping(name = "/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") UUID id) {
        UserDto user = userService.findById(id);
        userService.delete(user);
        return "redirect:/users/list";
    }
}
