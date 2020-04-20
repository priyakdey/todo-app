package io.todo.api.repository.impl;

public enum UserQueries {
    IF_EXISTS("select u from User u join u.userProfile up where u.username = :username or up.email = :email");

    private final String queryString;

    UserQueries(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }
}
