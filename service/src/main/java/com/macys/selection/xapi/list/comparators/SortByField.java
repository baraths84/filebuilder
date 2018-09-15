package com.macys.selection.xapi.list.comparators;

import java.util.Arrays;
import java.util.Optional;

public enum SortByField {
    RETAIL_PRICE("retailPrice"),
    AVG_REVIEW_RATING("avgReviewRating"),
    TOP_PICK("topPick"),
    ADDED_DATE("addedDate");

    private String field;

    SortByField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    public static Optional<SortByField> valueOfField(String field) {
        if (field != null) {
            return Arrays.stream(values())
                    .filter(f -> f.getField().equalsIgnoreCase(field))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }
}
