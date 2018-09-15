package com.macys.selection.xapi.list.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.response.CustomerWishListsResponse;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.data.converters.JsonToUserMapper;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.FavoriteList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.ErrorResponseUtil;
import com.macys.selection.xapi.list.util.ListUtil;
import com.macys.selection.xapi.list.util.ResponseAnalyticsUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * service for MSP customer calls.
 **/
@Service
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerServiceRestClient restClient;

    @Autowired
    private ListUtil listUtil;

    private JsonToObjectConverter<WishList> wishListConverter = new JsonToObjectConverter<>(WishList.class);

    private JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter = new JsonToObjectConverter<>(CustomerWishListsResponse.class);

    private JsonToObjectConverter<FavoriteList> favoriteListConverter = new JsonToObjectConverter<>(FavoriteList.class);

    private static final String RESPONSE_IS_NULL = "Response is null when updating wishlist";

    private static final String AVAILABILITY = "availability";

    private static final Charset CHARSET = StandardCharsets.UTF_8;

    public void setWishListConverter(JsonToObjectConverter<WishList> wishListConverter) {
        this.wishListConverter = wishListConverter;
    }

    public void setWishlistsConverter(JsonToObjectConverter<CustomerWishListsResponse> wishlistsConverter) {
        this.wishlistsConverter = wishlistsConverter;
    }

    public void setFavoriteListConverter(JsonToObjectConverter<FavoriteList> favoriteListConverter) {
        this.favoriteListConverter = favoriteListConverter;
    }

    public ListUtil getListUtil() {
        return listUtil;
    }

    public void setListUtil(ListUtil listUtil) {
        this.listUtil = listUtil;
    }

    /**
     * Get customer list by userid
     *
     * @throws ParseException
     * @throws JsonProcessingException
     **/
    public CustomerList getCustomerList(UserQueryParam userQueryParam, ListQueryParam listQueryParam, PaginationQueryParam paginationQueryParam) {

        LOGGER.debug("START of :: {}.getCustomerList.", this.getClass().getSimpleName());

        CustomerList result = new CustomerList();

        RestResponse response = restClient.get(userQueryParam, listQueryParam, paginationQueryParam);
        if (null != response) {
            String responseStr = response.getBody();
            if (response.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                CustomerWishListsResponse customerMspLists = wishlistsConverter.parseJsonToObject(responseStr);
                List<WishList> templists = customerMspLists.getLists();
                List<WishList> wishlists = null;
                if (CollectionUtils.isNotEmpty(templists)) {
                    wishlists = new ArrayList<>();
                    for (WishList tempWishlist : templists) {
                        if (tempWishlist != null && StringUtils.isNotEmpty(tempWishlist.getListGuid())) {
                            wishlists.add(tempWishlist);
                        }
                    }
                }

                result.setWishlist(wishlists);
                result.setUser(JsonToUserMapper.parse(responseStr, false));
            } else {
                if (response.getStatusCode() == WishlistConstants.BAD_REQUEST && ErrorResponseUtil.evaluateErrorResponse(responseStr)) {

                    LOGGER.error(" userId: {} might be incorrect so returning to default list ", userQueryParam.getUserId());
                    List<WishList> wishlists = new ArrayList<>();
                    result.setWishlist(wishlists);
                    return result;
                } else {
                    throw new ListServiceException(response.getStatusCode(), responseStr);
                }
            }
        }
        LOGGER.debug("END of :: {}.getCustomerList.", this.getClass().getSimpleName());

        List<WishList> wishlistList = result.getWishlist();
        if (wishlistList != null && !wishlistList.isEmpty() && wishlistList.size() > 1) {
            Collections.sort(result.getWishlist());
        }

        return result;
    }

    /**
     * Get customer list guid
     **/
    public CustomerList getCustomerListByGuid(String token, String listGuid, ListQueryParam listQueryParam, PaginationQueryParam paginationQueryParam) {
        LOGGER.debug("START of :: {}.getCustomerListByGuid.", this.getClass().getSimpleName());
        RestResponse response = null;
        CustomerList result = new CustomerList();
        WishList wishlist = new WishList();
        response = restClient.getListByGuid(token, listGuid, null, listQueryParam, paginationQueryParam);

        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() == Response.Status.OK.getStatusCode()) {
                wishlist = wishListConverter.parseJsonToObject(responseStr);
                result.setUser(JsonToUserMapper.parse(responseStr, true));
            } else {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }

        LOGGER.debug("END of :: {}.getCustomerListByGuid.", this.getClass().getSimpleName());

        //filter out the unavalible items if filterBy is set
        if (StringUtils.isNotEmpty(listQueryParam.getFilter()) && listQueryParam.getFilter().equals(AVAILABILITY)) {
            wishlist.setItems(ListUtil.filterListByAvailability(wishlist.getItems()));
        }
        
        //filter out finalPrice object from wishList response based on finalPrice value, priceDrop label and finalPrice attributes
        if (CollectionUtils.isNotEmpty(wishlist.getItems())) {
        	listUtil.filterFinalPrice(wishlist.getItems(), listQueryParam);
        }
               

        List<WishList> wishlists = new ArrayList<>();
        wishlists.add(wishlist);
        result.setWishlist(wishlists);
        result.setMeta(ListUtil.buildAnalyticsMeta(wishlist));
        return result;
    }

    public void moveItemToWishlist(String token, String listGuid, CustomerList inputCustomerList) {

        RestResponse response = restClient.moveItemToWishlist(token, listGuid, inputCustomerList);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }

    }

    public void deleteItem(String token, String listGuid, String itemGuid) {
        RestResponse response = restClient.delete(token, listGuid, itemGuid);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public void updateWishlist(ListCookies cookies, String listGuid, UserQueryParam userQueryParam, CustomerList inputJsonObj) {

        RestResponse response = restClient.updateWishlist(cookies, listGuid, userQueryParam, inputJsonObj);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public CustomerList createWishList(ListCookies cookie, CustomerList inputCustomerList) {
        //first create list
        try {
            RestResponse createListResponse = restClient.createList(cookie, inputCustomerList);
            CustomerList newCreatedList = getCustomerListFromRestResponse(createListResponse);
            //check if there is item need to add to this list
            if (needAddItemToList(inputCustomerList)) {
                boolean isDefaultList = newCreatedList.getWishlist().get(0).isDefaultList();
                //if it's default list, then add to the default list
                if (isDefaultList) {
                    return this.addToDefaultWishlist(newCreatedList);
                }
                String listGuid = newCreatedList.getWishlist().get(0).getListGuid();
                UserQueryParam userQueryParam = new UserQueryParam();
                if (newCreatedList.getUser().getGuid() != null) {
                    userQueryParam.setUserGuid(newCreatedList.getUser().getGuid());
                } else {
                    userQueryParam.setUserId(newCreatedList.getUser().getId());
                }
                //add to a given list
                RestResponse response = restClient.addItemToGivenListByUPC(cookie, userQueryParam, listGuid, inputCustomerList.getWishlist().get(0).getItems());
                return getCustomerListFromRestResponse(response);
            }
            return newCreatedList;

        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }

    }

    public boolean needAddItemToList(CustomerList inputCustomerList) {
        if (inputCustomerList != null && inputCustomerList.getWishlist() != null && !inputCustomerList.getWishlist().isEmpty()) {
            List<WishList> wishLists = inputCustomerList.getWishlist();
            WishList firstWishList = wishLists.get(0);
            if (firstWishList != null && firstWishList.getItems() != null && !firstWishList.getItems().isEmpty()) {
                List<Item> itemList = firstWishList.getItems();
                Item item = itemList.get(0);
                if (item != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public void emailShareWishlist(ListCookies cookies, String listGuid, EmailShare inputJsonObj) {

        try {
            if(inputJsonObj != null) {
                inputJsonObj.setLink(UriUtils.decode(inputJsonObj.getLink(), CHARSET.name()));
            }
            RestResponse response = restClient.emailShareWishlist(cookies, listGuid, inputJsonObj);
            if (response != null) {
                if (response.getStatusCode() != Response.Status.OK.getStatusCode() && response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                    String responseStr = response.getBody();
                    throw new ListServiceException(response.getStatusCode(), responseStr);
                }
            } else {
                throw new ListServiceException(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), RESPONSE_IS_NULL);
            }

        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        } catch (UnsupportedEncodingException e) {
            throw new ListServiceException(e.getMessage(), e);
        }
    }

    public CustomerList addToDefaultWishlist(CustomerList inputJsonObj) {
        try {
            RestResponse response = restClient.addToDefaultWishlist(inputJsonObj);
            return getCustomerListFromRestResponse(response);
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    public CustomerList addItemToGivenListByUPC(ListCookies cookie, UserQueryParam userQueryParam, String listGuid, CustomerList inputJsonObj) {

        try {
        	if (inputJsonObj.getUser() != null) {
        		userQueryParam.setUserId(inputJsonObj.getUser().getId());
        		userQueryParam.setUserGuid(inputJsonObj.getUser().getGuid());
        	}
            RestResponse response = restClient.addItemToGivenListByUPC(cookie, userQueryParam, listGuid, inputJsonObj.getWishlist().get(0).getItems());
            return getCustomerListFromRestResponse(response);
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    /**
     * deletes a given list using listGuid.
     **/
    public void deleteList(ListCookies cookie, String listGuid, Long userId) {
        RestResponse response = restClient.deleteList(cookie, listGuid, userId);
        String responseStr = response.getBody();
        if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new ListServiceException(response.getStatusCode(), responseStr);
        }
    }

    /**
     * Convert the response from rest client to CustomerList
     *
     * @param response RestReponse
     * @return CustomerList
     */
    private CustomerList getCustomerListFromRestResponse(RestResponse response) {
        CustomerList result = new CustomerList();
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() == WishlistConstants.STATUS_SUCCESS || response.getStatusCode() == WishlistConstants.STATUS_CREATED) {
                WishList wishlist = wishListConverter.parseJsonToObject(responseStr);
                List<WishList> wishlists = new ArrayList<>();
                wishlists.add(wishlist);
                result.setWishlist(wishlists);
                result.setUser(JsonToUserMapper.parse(responseStr, true));

                result.setMeta(ResponseAnalyticsUtil.buildResponseAnalyticsMeta(result));
            } else {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
        return result;
    }

    public void deleteFavorite(ListCookies cookie, String listGuid, ListQueryParam listQueryParam) {

        RestResponse response = restClient.deleteFavorite(cookie, listGuid, listQueryParam);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public CustomerList addFavItemToGivenListByPID(ListCookies cookie, CustomerList inputJsonObj) {

        try {
            RestResponse response = restClient.addFavItemToGivenListByPID(cookie, inputJsonObj);
            return getCustomerListFromRestResponse(response);
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }
    }

    public FavoriteList getFavItemFromListByGuid(ListCookies cookie, String userGuid) {

        try {
            RestResponse response = restClient.getFavItemFromListByGuid(cookie, userGuid);
            return getFavoriteListFromRestResponse(response);
        } catch (RestException re) {
            throw new ListServiceException(re.getMessage(), re);
        }

    }

    private FavoriteList getFavoriteListFromRestResponse(RestResponse response) {
        FavoriteList result = new FavoriteList();
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() == WishlistConstants.STATUS_SUCCESS || response.getStatusCode() == WishlistConstants.STATUS_CREATED) {
                return favoriteListConverter.parseJsonToObject(responseStr);
            } else {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
        return result;
    }

    public void mergeList(CustomerListMerge listMerge) {
        RestResponse response = restClient.mergeList(listMerge);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public void updateItemPriority(String listGuid, String itemGuid, Long userId, String userGuid, Item item) {
        ItemDTO itemRequest = new ItemDTO();
        Optional.ofNullable(item).ifPresent(i -> itemRequest.setPriority(i.getPriority()));
        RestResponse response = restClient.updateItemPriority(listGuid, itemGuid, userId, userGuid, itemRequest);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }
}