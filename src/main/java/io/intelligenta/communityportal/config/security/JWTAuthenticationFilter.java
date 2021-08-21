package io.intelligenta.communityportal.config.security;

import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.intelligenta.communityportal.models.dto.UserLogin;
import io.intelligenta.communityportal.models.exceptions.PasswordsNotTheSameException;
import io.intelligenta.communityportal.models.exceptions.UserNotFoundException;
import io.intelligenta.communityportal.models.auth.User;
import io.intelligenta.communityportal.models.auth.UserRole;
import io.intelligenta.communityportal.service.auth.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static io.intelligenta.communityportal.config.security.SecurityConstants.*;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private Logger logger = Logger.getLogger(JWTAuthenticationFilter.class.getName());

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, UserService userService, BCryptPasswordEncoder encoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(LOGIN_URL, "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            UserLogin creds = new ObjectMapper()
                    .readValue(req.getInputStream(), UserLogin.class);
            List<GrantedAuthority> authorities = new ArrayList<>();

            try {
                User user = userService.findByEmailAndActive(creds.getEmail().toLowerCase(), true);
                UserRole role = user.getRole();
                authorities.add(new SimpleGrantedAuthority(role.getAuthority()));
                if (!userService.passwordMatches(user, creds.getPassword())) {
                    throw new PasswordsNotTheSameException();
                }
            } catch (UserNotFoundException u) {
                throw new UserNotFoundException();
            }

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail().toLowerCase(),
                            creds.getPassword(),
                            authorities)
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException {

        UserDetails userDetails = (org.springframework.security.core.userdetails.User) auth.getPrincipal();

        String token = JWT.create()
                .withSubject(userDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("authority", (userDetails).getAuthorities().iterator().next().toString())
                .sign(HMAC512(SECRET.getBytes()));
        res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
        res.getWriter().append(token);
    }
}
