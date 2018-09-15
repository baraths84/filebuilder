package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;

import java.util.Comparator;
import java.util.Optional;

interface ListItemComparator extends Comparator<Item> {
    SortByField getSortBy();

    Optional<Comparator<Item>> getAdditionalComparator(SortOrder currentSortOrder);
}
