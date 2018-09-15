package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

@Component
class ListItemDateComparator implements ListItemComparator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListItemDateComparator.class);

    @Override
    public int compare(Item item1, Item item2) {
        LOGGER.debug("START comparing items by added date");
        Date date1 = item1.getAddedDate();
        Date date2 = item2.getAddedDate();

        if (date1 == null && date2 == null) {
            return 0;
        } else if (date1 == null) {
            return -1;
        } else if (date2 == null) {
            return 1;
        } else {
            return date1.compareTo(date2);
        }
    }

    @Override
    public SortByField getSortBy() {
        return SortByField.ADDED_DATE;
    }

    @Override
    public Optional<Comparator<Item>> getAdditionalComparator(SortOrder currentSortOrder) {
        return Optional.empty();
    }
}
