package com.personalproject.todo.member;

public enum Auth {

    NONE("ROLE_USER"), EMAIL_AUTH("ROLE_MEMBER"), ALL_AUTH("ROLE_ADMIN");

    private final String role;

    private Auth(String role) {
        this.role = role;
    }

    public String value() {
        return role;
    }

    public static Auth of(String value) {
        if (value == null) return null;
        for (Auth auth : Auth.values()) {
            if (auth.name().equalsIgnoreCase(value)) {
                return auth;
            }
        }
        return null;
    }
}
