package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.ListRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.request.CustomerListMerge;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.request.MergeListRequest;
import com.macys.selection.xapi.list.client.response.EmailItemDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;
import com.macys.selection.xapi.list.client.response.WishListsDTO;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.comparators.ListSortingExecutor;
import com.macys.selection.xapi.list.comparators.SortByField;
import com.macys.selection.xapi.list.comparators.SortOrder;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.exception.ErrorConstants;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.FavoriteList;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.ErrorResponseUtil;
import com.macys.selection.xapi.list.util.ListRequestParamUtil;
import com.macys.selection.xapi.list.util.ListUtil;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import javax.ws.rs.core.Response;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WishlistService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WishlistService.class);
    private JsonToObjectConverter<WishListDTO> wishListConverter = new JsonToObjectConverter<>(WishListDTO.class);
    private JsonToObjectConverter<WishListsDTO> wishlistsConverter = new JsonToObjectConverter<>(WishListsDTO.class);
    private JsonToObjectConverter<FavoriteList> favoriteListConverter = new JsonToObjectConverter<>(FavoriteList.class);

    private final ListRestClient restClient;
    private final ListSortingExecutor sortingExecutor;
    private final MapperFacade mapperFacade;
    private final ListCatalogService listCatalogService;
    private final CustomerUserService customerUserService;
    private final String customerHost;
    private final Boolean isWishlistV1CreateInvalidUser;
    private final Boolean listCreateUser;
    private final Integer findUsersLimit;

    @Autowired
    public WishlistService(ListRestClient restClient,
                           ListSortingExecutor sortingExecutor,
                           MapperFacade mapperFacade,
                           ListCatalogService listCatalogService,
                           CustomerUserService customerUserService,
                           @Value("${list.customer.service.host}") String customerHost,
                           @Value("${wishlist.v1.create.invaliduser}") Boolean isWishlistV1CreateInvalidUser,
                           @Value("${wishlist.create.user.enabled}") Boolean listCreateUser,
                           @Value("${customer.find.users.limit}") Integer findUsersLimit) {
        this.restClient = restClient;
        this.sortingExecutor = sortingExecutor;
        this.mapperFacade = mapperFacade;
        this.listCatalogService = listCatalogService;
        this.customerUserService = customerUserService;
        this.customerHost = customerHost;
        this.isWishlistV1CreateInvalidUser = isWishlistV1CreateInvalidUser;
        this.listCreateUser = listCreateUser;
        this.findUsersLimit = findUsersLimit;
    }


    public CustomerList findList(UserQueryParam userQueryParam, ListQueryParam listQueryParam, PaginationQueryParam paginationQueryParam) {
        LOGGER.debug("START of :: {}.findList.", this.getClass().getSimpleName());
        validateFindListInput(userQueryParam, listQueryParam);
        List<UserResponse> users = customerUserService.findUsers(listQueryParam.getFirstName(), listQueryParam.getLastName(), listQueryParam.getState(), findUsersLimit);
        if (CollectionUtils.isEmpty(users)) {
            return prepareEmptyList();
        }
        userQueryParam.setUserIds(users.stream().map(UserResponse::getId).collect(Collectors.toList()));
        CustomerList wishlist = getCustomerListWishListsResponse(restClient.getList(userQueryParam, listQueryParam, paginationQueryParam), userQueryParam.getUserId());
        if (CollectionUtils.isNotEmpty(wishlist.getWishlist()) && wishlist.getWishlist().size() > 1) {
            Collections.sort(wishlist.getWishlist());
        }
        User user = CollectionUtils.isNotEmpty(users) ? mapperFacade.map(users.get(0), User.class) : null;
        wishlist.setUser(user);
        LOGGER.debug("END of :: {}.findList.", this.getClass().getSimpleName());
        return wishlist;
    }

    private void validateFindListInput(UserQueryParam userQueryParam, ListQueryParam listQueryParam) {
        if (userQueryParam.getUserId() != null || userQueryParam.getUserGuid() != null ||
                StringUtils.isNotEmpty(listQueryParam.getFields()) || StringUtils.isNotEmpty(listQueryParam.getExpand())) {
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_INPUT);
        }
    }

    public CustomerList getList(UserQueryParam userQueryParam, ListQueryParam listQueryParam, PaginationQueryParam paginationQueryParam, String uriHost) {
        LOGGER.debug("START of :: {}.getList.", this.getClass().getSimpleName());
        sortingExecutor.validateSortField(listQueryParam.getSortBy());
        sortingExecutor.validateSortOrder(listQueryParam.getSortOrder());
        customerUserService.validateEitherUserIdOrGuidSpecified(userQueryParam.getUserId(), userQueryParam.getUserGuid());
        customerUserService.validateNotBothUserIdAndGuidSpecified(userQueryParam.getUserId(), userQueryParam.getUserGuid());
        listQueryParam.setDefaultList(Optional.ofNullable(listQueryParam.isDefaultList()).orElse(true));
        boolean expandUser = StringUtils.containsIgnoreCase(listQueryParam.getExpand(), ListRequestParamUtil.EXPAND_USER);
        boolean expandItems = StringUtils.containsIgnoreCase(listQueryParam.getExpand(), ListRequestParamUtil.EXPAND_ITEMS);
        boolean isRequestedByUserId = userQueryParam.getUserId() != null && userQueryParam.getUserId() > 0;
        boolean isRequestedByUserGuid = StringUtils.isNotEmpty(userQueryParam.getUserGuid());
        CustomerList wishlist;
        UserResponse userResponse = null;
        if (isRequestedByUserGuid || (isRequestedByUserId && (expandUser || !listQueryParam.isDefaultList()))) {
            try {
                boolean isExistingUserRequired = !isWishlistV1CreateInvalidUser || isRequestedByUserGuid;
                userResponse = customerUserService.retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), isExistingUserRequired);
            } catch (ListServiceException e) {
                if (ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode() == e.getServiceErrorCode()) {
                    return prepareEmptyList();
                } else {
                    throw e;
                }
            }
        }
        customerUserService.populateUserQueryParam(userQueryParam, userResponse, true);
        wishlist = getCustomerListWishListsResponse(restClient.getList(userQueryParam, listQueryParam, paginationQueryParam), userQueryParam.getUserId());
        populateWishListWithUserResponse(wishlist, userResponse);
        enrichGetListResponse(wishlist, userQueryParam.getUserId(), userQueryParam.getUserGuid(), uriHost, customerHost,
                expandUser, expandItems, listQueryParam.isDefaultList(), listQueryParam.getSortBy(), listQueryParam.getSortOrder());
        if (CollectionUtils.isNotEmpty(wishlist.getWishlist()) && wishlist.getWishlist().size() > 1) {
            Collections.sort(wishlist.getWishlist());
        }
        LOGGER.debug("END of :: {}.getList.", this.getClass().getSimpleName());
        return wishlist;
    }

    public CustomerList getCustomerListWishListsResponse(RestResponse response, Long userId) {
        CustomerList result = new CustomerList();
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() == WishlistConstants.STATUS_SUCCESS) {
                WishListsDTO customerMspLists = wishlistsConverter.parseJsonToObject(responseStr);
                List<WishList> wishlists = customerMspLists.getLists()
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(w -> StringUtils.isNotEmpty(w.getListGuid()))
                        .map(w -> mapperFacade.map(w, WishList.class))
                        .collect(Collectors.toList());
                result.setWishlist(wishlists);
                if (CollectionUtils.isNotEmpty(customerMspLists.getLists())) {
                    User user = new User();
                    user.setId(customerMspLists.getLists().get(0).getUserId());
                    user.setGuid(customerMspLists.getLists().get(0).getUserGuid());
                    result.setUser(user);
                }
            } else {
                if (response.getStatusCode() == WishlistConstants.BAD_REQUEST && ErrorResponseUtil.evaluateErrorResponse(responseStr)) {
                    LOGGER.error(" userId: {} might be incorrect so returning to default list ", userId);
                    result.setWishlist(Collections.emptyList());
                    return result;
                } else {
                    throw new ListServiceException(response.getStatusCode(), responseStr);
                }
            }
        }
        return result;
    }

    private CustomerList prepareEmptyList() {
        CustomerList wishList = new CustomerList();
        wishList.setWishlist(Collections.emptyList());
        return wishList;
    }

    public CustomerList getListByGuid(String listGuid, ListQueryParam listQueryParam, PaginationQueryParam paginationQueryParam, String uriHost) {
        LOGGER.debug("START of :: {}.getListByGuid.", this.getClass().getSimpleName());
        sortingExecutor.validateSortField(listQueryParam.getSortBy());
        sortingExecutor.validateSortOrder(listQueryParam.getSortOrder());
        CustomerList wishlist = getCustomerListWishlistResponse(restClient.getListByGuid(listGuid, listQueryParam, paginationQueryParam));
        Long userId = null;
        String userGuid = null;
        boolean expandUser = StringUtils.containsIgnoreCase(listQueryParam.getExpand(), ListRequestParamUtil.EXPAND_USER);
        boolean expandItems = StringUtils.containsIgnoreCase(listQueryParam.getExpand(), ListRequestParamUtil.EXPAND_ITEMS);
        if (wishlist.getUser() != null) {
            userId = wishlist.getUser().getId();
            userGuid = wishlist.getUser().getGuid();
        }
        if (expandUser) {
            UserResponse userResponse = customerUserService.retrieveUser(userId, userGuid, !isWishlistV1CreateInvalidUser);
            populateWishListWithUserResponse(wishlist, userResponse);
        }
        enrichGetListResponse(wishlist, userId, userGuid, uriHost, customerHost,
                expandUser, expandItems, true,
                listQueryParam.getSortBy(), listQueryParam.getSortOrder());
        if (CollectionUtils.isNotEmpty(wishlist.getWishlist())) {
            filterOutUnavailableItems(wishlist.getWishlist().get(0), listQueryParam.getFilter());
            wishlist.setMeta(ListUtil.buildAnalyticsMeta(wishlist.getWishlist().get(0)));
        }
        LOGGER.debug("END of :: {}.getListByGuid.", this.getClass().getSimpleName());
        return wishlist;
    }

    public CustomerList getListByGuid(String listGuid, ListQueryParam listQueryParam, String uriHost) {
        LOGGER.debug("START of :: {}.getListByGuid.", this.getClass().getSimpleName());
        CustomerList wishlist = getCustomerListWishlistResponse(restClient.getListByGuid(listGuid, listQueryParam, null));
        Long userId = null;
        String userGuid = null;
        boolean expandItems = StringUtils.containsIgnoreCase(listQueryParam.getExpand(), ListRequestParamUtil.EXPAND_ITEMS);
        if (wishlist.getUser() != null) {
            userId = wishlist.getUser().getId();
            userGuid = wishlist.getUser().getGuid();
        }
        if (expandItems) {
            listCatalogService.populateListsItemDetails(wishlist.getWishlist());
            sortingExecutor.sort(wishlist, listQueryParam.getSortBy(), listQueryParam.getSortOrder());
        }

        ListUtil.populateLinks(wishlist, userId, userGuid, uriHost, customerHost);

        LOGGER.debug("END of :: {}.getListByGuid.", this.getClass().getSimpleName());
        return wishlist;
    }

    private CustomerList getCustomerListWishlistResponse(RestResponse response) {
        CustomerList result = new CustomerList();
        WishList wishlist = new WishList();
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() == Response.Status.OK.getStatusCode()) {
                WishListDTO wishListDTO = wishListConverter.parseJsonToObject(responseStr);
                wishlist = mapperFacade.map(wishListDTO, WishList.class);
                User user = new User();
                user.setId(wishListDTO.getUserId());
                user.setGuid(wishListDTO.getUserGuid());
                result.setUser(user);
            } else {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
        result.setWishlist(Collections.singletonList(wishlist));
        return result;
    }

    private void filterOutUnavailableItems(WishList wishList, String filter) {
        if (StringUtils.isNotEmpty(filter) && filter.equals(ListRequestParamUtil.FILTER_AVAILABILITY)) {
            wishList.setItems(ListUtil.filterListByAvailability(wishList.getItems()));
        }
    }

    private void enrichGetListResponse(CustomerList wishlist, Long userId, String userGuid, String uriHost,
                                       String customerHost, boolean expandUser, boolean expandItems, boolean isDefaultList,
                                       String sortBy, String sortOrder) {
        if (expandItems) {
            listCatalogService.populateListsItemDetails(wishlist.getWishlist());
            sortingExecutor.sort(wishlist, sortBy, sortOrder);
        } else if (!isDefaultList) {
            listCatalogService.populateListItemImageUrlsList(wishlist.getWishlist());
        }
        excludeSubResourceFromResponse(wishlist, expandUser, expandItems, isDefaultList);
        ListUtil.populateLinks(wishlist, userId, userGuid, uriHost, customerHost);
    }

    private void excludeSubResourceFromResponse(CustomerList wishlist,
                                                boolean expandUser, boolean expandItems, boolean isDefaultList) {
        if (!expandUser) {
            wishlist.setUser(null);
        }
        wishlist.getWishlist().forEach(w -> excludeSubResourceFromListResponse(expandItems, w, isDefaultList));
    }

    private void excludeSubResourceFromListResponse(boolean expandItems, WishList wishList, boolean isDefaultList) {
        if (!expandItems) {
            wishList.setItems(null);
        }
        if (isDefaultList) {
            wishList.setImageUrlsList(null);
        }
    }

    public void deleteList(String token, String listGuid, UserQueryParam userQueryParam) {
        customerUserService.validateNotBothUserIdAndGuidSpecified(userQueryParam.getUserId(), userQueryParam.getUserGuid());
        UserResponse userResponse = customerUserService.retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), !isWishlistV1CreateInvalidUser);
        customerUserService.populateUserQueryParam(userQueryParam, userResponse, false);
        RestResponse response = restClient.deleteList(token, listGuid, userQueryParam);
        if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new ListServiceException(response.getStatusCode(), response.getBody());
        }
    }

    public void updateList(String token, String listGuid, UserQueryParam userQueryParam, CustomerList inputJsonObj) {
        customerUserService.validateNotBothUserIdAndGuidSpecified(userQueryParam.getUserId(), userQueryParam.getUserGuid());
        UserResponse userResponse = customerUserService.retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), !isWishlistV1CreateInvalidUser);
        customerUserService.populateUserQueryParam(userQueryParam, userResponse, false);
        RestResponse response = restClient.updateWishlist(token, listGuid, userQueryParam, inputJsonObj);
        if (response != null) {
            if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), response.getBody());
            }
        }
    }

    public void deleteItem(String listGuid, String itemGuid) {
        CustomerList wishlist = getCustomerListWishlistResponse(restClient.getListByGuid(listGuid, new ListQueryParam(), new PaginationQueryParam()));
        Boolean guestUser = false;
        if (wishlist != null && wishlist.getUser() != null) {
            guestUser = customerUserService.retrieveUserGuestInfo(wishlist.getUser().getId(), wishlist.getUser().getGuid(), !isWishlistV1CreateInvalidUser);
        }
        RestResponse response = restClient.deleteItem(listGuid, itemGuid, guestUser);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public void deleteFavoriteItem(String listGuid, ListQueryParam listQueryParam) {
        CustomerList wishlist = getCustomerListWishlistResponse(restClient.getListByGuid(listGuid, new ListQueryParam(), new PaginationQueryParam()));
        Boolean guestUser = false;
        if (wishlist != null && wishlist.getUser() != null) {
            guestUser = customerUserService.retrieveUserGuestInfo(wishlist.getUser().getId(), wishlist.getUser().getGuid(), !isWishlistV1CreateInvalidUser);
        }
        if (listQueryParam.getUpcId() == null || listQueryParam.getUpcId() == 0) {
            Integer upcId = listCatalogService.findUpcIdFromProduct(listQueryParam.getProductId());
            listQueryParam.setUpcId(upcId);
        }
        RestResponse response = restClient.deleteFavoriteItem(listGuid, guestUser, listQueryParam);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public void updateItemPriority(String token, String listGuid, String itemGuid, Long userId, String userGuid, Item item) {
        customerUserService.validateNotBothUserIdAndGuidSpecified(userId, userGuid);
        UserResponse userResponse = customerUserService.retrieveUser(userId, userGuid, !isWishlistV1CreateInvalidUser);
        Boolean guestUser = false;
        if (userResponse != null) {
            userId = userResponse.getId();
            userGuid = userResponse.getGuid();
            guestUser = userResponse.isGuestUser();
        }
        ItemDTO itemRequest = new ItemDTO();
        Optional.ofNullable(item).ifPresent(i -> itemRequest.setPriority(i.getPriority()));
        RestResponse response = restClient.updateItemPriority(token, listGuid, itemGuid, userId, userGuid, guestUser, itemRequest);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.OK.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public void moveItemToWishlist(String token, String listGuid, CustomerList customerList) {
        String itemGuid = customerList.getWishlist().get(0).getItems().get(0).getItemGuid();
        UserQueryParam userQueryParam = new UserQueryParam();
        if (customerList.getUser() != null) {
            customerUserService.validateNotBothUserIdAndGuidSpecified(customerList.getUser().getId(), customerList.getUser().getGuid());
            UserResponse userResponse = customerUserService.retrieveUser(customerList.getUser().getId(), customerList.getUser().getGuid(), !isWishlistV1CreateInvalidUser);
            customerUserService.populateUserQueryParam(userQueryParam, userResponse, false);
        }
        RestResponse response = restClient.moveItemToWishlist(token, listGuid, itemGuid, userQueryParam);
        if (response != null) {
            String responseStr = response.getBody();
            if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        }
    }

    public CustomerList createWishList(String token, CustomerList inputCustomerList) {

        validateInputCatalogData(inputCustomerList);

        UserResponse userResponse = null;
        UserQueryParam userQueryParam = new UserQueryParam();
        WishListDTO wishlistDTO = null;

        if (inputCustomerList != null && inputCustomerList.getWishlist() != null && inputCustomerList.getWishlist().get(0) != null) {
            WishList wishList = inputCustomerList.getWishlist().get(0);
            listCatalogService.populateWishListItemDetailsForNewItems(wishList);
            wishlistDTO = mapperFacade.map(wishList, WishListDTO.class);
            User user = Optional.ofNullable(inputCustomerList.getUser()).orElse(new User());
            customerUserService.validateNotBothUserIdAndGuidSpecified(user.getId(), user.getGuid());
            userResponse = customerUserService.retrieveUser(user.getId(), user.getGuid(), true);
            userQueryParam.setGuestUser(userResponse.isGuestUser());
            wishlistDTO.setUserGuid(userResponse.getGuid());
            wishlistDTO.setUserId(userResponse.getId());
        }

        RestResponse createListResponse = restClient.createList(token, wishlistDTO, userQueryParam);

        CustomerList newCreatedList = getCustomerListWishlistResponse(createListResponse);
        newCreatedList.setUser(mapperFacade.map(userResponse, User.class));
        if (inputCustomerList != null && CollectionUtils.isNotEmpty(inputCustomerList.getWishlist()) && CollectionUtils.isNotEmpty(newCreatedList.getWishlist())) {
            newCreatedList.setMeta(ListUtil.buildAnalyticsMeta(newCreatedList.getWishlist().get(0)));
            listCatalogService.copyItemCatalogDetails(inputCustomerList.getWishlist().get(0), newCreatedList.getWishlist().get(0));
        }

        return newCreatedList;
    }

    public CustomerList addItemToDefaultWishlist(CustomerList inputCustomerList) {

        validateInputCatalogData(inputCustomerList);

        WishList wishList = inputCustomerList.getWishlist().get(0);
        User user = Optional.ofNullable(inputCustomerList.getUser()).orElse(new User());
        UserQueryParam userQueryParam = new UserQueryParam();
        UserResponse userResponse;
        boolean noUserInfo = user.getId() == null && user.getGuid() == null;
        if (listCreateUser && noUserInfo) {
            userResponse = customerUserService.createGuestUser();
        } else {
            boolean createUserIfNotFoundByUserId = isWishlistV1CreateInvalidUser && user.getGuid() == null;
            boolean isExistingUserRequired = !createUserIfNotFoundByUserId;
            userResponse = customerUserService.retrieveUser(user.getId(), user.getGuid(), isExistingUserRequired);
            if (userResponse == null && createUserIfNotFoundByUserId) {
                userResponse = customerUserService.createGuestUser(user.getId());
            }
        }
        customerUserService.populateUserQueryParamWithFirstName(userQueryParam, userResponse);
        listCatalogService.populateWishListItemDetailsForNewItems(wishList);

        WishListDTO wishlistDTO = mapperFacade.map(wishList, WishListDTO.class);

        RestResponse response = restClient.addItemToDefaultWishlist(wishlistDTO.getItems(), userQueryParam, wishlistDTO.getOnSaleNotify());
        CustomerList newCreatedList = getCustomerListWishlistResponse(response);
        newCreatedList.setUser(mapperFacade.map(userResponse, User.class));
        newCreatedList.setMeta(ListUtil.buildAnalyticsMeta(wishList));
        listCatalogService.copyItemCatalogDetails(wishList, newCreatedList.getWishlist().get(0));

        return newCreatedList;
    }

    public CustomerList addItemToGivenList(String listGuid, CustomerList inputCustomerList, UserQueryParam userQueryParam) {

        validateInputCatalogData(inputCustomerList);

        WishList wishList = inputCustomerList.getWishlist().get(0);
        if (inputCustomerList.getUser() != null) {
            userQueryParam.setUserId(inputCustomerList.getUser().getId());
            userQueryParam.setUserGuid(inputCustomerList.getUser().getGuid());
        }
        customerUserService.validateNotBothUserIdAndGuidSpecified(userQueryParam.getUserId(), userQueryParam.getUserGuid());
        UserResponse userResponse = customerUserService.retrieveUser(userQueryParam.getUserId(), userQueryParam.getUserGuid(), !isWishlistV1CreateInvalidUser);
        customerUserService.populateUserQueryParam(userQueryParam, userResponse, false);

        listCatalogService.populateWishListItemDetailsForNewItems(wishList);

        WishListDTO wishlistDTO = mapperFacade.map(wishList, WishListDTO.class);

        RestResponse response = restClient.addItemToGivenList(listGuid, wishlistDTO.getItems(), userQueryParam);
        CustomerList newCreatedList = getCustomerListWishlistResponse(response);
        newCreatedList.setUser(mapperFacade.map(userResponse, User.class));
        newCreatedList.setMeta(ListUtil.buildAnalyticsMeta(wishList));
        listCatalogService.copyItemCatalogDetails(wishList, newCreatedList.getWishlist().get(0));

        return newCreatedList;

    }

    public void emailShareWishlist(String token, String listGuid, EmailShare inputJsonObj) {
        try {
            EmailShareDTO emailShareDTO = prepareEmailShareDTO(listGuid, inputJsonObj, token);
            RestResponse response = restClient.emailShareWishlist(token, listGuid, emailShareDTO);
            if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
                String responseStr = response.getBody();
                throw new ListServiceException(response.getStatusCode(), responseStr);
            }
        } catch (UnsupportedEncodingException e) {
            throw new ListServiceException(e.getMessage(), e);
        }
    }

    public void mergeList(String token, CustomerListMerge listMerge) {
        MergeListRequest mergeRequest = prepareMergeListRequest(listMerge);
        RestResponse response = restClient.mergeList(token, mergeRequest);
        if (response != null && response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new ListServiceException(response.getStatusCode(), response.getBody());
        }
    }

    protected MergeListRequest prepareMergeListRequest(CustomerListMerge listMerge) {
        MergeListRequest mergeRequest = new MergeListRequest();
        if (listMerge != null) {
            if (listMerge.getGuestUserId() != null && listMerge.getGuestUserId() > 0) {
                UserResponse guestUser = customerUserService.retrieveUser(listMerge.getGuestUserId(), null, false);
                if (guestUser != null) {
                    mergeRequest.setSrcUserId(guestUser.getId());
                    mergeRequest.setSrcGuestUser(guestUser.isGuestUser());
                }
            }
            if (listMerge.getUserId() != null && listMerge.getUserId() > 0) {
                UserResponse user = customerUserService.retrieveUser(listMerge.getUserId(), null, false);
                if(user != null) {
                    mergeRequest.setTargetUserId(user.getId());
                    mergeRequest.setTargetUserGuid(user.getGuid());
                    mergeRequest.setTargetGuestUser(user.isGuestUser());
                    if (user.getProfile() != null) {
                        mergeRequest.setTargetUserFirstName(user.getProfile().getFirstName());
                    }
                }
            }
        }
        return mergeRequest;
    }

    protected EmailShareDTO prepareEmailShareDTO(String listGuid, EmailShare inputJsonObj, String token) throws UnsupportedEncodingException {
        if (inputJsonObj == null) {
            return null;
        }
        inputJsonObj.setLink(UriUtils.decode(inputJsonObj.getLink(), StandardCharsets.UTF_8.name()));
        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        CustomerList wishlist = getCustomerListWishlistResponse(restClient.getListByGuid(listGuid, listQueryParam, new PaginationQueryParam()));
        if (wishlist.getUser() != null) {
            UserResponse user = customerUserService.retrieveUserProfile(token, wishlist.getUser().getId());
            if (user!=null && user.getLoginCredentials() != null){
                inputJsonObj.setFrom(user.getLoginCredentials().getUserName());
            }
        }
        EmailShareDTO emailShareDTO = mapperFacade.map(inputJsonObj, EmailShareDTO.class);

        if (CollectionUtils.isNotEmpty(wishlist.getWishlist()) && wishlist.getWishlist().get(0) != null) {
            WishList wishList = wishlist.getWishlist().get(0);
            listCatalogService.populateWishListItemDetails(wishList, true);
            List<Item> availableItems = ListUtil.filterListByAvailability(wishList.getItems());
            if (CollectionUtils.isNotEmpty(availableItems)) {
                sortingExecutor.sort(availableItems, SortByField.TOP_PICK, SortOrder.DESC);
                emailShareDTO.setItems(mapperFacade.mapAsList(availableItems, EmailItemDTO.class));
            }
        }

        return emailShareDTO;
    }

    private void validateInputCatalogData(CustomerList inputCustomerList) {
        if (inputCustomerList != null && inputCustomerList.getWishlist() != null && inputCustomerList.getWishlist().get(0) != null) {
            WishList wishlist = inputCustomerList.getWishlist().get(0);
            if (wishlist.getItems() != null) {
                wishlist.getItems().forEach(item -> {
                    boolean productDefined = item.getProduct() != null && item.getProduct().getId() != null && item.getProduct().getId() > 0;
                    boolean upcDefined = item.getUpc() != null && ((item.getUpc().getId() != null && item.getUpc().getId() > 0) ||
                            (item.getUpc().getUpcNumber() != null && item.getUpc().getUpcNumber() > 0));
                    if (productDefined && upcDefined) {
                        throw ListServiceException.buildExceptionWithStatusAndMessage(Response.Status.BAD_REQUEST.getStatusCode(), ErrorConstants.INVALID_INPUT);
                    }
                });
            }
        }
    }

    public FavoriteList getFavoriteItemsFromDefaultList(String userGuid) {
        try {
            UserResponse userResponse = customerUserService.retrieveUser(null, userGuid, false);
            Long userId = userResponse != null ? userResponse.getId() : null;
            RestResponse response = restClient.getFavoriteItemsFromDefaultList(userId);
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

    private void populateWishListWithUserResponse(CustomerList wishlist, UserResponse userResponse) {
        if (userResponse != null) {
            wishlist.setUser(mapperFacade.map(userResponse, User.class));
        }
    }
}
