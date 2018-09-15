package com.macys.selection.xapi.list.comparators;

import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class ListSortingExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListSortingExecutor.class);
    private static final SortByField DEFAULT_SORT_BY = SortByField.ADDED_DATE;
    private static final SortOrder DEFAULT_SORT_ORDER = SortOrder.DESC;

    private final ListItemComparatorFactory listItemComparatorFactory;

    @Autowired
    public ListSortingExecutor(ListItemComparatorFactory listItemComparatorFactory) {
        this.listItemComparatorFactory = listItemComparatorFactory;
    }

    public void sort(CustomerList list, String sortBy, String sortOrder) {
        LOGGER.debug("START : sort list");
        if (list == null || CollectionUtils.isEmpty(list.getWishlist())) {
            return;
        }
        SortByField sortByField = SortByField.valueOfField(sortBy).orElse(DEFAULT_SORT_BY);
        SortOrder order = SortOrder.valueOfOrder(sortOrder).orElse(DEFAULT_SORT_ORDER);
        Comparator<Item> mainComparator = listItemComparatorFactory.getComparator(sortByField, order);
        Optional<Comparator<Item>> additionalComparator = listItemComparatorFactory.getAdditionalComparator(sortByField, order);
        list.getWishlist().forEach(w -> {
            if (CollectionUtils.isNotEmpty(w.getItems())) {
                w.getItems().sort(mainComparator);
                additionalComparator.ifPresent(c -> w.getItems().sort(c));
            }
        });
        LOGGER.debug("END : sort list");
    }

    public void sort(List<Item> items, SortByField sortBy, SortOrder sortOrder) {
        if (CollectionUtils.isEmpty(items)) {
            return;
        }
        Comparator<Item> mainComparator = listItemComparatorFactory.getComparator(sortBy, sortOrder);
        Optional<Comparator<Item>> additionalComparator = listItemComparatorFactory.getAdditionalComparator(sortBy, sortOrder);
        items.sort(mainComparator);
        additionalComparator.ifPresent(items::sort);
    }

    public void validateSortField(String sortField) {
        if(sortField != null && !SortByField.valueOfField(sortField).isPresent()) {
            ListServiceException e = new ListServiceException();
            e.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            e.setServiceErrorCode(ListServiceErrorCodesEnum.INVALID_SORT_BY_FIELD.getInternalCode());
            throw e;
        }
    }

    public void validateSortOrder(String sortOrder) {
        if(sortOrder != null && !SortOrder.valueOfOrder(sortOrder).isPresent()) {
            ListServiceException e = new ListServiceException();
            e.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
            e.setServiceErrorCode(ListServiceErrorCodesEnum.INVALID_SORT_BY_ORDER.getInternalCode());
            throw e;
        }
    }
}
