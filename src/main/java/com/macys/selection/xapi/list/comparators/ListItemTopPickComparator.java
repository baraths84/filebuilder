package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;

@Component
class ListItemTopPickComparator implements ListItemComparator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListItemTopPickComparator.class);

    private final ListItemDateComparator listItemDateComparator;

    @Autowired
    public ListItemTopPickComparator(ListItemDateComparator listItemDateComparator) {
        this.listItemDateComparator = listItemDateComparator;
    }

    @Override
    public int compare(Item item1, Item item2) {
        LOGGER.debug("START comparing items by priority");
        String priority1 = item1.getPriority();
        String priority2 = item2.getPriority();
        int result;
        if ((StringUtils.isEmpty(priority1)) && (StringUtils.isEmpty(priority2))) {
            result = 0;
        } else if (StringUtils.isEmpty(priority1)) {
            result = -1;
        } else if (StringUtils.isEmpty(priority2)) {
            result = 1;
        } else {
            result = priority1.compareTo(priority2);
            if (result == 0) {
                result = listItemDateComparator.compare(item1, item2);
            }
        }
        return result;
    }

    @Override
    public SortByField getSortBy() {
        return SortByField.TOP_PICK;
    }

    @Override
    public Optional<Comparator<Item>> getAdditionalComparator(SortOrder currentSortOrder) {
        return Optional.empty();
    }
}
