package com.macys.selection.xapi.list.customer.stepdefs;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static org.hamcrest.Matchers.equalTo;
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
import static io.restassured.RestAssured.given;

public class AddItemToDefaultList {
    private static final Logger LOGGER = LoggerFactory.getLogger(AddItemToDefaultList.class);

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer adding item to default list with listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid(String listguid) {
        request = given();
    }

    @When("^customer adds an item to the default list$")
    public void a_customer_adds_item_to_default_list() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToDefaultList.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getListsItemsEndpoint());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1) To MSP Customer
          GET /api/customer/v1/profile/guestUser?userid=201336579
        2) To FCC:
          GET /api/catalog/v2/upcs/37509092?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        3) To FCC:
          GET /api/catalog/v2/products/3014013?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
        4) To MSP WishList:
          POST /api/selection/list/v1/wishlists/items?userFirstName=testUserNamef&guestUser=false&userId=201336579
        */
    }

    @When("^customer adds an item with both upc and product$")
    public void a_customer_adds_item_with_both_upc_and_product() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToDefaultListBothUpcAndProduct.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getListsItemsEndpoint());
        LOGGER.info(response.prettyPrint());
    }

    @When("^customer adds an item without user$")
    public void a_customer_adds_item_without_user() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToDefaultListNoUser.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getListsItemsEndpoint());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1) To MSP Customer
          GET /api/customer/v1/profile/createuser/isuser
        2) To MSP Customer
          GET /api/customer/v1/profile/isuser/guidbyuserid/201786759
        3) To FCC:
          GET /api/catalog/v2/upcs/37509092?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        4) To FCC:
          GET /api/catalog/v2/products/3014013?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
        4) To MSP WishList:
          POST /api/selection/list/v1/wishlists/items?userGuid=bdd7efc4-2653-4806-8276-cc9fd44f2713&guestUser=true&userId=201786759
          */
    }

    @When("^customer adds an item with invalid user id$")
    public void a_customer_adds_item_with_invalid_user_id() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToDefaultListInvalidUser.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getListsItemsEndpoint());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1) MSP Customer:
           GET /api/customer/v1/profile/guestUser?userid=111
        2) MSP Customer:
           GET /api/customer/v1/profile/createuser/isuser?userid=111
        3) MSP Customer:
           GET /api/customer/v1/profile/isuser/guidbyuserid/111
        4) FCC:
           GET /api/catalog/v2/upcs/37509092?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        5) FCC:
           GET /api/catalog/v2/products/3014013?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
        6) MSP Wishhlist:
           POST /api/selection/list/v1/wishlists/items?userGuid=bdd7efc4-2653-4806-8276-111&guestUser=true&userId=111
         */
    }

    @When("^customer adds an item with invalid upc$")
    public void a_customer_adds_item_with_invalid_upc() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToDefaultListInvalidUpc.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getListsItemsEndpoint());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1) MSP Customer:
           GET /api/customer/v1/profile/guestUser?userid=1212
        2) FCC:
           GET /api/catalog/v2/upcs/111?_fields=upc%2CproductId%2Cactive%2Cprice%2Ccolorway%2Cattributes%2Cavailability
        3) MSP Wishlist:
           POST /api/selection/list/v1/wishlists/items?userFirstName=testFirstName&guestUser=false&userId=1212
         */
    }

    @When("^customer adds an item with product only$")
    public void a_customer_adds_item_with_product_only() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/addItemToDefaultListWithProductOnly.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .body(body)
                .post(getListsItemsEndpoint());
        LOGGER.info(response.prettyPrint());

        /* New Flow calls:
        1) MSP Customer:
           GET /api/customer/v1/profile/guestUser?userid=11112222
        2) FCC:
           GET /api/catalog/v2/products/3014013?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
        3) MSP Wishlist:
           POST /api/selection/list/v1/wishlists/items?userFirstName=testFirstName&guestUser=false&userId=11112222
         */
    }

    @Then("^the addlist status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^i verify the add item to default list response$")
     public void response_equal() {
        json.body("CustomerList.list[0].name", equalTo("Test List"));
        json.body("CustomerList.list[0].listGuid", equalTo("7a58f9f7-4926-4f14-b986-a8609c488d39"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("f4fa2368-1c07-4da6-8ae1-1eec7cef7b23"));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(37509092));
    }

    @And("^i verify the add item to default list response after user created$")
    public void response_userGenerated_equal() {
        json.body("CustomerList.list[0].name", equalTo("Guest List"));
        json.body("CustomerList.list[0].listGuid", equalTo("7a58f9f7-4926-4f14-b986-a8609c488d39"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].defaultList", equalTo(true));
        json.body("CustomerList.list[0].searchable", equalTo(false));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(false));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(true));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("f4fa2368-1c07-4da6-8ae1-1eec7cef7b23"));
        json.body("CustomerList.list[0].items[0].id", equalTo(112500111));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].priority", equalTo("H"));
        json.body("CustomerList.list[0].items[0].retailPriceWhenAdded", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(37509092));
        json.body("CustomerList.list[0].items[0].upc.color", equalTo("Grey Heather"));
        json.body("CustomerList.list[0].items[0].upc.size", equalTo("L"));
        json.body("CustomerList.list[0].items[0].upc.price.retailPrice", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].upc.availability.available", equalTo(false));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(3014013));
        json.body("CustomerList.list[0].items[0].upc.product.active", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.available", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.primaryImage", equalTo("8780325.fpx"));
    }

    @And("^i verify error response$")
    public void error_response_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Incorrect request"));
    }

    @And("^i verify the add item to default list after invalid user passed and new user created$")
    public void response_invalidUserRegenerated_equal() {
        json.body("CustomerList.list[0].name", equalTo("Guest List"));
        json.body("CustomerList.list[0].listGuid", equalTo("7a58f9f7-4926-4f14-b986-a8609c488d39"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].numberOfItems", equalTo(1));
        json.body("CustomerList.list[0].defaultList", equalTo(true));
        json.body("CustomerList.user.id", equalTo(111));
        json.body("CustomerList.user.guid", equalTo("bdd7efc4-2653-4806-8276-111"));
        json.body("CustomerList.user.guestUser", equalTo(true));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("f4fa2368-1c07-4da6-8ae1-1eec7cef7b23"));
        json.body("CustomerList.list[0].items[0].id", equalTo(112500111));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].priority", equalTo("H"));
        json.body("CustomerList.list[0].items[0].retailPriceWhenAdded", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(37509092));
        json.body("CustomerList.list[0].items[0].upc.color", equalTo("Grey Heather"));
        json.body("CustomerList.list[0].items[0].upc.size", equalTo("L"));
        json.body("CustomerList.list[0].items[0].upc.price.retailPrice", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].upc.availability.available", equalTo(false));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(3014013));
        json.body("CustomerList.list[0].items[0].upc.product.active", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.available", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.primaryImage", equalTo("8780325.fpx"));
    }

    @And("^i verify error response with invalid upc$")
    public void error_response_equal_invalid_upc() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Upc details missing"));
    }

    @And("^i verify the add item to default list response with product only including fcc data and final price$")
    public void response_productOnly_equal() {
        json.body("CustomerList.list[0].name", equalTo("Test List"));
        json.body("CustomerList.list[0].listGuid", equalTo("7a58f9f7-4926-4f14-b986-a8609c488d39"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("f4fa2368-1c07-4da6-8ae1-1eec7cef7b23"));
        json.body("CustomerList.list[0].items[0].retailPriceWhenAdded", equalTo(33.0f));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(3));

        json.body("CustomerList.list[0].items[0].upc.id", equalTo(37509084));
        json.body("CustomerList.list[0].items[0].upc.upcNumber", equalTo(6083567));
        json.body("CustomerList.list[0].items[0].upc.color", equalTo("Bright White"));
        json.body("CustomerList.list[0].items[0].upc.size", equalTo("2XL"));

        json.body("CustomerList.list[0].items[0].upc.price.retailPrice", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].upc.price.originalPrice", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].upc.price.intermediateSalesValue", equalTo(0.0f));
        json.body("CustomerList.list[0].items[0].upc.price.salesValue", equalTo(0.0f));
        json.body("CustomerList.list[0].items[0].upc.price.onSale", equalTo(false));
        json.body("CustomerList.list[0].items[0].upc.price.upcOnSale", equalTo(false));
        json.body("CustomerList.list[0].items[0].upc.price.priceType", equalTo(13));
        json.body("CustomerList.list[0].items[0].upc.price.priceTypeText", equalTo("Everyday Value"));
        json.body("CustomerList.list[0].items[0].upc.price.basePriceType", equalTo(0));

        json.body("CustomerList.list[0].items[0].upc.availability.available", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.availability.upcAvailabilityMessage", equalTo("In Stock: Usually ships within 2 business days."));
        json.body("CustomerList.list[0].items[0].upc.availability.inStoreEligible", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.availability.orderMethod", equalTo("POOL"));

        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(3014013));
        json.body("CustomerList.list[0].items[0].upc.product.name", equalTo("American Rag Men's Thermal Long Sleeve Shirt, Created for Macy's "));
        json.body("CustomerList.list[0].items[0].upc.product.active", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.primaryImage", equalTo("8780325.fpx"));
        json.body("CustomerList.list[0].items[0].upc.product.imageURL", equalTo("2/optimized/8780322_fpx.tif"));
        json.body("CustomerList.list[0].items[0].upc.product.live", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.available", equalTo(true));
        json.body("CustomerList.list[0].items[0].upc.product.reviewStatistics.averageRating", equalTo(4.3636f));
        json.body("CustomerList.list[0].items[0].upc.product.reviewStatistics.reviewCount", equalTo(22));

        json.body("CustomerList.list[0].items[0].product.id", equalTo(3014013));
        json.body("CustomerList.list[0].items[0].product.name", equalTo("American Rag Men's Thermal Long Sleeve Shirt, Created for Macy's "));
        json.body("CustomerList.list[0].items[0].product.active", equalTo(true));
        json.body("CustomerList.list[0].items[0].product.available", equalTo(true));
        json.body("CustomerList.list[0].items[0].product.primaryImage", equalTo("8780325.fpx"));
        json.body("CustomerList.list[0].items[0].product.imageURL", equalTo("2/optimized/8780322_fpx.tif"));
        json.body("CustomerList.list[0].items[0].product.live", equalTo(true));

        json.body("CustomerList.list[0].items[0].product.price.retailPrice", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].product.price.originalPrice", equalTo(17.98f));
        json.body("CustomerList.list[0].items[0].product.price.intermediateSalesValue", equalTo(0.0f));
        json.body("CustomerList.list[0].items[0].product.price.onSale", equalTo(false));
        json.body("CustomerList.list[0].items[0].product.price.upcOnSale", equalTo(false));
        json.body("CustomerList.list[0].items[0].product.price.priceType", equalTo(13));
        json.body("CustomerList.list[0].items[0].product.price.priceTypeText", equalTo("Everyday Value"));

        json.body("CustomerList.list[0].items[0].product.reviewStatistics.averageRating", equalTo(4.3636f));
        json.body("CustomerList.list[0].items[0].product.reviewStatistics.reviewCount", equalTo(22));

        json.body("CustomerList.list[0].items[0].product.multipleUpc", equalTo(true));

        json.body("CustomerList.list[0].items[0].product.finalPrice.finalPrice", equalTo(333.0f));
        json.body("CustomerList.list[0].items[0].product.finalPrice.finalPriceHigh", equalTo(0.0f));
        json.body("CustomerList.list[0].items[0].product.finalPrice.displayFinalPrice", equalTo("Always Show"));
        json.body("CustomerList.list[0].items[0].product.finalPrice.productTypePromotion", equalTo("NONE"));
        json.body("CustomerList.list[0].items[0].product.finalPrice.promotions[0].promotionId", equalTo(19883730));
        json.body("CustomerList.list[0].items[0].product.finalPrice.promotions[0].promotionName", equalTo("SITEWIDE"));
        json.body("CustomerList.list[0].items[0].product.finalPrice.promotions[0].global", equalTo(false));
    }
}
