package io.todo.api.security.filter;

import io.todo.api.security.jwt.JwtBean;
import io.todo.api.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.EXPIRES;

/**
 * This class is to intercept all secured resource related requests to filter out the ones who are Authorized
 */
public class CustomAuthorizationFilter extends BasicAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomAuthorizationFilter.class);

    private final JwtBean jwtBean;


    public CustomAuthorizationFilter(AuthenticationManager authenticationManager, JwtBean jwtBean) {
        super(authenticationManager);
        this.jwtBean = jwtBean;
    }

    @Override
    protected void doFilterInternal
            (HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("Username");
        String token = request.getHeader(AUTHORIZATION);


        if (username == null || token == null || !token.startsWith(jwtBean.getPrefix()))
            // TODO: Bad request. Custom exception
            throw new RuntimeException("Invalid request/Headers Missing");

        boolean isValid = JwtUtils.validateBearerToken(username, token.split(" ")[1], jwtBean);

    }
}
