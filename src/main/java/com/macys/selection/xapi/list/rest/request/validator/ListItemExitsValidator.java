package com.macys.selection.xapi.list.rest.request.validator;

import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.springframework.stereotype.Component;
import java.util.List;

/**
 *  checks if there are no item in the list
 **/
@Component
public class ListItemExitsValidator implements RequestValidators<CustomerList, Boolean> {

  @Override
  public Boolean isValid(CustomerList customerList) {
    if(customerList == null) {
      return false;
    }

    List<WishList> wishLists = customerList.getWishlist();
    if (wishLists != null && !wishLists.isEmpty()) {
      WishList wishList = wishLists.get(0);
        List<Item>itemList = wishList.getItems();
        if (itemList != null && !itemList.isEmpty()) {
          return true;
        }
    }

    return false;
  }


}
