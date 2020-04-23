package io.todo.api.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.todo.api.model.request.LoginModel;
import io.todo.api.security.jwt.JwtBean;
import io.todo.api.security.jwt.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static org.springframework.http.HttpHeaders.*;

/**
 * This class is to intercept all requests to /login to authenticate credentials and provide a token if success
 */
public class CustomUsernameAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomUsernameAuthenticationFilter.class);

    private final JwtBean jwtBean;

    public CustomUsernameAuthenticationFilter(AuthenticationManager authenticationManager, JwtBean jwtBean) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtBean = jwtBean;
    }


    /**
     * Method which intercepts the /login requests and attempts to authentication the creds
     * @param request
     * @param response
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication attemptAuthentication
            (HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        LoginModel loginModel = null;
        try {
            loginModel = parse(request);
        } catch (IOException e) {
            LOGGER.error("Error parsing the request.", e);
            //TODO: Authentication Service exception : Data parsing exception
            throw new RuntimeException(e.getMessage());
        }


        if (loginModel != null) {
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(loginModel.getUsername(), loginModel.getPassword());
            return super.getAuthenticationManager().authenticate(authentication);
        }

        // TODO: Custom exception: But this scenario will not occur logically
        throw new RuntimeException("Login Model is null");
    }

    private LoginModel parse(HttpServletRequest request) throws IOException {
       return new ObjectMapper().readValue(request.getInputStream(), LoginModel.class);

    }


    /**
     * In credentials provided is valid and User account is enabled, the method is triggered.
     * This method adds a Bearer token to the header of the response
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication
            (HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        // Get the credentials of the user
        String username = ((UserDetails) authResult.getPrincipal()).getUsername();

        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM dd HH:mm:ss Z yyyy", Locale.getDefault());
        Date iat = new Date();
        Date exp = new Date(System.currentTimeMillis());

        String token = jwtBean.getPrefix() + " " + JwtUtils.generateAuthorizationToken(username, jwtBean);

        response.setHeader("Username", username);
        response.addHeader(AUTHORIZATION, token);
        response.addHeader(DATE, dateFormat.format(iat));
        response.addHeader(EXPIRES, dateFormat.format(exp));
    }



    /**
     * When credentials are bad/ user Account is disabled, this method is triggered
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication
            (HttpServletRequest request, HttpServletResponse response, AuthenticationException failed)
            throws IOException, ServletException {
        LOGGER.info("Authentication failed. Cause - " + failed.getMessage());
        super.unsuccessfulAuthentication(request, response, failed);
    }
}
