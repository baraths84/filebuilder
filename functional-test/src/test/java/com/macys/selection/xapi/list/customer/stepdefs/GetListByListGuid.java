package com.macys.selection.xapi.list.customer.stepdefs;

import com.macys.selection.xapi.list.util.TestUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getListByGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;


/**
 * Step definition file for get list by list guid.
 **/
public class GetListByListGuid {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    //########################### Call with expands ############################

    @Given("^a customer wants to retrieve list with expands=user,items and listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid(String listGuid) {
        request = given().pathParam("listGuid", listGuid).queryParam("expand", "user,items");
        System.out.println("Customer list request" + request);
    }

    @Then("^the get list by list guid status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @When("^customer retrieves list by listguid$")
    public void a_customer_retrieves_list_with_expands_by_listguid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListByGuidEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());

        /* New Flow calls for the case with expands=user,items:
            GET /api/selection/list/v1/wishlists/8c15d7d8-d71c-4fe8-8724-eca38ef4bf6f?_expand=items
            GET /api/customer/v1/profile/guestUser?userid=55250007&userguid=74eddb1d-a22f-4bb3-b68c-55250007
            GET /api/catalog/v2/upcs/36846878?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability

            New Flow calls for the case with expands=items and sorting:
            GET /api/selection/list/v1/wishlists/8c15d7d8-111?_expand=items
            GET /api/catalog/v2/upcs/3680444,3680333,3680222,3680111?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
            GET /api/catalog/v2/products/3680444,3680222?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
         */
    }

    @And("^get list by list guid with expands response includes the following$")
    public void get_list_response_by_listguid() {
        json.body("CustomerList.user.id", equalTo(55250007));
        json.body("CustomerList.user.guid", equalTo("74eddb1d-a22f-4bb3-b68c-55250007"));
        json.body("CustomerList.user.guestUser", equalTo(Boolean.FALSE));

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

    //########################### Call without expands ############################

    @Given("^a customer wants to retrieve list without expands with listguid \"([^\"]*)\"$")
    public void a_customer_exits_with_userguid(String listGuid) {
        request = given().pathParam("listGuid", listGuid);
        System.out.println("Customr list request" + request);
    }

    @When("^customer retrieves the list by listguid without expands$")
    public void a_customer_retrieves_list_without_expands_by_listguid() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListByGuidEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());

        /* New Flow calls:
        GET /api/selection/list/v1/wishlists/8c15d7d8-d71c-4fe8-8724-eca38ef4bf6f
                */
    }

    @And("^get list by list guid without expands response includes the following$")
    public void get_list_no_expands_response_by_listguid() {
        json.body("CustomerList.list[0].name", equalTo("test's List"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));
    }

    //########################### Calls with sorting ############################

    @Given("^a customer wants to retrieve list with listguid \"([^\"]*)\" and sortBy \"([^\"]*)\" and sortOrder \"([^\"]*)\"$")
    public void a_customer_with_listguid_and_sorting(String listGuid, String sortBy, String sortOrder) {
        request = given().pathParam("listGuid", listGuid).queryParam("expand", "items").queryParam("sortBy", sortBy).queryParam("sortOrder", sortOrder);
        System.out.println("Customer list request" + request);
    }

    @And("^get list by list guid with sortBy=retailPrice sortOrder=asc response includes the following$")
    public void get_list_response_by_listguid_sortBy_retailPrice_sortOrder_asc() {
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

    @And("^get list by list guid with sortBy=retailPrice sortOrder=desc response includes the following$")
    public void get_list_response_by_listguid_sortBy_retailPrice_sortOrder_desc() {
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

    @And("^get list by list guid with sortBy=avgReviewRating sortOrder=asc response includes the following$")
    public void get_list_response_by_listguid_sortBy_avgReviewRating_sortOrder_asc() {
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

    @And("^get list by list guid with sortBy=avgReviewRating sortOrder=desc response includes the following$")
    public void get_list_response_by_listguid_sortBy_avgReviewRating_sortOrder_desc() {
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

    @And("^get list by list guid with sortBy=topPick sortOrder=asc response includes the following$")
    public void get_list_response_by_listguid_sortBy_topPick_sortOrder_asc() {
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

    @And("^get list by list guid with sortBy=topPick sortOrder=desc response includes the following$")
    public void get_list_response_by_listguid_sortBy_topPick_sortOrder_desc() {
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

    @And("^get list by list guid with sortBy=addedDate sortOrder=asc response includes the following$")
    public void get_list_response_by_listguid_sortBy_addedDate_sortOrder_asc() {
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

    @And("^get list by list guid with sortBy=addedDate sortOrder=desc response includes the following$")
    public void get_list_response_by_listguid_sortBy_addedDate_sortOrder_desc() {
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

    @And("^i verify error on the get list by list guid with invalid sortBy$")
     public void error_response_invalid_sortBy_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Unsupported sort by value."));
    }

    @And("^i verify error on the get list by list guid with invalid sortOrder$")
    public void error_response_invalid_sortOrder_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Invalid sort order value. Valid values are asc/desc."));
    }

    //########################### Links Tests ############################

    @And("^get list by list guid response includes links when userGuid is present$")
    public void get_list_response_by_listguid_links_by_userGuid() {
        json.body("CustomerList.list[0].links[0].rel", equalTo("user"));
        json.body("CustomerList.list[0].links[0].ref", containsString("/customer/v1/users/userdetails/getuser?userguid=74eddb1d-a22f-4bb3-b68c-55250007"));

        json.body("CustomerList.list[0].links[1].rel", equalTo("self"));
        json.body("CustomerList.list[0].links[1].ref", containsString("/xapi/wishlist/v1/lists?userGuid=74eddb1d-a22f-4bb3-b68c-55250007"));

        json.body("CustomerList.list[0].links[2].rel", equalTo("items"));
        json.body("CustomerList.list[0].links[2].ref", containsString("/xapi/wishlist/v1/lists/8c15d7d8-d71c-4fe8-8724-eca38ef4bf6f?expand=items"));
    }

    @And("^get list by list guid response includes links when userGuid is absent")
    public void get_list_response_by_listguid_links_by_userId() {
        json.body("CustomerList.list[0].links[0].rel", equalTo("user"));
        json.body("CustomerList.list[0].links[0].ref", containsString("/customer/v1/users/userdetails/getuser?userid=55250007"));

        json.body("CustomerList.list[0].links[1].rel", equalTo("self"));
        json.body("CustomerList.list[0].links[1].ref", containsString("/xapi/wishlist/v1/lists?userId=55250007"));

        json.body("CustomerList.list[0].links[2].rel", equalTo("items"));
        json.body("CustomerList.list[0].links[2].ref", containsString("/xapi/wishlist/v1/lists/6f522f8c-09c7-4306-b9e1-f331cf2244e6?expand=items"));
    }

    //########################### FCC data and final price ############################

    @And("^get list by list guid response includes fcc data and final price$")
    public void get_list_response_by_listguid_fcc_data_final_price() {
        json.body("CustomerList.list[0].items[0].retailPriceWhenAdded", equalTo(300.0f));
        json.body("CustomerList.list[0].items[0].retailPriceDropAfterAddedToList", equalTo(100.0f));
        json.body("CustomerList.list[0].items[0].retailPriceDropPercentage", equalTo(33));

        json.body("CustomerList.list[0].items[0].upc.id", equalTo(38774892));
        json.body("CustomerList.list[0].items[0].upc.upcNumber", equalTo(38774892));
        json.body("CustomerList.list[0].items[0].upc.color", equalTo("Oat Heather"));
        json.body("CustomerList.list[0].items[0].upc.size", equalTo("XL"));
        json.body("CustomerList.list[0].items[0].upc.type", equalTo("Regular Strength"));

        json.body("CustomerList.list[0].items[0].upc.price.retailPrice", equalTo(200.0f));
        json.body("CustomerList.list[0].items[0].upc.price.originalPrice", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].upc.price.intermediateSalesValue", equalTo(0.0f));
        json.body("CustomerList.list[0].items[0].upc.price.salesValue", equalTo(0.0f));
        json.body("CustomerList.list[0].items[0].upc.price.onSale", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.price.upcOnSale", equalTo(false));
        json.body("CustomerList.list[0].items[0].upc.price.priceType", equalTo(7));
        json.body("CustomerList.list[0].items[0].upc.price.basePriceType", equalTo(0));
        json.body("CustomerList.list[0].items[0].upc.price.originalPriceLabel", equalTo("Orig."));
        json.body("CustomerList.list[0].items[0].upc.price.retailPriceLabel", equalTo("Now"));

        json.body("CustomerList.list[0].items[0].upc.availability.available", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.availability.upcAvailabilityMessage", equalTo("This item is not available."));
        json.body("CustomerList.list[0].items[0].upc.availability.inStoreEligible", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.availability.orderMethod", equalTo("NA"));

        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(6032754));
        json.body("CustomerList.list[0].items[0].upc.product.name", equalTo("Calvin Klein Illusion Bandage Dress"));
        json.body("CustomerList.list[0].items[0].upc.product.active", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.primaryImage", equalTo("9623190.fpx"));
        json.body("CustomerList.list[0].items[0].upc.product.imageURL", equalTo("3/optimized/8780323_fpx.tif"));
        json.body("CustomerList.list[0].items[0].upc.product.live", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.available", equalTo(true));

        json.body("CustomerList.list[0].items[0].upc.finalPrice.finalPrice", equalTo(106.24f));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.finalPriceHigh", equalTo(0.0f));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.displayFinalPrice", equalTo("Always Show"));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.productTypePromotion", equalTo("NONE"));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.promotions[0].promotionId", equalTo(19883730));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.promotions[0].promotionName", equalTo("SITEWIDE"));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.promotions[0].global", equalTo(false));

        json.body("CustomerList.list[0].items[1].upc.finalPrice.finalPrice", equalTo(106.24f));
        json.body("CustomerList.list[0].items[1].upc.finalPrice.finalPriceHigh", equalTo(0.0f));
        json.body("CustomerList.list[0].items[1].upc.finalPrice.displayFinalPrice", equalTo("Always Show"));
        json.body("CustomerList.list[0].items[1].upc.finalPrice.productTypePromotion", equalTo("NONE"));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.promotions[0].promotionId", equalTo(19883730));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.promotions[0].promotionName", equalTo("SITEWIDE"));
        json.body("CustomerList.list[0].items[0].upc.finalPrice.promotions[0].global", equalTo(false));

        json.body("CustomerList.list[0].items[1].product.id", equalTo(6032754));
        json.body("CustomerList.list[0].items[1].product.name", equalTo("Calvin Klein Illusion Bandage Dress"));
        json.body("CustomerList.list[0].items[1].product.active", equalTo(true));
        json.body("CustomerList.list[0].items[1].product.primaryImage", equalTo("9623190.fpx"));
        json.body("CustomerList.list[0].items[1].product.live", equalTo(true));
        json.body("CustomerList.list[0].items[1].product.available", equalTo(true));

        json.body("CustomerList.list[0].items[1].product.price.retailPrice", equalTo(124.99f));
        json.body("CustomerList.list[0].items[1].product.price.originalPrice", equalTo(139.0f));
        json.body("CustomerList.list[0].items[1].product.price.intermediateSalesValue", equalTo(0.0f));
        json.body("CustomerList.list[0].items[1].product.price.onSale", equalTo(true));
        json.body("CustomerList.list[0].items[1].product.price.upcOnSale", equalTo(false));
        json.body("CustomerList.list[0].items[1].product.price.priceType", equalTo(1));
        json.body("CustomerList.list[0].items[1].product.price.originalPriceLabel", equalTo("Reg."));
        json.body("CustomerList.list[0].items[1].product.price.retailPriceLabel", equalTo("Sale"));

        json.body("CustomerList.list[0].items[1].product.supressReviews", equalTo(true));
        json.body("CustomerList.list[0].items[1].product.phoneOnly", equalTo(
                "To purchase furniture, please call us toll free at 1-800-BUY-MACYS Monday-Friday, 9am-9pm, Saturday 9am-7pm and Sunday 11am-7pm to place your order."));
        json.body("CustomerList.list[0].items[1].product.multipleUpc", equalTo(true));
    }

    @And("^get list by list guid response includes error when no product in fcc$")
    public void error_response_invalid_product() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Unable to find upc/product info from fcc."));
    }

    @And("^get list by list guid response includes error when no upc in fcc$")
    public void error_response_invalid_upc() {
        json.body("errors.error[0].errorCode", equalTo("50002"));
        json.body("errors.error[0].developerMessage", equalTo("Error while fetching the product info"));
    }

    @And("^get list by list guid response includes error in case of Master product$")
    public void error_response_master_product() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Invalid Add Product: Can't add master product."));
    }

}
