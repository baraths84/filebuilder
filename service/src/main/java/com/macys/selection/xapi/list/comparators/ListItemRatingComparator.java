package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;

@Component
class ListItemRatingComparator implements ListItemComparator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListItemRatingComparator.class);

    private final ListItemSuppressReviewComparator listItemSuppressReviewComparator;

    @Autowired
    public ListItemRatingComparator(ListItemSuppressReviewComparator listItemSuppressReviewComparator) {
        this.listItemSuppressReviewComparator = listItemSuppressReviewComparator;
    }

    @Override
    public int compare(Item item1, Item item2) {
        LOGGER.debug("START comparing items by rating");
        int result;
        Float change1 = null;
        Float change2 = null;
        if (hasReviewRating(item1)) {
            change1 = item1.getUpc().getProduct().getReviewStatistics().getAverageRating();
        }
        if (hasReviewRating(item2)) {
            change2 = item2.getUpc().getProduct().getReviewStatistics().getAverageRating();
        }
        if (change1 == null && change2 == null) {
            result = 0;
        } else if (change1 == null) {
            result = -1;
        } else if (change2 == null) {
            result = 1;
        } else {
            result = change1.compareTo(change2);
        }
        return result;
    }

    private boolean hasReviewRating(Item item) {
        return item != null && item.getUpc() != null && item.getUpc().getProduct() != null &&
                item.getUpc().getProduct().getReviewStatistics() != null;
    }

    @Override
    public SortByField getSortBy() {
        return SortByField.AVG_REVIEW_RATING;
    }

    @Override
    public Optional<Comparator<Item>> getAdditionalComparator(SortOrder currentSortOrder) {
        return Optional.of(currentSortOrder == SortOrder.ASC ? listItemSuppressReviewComparator.reversed() : listItemSuppressReviewComparator);
    }
}