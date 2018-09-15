package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
class ListItemSuppressReviewComparator implements Comparator<Item> {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListItemSuppressReviewComparator.class);

    @Override
    public int compare(Item item1, Item item2) {
        LOGGER.debug("START comparing items by suppress reviews");
        Boolean suppressReviews1 = null;
        Boolean suppressReviews2 = null;
        if (hasSuppressFlag(item1)) {
            suppressReviews1 = item1.getUpc().getProduct().isSuppressReviews();
        }
        if (hasSuppressFlag(item2)) {
            suppressReviews2 = item2.getUpc().getProduct().isSuppressReviews();
        }
        int result;
        if (suppressReviews1 == null && suppressReviews2 == null) {
            result = 0;
        } else if (suppressReviews1 == null) {
            result = -1;
        } else if (suppressReviews2 == null) {
            result = 1;
        } else {
            result = suppressReviews1.compareTo(suppressReviews2);
        }
        return result;
    }

    private boolean hasSuppressFlag(Item item) {
        return item != null && item.getUpc() != null && item.getUpc().getProduct() != null
                && item.getUpc().getProduct().isSuppressReviews() != null;
    }
}
