package com.macys.selection.xapi.list.customer.stepdefs;

import com.macys.selection.xapi.list.util.TestUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class AddItemToListByGuid {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddItemToListByGuid.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer adds item to existing list with listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid(String listguid) {
        request = given().pathParam("listGuid", listguid);
    }

    @When("^customer adds an item to the existing list$")
    public void a_customer_adds_item_to_existing_list() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToListByGuid.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg")
                .post(getItemsByListGuidEndpoint());
        LOGGER.info(response.prettyPrint());

        /*
        New Flow:
        1) TO MCP Customer
        GET /api/customer/v1/profile/guestUser?userid=201336579
        2) To FCC:
        GET /api/catalog/v2/upcs/37509092?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        3) To FCC:
        GET /api/catalog/v2/products/3014013?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
        4) To MSP Wishlist
        POST /api/selection/list/v1/wishlists/71adefa2-183a-4a9e-aa26-a3b96c11db24/items?userId=201336579&guestUser=false
         */
    }

    @Then("^the add to existing list status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^i verify the add item to existing list response$")
    public void response_equal() {
        json.body("CustomerList.user.id", equalTo(201336579));
        json.body("CustomerList.user.guestUser", equalTo(false));
        json.body("CustomerList.list[0].name", equalTo("Test List"));
        json.body("CustomerList.list[0].listGuid", equalTo("7a58f9f7-4926-4f14-b986-a8609c488d39"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(false));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(false));
        json.body("CustomerList.list[0].searchable", equalTo(false));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(true));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("f4fa2368-1c07-4da6-8ae1-1eec7cef7b23"));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(37509092));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(3014013));
    }

    @When("^customer adds an item to the existing list with upc and product$")
    public void a_customer_adds_item_to_existing_list_both_upc_product() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToListByGuidWithUpcAndProduct.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg")
                .post(getItemsByListGuidEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @And("^i verify error response on both upc and product$")
    public void error_response_both_upc_product_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Incorrect request"));
    }

    @When("^customer adds an item to the existing list by user guid$")
    public void a_customer_adds_item_to_existing_list_by_user_guid() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToListByGuidByUserGuid.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg")
                .post(getItemsByListGuidEndpoint());
        LOGGER.info(response.prettyPrint());

        /*
        New Flow:
        1) TO MCP Customer
        GET /api/customer/v1/profile/guestUser?userguid=4b61ee86-c46e-4f62-b97a-f52a20b512e8
        2) To FCC:
        GET /api/catalog/v2/upcs/37509092?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        3) To FCC:
        GET /api/catalog/v2/products/3014013?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
        4) To MSP Wishlist
        POST /api/selection/list/v1/wishlists/71adefa2-183a-4a9e-aa26-a3b96c11db24/items?userId=41&userGuid=4b61ee86-c46e-4f62-b97a-f52a20b512e8&guestUser=false
         */
    }

    @And("^i verify the add item to existing list response for user guid$")
    public void response_with_user_guid_equal() {
        json.body("CustomerList.user.id", equalTo(41));
        json.body("CustomerList.user.guestUser", equalTo(false));
        json.body("CustomerList.list[0].name", equalTo("Test List"));
        json.body("CustomerList.list[0].listGuid", equalTo("7a58f9f7-4926-4f14-b986-a8609c488d39"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(false));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(false));
        json.body("CustomerList.list[0].searchable", equalTo(false));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(true));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("f4fa2368-1c07-4da6-8ae1-1eec7cef7b23"));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(37509092));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(3014013));
    }

    @Given("^a customer adds item to existing list with listguid \"([^\"]*)\" and userId \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_userId_in_query(String listguid, String userId) {
        request = given().pathParam("listGuid", listguid).queryParam("userId", userId);
    }

    @Given("^a customer adds item to existing list with listguid \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_userGuid_in_query(String listguid, String userGuid) {
        request = given().pathParam("listGuid", listguid).queryParam("userGuid", userGuid);
    }

    @When("^customer adds an item to the existing list without user in json$")
    public void a_customer_adds_item_to_existing_list_without_user_in_json() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToListByGuidNoUser.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg")
                .post(getItemsByListGuidEndpoint());
        LOGGER.info(response.prettyPrint());

    }
}
