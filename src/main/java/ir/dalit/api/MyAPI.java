package ir.dalit.api;

import ir.dalit.model.constants.Roles;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/my-api")
public class MyAPI {

    @GetMapping("/test")
    @Secured({Roles.DEFAULT})
    public ResponseEntity<String> testAPI(){
        return ResponseEntity.ok("Every thing is ok !");
    }
}
