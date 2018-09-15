package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class ListItemComparatorFactory {
    private final Map<SortByField, ListItemComparator> listItemComparators;

    @Autowired
    public ListItemComparatorFactory(List<ListItemComparator> listItemComparators) {
        this.listItemComparators = listItemComparators
                .stream()
                .collect(Collectors.toMap(ListItemComparator::getSortBy, Function.identity()));
    }

    public Comparator<Item> getComparator(SortByField sortBy, SortOrder sortOrder) {
        ListItemComparator comparator = this.listItemComparators.get(sortBy);
        return SortOrder.ASC == sortOrder ? comparator : comparator.reversed();
    }

    public Optional<Comparator<Item>> getAdditionalComparator(SortByField sortBy, SortOrder sortOrder) {
        ListItemComparator comparator = this.listItemComparators.get(sortBy);
        return comparator.getAdditionalComparator(sortOrder);
    }
}
