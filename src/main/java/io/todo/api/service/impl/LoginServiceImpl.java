package io.todo.api.service.impl;

import io.todo.api.entity.dto.UserDetailsDTO;
import io.todo.api.repository.UserRepository;
import io.todo.api.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

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
        UserDetailsDTO userDetailsDTO = userRepository.loadByUsername(username)
                                                        .orElseThrow(
                                                                () -> new UsernameNotFoundException("user not found")
                                                        );
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                //TODO: Implement Authority based ACL
                return new ArrayList<SimpleGrantedAuthority>();
            }

            @Override
            public String getPassword() {
                return userDetailsDTO.getPassword();
            }

            @Override
            public String getUsername() {
                return userDetailsDTO.getUsername();
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
                // TODO: No need to change creds
                return true;
            }

            @Override
            public boolean isEnabled() {
                return userDetailsDTO.isEnabled();
            }
        };

    }
}
