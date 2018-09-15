package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.rest.response.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.Optional;

@Component
class ListItemPriceComparator implements ListItemComparator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListItemPriceComparator.class);

    private ListItemAvailabilityComparator listItemAvailabilityComparator;

    @Autowired
    public ListItemPriceComparator(ListItemAvailabilityComparator listItemAvailabilityComparator) {
        this.listItemAvailabilityComparator = listItemAvailabilityComparator;
    }

    @Override
    public int compare(Item item1, Item item2) {
        LOGGER.debug("START comparing items by retail price");
        Double unitPrice1 = null;
        Double unitPrice2 = null;
        if (hasProductPrice(item1)) {
            unitPrice1 = item1.getProduct().getPrice().getRetailPrice();
        } else if (hasUpcPrice(item1)) {
            unitPrice1 = item1.getUpc().getPrice().getRetailPrice();
        }
        if (hasProductPrice(item2)) {
            unitPrice2 = item2.getProduct().getPrice().getRetailPrice();
        } else if (hasUpcPrice(item2)) {
            unitPrice2 = item2.getUpc().getPrice().getRetailPrice();
        }
        int result;
        if (unitPrice1 == null && unitPrice2 == null) {
            result = 0;
        } else if (unitPrice1 == null) {
            result = -1;
        } else if (unitPrice2 == null) {
            result = 1;
        } else {
            result = unitPrice1.compareTo(unitPrice2);
        }

        return result;
    }

    private boolean hasUpcPrice(Item item) {
        return item != null && item.getUpc() != null && item.getUpc().getPrice() != null;
    }

    private boolean hasProductPrice(Item item) {
        return item != null && item.getProduct() != null && item.getProduct().getPrice() != null;
    }

    @Override
    public SortByField getSortBy() {
        return SortByField.RETAIL_PRICE;
    }

    @Override
    public Optional<Comparator<Item>> getAdditionalComparator(SortOrder currentSortOrder) {
        return Optional.of(listItemAvailabilityComparator.reversed());
    }
}
