package io.todo.api.security;

import io.todo.api.security.filter.CustomAuthorizationFilter;
import io.todo.api.security.filter.CustomUsernameAuthenticationFilter;
import io.todo.api.security.jwt.JwtBean;
import io.todo.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.Filter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

/**
 * This class is a configuration class to customize Web Security Config and resource ACL
 */
@Configuration
@EnableWebSecurity
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
                .antMatchers(POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(authenticationFilter())
                .addFilterAfter(authorizationFilter(), CustomUsernameAuthenticationFilter.class);
    }

    private Filter authorizationFilter() throws Exception {
        return new CustomAuthorizationFilter(super.authenticationManager(), jwtBean);
    }

    private Filter authenticationFilter() throws Exception {
        return new CustomUsernameAuthenticationFilter(super.authenticationManager(), jwtBean);
    }
}
