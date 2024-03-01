package ir.dalit.model.service;

import io.jsonwebtoken.Claims;
import ir.dalit.model.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import static ch.qos.logback.core.CoreConstants.EMPTY_STRING;

@Component
@RequiredArgsConstructor
public class JWTAuthFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String BEARER_KEYWORD = "Bearer "; //it has space.
    private final TokenManagerService tokenManagerService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final var authenticationHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
        final String jwt;
        final String username;
        if (Objects.isNull(authenticationHeader) || !authenticationHeader.startsWith(BEARER_KEYWORD)) {
            filterChain.doFilter(request, response);
        } else {
            jwt = authenticationHeader.replace(BEARER_KEYWORD, EMPTY_STRING);
            Claims claims = tokenManagerService.getClaims(jwt);
            username = claims.getSubject();
            if (Objects.nonNull(username) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (tokenManagerService.isValid(claims, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());
                    WebAuthenticationDetails webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
                    authenticationToken.setDetails(webAuthenticationDetails);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request,response);
                }
            }
        }
    }


}
