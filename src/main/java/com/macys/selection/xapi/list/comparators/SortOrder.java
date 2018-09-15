package com.macys.selection.xapi.list.comparators;

import java.util.Arrays;
import java.util.Optional;

public enum SortOrder {
    ASC("asc"),
    DESC("desc");
    private String value;

    SortOrder(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static Optional<SortOrder> valueOfOrder(String value) {
        if (value != null) {
            return Arrays.stream(values())
                    .filter(v -> v.getValue().equalsIgnoreCase(value))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }
}
