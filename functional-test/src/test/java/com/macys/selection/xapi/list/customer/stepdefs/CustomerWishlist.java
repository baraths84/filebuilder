package com.macys.selection.xapi.list.customer.stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

/**
 * Step definition file for retrieving customer info by userid and guid.
 **/
public class CustomerWishlist {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;
    private String mspCustomerUrl = getListsEndpoint();
    private String GET_LIST_BY_GUID_URL = getListByGuidEndpoint();
    private String FIND_LIST_BY_FIRST_AND_LAST_NAME = getListsEndpoint();
    private String MOVE_TO_LIST_BY_LISTGUID = getMoveItemByListGuidEndpoint();
    private String DELETE_ITEM_BY_LISTGUID_ITEMGUID = getItemByGuidEndpoint();

    @Given("^a customer userid \"([^\"]*)\"$")
    public void a_customer_exits_with_userid(String userid) {
        request = given().queryParam("userId", userid).queryParam("default", true).queryParam("listType", "w").queryParam("expand", "user,items");
        System.out.println("Customer list request" + request);

        /* New Flow calls:
        1) To MSP Customer:
          /api/customer/v1/profile/guestUser?userid=201306168
        2) To MSP Wishlist
          /api/selection/list/v1/wishlists?userId=201306168&userGuid=8362659c-724e-40eb-a357-61b32ecf8fd5&guestUser=true&default=true&listType=w&_expand=items
        3) To FCC:
          /api/catalog/v2/upcs/37609138?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        4) To FCC:
           /api/catalog/v2/products/3014013?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
         */

    }

    @When("^customer retrieves the list by userid$")
    public void a_customer_retrieves_list_by_userid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(mspCustomerUrl);
        System.out.println("Customer list response" + response.prettyPrint());

        /* New Flow calls:
        1) To MSP Customer:
           GET /api/customer/v1/profile/guestUser?userid=201336627
        2) To MSP Wishlist
           GET /api/selection/list/v1/wishlists?userId=201336627&guestUser=false&default=false&listType=w
        */
    }

    @Then("^the status code is \"([^\"]*)\"$")
    public void verify_status_code(int stautsCode) {
        json = response.then().statusCode(stautsCode);
    }

    @And("^response includes the following$")
    public void response_equal() {
        json.body("CustomerList.list[0].name", equalTo("Guest List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].createdDate", containsString("2017-09-14"));
        json.body("CustomerList.list[0].lastModified", containsString("2017-09-14"));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("f4fa2368-1c07-4da6-8ae1-1eec7cef7b23"));
        json.body("CustomerList.list[0].items[0].retailPriceWhenAdded", equalTo((Float.valueOf(99))));
        json.body("CustomerList.list[0].items[0].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[0].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].addedDate", containsString("2017-09-14"));
        json.body("CustomerList.list[0].items[0].lastModified", containsString("2017-09-14"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(37609138));
    }

    @And("^find lists response includes the following$")
    public void response_find_lists_equal() {
        json.body("CustomerList.list[0].name", equalTo("Guest List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].lastModified", containsString("2017-09-14T"));
    }

    @And("^response includes user info$")
    public void response_user_info_equal() {
        json.body("CustomerList.user.id", equalTo(201306168));
        json.body("CustomerList.user.guid", equalTo("8362659c-724e-40eb-a357-61b32ecf8fd5"));
        json.body("CustomerList.user.guestUser", equalTo(true));
    }

    @Given("^a customer with firstname \"([^\"]*)\" and lastname \"([^\"]*)\"$")
    public void a_customer_exits_with_first_and_last_name(String firstname, String lastname) {
        request = given().queryParam("firstName", firstname).queryParam("lastName", lastname);

        /*
        New Flow
        GET /api/customer/v2/profile/search?firstName=myfirstname&lastName=mylastname
        GET /api/selection/list/v1/wishlists?userIds=2150919659%2C2150919662&userFirstName=myfirstname
         */
    }

    @When("^customer moves item to another list by userid$")
    public void customer_moves_item_to_another_list() {

        String moveToList = "{\n" +
                "                \"CustomerList\": {\n" +
                "            \"user\": {\n" +
                "                \"id\": 201306153\n" +
                "            },\n" +
                "            \"list\": [\n" +
                "            {\n" +
                "                \"items\": [\n" +
                "                {\n" +
                "                    \"itemGuid\": \"3577fbc5-3385-4bc1-866f-36078928bbcd\"\n" +
                "                }\n" +
                "                ]\n" +
                "            }\n" +
                "            ]\n" +
                "        }\n" +
                "        }";

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when().body(moveToList)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg")
                .put(MOVE_TO_LIST_BY_LISTGUID);
        System.out.println("Move to list response" + response.prettyPrint());

        /* New Flow calls:
        1) To MSP Customer:
          /api/customer/v1/profile/guestUser?userid=201306153
        2) To MSP Wishlist
          POST /api/selection/list/v1/wishlists/7dfa1ece-db69-4369-a222-8ecabc2ad0c9/items/move?itemGuid=3577fbc5-3385-4bc1-866f-36078928bbcd&userId=201306153&userGuid=74eddb1d-a22f-4bb3-b68c-cf445f4d3c3f&guestUser=false
        */

    }

    @When("^customer tries to find the list by first and last name$")
    public void a_customer_tries_to_find_list_by_first_and_last_name() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(FIND_LIST_BY_FIRST_AND_LAST_NAME);
        System.out.println("Customr list response" + response.prettyPrint());
    }


    @Given("^a customer with listguid \"([^\"]*)\"$")
    public void a_customer_exits_with_userguid(String listGuid) {
        request = given().pathParam("listGuid", listGuid).queryParam("expand", "items");
        System.out.println("Customr list request" + request);
    }

    @Given("^a customer with item to move to listguid \"([^\"]*)\"$")
    public void a_customer_with_item_to_move_to_listguid(String listGuid) {
        request = given().pathParam("listGuid", listGuid);
        System.out.println("Move to list request" + request);
    }

    @Given("^a customer with listGuid \"([^\"]*)\" and itemGuid \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_itemguid(String listGuid, String itemGuid) {
        request = given().pathParam("listGuid", listGuid).pathParam("itemGuid", itemGuid);
        System.out.println("Delete item from list request" + request);
    }

    @When("^customer deletes item from list$")
    public void customer_delete_item_from_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .delete(DELETE_ITEM_BY_LISTGUID_ITEMGUID);
        System.out.println("Delete item from list response" + response.prettyPrint());

        /* New Flow calls:
        1) To MSP WishList:
           GET http://localhost:8888/api/selection/list/v1/wishlists/6f522f8c-09c7-4306-b9e1-f331cf2244e6
        2) To MSP Customer:
           GET http://localhost:8888/api/customer/v1/profile/guestUser?userid=55250007
        3) To To MSP WishList:
           DELETE http://localhost:8888/api/selection/list/v1/wishlists/6f522f8c-09c7-4306-b9e1-f331cf2244e6/items/f83e486c-15ad-4a28-94dd-4a9bc6af3639?guestUser=false
        */
    }

    @When("^customer retrieves the list by listguid$")
    public void a_customer_retrieves_list_by_listguid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(GET_LIST_BY_GUID_URL);
        System.out.println("Customr list response" + response.prettyPrint());

        /* New Flow calls:
        1) To MSP WishList:
          /api/selection/list/v1/wishlists/7184c6f5-32c6-4e1f-b92f-098c9c07b8ba?_expand=items
        2) To FCC
          /api/catalog/v2/upcs/36846878?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
         */
    }

    @And("^response includes the following info about pricing policy$")
    public void response_equal_pricing() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[0].upc.price.pricingPolicy", equalTo("/catalog/product/pricingpolicy.ognc?fpriceTypeId=3&daysInC=17504&ID=22805"));


    }

    @Given("^a customer userid \"([^\"]*)\" for all lists$")
    public void a_customer_exits_with_userid_for_all(String userid) {
        request = given().queryParam("userId", userid).queryParam("default", false).queryParam("listType", "w");
        System.out.println("Customr list request" + request);
    }


    @And("^GET all list response includes the following$")
    public void response_equal_all() {
        json.body("CustomerList.list[0].name", equalTo("cam's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(2));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));

        json.body("CustomerList.list[0].listGuid", equalTo("70dbc1c2-3c53-49f4-9bdb-e117988603bc"));
        json.body("CustomerList.list[2].listGuid", equalTo("b2094fa5-a38c-4e45-8cda-d24007132745"));
        json.body("CustomerList.list[2].defaultList", equalTo(Boolean.FALSE));

        json.body("CustomerList.list[1].listGuid", equalTo("5d4b1ee9-5695-4b59-815b-5069f944ce3a"));
        json.body("CustomerList.list[1].defaultList", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[1].name", equalTo("cams"));

        /* New Flow calls:
        1) To MSP Customer:
          /api/customer/v1/profile/guestUser?userid=201336627
        2) To MSP Wishlist:
          GET /api/selection/list/v1/wishlists?userId=201336627&guestUser=false&default=false&listType=w
          */

    }

    @And("^get list response by listGuid includes the following$")
    public void get_list_response_by_listguid() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-3715-403f-a8ff-7c4069a5649b"));
        json.body("CustomerList.list[0].items[0].retailPriceWhenAdded", equalTo((Float.valueOf(195))));
        json.body("CustomerList.list[0].items[0].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[0].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(36846878));
    }

}
