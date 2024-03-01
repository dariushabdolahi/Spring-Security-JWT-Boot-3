package ir.dalit.api;

import ir.dalit.model.domain.User;
import ir.dalit.model.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationAPI {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody User user){
        return ResponseEntity.ok(authenticationService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody User user){
        return ResponseEntity.ok(authenticationService.login(user));
    }

}
