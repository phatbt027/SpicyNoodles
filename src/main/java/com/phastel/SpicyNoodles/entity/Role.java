package com.phastel.SpicyNoodles.entity;

public enum Role {
    ADMIN("ROLE_ADMIN"),
    MANAGER("ROLE_MANAGER"),
    STAFF("ROLE_STAFF");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
} 