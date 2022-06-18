package ua.goit.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.goit.users.exception.UserEmailAlreadyExistException;
import ua.goit.users.exception.UsernameAlreadyExistException;

import javax.validation.Valid;

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
    public String registrationUser(@ModelAttribute() @Valid UserDto user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "registration";
        }
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
        } catch (UsernameAlreadyExistException | UserEmailAlreadyExistException ex ) {
            model.addAttribute("message", ex.getMessage());
        }
        return "login";
    }

}
