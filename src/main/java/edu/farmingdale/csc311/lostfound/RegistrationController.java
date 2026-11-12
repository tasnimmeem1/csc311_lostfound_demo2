package edu.farmingdale.csc311.lostfound;

import edu.farmingdale.csc311.lostfound.user.AppUser;
import edu.farmingdale.csc311.lostfound.user.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;

    public RegistrationController(AppUserRepository users, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password,
            Model model) {
        if (username == null || username.length() < 3) {
            return showError(model, username, "Username must be at least 3 characters.");
        }
        if (password == null || password.length() < 6) {
            return showError(model, username, "Password must be at least 6 characters.");
        }
        if (users.existsByUsername(username)) {
            return showError(model, username, "That username is already taken.");
        }

        users.save(new AppUser(username, passwordEncoder.encode(password)));
        return "redirect:/login?registered";
    }

    private String showError(Model model, String username, String message) {
        model.addAttribute("username", username);
        model.addAttribute("error", message);
        return "register";
    }
}
