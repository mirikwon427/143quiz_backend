package garlicbears._quiz.controller;

import garlicbears._quiz.domain.user.domain.User;
import garlicbears._quiz.global.domain.Active;
import garlicbears._quiz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public @ResponseBody String home() {
        return "Hello User";
    }

    @PostMapping("/signup")
    public String signup(@RequestBody User user) {
        user.setUserPassword(bCryptPasswordEncoder.encode(user.getUserPassword()));
        if (user.getUserActive() == null)
        {
            user.setUserActive(Active.active);
        }
        userRepository.save(user);
        return "success";
    }
}
