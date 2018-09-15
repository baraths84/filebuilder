package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.ListRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.CollaborativeListsDTO;
import com.macys.selection.xapi.list.client.response.ListsDTO;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.common.CollaboratorTypeEnum;
import com.macys.selection.xapi.list.common.ListTypeEnum;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.CollaborativeList;
import com.macys.selection.xapi.list.rest.response.Collaborator;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.ListSimple;
import com.macys.selection.xapi.list.rest.response.ListsPresentation;
import com.macys.selection.xapi.list.rest.response.ListsResponse;
import com.macys.selection.xapi.list.rest.response.WishList;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@Service
public class ListsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListsService.class);

    @Value("${collaborativeList.limit}")
    public Integer listsLimit;

    private final CustomerUserService customerUserService;
    private final MapperFacade mapperFacade;
    private final ListRestClient restClient;
    private final WishlistService wishlistService;
    private final CollaborativeService collaborativeService;
    private final ListCatalogService listCatalogService;

    private JsonToObjectConverter<CollaborativeListsDTO> converterCollaborativeLists = new JsonToObjectConverter<>(CollaborativeListsDTO.class);
    private JsonToObjectConverter<ListsDTO> converterLists = new JsonToObjectConverter<>(ListsDTO.class);

    @Autowired
    public ListsService(MapperFacade mapperFacade,
                        ListRestClient restClient,
                        WishlistService wishlistService,
                        CollaborativeService collaborativeService,
                        CustomerUserService customerUserService,
                        ListCatalogService listCatalogService) {
        this.mapperFacade = mapperFacade;
        this.restClient = restClient;
        this.wishlistService = wishlistService;
        this.collaborativeService = collaborativeService;
        this.customerUserService = customerUserService;
        this.listCatalogService = listCatalogService;
    }

    public ListsPresentation getListsByTypes(UserQueryParam userQueryParam, ListQueryParam listQueryParam, PaginationQueryParam paginationQueryParam) {
        LOGGER.debug("START of :: {}.getListsByTypes.", this.getClass().getSimpleName());
        ListsPresentation response = new ListsPresentation();
        response.setLists(new HashMap<>());

        UserResponse userResponse = customerUserService.retrieveUser(null, userQueryParam.getUserGuid());
        customerUserService.populateUserQueryParam(userQueryParam, userResponse, null);
        RestResponse wishlistResponse = restClient.getList(userQueryParam, listQueryParam, paginationQueryParam);
        CustomerList customerList = wishlistService.getCustomerListWishListsResponse(wishlistResponse, userQueryParam.getUserId());
        List<WishList> wishLists = Collections.emptyList();
        if (CollectionUtils.isNotEmpty(customerList.getWishlist())) {
            WishList list = customerList.getWishlist().get(0);
            if (list.getItems() == null) {
                list.setItems(Collections.emptyList());
            }
            wishLists = Collections.singletonList(list);
            listCatalogService.populateListsItemDetails(wishLists);
        }
        response.getLists().put(ListTypeEnum.WISH_LIST.getText(), wishLists);

        RestResponse ownerCollabResponse = restClient.getUserCollaborativeLists(
            userQueryParam.getUserGuid(), listsLimit, null, true);
        CollaborativeListsDTO collabLists = converterCollaborativeLists.parseJsonToObject(ownerCollabResponse.getBody());
        if (CollectionUtils.isEmpty(collabLists.getCollaboratorLists())) {
            RestResponse collabResponse = restClient.getUserCollaborativeLists(
                userQueryParam.getUserGuid(), listsLimit, null, false);
            collabLists = converterCollaborativeLists.parseJsonToObject(collabResponse.getBody());
        }

        List<CollaborativeList> collaborativeLists =  mapperFacade.mapAsList(
            collabLists.getCollaboratorLists() != null
                ? collabLists.getCollaboratorLists() : Collections.emptyList(), CollaborativeList.class);

        populateCollaborativeLists(collaborativeLists);

        response.getLists().put(ListTypeEnum.COLLABORATIVE_LIST.getText(), collaborativeLists);

        LOGGER.debug("END of :: {}.getListsByTypes.", this.getClass().getSimpleName());
        return response;
    }

    public ListsPresentation getCollaborativeLists(String userGuid, boolean hideOwner, boolean hideCollab) {
        LOGGER.debug("START of :: {}.getCollaborativeLists.", this.getClass().getSimpleName());
        customerUserService.retrieveUser(null, userGuid);
        ListsPresentation response = new ListsPresentation();
        response.setLists(new HashMap<>());
        if (!hideOwner) {
            response.getLists().put(CollaboratorTypeEnum.LIST_OWNER.getValue(), getCollabLists(userGuid, true));
        }
        if (!hideCollab) {
            response.getLists().put(CollaboratorTypeEnum.LIST_COLLABORATOR.getValue(), getCollabLists(userGuid, false));
        }
        LOGGER.debug("END of :: {}.getCollaborativeLists.", this.getClass().getSimpleName());
        return response;
    }

    public ListsResponse getAllListsByUserGuid(String userGuid) {
        LOGGER.debug("START of :: {}.getAllListsByUserGuid.", this.getClass().getSimpleName());
        customerUserService.retrieveUser(null, userGuid);
        RestResponse restResponse = restClient.getAllListsByUserGuid(userGuid);
        ListsDTO dto = converterLists.parseJsonToObject(restResponse.getBody());
        ListsResponse response = new ListsResponse();
        response.setList(mapperFacade.mapAsList(dto.getLists(), ListSimple.class));
        LOGGER.debug("END of :: {}.getAllListsByUserGuid.", this.getClass().getSimpleName());
        return response;
    }

    private List<CollaborativeList> getCollabLists(String userGuid, boolean owner) {
        RestResponse response = restClient.getUserCollaborativeLists(userGuid, null, null, owner);
        CollaborativeListsDTO dto = converterCollaborativeLists.parseJsonToObject(response.getBody());
        List<CollaborativeList> list = CollectionUtils.isNotEmpty(dto.getCollaboratorLists())
                ? mapperFacade.mapAsList(dto.getCollaboratorLists(), CollaborativeList.class)
                : Collections.EMPTY_LIST;

        populateCollaborativeLists(list);
        return list;
    }

    private void populateCollaborativeLists(List<CollaborativeList> list) {
        List<Collaborator> collaborators = new ArrayList<>();
        list.forEach(l -> {
            if (CollectionUtils.isNotEmpty(l.getCollaborators())) {
                collaborators.addAll(l.getCollaborators());
            }
        });
        listCatalogService.populateWishListItemDetails(list);
        collaborativeService.populateCollaboratorUsers(collaborators);
        collaborativeService.populateCollaboratorAvatars(collaborators);
    }

}
