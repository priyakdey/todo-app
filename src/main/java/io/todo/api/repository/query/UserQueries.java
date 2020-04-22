package io.todo.api.repository.query;

public enum UserQueries {
    IF_EXISTS("select u from User u join u.userProfile up where u.username = :username or up.email = :email"),
    LOAD_BY_USERNAME("select u from User u join fetch u.authorities ua where u.username = :username"),
    FETCH_TASKS("select u from User u left join fetch u.tasks t where u.username = :username");

    private final String queryString;

    UserQueries(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }
}
