package io.todo.api.security;

import io.todo.api.security.filter.CustomAuthorizationFilter;
import io.todo.api.security.filter.CustomUsernameAuthenticationFilter;
import io.todo.api.security.jwt.JwtBean;
import io.todo.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;

import static io.todo.api.entity.UserAuthority.USER_READ;
import static org.springframework.http.HttpMethod.*;

/**
 * This class is a configuration class to customize Web Security Config and resource ACL
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final LoginService loginService;
    private final PasswordEncoder passwordEncoder;
    private final JwtBean jwtBean;

    @Autowired
    public AppWebSecurityConfiguration(LoginService loginService, PasswordEncoder passwordEncoder, JwtBean jwtBean) {
        this.loginService = loginService;
        this.passwordEncoder = passwordEncoder;
        this.jwtBean = jwtBean;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(loginService).passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(POST, "/signup")
                .antMatchers(GET, "/verify");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(GET, "/users/**").hasAnyAuthority(USER_READ.getPermission(), USER_READ.getPermission())
                .antMatchers(PUT, "/users/**").hasAnyAuthority(USER_READ.getPermission(), USER_READ.getPermission())
                .antMatchers(GET, "/users/**").hasAnyAuthority(USER_READ.getPermission(), USER_READ.getPermission())
                .antMatchers(POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter())
                .addFilterAfter(authorizationFilter(), CustomUsernameAuthenticationFilter.class);
    }

    private Filter authorizationFilter() throws Exception {
        return new CustomAuthorizationFilter(super.authenticationManager(), loginService, jwtBean);
    }

    private Filter authenticationFilter() throws Exception {
        return new CustomUsernameAuthenticationFilter(super.authenticationManager(), jwtBean);
    }
}
