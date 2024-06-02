package com.example.sqlinjection;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private final UserRepository userRepository;

    public LoginController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/login")
    public String loginMain(Model model) {
        model.addAttribute("message", "<script>alert('悪い処理')</script>");
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session) {
        Optional<User> user = userRepository.findByUsernameAndPasswordBad(email, password);
        if (user.isEmpty()) {
            return "redirect:/login?error";
        }
        session.setAttribute("user", user.get());
        return "redirect:/";
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
