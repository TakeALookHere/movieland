package com.miskevich.movieland.model;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Role getRoleByName(String name) {
        for (Role role : values()) {
            if (role.value.equalsIgnoreCase(name)) {
                return role;
            }
        }
        String message = "User has not applicable role: " + name + "!";
        throw new IllegalArgumentException(message);
    }
}
