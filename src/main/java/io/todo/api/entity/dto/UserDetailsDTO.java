package io.todo.api.entity.dto;

import io.todo.api.entity.UserAuthority;

import java.util.Set;

public class UserDetailsDTO {
    private String username;
    private String password;
    private Boolean enabled;
    private Set<UserAuthority> authorities;

    public UserDetailsDTO(String username, String password, Boolean enabled, Set<UserAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.enabled = enabled;
        this.authorities = authorities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<UserAuthority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<UserAuthority> authorities) {
        this.authorities = authorities;
    }
}
