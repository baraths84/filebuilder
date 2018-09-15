package com.macys.selection.xapi.list.data.converters;

import com.macys.selection.xapi.list.common.ListTypeEnum;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListManageRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListRequest;
import com.macys.selection.xapi.list.rest.response.*;

import java.util.Collections;
import java.util.List;

public class CollaborativeListConverter {

    public static CollaborativeListResponse convertFromCustomerList(CustomerList customerList) {
        CollaborativeListResponse response = new CollaborativeListResponse();
        response.setUser(customerList.getUser());
        response.setWishlist(customerList.getWishlist().get(0));
        return response;
    }

    public static CustomerList convertToCustomerList(CollaborativeListRequest listRequest) {
        CustomerList response = new CustomerList();
        User user = new User();
        user.setGuid(listRequest.getUserGuid());
        response.setUser(user);

        WishList wishList = new WishList();
        wishList.setName(listRequest.getName());
        List<Item> items = listRequest.getItems();
        if (items != null) {
            for (Item item : items) {
                item.setQtyRequested(1);
            }
        }
        wishList.setItems(items);
        wishList.setListType(ListTypeEnum.COLLABORATIVE_LIST.getValue());
        wishList.setOnSaleNotify(listRequest.getOnSaleNotify());
        response.setWishlist(Collections.singletonList(wishList));
        return response;
    }

    public static CustomerList convertToCustomerList(String listGuid, CollaborativeListManageRequest listManageRequest) {
        CustomerList response = new CustomerList();
        WishList wishList = new WishList();
        wishList.setListGuid(listGuid);
        wishList.setName(listManageRequest.getName());
        wishList.setOnSaleNotify(listManageRequest.getOnSaleNotify());
        response.setWishlist(Collections.singletonList(wishList));
        return response;
    }

}
