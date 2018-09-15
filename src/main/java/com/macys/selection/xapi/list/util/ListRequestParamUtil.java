package com.macys.selection.xapi.list.util;

import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.SortQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.macys.selection.xapi.list.util.ListQueryParameterEnum.*;

@Component
public class ListRequestParamUtil {
    public static final String EXPAND_USER = "user";
    public static final String EXPAND_ITEMS = "items";
    public static final String FILTER_AVAILABILITY = "availability";
    public static final String DIVIDER = ",";

    public void addQueryParams(RxWebTarget rxWebTarget, ListQueryParam listQueryParam,
                               UserQueryParam userQueryParam,
                               PaginationQueryParam paginationQueryParam) {
        addUserQueryParam(rxWebTarget, userQueryParam);
        addListQueryParam(rxWebTarget, listQueryParam);
        addPaginationQueryParam(rxWebTarget, paginationQueryParam);
    }

    public void addUserQueryParam(RxWebTarget rxWebTarget, UserQueryParam userQueryParam) {
        if (userQueryParam != null) {
            addIfNotNull(rxWebTarget, USER_ID, userQueryParam.getUserId());
            addIfNotEmpty(rxWebTarget, USER_IDS, userQueryParam.getUserIds());
            addIfNotEmpty(rxWebTarget, USER_GUID, userQueryParam.getUserGuid());
            addIfNotNull(rxWebTarget, GUEST_USER, userQueryParam.getGuestUser());
            addIfNotEmpty(rxWebTarget, USER_FIRST_NAME, userQueryParam.getFirstName());
        }
    }

    public void addPaginationQueryParam(RxWebTarget rxWebTarget, PaginationQueryParam paginationQueryParam) {
        if (paginationQueryParam != null) {
            addIfNotNull(rxWebTarget, LIMIT, paginationQueryParam.getLimit());
            addIfNotNull(rxWebTarget, OFFSET, paginationQueryParam.getOffset());
        }
    }

    public void addSortQueryParam(RxWebTarget rxWebTarget, SortQueryParam sortQueryParam) {
        if (sortQueryParam != null) {
            addIfNotNull(rxWebTarget, SORT_BY, sortQueryParam.getSortBy());
            addIfNotNull(rxWebTarget, SORT_ORDER, sortQueryParam.getSortOrder());
        }
    }

    public void addListQueryParam(RxWebTarget rxWebTarget, ListQueryParam listQueryParam) {
        if (listQueryParam != null) {
            addIfNotNull(rxWebTarget, DEFAULT, listQueryParam.isDefaultList());
            addIfNotEmpty(rxWebTarget, LIST_TYPE, listQueryParam.getListType());
            addIfNotEmpty(rxWebTarget, LIST_TYPES, listQueryParam.getListTypes());
            addIfNotNull(rxWebTarget, LIST_LIMIT, listQueryParam.getListLimit());
            addIfNotEmpty(rxWebTarget, FILTER, listQueryParam.getFilter());
            addIfNotEmpty(rxWebTarget, FIELDS, listQueryParam.getFields());
            addIfNotEmpty(rxWebTarget, EXPAND, excludeExtraExpandOption(listQueryParam.getExpand()));
            addIfPositive(rxWebTarget, UPC_ID, listQueryParam.getUpcId());
            addIfPositive(rxWebTarget, PRODUCT_ID, listQueryParam.getProductId());
            addIfNotEmpty(rxWebTarget, CUSTOMERSTATE, listQueryParam.getCustomerState());
        }
    }

    private String excludeExtraExpandOption(String expand) {
        return StringUtils.isNotEmpty(expand) ? Stream.of(expand.split(DIVIDER))
                .filter(e -> !e.equalsIgnoreCase(EXPAND_USER))
                .collect(Collectors.joining(DIVIDER)) : expand;
    }

    public void addIfNotNull(RxWebTarget rxWebTarget, ListQueryParameterEnum param, Object value) {
        if(value != null) {
            rxWebTarget.queryParam(param.getParamName(), String.valueOf(value));
        }
    }

    public void addIfNotEmpty(RxWebTarget rxWebTarget, ListQueryParameterEnum param, String value) {
        if(StringUtils.isNotEmpty(value)) {
            rxWebTarget.queryParam(param.getParamName(), value);
        }
    }

    public void addIfNotEmpty(RxWebTarget rxWebTarget, ListQueryParameterEnum param, Collection<?> values) {
        if (CollectionUtils.isNotEmpty(values)) {
            rxWebTarget.queryParam(param.getParamName(), values.stream().map(String::valueOf).collect(Collectors.joining(",")));
        }
    }

    private void addIfPositive(RxWebTarget rxWebTarget, ListQueryParameterEnum param, Integer value) {
        if(value != null && value > 0) {
            rxWebTarget.queryParam(param.getParamName(), String.valueOf(value));
        }
    }
}
