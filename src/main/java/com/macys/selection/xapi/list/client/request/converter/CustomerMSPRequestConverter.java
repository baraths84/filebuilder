package com.macys.selection.xapi.list.client.request.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.macys.selection.xapi.list.client.request.CustomerListRequest;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.WishList;

import java.util.List;

public class CustomerMSPRequestConverter {
	
	private static ObjectMapper wrapRootObjectMapper = new ObjectMapper();
	private static ObjectMapper noRootObjectMapper = new ObjectMapper();
	
	static {
		wrapRootObjectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		noRootObjectMapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
	}
	
	private CustomerMSPRequestConverter() {

	}
	
	public static String convert(CustomerList customerList) throws JsonProcessingException {
		CustomerListRequest listRequest = new CustomerListRequest();
        if (customerList != null) {
        	listRequest.setUser(customerList.getUser());
        	if (customerList.getWishlist() != null && !customerList.getWishlist().isEmpty()) {
        		//only get the first wishlist, since all the requests to CustomerMSP is one list only
        		WishList wishList = customerList.getWishlist().get(0);
        		if (wishList != null) {
        			listRequest.setGuid(wishList.getListGuid());
        			listRequest.setDefaultList(wishList.isDefaultList());
        			listRequest.setListType(wishList.getListType());
        			listRequest.setName(wishList.getName());
        			listRequest.setOnSaleNotify(wishList.getOnSaleNotify());
        			listRequest.setSearchable(wishList.getSearchable());
        			listRequest.setShowPurchaseInfo(wishList.isShowPurchaseInfo());
        			listRequest.setItems(wishList.getItems());
        		}
        	}
        }
		
		return wrapRootObjectMapper.writeValueAsString(listRequest);
	}
	
	public static String convert(List<Item> itemList) throws JsonProcessingException {
        if (itemList != null && !itemList.isEmpty()) {
        	return noRootObjectMapper.writeValueAsString(itemList);
        }
		
		return null;
	}
	
	public static String convertEmailShare(EmailShare emailShare) throws JsonProcessingException {		
		return wrapRootObjectMapper.writeValueAsString(emailShare);
	}

	public static String convertListMerge(CustomerListMerge listMerge) throws JsonProcessingException {
		return wrapRootObjectMapper.writeValueAsString(listMerge);
	}

	public static String convertItem(ItemDTO itemResponse) throws JsonProcessingException {
		return wrapRootObjectMapper.writeValueAsString(itemResponse);
	}
}
