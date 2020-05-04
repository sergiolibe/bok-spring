package com.klever.bok.models;

public enum ERole {
    ROLE_USER,
    ROLE_MODERATOR,
    ROLE_ADMIN;

    public static ERole defaultRole() {
        return ROLE_USER;
    }

    public static ERole byName(String name) {
        for (ERole eRole : values())
            if (eRole.toString().equals(name))
                return eRole;
        return defaultRole();
    }
}
