package io.todo.api.entity;

public enum UserAuthority {
    USER_READ("user:read"),
    USER_UPDATE("user:update");


    private final String permission;

    UserAuthority(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
