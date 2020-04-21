package io.todo.api.repository.impl;

public enum UserQueries {
    IF_EXISTS("select u from User u join u.userProfile up where u.username = :username or up.email = :email"),
    LOAD_BY_USERNAME("select new io.todo.api.entity.dto.UserDetailsDTO(u.username, u.password, u.enabled) from User u where u.username = :username");

    private final String queryString;

    UserQueries(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }
}
