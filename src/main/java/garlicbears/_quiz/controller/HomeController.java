package garlicbears._quiz.controller;

import garlicbears._quiz.domain.user.domain.User;
import garlicbears._quiz.global.domain.Active;
import garlicbears._quiz.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {
    @GetMapping("/")
    public @ResponseBody String home() {
        return "Hello World";
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }


}
