package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.ListRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.client.response.ActivityLogDTO;
import com.macys.selection.xapi.list.client.response.ActivityLogPageDTO;
import com.macys.selection.xapi.list.client.response.CollaborativeListsDTO;
import com.macys.selection.xapi.list.client.response.CollaborativeRequestDTO;
import com.macys.selection.xapi.list.client.response.CollaborativeRequestsDTO;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.CollaboratorsDTO;
import com.macys.selection.xapi.list.client.response.ItemFeedbackDTO;
import com.macys.selection.xapi.list.client.response.ListFeedbackDTO;
import com.macys.selection.xapi.list.client.response.UserAvatarDTO;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.common.CollaboratorPrivilegeEnum;
import com.macys.selection.xapi.list.comparators.SortOrder;
import com.macys.selection.xapi.list.data.converters.CollaborativeListConverter;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.SortQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListManageRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaborativeListRequest;
import com.macys.selection.xapi.list.rest.request.collaborators.CollaboratorPrivilegeRequest;
import com.macys.selection.xapi.list.rest.request.cookie.ListCookies;
import com.macys.selection.xapi.list.rest.response.ActivityLog;
import com.macys.selection.xapi.list.rest.response.ActivityLogPage;
import com.macys.selection.xapi.list.rest.response.CollaborativeList;
import com.macys.selection.xapi.list.rest.response.CollaborativeListDetails;
import com.macys.selection.xapi.list.rest.response.CollaborativeListOwner;
import com.macys.selection.xapi.list.rest.response.CollaborativeListResponse;
import com.macys.selection.xapi.list.rest.response.CollaborativeRequestResponse;
import com.macys.selection.xapi.list.rest.response.Collaborator;
import com.macys.selection.xapi.list.rest.response.CollaboratorApprovalResponse;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.UserProfile;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.ListRequestParamUtil;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CollaborativeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CollaborativeService.class);

    private JsonToObjectConverter<CollaboratorsDTO> collaboratorsConverter = new JsonToObjectConverter<>(CollaboratorsDTO.class);
    private JsonToObjectConverter<CollaboratorDTO> converterCollaborator = new JsonToObjectConverter<>(CollaboratorDTO.class);
    private JsonToObjectConverter<ActivityLogPageDTO> activityLogConverter = new JsonToObjectConverter<>(ActivityLogPageDTO.class);
    private JsonToObjectConverter<CollaborativeListsDTO> converterCollaborativeLists = new JsonToObjectConverter<>(CollaborativeListsDTO.class);
    private JsonToObjectConverter<CollaborativeRequestsDTO> converterCollaborativeRequests = new JsonToObjectConverter<>(CollaborativeRequestsDTO.class);
    private JsonToObjectConverter<ListFeedbackDTO> itemFeedbackConverter = new JsonToObjectConverter<>(ListFeedbackDTO.class);
    private final ListRestClient restClient;

    private final WishlistService wishlistService;
    private final CustomerUserService customerUserService;
    private final UpcService upcService;
    private final ProductService productService;
    private final PromotionService promotionService;
    private final UserAvatarService userAvatarService;
    private final MapperFacade mapperFacade;

    @Autowired
    public CollaborativeService(WishlistService wishlistService,
                                ListRestClient restClient,
                                CustomerUserService customerUserService,
                                UpcService upcService,
                                ProductService productService,
                                PromotionService promotionService,
                                UserAvatarService userAvatarService,
                                MapperFacade mapperFacade) {
        this.restClient = restClient;
        this.wishlistService = wishlistService;
        this.customerUserService = customerUserService;
        this.upcService = upcService;
        this.productService = productService;
        this.promotionService = promotionService;
        this.userAvatarService = userAvatarService;
        this.mapperFacade = mapperFacade;
    }

    public String createCollaborativeList(ListCookies cookie, CollaborativeListRequest inputCollaborativeList) {
        LOGGER.debug("Start creating Collaborative list");
        CustomerList listResponse = wishlistService.createWishList(
            cookie.getToken(), CollaborativeListConverter.convertToCustomerList(inputCollaborativeList));
        String listGuid = listResponse.getWishlist().get(0).getListGuid();


        restClient.addCollaborator(mapperFacade.map(createOwner(listGuid, inputCollaborativeList.getUserGuid()), CollaboratorDTO.class));
        LOGGER.debug("Finish creating Collaborative list");
        return listGuid;
    }

    public void manageList(String token, String listGuid, UserQueryParam userQueryParam, CollaborativeListManageRequest inputCollaborativeList) {
        wishlistService.updateList(token, listGuid, userQueryParam, CollaborativeListConverter.convertToCustomerList(listGuid, inputCollaborativeList));
    }

    public CollaboratorDTO addCollaborator(Collaborator collaborator) {
        LOGGER.debug("Start adding collaborators");
        validateCollaborators(Collections.singleton(collaborator));
        collaborator.setPrivilege(CollaboratorPrivilegeEnum.PENDING.name());
        RestResponse restResponse = restClient.addCollaborator(mapperFacade.map(collaborator, CollaboratorDTO.class));
        CollaboratorDTO response = converterCollaborator.parseJsonToObject(restResponse.getBody());
        LOGGER.debug("Finish adding collaborators");
        return response;
    }

    public CollaborativeListResponse getCollaboratorsByListGuid(String listGuid) {
        LOGGER.debug("Start fetching collaborators");
        List<Collaborator> collaborators = getCollaborators(listGuid);
        populateCollaboratorUsers(collaborators);
        populateCollaboratorAvatars(collaborators);
        LOGGER.debug("Finish fetching collaborators");
        return createResponse(collaborators);
    }

    public CollaborativeListResponse getUserCollaborators(String userGuid, Set<String> excludeIds) {
        LOGGER.debug("Start fetching collaborators");
        RestResponse restResponse = restClient.getUserCollaborators(userGuid, excludeIds);
        CollaboratorsDTO response = collaboratorsConverter.parseJsonToObject(restResponse.getBody());
        List<Collaborator> collaborators = mapperFacade.mapAsList(response.getCollaborators(), Collaborator.class);
        populateCollaboratorUsers(collaborators);
        populateCollaboratorAvatars(collaborators);
        LOGGER.debug("Finish fetching collaborators");
        return createResponse(collaborators);
    }

    public List<Collaborator> getCollaborators(String listGuid) {
        RestResponse restResponse = restClient.getCollaboratorsByGuid(listGuid);
        CollaboratorsDTO response = collaboratorsConverter.parseJsonToObject(restResponse.getBody());
        return mapperFacade.mapAsList(response.getCollaborators(), Collaborator.class);
    }

    public RestResponse updateCollaboratorPrivilege(String listGuid, CollaboratorPrivilegeRequest privilegeRequest) {
        LOGGER.debug("Start updating collaborators privilege");
        Collaborator collaborator = new Collaborator();
        collaborator.setUserGuid(privilegeRequest.getCollaborator().getUserGuid());
        collaborator.setPrivilege(privilegeRequest.getCollaborator().getPrivilege());
        RestResponse response = restClient.updateCollaboratorPrivilege(
            listGuid,
            mapperFacade.map(collaborator, CollaboratorDTO.class)
        );
        LOGGER.debug("Finish updating collaborators privilege");
        return response;
    }

    public CollaboratorApprovalResponse getApprovals(UserQueryParam userQueryParam, PaginationQueryParam paginationQueryParam) {
        RestResponse restResponse = restClient.getApprovals(userQueryParam, paginationQueryParam);

        CollaborativeListsDTO listsDTO = converterCollaborativeLists.parseJsonToObject(restResponse.getBody());
        CollaboratorApprovalResponse collaboratorApprovalResponse = new CollaboratorApprovalResponse();
        List<CollaborativeList> list = mapperFacade.mapAsList(listsDTO.getCollaboratorLists(), CollaborativeList.class);

        List<Collaborator> collaborators = new ArrayList<>();
        list.forEach(l -> {
            if (CollectionUtils.isNotEmpty(l.getCollaborators())) {
                collaborators.addAll(l.getCollaborators());
            }
        });
        populateCollaboratorUsers(collaborators);
        populateCollaboratorAvatars(collaborators);
        collaboratorApprovalResponse.setList(list);

        return collaboratorApprovalResponse;
    }

    public CollaborativeRequestResponse getRequests(UserQueryParam userQueryParam, PaginationQueryParam paginationQueryParam) {
        RestResponse restResponse = restClient.getRequests(userQueryParam, paginationQueryParam);

        CollaborativeRequestsDTO listsDTO = converterCollaborativeRequests.parseJsonToObject(restResponse.getBody());
        CollaborativeRequestResponse collaborativeRequestResponse = new CollaborativeRequestResponse();

        Map<CollaboratorPrivilegeEnum, List<CollaborativeListOwner>> result = new HashMap<>();
        for (CollaborativeRequestDTO dto: listsDTO.getRequests()) {
            CollaborativeListOwner collaborativeListOwner = new CollaborativeListOwner();
            collaborativeListOwner.setListGuid(dto.getListGuid());
            collaborativeListOwner.setName(dto.getName());
            collaborativeListOwner.setOwner(mapperFacade.map(dto.getCollaborators().get(0), Collaborator.class));

            CollaboratorPrivilegeEnum privilege = CollaboratorPrivilegeEnum.parsePrivilege(dto.getRequestPrivilege());
            if (privilege != null) {
                if (result.containsKey(privilege)) {
                    List<CollaborativeListOwner> lists = new ArrayList<>(result.get(privilege));
                    lists.add(collaborativeListOwner);
                    result.put(privilege, lists);
                } else {
                    result.put(privilege, Collections.singletonList(collaborativeListOwner));
                }
            }
        }

        List<Collaborator> collaborators = new ArrayList<>();
        result.values().forEach(lists -> lists.forEach(l -> {
                if (l.getOwner() != null) {
                    collaborators.add(l.getOwner());
                }
            }
        ));
        populateCollaboratorUsers(collaborators);
        populateCollaboratorAvatars(collaborators);
        collaborativeRequestResponse.setRequests(result);

        return collaborativeRequestResponse;
    }

    void populateCollaboratorUsers(Collection<Collaborator> collaborators) {
        if (CollectionUtils.isEmpty(collaborators)) {
            return;
        }
        List<UserResponse> users = getCollaboratorUsers(collaborators);
        if (CollectionUtils.isNotEmpty(users)) {
            Map<String, UserResponse> userMap = users.stream().collect(Collectors.toMap(UserResponse::getGuid, item -> item));
            populateCollaboratorUsers(collaborators, userMap);
        }
    }

    void populateCollaboratorAvatars(Collection<Collaborator> collaborators) {
        if (CollectionUtils.isEmpty(collaborators)) {
            return;
        }
        List<UserAvatarDTO> avatars = getCollaboratorProfilePictures(collaborators);
        if (CollectionUtils.isNotEmpty(avatars)) {
            Map<String, UserAvatarDTO> avatarsMap = avatars.stream().collect(Collectors.toMap(UserAvatarDTO::getUserGuid, item -> item));
            populateCollaboratorAvatars(collaborators, avatarsMap);
        }
    }

    private void populateCollaboratorUsers(Collection<Collaborator> collaborators, Map<String, UserResponse> userMap) {
       collaborators.forEach(collaborator -> {
            UserResponse user = userMap.get(collaborator.getUserGuid());
            if (user != null && user.getProfile() != null) {
                collaborator.setFirstName(user.getProfile().getFirstName());
                collaborator.setLastName(user.getProfile().getLastName());
            }
        });
    }

    private void populateCollaboratorAvatars(Collection<Collaborator> collaborators, Map<String, UserAvatarDTO> avatarsMap) {
        collaborators.forEach(collaborator -> {
            UserAvatarDTO avatar = avatarsMap.get(collaborator.getUserGuid());
            if (avatar != null) {
                collaborator.setProfilePicture(avatar.getAvatar());
            }
        });
    }

    private  List<UserResponse> getCollaboratorUsers(Collection<Collaborator> collaborators) {
        List<UserResponse> users = null;
        if (CollectionUtils.isNotEmpty(collaborators)) {
            Set<String> userGuids = collaborators.stream().map(Collaborator::getUserGuid).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(userGuids)) {
                users = customerUserService.retrieveUsersByGuids(userGuids);
            }
        }
        return users;
    }

    private  List<UserAvatarDTO> getCollaboratorProfilePictures(Collection<Collaborator> collaborators) {
        List<UserAvatarDTO> avatars = null;
        if (CollectionUtils.isNotEmpty(collaborators)) {
            Set<String> userGuids = collaborators.stream().map(Collaborator::getUserGuid).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
            if (CollectionUtils.isNotEmpty(userGuids)) {
                avatars = userAvatarService.getUserAvatars(userGuids);
            }
        }
        return avatars;
    }

    private void validateCollaborators(Collection<Collaborator> collaborators) {
        if (CollectionUtils.isNotEmpty(collaborators)) {
           List<UserResponse> users = getCollaboratorUsers(collaborators);
           List<String> loadedUsersGuids = users != null ? users.stream().map(UserResponse::getGuid).collect(Collectors.toList()) : new ArrayList<>();
           for (Collaborator collaborator : collaborators) {
               if (!loadedUsersGuids.contains(collaborator.getUserGuid())) {
                   throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_COLLABORATOR_ID);
               }
           }
        }
    }

    public void addItemFeedback(String listGuid, String itemGuid, String userGuid, String itemFeedback) {
        UserResponse userResponse = customerUserService.retrieveUser(null, userGuid);
        UserQueryParam userQueryParam = new UserQueryParam();
        customerUserService.populateUserQueryParam(userQueryParam, userResponse, false);
        RestResponse response = restClient.addItemFeedback(listGuid, itemGuid, userGuid, itemFeedback);
        if (response.getStatusCode() != Response.Status.NO_CONTENT.getStatusCode()) {
            throw new ListServiceException(response.getStatusCode(), response.getBody());
        }
    }

    private Collaborator createOwner(String listGuid, String userGuid) {
        Collaborator collaborator = new Collaborator();
        collaborator.setUserGuid(userGuid);
        collaborator.setListGuid(listGuid);
        collaborator.setPrivilege(CollaboratorPrivilegeEnum.OWNER.name());
        return collaborator;
    }

    private CollaborativeListResponse createResponse(List<Collaborator> collaborators) {
        CollaborativeListResponse response = new CollaborativeListResponse();
        response.setCollaborators(collaborators);
        return response;
    }

    private ListFeedbackDTO getListFeedback(String listGuid, String userGuid, String viewerGuid) {
        RestResponse response = restClient.getListFeedback(listGuid, userGuid, viewerGuid);
        return itemFeedbackConverter.parseJsonToObject(response.getBody());
    }

    public ActivityLogPage getActivityLog(String listGuid, String userGuid, PaginationQueryParam paginationQueryParam,
                                          SortQueryParam sortQueryParam, boolean populateUsers) {
       RestResponse response = restClient.getActivityLog(listGuid, userGuid, paginationQueryParam, sortQueryParam);
        if (response.getStatusCode() == Response.Status.OK.getStatusCode()) {
            return getActivityLogPageFromResponse(response.getBody(), populateUsers);
        } else {
            throw new ListServiceException(response.getStatusCode(), response.getBody());
        }
    }

    public CollaborativeListDetails getCollaborativeListDetails(String token, String listGuid, String viewerGuid, String uriHost) {
        CollaborativeListDetails details = new CollaborativeListDetails();

        UserResponse viewer = customerUserService.retrieveUser(null, viewerGuid);
        details.setViewer(mapperFacade.map(viewer, UserProfile.class));

        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);
        CustomerList customerList = wishlistService.getListByGuid(listGuid, listQueryParam, uriHost);

        if (CollectionUtils.isEmpty(customerList.getWishlist())) {
            return details;
        }

        WishList listResponse = customerList.getWishlist().get(0);

        listResponse = promotionService.getPromotions(listResponse, listQueryParam);

        mapperFacade.map(listResponse, details);

        details.setOwner(new UserProfile());
        if (customerList.getUser() != null) {
            details.getOwner().setGuid(customerList.getUser().getGuid());
        }

        details.setCollaborators(getCollaborators(listResponse.getListGuid()));

        if (CollectionUtils.isNotEmpty(listResponse.getItems())) {
            PaginationQueryParam activityPagination = new PaginationQueryParam(0, 1);
            SortQueryParam sortQueryParam = new SortQueryParam();
            sortQueryParam.setSortOrder(SortOrder.DESC.getValue());
            ActivityLogPage activityLogPage = getActivityLog(listResponse.getListGuid(), customerList.getUser().getGuid(),
                    activityPagination, sortQueryParam, false);
            if (activityLogPage != null && CollectionUtils.isNotEmpty(activityLogPage.getActivityLog())) {
                details.setRecentActivity(activityLogPage.getActivityLog().get(0));
            }
        }

        populateItemFeedback(details);

        details.setNumberOfCollaborators(details.getCollaborators() != null ? details.getCollaborators().size() : 0);
        populateUserDetails(details);

        populateUserAvatars(details);

        return details;
    }

    private void populateUserDetails(CollaborativeListDetails details) {
        Set<String> userGuids = new HashSet<>();
        userGuids.add(details.getOwner().getGuid());
        if(details.getRecentActivity() != null) {
            userGuids.add(details.getRecentActivity().getUserGuid());
        }
        if (CollectionUtils.isNotEmpty(details.getCollaborators())) {
            userGuids.addAll(details.getCollaborators().stream().map(Collaborator::getUserGuid).collect(Collectors.toList()));
        }

        List<UserResponse> users = customerUserService.retrieveUsersByGuids(userGuids);
        Map<String, UserResponse> userMap = users.stream().collect(Collectors.toMap(UserResponse::getGuid, item -> item));
        UserResponse owner = userMap.get(details.getOwner().getGuid());
        if (owner != null) {
            details.setOwner(mapperFacade.map(owner, UserProfile.class));
        }
        if (CollectionUtils.isNotEmpty(details.getCollaborators())) {
            populateCollaboratorUsers(details.getCollaborators(), userMap);
        }
        if(details.getRecentActivity() != null) {
            UserResponse resentActivityUser = userMap.get(details.getRecentActivity().getUserGuid());
            if (resentActivityUser != null && resentActivityUser.getProfile() != null) {
                details.getRecentActivity().setUserLastName(resentActivityUser.getProfile().getLastName());
                details.getRecentActivity().setUserFirstName(resentActivityUser.getProfile().getFirstName());
            }
        }
    }

    private void populateUserAvatars(CollaborativeListDetails details) {
        Set<String> userGuids = new HashSet<>();
        userGuids.add(details.getViewer().getGuid());
        userGuids.add(details.getOwner().getGuid());
        if(details.getRecentActivity() != null) {
            userGuids.add(details.getRecentActivity().getUserGuid());
        }
        if (CollectionUtils.isNotEmpty(details.getCollaborators())) {
            userGuids.addAll(details.getCollaborators().stream().map(Collaborator::getUserGuid).collect(Collectors.toList()));
        }

        List<UserAvatarDTO> avatars = userAvatarService.getUserAvatars(userGuids);
        Map<String, UserAvatarDTO> avatarMap = avatars.stream().collect(Collectors.toMap(UserAvatarDTO::getUserGuid, item -> item));
        UserAvatarDTO viewer = avatarMap.get(details.getViewer().getGuid());
        if (viewer != null) {
            details.getViewer().setProfilePicture(viewer.getAvatar());
        }
        UserAvatarDTO owner = avatarMap.get(details.getOwner().getGuid());
        if (owner != null) {
            details.getOwner().setProfilePicture(owner.getAvatar());
        }
        if (CollectionUtils.isNotEmpty(details.getCollaborators())) {
            populateCollaboratorAvatars(details.getCollaborators(), avatarMap);
        }
        if(details.getRecentActivity() != null) {
            UserAvatarDTO resentActivityUserAvatar = avatarMap.get(details.getRecentActivity().getUserGuid());
            if (resentActivityUserAvatar != null) {
                details.getRecentActivity().setProfilePictureUrl(resentActivityUserAvatar.getAvatar());
            }
        }
    }

    private void populateItemFeedback(CollaborativeListDetails details) {
        ListFeedbackDTO listFeedback = getListFeedback(details.getListGuid(), details.getOwner().getGuid(), details.getViewer().getGuid());
        if (listFeedback == null || CollectionUtils.isEmpty(listFeedback.getItemFeedback())) {
            return;
        }

        Map<String, ItemFeedbackDTO> itemFeedbackDTOMap = listFeedback.getItemFeedback().stream()
                .collect(Collectors.toMap(ItemFeedbackDTO::getItemGuid, itemFeedback -> itemFeedback));

        details.getItems().forEach(item -> {
            ItemFeedbackDTO itemFeedbackDTO = itemFeedbackDTOMap.get(item.getItemGuid());
            if (itemFeedbackDTO != null) {
                item.setLikes(itemFeedbackDTO.getLikes());
                item.setDislikes(itemFeedbackDTO.getDislikes());
                item.setFeedback(itemFeedbackDTO.getFeedback());
            }
        });
    }

    private ActivityLogPage getActivityLogPageFromResponse(String responseBody, boolean populateUsers) {
        ActivityLogPageDTO activityLogPageDTO = activityLogConverter.parseJsonToObject(responseBody);
        ActivityLogPage activityLogPage = new ActivityLogPage();
        if (activityLogPageDTO == null) {
            return activityLogPage;
        }
        activityLogPage.setTotalNumberOfLogs(activityLogPageDTO.getTotalNumberOfLogs());

        if (CollectionUtils.isNotEmpty(activityLogPageDTO.getActivityLog())) {
            List<ActivityLog> activityLogs = new ArrayList<>();

            List<Integer> upcIds = activityLogPageDTO.getActivityLog().stream()
                .map(activity -> activity.getUpcId().intValue()).distinct().collect(Collectors.toList());
            Map<Integer, String> upcProductNames = getProductNameMapForUpc(upcIds);
            activityLogPageDTO.getActivityLog().forEach(activityLogDTO -> {
                ActivityLog activityLog = mapperFacade.map(activityLogDTO, ActivityLog.class);
                activityLog.setProductName(upcProductNames.get(activityLogDTO.getUpcId().intValue()));
                activityLogs.add(activityLog);
            });

            if (populateUsers) {
                List<String> userGuids = activityLogPageDTO.getActivityLog().stream()
                    .map(ActivityLogDTO::getUserGuid).distinct().collect(Collectors.toList());
                List<UserResponse> users = customerUserService.retrieveUsersByGuids(userGuids);
                Map<String, UserResponse> userMap = users != null
                    ? users.stream().collect(Collectors.toMap(UserResponse::getGuid, user -> user)) : new HashMap<>();
                List<UserAvatarDTO> avatars = userAvatarService.getUserAvatars(userGuids);
                Map<String, UserAvatarDTO> avatarsMap = avatars != null ?
                        avatars.stream().collect(Collectors.toMap(UserAvatarDTO::getUserGuid, avatar -> avatar)) : new HashMap<>();

                activityLogs.forEach(activityLog -> {
                    UserResponse user = userMap.get(activityLog.getUserGuid());
                    if (user != null && user.getProfile() != null) {
                        activityLog.setUserFirstName(user.getProfile().getFirstName());
                        activityLog.setUserLastName(user.getProfile().getLastName());
                    }
                    UserAvatarDTO avatarDTO = avatarsMap.get(activityLog.getUserGuid());
                    if (avatarDTO != null) {
                        activityLog.setProfilePictureUrl(avatarDTO.getAvatar());
                    }
                });
            }

            activityLogPage.setActivityLog(activityLogs);
        }
        return activityLogPage;
    }

    private Map<Integer, String> getProductNameMapForUpc(List<Integer> upcIds) {
        Map<Integer, String> upcProductNames = new HashMap<>();
        if (CollectionUtils.isNotEmpty(upcIds)) {
            List<UpcResponse> upcs = upcService.getUpcsByUpcIds(upcIds);
            List<Integer> productIds = upcs.stream().map(UpcResponse::getProductId).distinct().collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(productIds)) {
                List<ProductResponse> products = productService.getProductsByProdIds(productIds);
                products.forEach(product -> product.getUpcs().forEach(upcResponse -> {
                    if (upcIds.contains(upcResponse.getId())) {
                        upcProductNames.put(upcResponse.getId(), product.getName());
                    }
                }));
            }
        }
        return upcProductNames;
    }
}
