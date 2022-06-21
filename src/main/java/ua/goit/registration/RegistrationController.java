package ua.goit.registration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import ua.goit.users.UserDto;
import ua.goit.users.UserService;
import ua.goit.users.exception.UserEmailAlreadyExistException;
import ua.goit.users.exception.UsernameAlreadyExistException;

import javax.validation.Valid;
import java.util.Collections;

@Controller
public class RegistrationController {

    private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";

    private final UserService userService;

    private final RestTemplate restTemplate;

    private final PasswordEncoder passwordEncoder;

    @Value("${recaptcha.secret}")
    private String secret;

    public RegistrationController(UserService userService, RestTemplate restTemplate, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.restTemplate = restTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/users/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("users/registration")
    public String addUser(
            @RequestParam("g-recaptcha-response") String captchaResponse,
            @Valid UserDto userDto,
            Model model
            ) {
        String url = String.format(CAPTCHA_URL, secret, captchaResponse);
        CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);

        if (!(response != null && response.isSuccess())) {
            model.addAttribute("message", "Fill the CAPTCHA");
            return "registration";
        }
        try {
            userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userService.save(userDto);
        } catch (UsernameAlreadyExistException | UserEmailAlreadyExistException ex) {
            model.addAttribute("message", ex.getMessage());
            return "registration";
        }

        return "redirect:/login";
    }
}
