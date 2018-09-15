package com.macys.selection.xapi.list.util;

import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author m785440
 *
 */
@Component
public class CustomerRequestParamUtil {

  public Map<CustomerQueryParameterEnum, String> createGetListQueryParamMap(
      UserQueryParam userQueryParam, ListQueryParam listQueryParam,
      PaginationQueryParam paginationQueryParam) {

    Map<CustomerQueryParameterEnum, String> getListQueryParamMap = new EnumMap<>(CustomerQueryParameterEnum.class);
    addUserQueryParam(userQueryParam, getListQueryParamMap);
    addListQueryParam(listQueryParam, getListQueryParamMap);
    addPaginationQueryParam(paginationQueryParam, getListQueryParamMap);
    return getListQueryParamMap;
  }

  private void addPaginationQueryParam(PaginationQueryParam paginationQueryParam,
      Map<CustomerQueryParameterEnum, String> getListQueryParamMap) {

    if (paginationQueryParam != null) {
      if (paginationQueryParam.getLimit() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.LIMIT,
            Integer.toString(paginationQueryParam.getLimit()));
      }
      if (paginationQueryParam.getOffset() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.OFFSET,
            Integer.toString(paginationQueryParam.getOffset()));
      }
    }
  }

  private void addListQueryParam(ListQueryParam listQueryParam,
      Map<CustomerQueryParameterEnum, String> getListQueryParamMap) {

    if (listQueryParam != null) {

      if (listQueryParam.isDefaultList() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.DEFAULT,
            Boolean.toString(listQueryParam.isDefaultList()));
      }
      if (StringUtils.isNotEmpty(listQueryParam.getListType())) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.LISTTYPE, listQueryParam.getListType());
      }
      if (StringUtils.isNotEmpty(listQueryParam.getSortBy())) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.SORTBY, listQueryParam.getSortBy());
      }
      if (StringUtils.isNotEmpty(listQueryParam.getSortOrder())) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.SORTORDER, listQueryParam.getSortOrder());
      }
      if (listQueryParam.getListLimit() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.LISTLIMIT,
            Integer.toString(listQueryParam.getListLimit()));
      }
      if(StringUtils.isNotEmpty(listQueryParam.getCustomerState())) {
          getListQueryParamMap.put(CustomerQueryParameterEnum.CUSTOMERSTATE, listQueryParam.getCustomerState());
      }
      if (StringUtils.isNotEmpty(listQueryParam.getFilter())) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.FILTER, listQueryParam.getFilter());
      }
      if (StringUtils.isNotEmpty(listQueryParam.getFields())) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.FIELDS, listQueryParam.getFields());
      }
      if (StringUtils.isNotEmpty(listQueryParam.getExpand())) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.EXPAND, listQueryParam.getExpand());
      }
      if (listQueryParam.getProductId() > 0) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.PRODUCTID, String.valueOf(listQueryParam.getProductId()));
      }
      if(listQueryParam.getFirstName() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.FIRSTNAME, String.valueOf(listQueryParam.getFirstName()));
      }
      if(listQueryParam.getLastName() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.LASTNAME, String.valueOf(listQueryParam.getLastName()));
      }
      if(listQueryParam.getState() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.STATE, String.valueOf(listQueryParam.getState()));
      }
    }
  }

  private void addUserQueryParam(UserQueryParam userQueryParam,
      Map<CustomerQueryParameterEnum, String> getListQueryParamMap) {

    if (userQueryParam != null) {
      if (userQueryParam.getUserId() != null) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.USERID,
            Long.toString(userQueryParam.getUserId()));
      }
      if (StringUtils.isNotEmpty(userQueryParam.getUserGuid())) {
        getListQueryParamMap.put(CustomerQueryParameterEnum.USERGUID, userQueryParam.getUserGuid());
      }
    }
  }

  /**
   * constucting the response based on filter options. if onsale is the filter option only onsale items are returned.
   * if promotions is filter option only promotions items are returned.
   * if filter options are onsale and promotion both onsale and promotion items are returned.
   * @param response
   * @param filterOptions
   *
   */
      public void buildResponseWithFilterOptions(CustomerList response, String[] filterOptions){
        List<Item> newItemsList = new ArrayList<>();
        if (!ArrayUtils.isEmpty(filterOptions)){
          for(String filter : filterOptions) {
              //get items from response and modifying the items list based on filter options
              List<Item> items = response.getWishlist().get(0).getItems();
              if (CollectionUtils.isNotEmpty(items)){
                  for (Item item : items) {
                      if (WishlistConstants.ON_SALE_FILTER.equals(filter)) {
                          if (item.getUpc() != null && item.getUpc().getPrice() != null && item.getUpc().getPrice().getOnSale() != null) {
                              if (item.getUpc().getPrice().getOnSale()) {
                                  addToNewItemsList(newItemsList, item);
                              }
                          }
                      } else if (WishlistConstants.PROMOTIONS_FILTER.equals(filter)) {
                          if (CollectionUtils.isNotEmpty(item.getPromotions())) {
                              addToNewItemsList(newItemsList, item);
                          }
                      }
                  }
               }
          }
          //Based on filter options send only those items. if onSale items are not there or promotion items are not there
            // and trying to get with filter=onSale or filter=promotions will get empty list.
            response.getWishlist().get(0).setItems(newItemsList);
        }

  }

  /**
   * checking whether the item is already added to the new item list based on upc id.
   * @param newItemsList
   * @param item
   */
      public  void addToNewItemsList(List<Item> newItemsList, Item item){
        List<Integer> upcIdsList= new ArrayList<>();
        if(CollectionUtils.isNotEmpty(newItemsList) ) {
              for(Item newItem: newItemsList){
                  if(newItem.getUpc()!=null) {
                      upcIdsList.add(newItem.getUpc().getId());
                  }
              }
             if(item.getUpc() != null && !upcIdsList.contains(item.getUpc().getId())){
                 newItemsList.add(item);
             }
        }else{
            newItemsList.add(item);
        }
      }
}

