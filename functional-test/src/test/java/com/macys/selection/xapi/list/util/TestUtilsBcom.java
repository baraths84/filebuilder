package com.macys.selection.xapi.list.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class TestUtilsBcom {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestUtilsBcom.class);
    private static final String PROPERTY_FILE_PATH = "application-config-bcom.properties";
    private static Properties properties = new Properties();

    private static final String WIREMOCK_DEFAULT_PORT= "8888";
    private static final String XAPI_LIST_URL_PROPERTY_NAME = "xapi.list.host";
    private static final String XAPI_WIREMOCK_PROPERTY_NAME = "xapi.wiremock";
    private static final String WIREMOCK_PORT_PROPERTY_NAME = "xapi.wiremock.port";
    private static final String WISHLIST_AND_FCC_FLOW_ENABLED_PROPERTY_NAME = "wishlist.enabled";
    private static final String APPLICATION_NAME = "application.name";

    private static final String LISTS_RESOURCE = "/xapi/wishlist/v1/lists";
    private static final String LISTS_ITEMS_RESOURCE = "/xapi/wishlist/v1/lists/items";
    private static final String MERGE_LISTS_RESOURCE = "/xapi/wishlist/v1/lists/merge";
    private static final String LIST_BY_GUID_RESOURCE = "/xapi/wishlist/v1/lists/{listGuid}";
    private static final String ITEMS_BY_LIST_GUID_RESOURCE = "/xapi/wishlist/v1/lists/{listGuid}/items";
    private static final String MOVE_ITEM_BY_LIST_GUID_RESOURCE = "/xapi/wishlist/v1/lists/{listGuid}/item/move";
    private static final String ITEM_BY_GUID_RESOURCE = "/xapi/wishlist/v1/lists/{listGuid}/items/{itemGuid}";
    private static final String SHARE_EMAIL_RESOURCE = "/xapi/wishlist/v1/lists/{listGuid}/shareemail";

    private static final String FAVORITES_BY_USER_GUID_RESOURCE = "/xapi/wishlist/v1/{userguid}/favorites";
    private static final String FAVORITES_RESOURCE = "/xapi/wishlist/v1/favorites";
    private static final String FAVORITES_BY_LIST_GUID_RESOURCE = "/xapi/wishlist/v1/favorites/{listguid}";

    private static final String COLLABORATIVE_LIST = "/xapi/collaborative/v1/lists";
    private static final String GET_COLLABORATIVE_LIST_BY_GUID = "/xapi/collaborative/v1/lists/{listguid}";
    private static final String ITEMS_BY_COLLABORATIVE_LIST_GUID_RESOURCE = "/xapi/collaborative/v1/lists/{listGuid}/items";
    private static final String GET_CUSTOMER_ALL_LISTS_RESOURCE = "/xapi/lists/v1/lists";
    private static final String GET_COLLABORATIVE_LISTS_BY_TYPES_RESOURSE = "/xapi/collaborative/v1/lists/all";
    private static final String POST_COLLABORATOR_FEEDBACK_RESOURSE = "/xapi/collaborative/v1/lists/{listGuid}/feedback/{itemGuid}";
    private static final String GET_LIST_ACTIVITY_LOG_RESOURSE = "/xapi/collaborative/v1/lists/{listGuid}/activitylog";

    static {
        readPropertyFile();
    }

    private static void readPropertyFile () {
        InputStream input = null;
        try {
            input = TestUtilsBcom.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_PATH);

            // load a properties file
            properties.load(input);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String readFile(String filePath) throws IOException {
        InputStream in = null;
        try {
            in = ClassLoader.getSystemClassLoader().getResourceAsStream(filePath);
            return IOUtils.toString(in);
        } finally {
            if (in != null) { in.close();}
        }
    }

    public static String getPropertyValue(String propertyName) {
        return properties.getProperty(propertyName);
    }

    public static String getXapiListBasicUrl() {
        String hostName = ObjectUtils.defaultIfNull(getPropertyValue(XAPI_LIST_URL_PROPERTY_NAME), "http://localhost:8080");
        LOGGER.info("hostName: {}", hostName);
        return hostName;
    }

    // resourcePath example: /xapi/wishlist/v1/lists/merge
    public static String getXapiFullEndpointWithResource(String resourcePath) {
        return (getXapiListBasicUrl() + resourcePath);
    }

    public static String getListsEndpoint() {
        return getXapiFullEndpointWithResource(LISTS_RESOURCE);
    }

    public static String getCollaborativeListEndpoint() {
        return getXapiFullEndpointWithResource(COLLABORATIVE_LIST);
    }

    public static String getGetCollaborativeListByGuidEndpoint() {
        return getXapiFullEndpointWithResource(GET_COLLABORATIVE_LIST_BY_GUID);
    }

    public static String addCollaboratorFeedback() {
        return getXapiFullEndpointWithResource(POST_COLLABORATOR_FEEDBACK_RESOURSE);
    }

    public static String getListActivityLog() {
        return getXapiFullEndpointWithResource(GET_LIST_ACTIVITY_LOG_RESOURSE);
    }

    public static String getCustomerAllLists() {
        return getXapiFullEndpointWithResource(GET_CUSTOMER_ALL_LISTS_RESOURCE);
    }


    public static String getCollaborativeListsByTypes() {
        return getXapiFullEndpointWithResource(GET_COLLABORATIVE_LISTS_BY_TYPES_RESOURSE);
    }

    public static String getListsItemsEndpoint() {
        return getXapiFullEndpointWithResource(LISTS_ITEMS_RESOURCE);
    }

    public static String getMergeListsEndpoint() {
        return getXapiFullEndpointWithResource(MERGE_LISTS_RESOURCE);
    }

    public static String getListByGuidEndpoint() {
        return getXapiFullEndpointWithResource(LIST_BY_GUID_RESOURCE);
    }

    public static String getItemsByListGuidEndpoint() {
        return getXapiFullEndpointWithResource(ITEMS_BY_LIST_GUID_RESOURCE);
    }

    public static String getItemsByCollaborativeListGuidEndpoint() {
        return getXapiFullEndpointWithResource(ITEMS_BY_COLLABORATIVE_LIST_GUID_RESOURCE);
    }

    public static String getMoveItemByListGuidEndpoint() {
        return getXapiFullEndpointWithResource(MOVE_ITEM_BY_LIST_GUID_RESOURCE);
    }

    public static String getItemByGuidEndpoint() {
        return getXapiFullEndpointWithResource(ITEM_BY_GUID_RESOURCE);
    }

    public static String getShareEmailEndpoint() {
        return getXapiFullEndpointWithResource(SHARE_EMAIL_RESOURCE);
    }

    public static String getFavoritesByUserGuidEndpoint() {
        return getXapiFullEndpointWithResource(FAVORITES_BY_USER_GUID_RESOURCE);
    }

    public static String getFavoritesEndpoint() {
        return getXapiFullEndpointWithResource(FAVORITES_RESOURCE);
    }

    public static String getFavoritesByListGuidEndpoint() {
        return getXapiFullEndpointWithResource(FAVORITES_BY_LIST_GUID_RESOURCE);
    }

    public static boolean checkWiremockModeOn() {
        String wiremockMode = getPropertyValue(XAPI_WIREMOCK_PROPERTY_NAME);
        return wiremockMode == null || Boolean.valueOf(wiremockMode);
    }

    public static int getWiremockPort() {
        // port wiremock runs on
        String port = getPropertyValue(WIREMOCK_PORT_PROPERTY_NAME);
        return Integer.valueOf((port != null ? Integer.valueOf(port) : Integer.valueOf(WIREMOCK_DEFAULT_PORT)));
    }

    public static boolean getWishlistAndFccFlowEnabled() {
        if (getPropertyValue(WISHLIST_AND_FCC_FLOW_ENABLED_PROPERTY_NAME) == null) {
            // By default Old Flow is on.
            return false;
        }

        return Boolean.parseBoolean(getPropertyValue(WISHLIST_AND_FCC_FLOW_ENABLED_PROPERTY_NAME));
    }
    public static String getApplicationName(){
        return getPropertyValue(APPLICATION_NAME);
    }
}
