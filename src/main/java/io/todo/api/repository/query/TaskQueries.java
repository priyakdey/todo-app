package io.todo.api.repository.query;

public enum TaskQueries {
    FIND_ALL("select t from Task t where t.user.username = :username order by t.startedOn desc"),
    FIND_FILTERED("select t from Task t where t.user.username = :username and t.completed = :completed order by t.startedOn desc");

    private final String queryString;

    TaskQueries(String queryString) {
        this.queryString = queryString;
    }

    public String getQueryString() {
        return queryString;
    }
}
