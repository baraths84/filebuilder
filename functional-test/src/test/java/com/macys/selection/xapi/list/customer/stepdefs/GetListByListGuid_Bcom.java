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

import static com.macys.selection.xapi.list.util.TestUtilsBcom.getListByGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;


/**
 * Step definition file for get list by list guid.
 **/
public class GetListByListGuid_Bcom {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    //########################### Call with expands ############################

    @Given("^a customer wants to retrieve list with filter=onSale,promotions and listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid_onSalePromotions_filter(String listGuid) {
        request = given().pathParam("listGuid", listGuid).queryParam("expand", "items,user").queryParam("filter", "onSale,promotions");
        System.out.println("Customer list request" + request);
    }

    @Then("^the get bcom list by list guid status code is \"([^\"]*)\"$")
    public void verify_bcom_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @When("^customer retrieves list by listguid with filter options$")
    public void a_customer_retrieves_list_by_listguid_with_filters() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getListByGuidEndpoint());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @And("^get list by list guid with onSale,promotions filters response includes the following$")
    public void get_list_response_by_listguid_onSalePromotions_filter() {
        json.body("CustomerList.user.id", equalTo(201426549));
        json.body("CustomerList.user.guid", equalTo("6a3e98d6-f26b-428f-9578-75381d365f37"));
        json.body("CustomerList.user.guestUser", equalTo(Boolean.FALSE));

        json.body("CustomerList.list[0].name", equalTo("Saritha's List 3"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("66a6823c-ab5c-4f59-b96e-c51290e0b0fe"));
        json.body("CustomerList.list[0].items[0].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[0].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3677909));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(2530428));
        json.body("CustomerList.list[0].items[0].upc.price.onSale", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[0].promotions[0].promotionId",equalTo(20726));
        json.body("CustomerList.list[0].items[0].promotions[0].badgeTextAttributeValue",equalTo("Get Double Points in shoes every day!"));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("b7884f7e-52e2-4194-920d-51783c2af8fc"));
        json.body("CustomerList.list[0].items[1].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[1].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[1].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[1].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(2870613));
        json.body("CustomerList.list[0].items[1].upc.product.id", equalTo(1665592));
        json.body("CustomerList.list[0].items[1].upc.price.onSale", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[1].promotions.size", equalTo(0));

        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("08e9ff1a-db16-472e-91a4-f08661a21b9b"));
        json.body("CustomerList.list[0].items[2].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[2].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[2].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[2].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(1185259));
        json.body("CustomerList.list[0].items[2].upc.product.id", equalTo(608452));
        json.body("CustomerList.list[0].items[2].upc.price.onSale", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].items[2].promotions[0].promotionId",equalTo(22055));
        json.body("CustomerList.list[0].items[2].promotions[0].badgeTextAttributeValue",equalTo("Buy 2 HUE Tights for $16"));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("3b00f3f1-b3e0-43d9-8718-9490d82aa2dc"));
        json.body("CustomerList.list[0].items[3].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[3].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[3].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[3].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(3831814));
        json.body("CustomerList.list[0].items[3].upc.product.id", equalTo(2856721));
        json.body("CustomerList.list[0].items[3].upc.price.onSale", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].items[3].promotions[0].promotionId",equalTo(21957));
        json.body("CustomerList.list[0].items[3].promotions[0].badgeTextAttributeValue",equalTo("GIFT WITH PURCHASE"));
        json.body("CustomerList.list[0].items[3].promotions[1].promotionId",equalTo(21999));
        json.body("CustomerList.list[0].items[3].promotions[1].badgeTextAttributeValue",equalTo("GIFT WITH PURCHASE"));
        json.body("CustomerList.list[0].items[3].promotions[2].promotionId",equalTo(20725));
        json.body("CustomerList.list[0].items[3].promotions[2].badgeTextAttributeValue",equalTo("Get Double Points in cosmetics and fragrances every day!"));

    }

    @Given("^a customer wants to retrieve list with filter=onSale and listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid_onSale_filter(String listGuid) {
        request = given().pathParam("listGuid", listGuid).queryParam("expand", "items,user").queryParam("filter", "onSale");
        System.out.println("Customer list request" + request);
    }

    @And("^get list by list guid with onSale filter response includes the following$")
    public void get_list_response_by_listguid_onSale_filter() {
        json.body("CustomerList.user.id", equalTo(201426549));
        json.body("CustomerList.user.guid", equalTo("6a3e98d6-f26b-428f-9578-75381d365f37"));
        json.body("CustomerList.user.guestUser", equalTo(Boolean.FALSE));

        json.body("CustomerList.list[0].name", equalTo("Saritha's List 3"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("66a6823c-ab5c-4f59-b96e-c51290e0b0fe"));
        json.body("CustomerList.list[0].items[0].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[0].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(3677909));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(2530428));
        json.body("CustomerList.list[0].items[0].upc.price.onSale", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[0].promotions[0].promotionId",equalTo(20726));
        json.body("CustomerList.list[0].items[0].promotions[0].badgeTextAttributeValue",equalTo("Get Double Points in shoes every day!"));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("b7884f7e-52e2-4194-920d-51783c2af8fc"));
        json.body("CustomerList.list[0].items[1].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[1].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[1].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[1].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(2870613));
        json.body("CustomerList.list[0].items[1].upc.product.id", equalTo(1665592));
        json.body("CustomerList.list[0].items[1].upc.price.onSale", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[1].promotions.size", equalTo(0));

    }
    @Given("^a customer wants to retrieve list with filter=promotions and listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid_promotions_filter(String listGuid) {
        request = given().pathParam("listGuid", listGuid).queryParam("expand", "items,user").queryParam("filter", "promotions");
        System.out.println("Customer list request" + request);
    }

    @And("^get list by list guid with promotions filter response includes the following$")
    public void get_list_response_by_listguid_promotions_filter() {
        json.body("CustomerList.user.id", equalTo(201426549));
        json.body("CustomerList.user.guid", equalTo("6a3e98d6-f26b-428f-9578-75381d365f37"));
        json.body("CustomerList.user.guestUser", equalTo(Boolean.FALSE));

        json.body("CustomerList.list[0].name", equalTo("Saritha's List 3"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("08e9ff1a-db16-472e-91a4-f08661a21b9b"));
        json.body("CustomerList.list[0].items[0].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[0].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(1185259));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(608452));
        json.body("CustomerList.list[0].items[0].upc.price.onSale", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].items[0].promotions[0].promotionId",equalTo(22055));
        json.body("CustomerList.list[0].items[0].promotions[0].badgeTextAttributeValue",equalTo("Buy 2 HUE Tights for $16"));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("3b00f3f1-b3e0-43d9-8718-9490d82aa2dc"));
        json.body("CustomerList.list[0].items[1].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[1].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[1].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[1].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3831814));
        json.body("CustomerList.list[0].items[1].upc.product.id", equalTo(2856721));
        json.body("CustomerList.list[0].items[1].upc.price.onSale", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].items[1].promotions[0].promotionId",equalTo(21957));
        json.body("CustomerList.list[0].items[1].promotions[0].badgeTextAttributeValue",equalTo("GIFT WITH PURCHASE"));
        json.body("CustomerList.list[0].items[1].promotions[1].promotionId",equalTo(21999));
        json.body("CustomerList.list[0].items[1].promotions[1].badgeTextAttributeValue",equalTo("GIFT WITH PURCHASE"));
        json.body("CustomerList.list[0].items[1].promotions[2].promotionId",equalTo(20725));
        json.body("CustomerList.list[0].items[1].promotions[2].badgeTextAttributeValue",equalTo("Get Double Points in cosmetics and fragrances every day!"));


        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("66a6823c-ab5c-4f59-b96e-c51290e0b0fe"));
        json.body("CustomerList.list[0].items[2].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[2].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[2].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[2].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3677909));
        json.body("CustomerList.list[0].items[2].upc.product.id", equalTo(2530428));
        json.body("CustomerList.list[0].items[2].upc.price.onSale", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[2].promotions[0].promotionId",equalTo(20726));
        json.body("CustomerList.list[0].items[2].promotions[0].badgeTextAttributeValue",equalTo("Get Double Points in shoes every day!"));

    }
    @Given("^a customer wants to retrieve list with filter=promotions,onSale and listguid \"([^\"]*)\"$")
    public void a_customer_with_listguid_promotionsOnSale_filter(String listGuid) {
        request = given().pathParam("listGuid", listGuid).queryParam("expand", "items,user").queryParam("filter", "promotions,onSale");
        System.out.println("Customer list request" + request);
    }

    @And("^get list by list guid with promotions,onSale filters response includes the following$")
    public void get_list_response_by_listguid_promotionsOnSale_filter() {
        json.body("CustomerList.user.id", equalTo(201426549));
        json.body("CustomerList.user.guid", equalTo("6a3e98d6-f26b-428f-9578-75381d365f37"));
        json.body("CustomerList.user.guestUser", equalTo(Boolean.FALSE));

        json.body("CustomerList.list[0].name", equalTo("Saritha's List 3"));
        json.body("CustomerList.list[0].listType", equalTo("W"));
        json.body("CustomerList.list[0].defaultList", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].onSaleNotify", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].searchable", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].numberOfItems", equalTo(4));
        json.body("CustomerList.list[0].showPurchaseInfo", equalTo(Boolean.TRUE));

        json.body("CustomerList.list[0].items[0].itemGuid", equalTo("08e9ff1a-db16-472e-91a4-f08661a21b9b"));
        json.body("CustomerList.list[0].items[0].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[0].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[0].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[0].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[0].upc.id", equalTo(1185259));
        json.body("CustomerList.list[0].items[0].upc.product.id", equalTo(608452));
        json.body("CustomerList.list[0].items[0].upc.price.onSale", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].items[0].promotions[0].promotionId",equalTo(22055));
        json.body("CustomerList.list[0].items[0].promotions[0].badgeTextAttributeValue",equalTo("Buy 2 HUE Tights for $16"));

        json.body("CustomerList.list[0].items[1].itemGuid", equalTo("3b00f3f1-b3e0-43d9-8718-9490d82aa2dc"));
        json.body("CustomerList.list[0].items[1].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[1].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[1].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[1].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[1].upc.id", equalTo(3831814));
        json.body("CustomerList.list[0].items[1].upc.product.id", equalTo(2856721));
        json.body("CustomerList.list[0].items[1].upc.price.onSale", equalTo(Boolean.FALSE));
        json.body("CustomerList.list[0].items[1].promotions[0].promotionId",equalTo(21957));
        json.body("CustomerList.list[0].items[1].promotions[0].badgeTextAttributeValue",equalTo("GIFT WITH PURCHASE"));
        json.body("CustomerList.list[0].items[1].promotions[1].promotionId",equalTo(21999));
        json.body("CustomerList.list[0].items[1].promotions[1].badgeTextAttributeValue",equalTo("GIFT WITH PURCHASE"));
        json.body("CustomerList.list[0].items[1].promotions[2].promotionId",equalTo(20725));
        json.body("CustomerList.list[0].items[1].promotions[2].badgeTextAttributeValue",equalTo("Get Double Points in cosmetics and fragrances every day!"));


        json.body("CustomerList.list[0].items[2].itemGuid", equalTo("66a6823c-ab5c-4f59-b96e-c51290e0b0fe"));
        json.body("CustomerList.list[0].items[2].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[2].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[2].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[2].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[2].upc.id", equalTo(3677909));
        json.body("CustomerList.list[0].items[2].upc.product.id", equalTo(2530428));
        json.body("CustomerList.list[0].items[2].upc.price.onSale", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[2].promotions[0].promotionId",equalTo(20726));
        json.body("CustomerList.list[0].items[2].promotions[0].badgeTextAttributeValue",equalTo("Get Double Points in shoes every day!"));

        json.body("CustomerList.list[0].items[3].itemGuid", equalTo("b7884f7e-52e2-4194-920d-51783c2af8fc"));
        json.body("CustomerList.list[0].items[3].retailPriceDropAfterAddedToList", equalTo(Float.valueOf(0)));
        json.body("CustomerList.list[0].items[3].retailPriceDropPercentage", equalTo(0));
        json.body("CustomerList.list[0].items[3].qtyRequested", equalTo(1));
        json.body("CustomerList.list[0].items[3].qtyStillNeeded", equalTo(0));
        json.body("CustomerList.list[0].items[3].upc.id", equalTo(2870613));
        json.body("CustomerList.list[0].items[3].upc.product.id", equalTo(1665592));
        json.body("CustomerList.list[0].items[3].upc.price.onSale", equalTo(Boolean.TRUE));
        json.body("CustomerList.list[0].items[3].promotions.size", equalTo(0));
    }
}
