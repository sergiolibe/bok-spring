package com.klever.bok.models.entity;

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
//        return defaultRole();
        throw new RuntimeException("Error: Role < " + name + " > is not found.");
    }
}
