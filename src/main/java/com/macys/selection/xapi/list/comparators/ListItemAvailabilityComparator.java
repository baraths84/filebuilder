package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
class ListItemAvailabilityComparator implements Comparator<Item> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListItemAvailabilityComparator.class);

    @Override
    public int compare(Item item1, Item item2) {
        LOGGER.debug("START comparing items by availability");
        Boolean available1 = null;
        Boolean available2 = null;
        if (item1.getProduct() != null && item1.getProduct().isAvailable() != null) {
            available1 = item1.getProduct().isAvailable();
        } else if (item1.getUpc() != null && item1.getUpc().getAvailability() != null) {
            available1 = item1.getUpc().getAvailability().isAvailable();
        }

        if (item2.getProduct() != null && item2.getProduct().isAvailable() != null) {
            available2 = item2.getProduct().isAvailable();
        } else if (item2.getUpc() != null && item2.getUpc().getAvailability() != null) {
            available2 = item2.getUpc().getAvailability().isAvailable();
        }

        if (available1 == null && available2 == null) {
            return 0;
        } else if (available1 == null) {
            return -1;
        } else if (available2 == null) {
            return 1;
        } else {
            return available1.compareTo(available2);
        }

    }
}
