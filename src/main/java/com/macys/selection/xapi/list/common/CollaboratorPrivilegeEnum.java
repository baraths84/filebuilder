package com.macys.selection.xapi.list.common;

import java.util.Arrays;

public enum CollaboratorPrivilegeEnum {
    LIKE,
    OWNER,
    COMMENT,
    EDIT,
    PENDING,
    REJECTED,
    REPORTED,
    REMOVED,
    BLACKLISTED;

    public static CollaboratorPrivilegeEnum parsePrivilege(String collaboratorPrivilege) {
        return Arrays.stream(values()).filter(
                v -> v.name().equalsIgnoreCase(collaboratorPrivilege)).findFirst().orElse(null);
    }
}
