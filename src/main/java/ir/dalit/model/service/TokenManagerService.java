package ir.dalit.model.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class TokenManagerService {

    private SecretKey secretKey;

    @PostConstruct
    public void configure() {
        secretKey = Jwts.SIG.HS512.key().build();
        log.info(">>> HS512 Secret key has been generated");
    }

    public Claims getClaims(String jwt) {
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
    }

    public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
        return Jwts.builder().claims(claims).subject(userDetails.getUsername()).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + 1000 * 3600)) //one hour
                .signWith(secretKey).compact();
    }

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public boolean isValid(Claims claims,UserDetails userDetails){
        String username = claims.getSubject();
        return userDetails.getUsername().equals(username) && !isTokenExpired(claims);
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }


}
