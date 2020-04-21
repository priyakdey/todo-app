package io.todo.api.service.impl;

import io.todo.api.entity.User;
import io.todo.api.repository.UserRepository;
import io.todo.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Service
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;

    @Autowired
    public LoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.loadByUsername(username)
                                .orElseThrow(
                                    () -> new RuntimeException("No user found by username")
                                );

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
                user.getAuthorities()
                                    .stream()
                                    .map(userAuthority -> new SimpleGrantedAuthority(userAuthority.getPermission()))
                                    .forEach(grantedAuthorities::add);
                return grantedAuthorities;
            }

            @Override
            public String getPassword() {
                return user.getPassword();
            }

            @Override
            public String getUsername() {
                return user.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                // TODO: Currently Account is always active
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                // TODO: Currently account is never locked
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                // TODO: No need to change creds/ credentials do not expire
                return true;
            }

            @Override
            public boolean isEnabled() {
                return user.isEnabled();
            }
        };

    }
}
