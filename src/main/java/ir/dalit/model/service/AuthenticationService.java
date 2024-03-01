package ir.dalit.model.service;

import ir.dalit.model.constants.Roles;
import ir.dalit.model.domain.User;
import ir.dalit.model.domain.UserRoles;
import ir.dalit.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenManagerService tokenManagerService;
    private final AuthenticationManager authenticationManager;

    public User register(User user) {
        UserRoles defaultRole = new UserRoles().setRole(Roles.DEFAULT);
        user
                .setEnabled(true)
                .setExpiration(LocalDateTime.now().plusYears(1))
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setLockStatus(false)
                .setUserRoles(List.of(defaultRole));
        userRepository.save(user);
        return user;
    }


    public String login(User user) {
        UserDetails userDetail = userRepository
                .findByUsername(user.getUsername())
                .filter(userDetails -> passwordEncoder.matches(user.getPassword(),userDetails.getPassword()))
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        UsernamePasswordAuthenticationToken authenticationToken =   new UsernamePasswordAuthenticationToken(
                userDetail,
                user.getPassword(),
                userDetail.getAuthorities());
        authenticationManager.authenticate(authenticationToken);
         return tokenManagerService.generateToken(userDetail);
    }
}
