package com.macys.selection.xapi.list.customer.stepdefs;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getListsEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

/**
 * Step definition file for get list by list guid.
 **/
public class GetAllLists {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    //########################### Call with userId/userGuid ############################

    @Given("^a customer wants to retrieve default list with userid \"([^\"]*)\"$")
    public void a_customer_exits_with_userid(String userid) {
        request = given().queryParam("userId", userid).queryParam("default", true).queryParam("listType", "w");
        System.out.println("Customer list request" + request);

        /*
        New Flow:
        GET /api/selection/list/v1/wishlists?userId=111&guestUser=true&default=true&listType=w
         */
    }

    @When("^customer retrieves all lists$")
    public void a_customer_retrieves_list_by_userid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListsEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get all lists the status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^get all list response with invalid user includes the following$")
    public void response_equal_all() {
        json.body("CustomerList.list[0].name", equalTo("wishlist1"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
    }

    @Given("^a customer wants to retrieve default list with userguid \"([^\"]*)\"$")
    public void a_customer_exits_with_userguid(String userGuid) {
        request = given().queryParam("userGuid", userGuid).queryParam("default", true).queryParam("listType", "w");
        System.out.println("Customer list request" + request);
    }

    @Given("^a customer wants to retrieve all lists with userid \"([^\"]*)\" and userguid \"([^\"]*)\"$")
    public void a_customer_with_userid_and_userguid(String userId, String userGuid) {
        request = given().queryParam("userId", userId).queryParam("userGuid", userGuid).queryParam("default", true).queryParam("listType", "w");
        System.out.println("Customer list request" + request);
    }

    @And("^error response with both userId and userGuid includes the following$")
    public void error_response_both_userId_userGuid_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
    }

    @Given("^a customer wants to retrieve all lists without user info$")
    public void a_customer_without_user_info() {
        request = given().queryParam("default", true).queryParam("listType", "w");
        System.out.println("Customer list request" + request);
    }

    @And("^error response without user includes the following$")
    public void error_response_no_user_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Either user id or user guid missing or invalid."));
    }

    //########################### Default parameter ############################

    @Given("^a customer wants to retrieve any list with default=false and expand=items$")
    public void a_customer_retrieves_list_with_defaultFalse_expandItems() {
        request = given().queryParam("userId", "55250007").queryParam("default", false).queryParam("listType", "w").queryParam("expand", "items");
        System.out.println("Customer list request" + request);

        /*
        New Flow:
        GET /api/customer/v1/profile/guestUser?userid=55250007
        GET /api/selection/list/v1/wishlists?userId=55250007&userGuid=74eddb1d-a22f-4bb3-b68c-55250007&guestUser=false&default=false&listType=w&_expand=items
         */
    }

    @And("^error response with default=false and expand=items$")
    public void error_response_invalid_expand_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Invalid expand option"));
    }

    @Given("^a customer wants to retrieve any list without default parameter and userid \"([^\"]*)\"$")
    public void a_customer_retrieves_list_without_default_parameter(String userId) {
        request = given().queryParam("userId", userId).queryParam("listType", "w");
        System.out.println("Customer list request" + request);

        /*
        New Flow:
        GET /api/selection/list/v1/wishlists?userId=201336627&guestUser=true&listType=w
         */
    }

    @And("^get all list response with default parameter by default includes the following$")
    public void response_no_default_parameter_all() {
        json.body("CustomerList.list[0].name", equalTo("wishlist1"));
    }


    //########################### Sorting ############################

    @Given("^a customer wants to retrieve all non-default lists for userid \"([^\"]*)\"$")
    public void a_customer_retrieves_nonDefault_lists(String userId) {
        request = given().queryParam("userId", userId).queryParam("default", false).queryParam("listType", "w");
        System.out.println("Customer list request" + request);

        /*
        New Flow:
        GET /api/customer/v1/profile/guestUser?userid=201306458
        GET /api/selection/list/v1/wishlists?userId=201306458&guestUser=true&default=false&listType=w
         */
    }

    @And("^get all list sorting is as following$")
    public void response_lists_sorting_equal() {
        // Default list is always the first, then lists are sorted by created date.
        json.body("CustomerList.list[0].id", equalTo(1));
        json.body("CustomerList.list[0].defaultList", equalTo(true));

        json.body("CustomerList.list[1].id", equalTo(2));
        json.body("CustomerList.list[1].defaultList", equalTo(false));
        json.body("CustomerList.list[1].createdDate", containsString("2017-05"));

        json.body("CustomerList.list[2].id", equalTo(5));
        json.body("CustomerList.list[2].defaultList", equalTo(false));
        json.body("CustomerList.list[2].createdDate", containsString("2011-05"));

        json.body("CustomerList.list[3].id", equalTo(3));
        json.body("CustomerList.list[3].defaultList", equalTo(false));
        json.body("CustomerList.list[3].createdDate", containsString("2008-05"));

        json.body("CustomerList.list[4].id", equalTo(4));
        json.body("CustomerList.list[4].defaultList", equalTo(false));
        json.body("CustomerList.list[4].createdDate", containsString("2000-05"));
    }

    @Given("^a customer wants to retrieve default list with sortBy \"([^\"]*)\" and sortOrder \"([^\"]*)\"$")
    public void a_customer_retrieves_nonDefault_list(String sortBy, String sortOrder) {
        request = given().queryParam("userId", "201306333").queryParam("default", true).queryParam("listType", "w").
                queryParam("expand", "items").queryParam("sortBy", sortBy).queryParam("sortOrder", sortOrder);
        System.out.println("Customer list request" + request);

        /*
        New Flow:
        GET /api/selection/list/v1/wishlists?userId=201306333&guestUser=true&default=true&listType=w&_expand=items&_deviceType=DESKTOP&_application=SITE&_regionCode=US&_navigationType=BROWSE&_customerExperiment=NO_EXPERIMENT&_shoppingMode=SITE
        GET /api/catalog/v2/upcs/3680444,3680333,3680222,3680111?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        GET /api/catalog/v2/products/3680444,3680222?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds%2CfinalPrice&active=true&includeFinalPrice=false
         */
    }

    @And("^get default list with sortBy=retailPrice sortOrder=asc response includes the following$")
    public void get_default_list_response_sortBy_retailPrice_sortOrder_asc() {
        // Item with upcId=3680111 has productId in item => product has retailPrice=500, that is taken for upc for sorting, instead of 50.
        // retailPrice is taken from upc, not item.
        // unavailable upcs are always at the end of the list not depending on price and asc/desc.
        // upcId=3680111 retailPrice=500
        // upcId=3680222 retailPrice=8.99
        // upcId=3680333 retailPrice=200  - unavailable
        // upcId=3680444 retailPrice=100
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680222));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680444));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680111));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680333));
    }

    @And("^get default list with sortBy=retailPrice sortOrder=desc response includes the following$")
    public void get_default_list_response_sortBy_retailPrice_sortOrder_desc() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680111));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680444));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680222));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680333));
    }

    @And("^get default list with sortBy=avgReviewRating sortOrder=asc response includes the following$")
    public void get_default_list_response_sortBy_avgReviewRating_sortOrder_asc() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680111));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680222));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680333));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680444));
    }

    @And("^get default list with sortBy=avgReviewRating sortOrder=desc response includes the following$")
    public void get_default_list_response_sortBy_avgReviewRating_sortOrder_desc() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680333));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680444));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680111));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680222));
    }

    @And("^get default list with sortBy=topPick sortOrder=asc response includes the following$")
    public void get_default_list_response_sortBy_topPick_sortOrder_asc() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680222));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680444));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680111));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680333));
    }

    @And("^get default list with sortBy=topPick sortOrder=desc response includes the following$")
    public void get_default_list_response_sortBy_topPick_sortOrder_desc() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680333));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680111));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680222));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680444));
    }

    @And("^get default list with sortBy=addedDate sortOrder=asc response includes the following$")
    public void get_default_list_response_sortBy_addedDate_sortOrder_asc() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680111));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680444));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680222));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680333));
    }

    @And("^get default list with sortBy=addedDate sortOrder=desc response includes the following$")
    public void get_default_list_response_sortBy_addedDate_sortOrder_desc() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("0cba5e7f-333"));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3680333));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("0cba5e7f-222"));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3680222));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("0cba5e7f-444"));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3680444));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("0cba5e7f-111"));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3680111));
    }

    @And("^i verify error on the get default list with invalid sortBy$")
     public void error_response_invalid_sortBy_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Unsupported sort by value."));
    }

    @And("^i verify error on the get default list with invalid sortOrder$")
    public void error_response_invalid_sortOrder_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Invalid sort order value. Valid values are asc/desc."));
    }

    //########################### Links Tests ############################

    @And("^get default list response includes links when userGuid is present$")
    public void get_list_response_by_listguid_links_by_userGuid() {
        json.body("CustomerList.list[0].links[0].rel", equalTo("user"));
        json.body("CustomerList.list[0].links[0].ref", containsString("/customer/v1/users/userdetails/getuser?userguid=4b61ee86-c46e-4f62-b97a-f52a20b512e8"));

        json.body("CustomerList.list[0].links[1].rel", equalTo("self"));
        json.body("CustomerList.list[0].links[1].ref", containsString("/xapi/wishlist/v1/lists?userGuid=4b61ee86-c46e-4f62-b97a-f52a20b512e8"));

        json.body("CustomerList.list[0].links[2].rel", equalTo("items"));
        json.body("CustomerList.list[0].links[2].ref", containsString("/xapi/wishlist/v1/lists/a78a1ed1-d7c0-4aae-55250001-1?expand=items"));
    }

    @And("^get default list response includes links when userGuid is absent")
    public void get_list_response_by_listguid_links_by_userId() {
        json.body("CustomerList.list[0].links[0].rel", equalTo("user"));
        json.body("CustomerList.list[0].links[0].ref", containsString("/customer/v1/users/userdetails/getuser?userid=111"));

        json.body("CustomerList.list[0].links[1].rel", equalTo("self"));
        json.body("CustomerList.list[0].links[1].ref", containsString("/xapi/wishlist/v1/lists?userId=111"));

        json.body("CustomerList.list[0].links[2].rel", equalTo("items"));
        json.body("CustomerList.list[0].links[2].ref", containsString("/xapi/wishlist/v1/lists/a78a1ed1-d7c0-4aae-55250001-1?expand=items"));
    }

    @Given("^a customer wants to retrieve non-default list with limit > 0 with userid \"([^\"]*)\"$")
    public void a_customer_retrieves_list_with_limit(String userid) {
        request = given().queryParam("userId", userid).queryParam("default", false).queryParam("limit", "5");
        System.out.println("Customer list request" + request);

        /*
        New Flow:
        GET /api/customer/v1/profile/guestUser?userid=201306153
        GET /api/selection/list/v1/wishlists?userId=201306153&userGuid=74eddb1d-a22f-4bb3-b68c-cf445f4d3c3f&guestUser=false&default=false&_limit=5
        GET /api/catalog/v2/upcs/38774892,38774891?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        GET /api/catalog/v2/products/6032754?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds%2CfinalPrice&active=true&includeFinalPrice=true
         */
    }

    @And("^get default list response includes imageUrlsList")
    public void get_list_response_by_listguid_imageUrlsList() {
        json.body("CustomerList.list[0].imageUrlsList", hasItem("3/optimized/8780323_fpx.tif"));
        json.body("CustomerList.list[1].imageUrlsList", hasItem("4/optimized/8780324_fpx.tif"));
    }

}
