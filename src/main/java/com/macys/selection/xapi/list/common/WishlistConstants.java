package com.macys.selection.xapi.list.common;

public final class WishlistConstants {

  public static final int STATUS_SUCCESS = 200;
  public static final int BAD_REQUEST = 400;
  public static final int NO_CONTENT = 204;
  public static final int STATUS_CREATED = 201;

  public static final String CUSTOMER_LIST_CLIENT_POOL = "customerListClientPool";
  public static final String LIST_CLIENT_POOL = "listClientPool";
  public static final String LIST_GUID = "listGuid";
  public static final String APPROVALS = "approvals";
  public static final String REQUESTS = "requests";
  public static final String PATH_PARAMETER_LIST_GUID = "/{listGuid}";
  public static final String PATH_PARAMETER_USER_GUID = "/{userGuid}";
  public static final String PRIVILEGE = "privilege";
  public static final String ITEM_GUID = "itemGuid";
  public static final String ITEM_FEEDBACK = "itemFeedback";
  public static final String DELETE_ITEM_PATH = "/{listGuid}/items/{itemGuid}";
  public static final String DELETE_LIST_PATH = "/{listGuid}";
  public static final String MOVE_ITEM_PATH = "/items/move";
  public static final String USER_ID = "userid";
  public static final String USER_GUID = "userGuid";
  public static final String CUSTOMER_WISH_LIST_CLIENT_POOL = "customerWishlistClientPool";
  public static final String MERGE_LIST_PATH = "/merge";
  public static final String COLLABORATORS = "/collaborators";
  public static final String AVATAR = "/avatar";
  public static final String OWNER = "/owner";
  public static final String COLLABORATOR = "/collaborator";
  public static final String USER_COLLABORATORS = "/userCollaborators";
  public static final String PROMOTIONS_CLIENT_POOL = "promotionsClientPool";
  public static final String PROMOTIONS_PATH = "/product/productids/{productids}";
  public static final String PRODUCT_IDS = "productids";
  public static final String PATH_PARAMETER_FILTER_NAME = "_filter";
  public static final String PATH_PARAMETER_FIELDS_NAME = "_fields";
  public static final String PATH_PARAMETER_FILTER_VALUE = "promotionAttribute.promotionAttributeName,BADGE_TEXT";
  public static final String PATH_PARAMETER_FIELDS_VALUE = "badgeTextAttributeValue";
  public static final String SECURITY_TOKEN_HEADER_NAME = "X-Macys-SecurityToken";
  public static final String SHARE_EMAIL_PATH_LIST_GUID = "/{listGuid}/shareemail";
  public static final String ADD_TO_GIVEN_LIST_BY_UPC_PATH = "/{listGuid}/items";
  public static final String ADD_TO_DEFAULT_LIST_PATH = "/items";
  public static final String WISH_LIST_TYPE_VALUE = "W";
  public static final String DELETE_FAVORITE = "/api/customer/v1/favorites/{listGuid}";
  public static final String ADD_FAV_TO_GIVEN_LIST_BY_PID_PATH = "/api/customer/v1/favorites";
  public static final String EMPTY_STRING = "";
  public static final String COMMA = ",";
  public static final String DECIMAL_FORMAT = "#.00";
  public static final String WISHLIST_ADD = "wishlist add";
  public static final String UPC_CLIENT_POOL = "upcClientPool";
  public static final String UPCS_PATH = "/upcs";
  public static final String UPCS_BY_IDS_PATH = "/upcs/{upcIds}";
  public static final String UPCS_IDS = "upcIds";
  public static final String UPCS_NUMBERS = "upcNumber";
  public static final String PRODUCT_CLIENT_POOL = "productClientPool";
  public static final String PRODUCTS_BY_IDS_PATH = "/products/{productids}";
  public static final String WISHLISTS_PATH = "/wishlists";
  public static final String FAVORITES_PATH = "/favorites";
  public static final String UPDATE_ITEM_PRIORITY_PATH = "/{listGuid}/items/{itemGuid}";
  public static final String CUSTOMER_USER_PROFILE_V1_CLIENT_POOL = "customerUserProfileV1ClientPool";
  public static final String CUSTOMER_USER_PROFILE_V2_CLIENT_POOL = "customerUserProfileV2ClientPool";
  public static final String RETRIEVE_USER_PATH = "/guestUser";
  public static final String RETRIEVE_USER_PROFILE_PATH = "/{userId}";
  public static final String CREATE_USER_PATH = "/createuser/isuser";
  public static final String RETRIEVE_USER_GUID_PATH = "/isuser/guidbyuserid/{userid}";
  public static final String FIND_USERS_PATH = "/search";
  public static final String RETRIEVE_USERS_BY_IDS_PATH = "/list";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String USER_IDS = "userIds";
  public static final String USER_GUIDS = "userGuids";
  public static final String UPDATE_PRIVILEGE = "updatePrivilege";
  public static final String ALL_PATH = "/all";

  public static final String STATE = "state";
  public static final String INCLUDE_FINAL_PRICE = "includeFinalPrice";

  public static final String ADD_ITEM_FEEDBACK_PATH = "/{listGuid}/feedback/{itemGuid}";
  public static final String LIST_FEEDBACK_PATH = "/{listGuid}/feedback";
  public static final String GET_ACTIVITY_LOG_PATH = "/{listGuid}/activitylog";

  public static final String FINAL_PRICE_ALWAYS_SHOW = "Always Show";
  public static final String FINAL_PRICE_NEVER_SHOW = "Never Show";
  public static final String FINAL_PRICE_CONDITIONAL_SHOW = "Conditionally Show";
  public static final String MAP = "MAP";
  public static final String MASK = "MASK";
  public static final String SIGNED_IN = "SIGNED_IN";
  public static final String RECOGNIZED = "RECOGNIZED";
  public static final String GUEST =  "GUEST";
  public static final String FINAL_PRICE_GUEST_BADGE_TEXT="See Bag for price";
  public  static final String PROMOTION_OFFER_DESCRIPTION = "offerDescription";
  public  static final String PROMOTION_TYPE = "promotionType";
  public  static final String PROMOTION_ATTRIBUTE = "promotionAttribute";
  public  static final String PROMOTION_QUERY_PARAMS = "offerDescription,promotionType,promotionAttribute";
  public static final String PROMOTION_ATTRIBUTE_NAME = "promotionAttributeName";
  public static  final String OFFER_DESCRIPTION = "OFFER_DESCRIPTION";
  public static final String PROMOTION_ATTRIBUTE_VALUE = "attributeValue";
  public static final String LOYALTY_OFFER_MULTIPLIER = "Loyalty Offer Multiplier";
    public static final String LOYALTY_OFFER_FIXED = "Loyalty Offer Fixed";

    public static final String ON_SALE_FILTER = "onSale";
    public static final String PROMOTIONS_FILTER = "promotions";
    private WishlistConstants() {}

}
