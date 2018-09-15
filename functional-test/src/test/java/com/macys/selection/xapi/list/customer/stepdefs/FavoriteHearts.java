package com.macys.selection.xapi.list.customer.stepdefs;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import javax.ws.rs.core.MediaType;

/**
 * Step definition file for retrieving customer info by userid and guid.
 **/
public class FavoriteHearts {

  private Response response;
  private ValidatableResponse json;
  private RequestSpecification request;
  private String GET_FAVLIST_URL = getFavoritesByUserGuidEndpoint();
  private String ADD_FAVLIST_URL = getFavoritesEndpoint();
  private String DELETE_FAVLIST_URL = getFavoritesByListGuidEndpoint();

  @When("^customer retrieves the fav list by userid$")
  public void a_customer_retrieves_fav_list_by_userid() {
    response = request
            .contentType(MediaType.APPLICATION_JSON)
            .when()
            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
            .get(GET_FAVLIST_URL);
    System.out.println("favorite list response" + response.prettyPrint());

      /* New flow:
      1) MSP Customer:
         GET /api/customer/v1/profile/guestUser?userguid=4b61ee86-c46e-4f62-b97a-f52a20b512e8
      2) MSP Wishlist:
         GET /api/selection/list/v1/favorites?userId=41
      */

  }
  @Then("^the fav status code is \"([^\"]*)\"$")
  public void verify_status_code(int stautsCode) {
    json = response.then().statusCode(stautsCode);
  }

  @And("^response includes the following productID$")
  public void response_equals() {
    json.body("list.products[0].pid", equalTo(22805));
    json.body("list.listGuid", equalTo("c3e6a1b9-092d-4ac5-8b09-2cbd9841e202"));

  }

  @Given("^a customer userguid \"([^\"]*)\"$")
  public void a_customer_with_userguid(String userguid) {
    request = given().pathParam("userguid", userguid);
    System.out.println("Customr favorite request" + request);
  }

  @Given("^a customer without userguid$")
  public void a_customer_without_userguid() {
    request = given();
    System.out.println("Customr favorite request" + request);
  }

  @Given("^customer add products to fav list by userid$")
  public void a_customer_add_fav_list_by_userid() {

    String FavList = "{\n" +
            "  \"CustomerList\": {\n" +
            "    \"user\": {\n" +
            "      \"guid\": \"4b61ee86-c46e-4f62-b97a-f52a20b512e8\"\n" +
            "    },\n" +
            "    \"list\": [\n" +
            "      {\n" +
            "        \"items\": [\n" +
            "          {\n" +
            "            \"qtyRequested\": 4,\n" +
            "            \"product\": {\n" +
            "              \"id\": 3340613\n" +
            "            }\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";
    response = request
            .contentType(MediaType.APPLICATION_JSON)
            .when().body(FavList)
            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
            .post(ADD_FAVLIST_URL);
    System.out.println("favorite list response" + response.prettyPrint());

  }

  @Given("^customer add products to fav list by not having userid$")
  public void a_customer_add_fav_list_by_not_having_userid() {

    String FavList = "{\n" +
            "  \"CustomerList\": {\n" +
            "    \"list\": [\n" +
            "      {\n" +
            "        \"items\": [\n" +
            "          {\n" +
            "            \"qtyRequested\": 1,\n" +
            "            \"product\": {\n" +
            "              \"id\": 22805\n" +
            "            }\n" +
            "          }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    response = request
            .contentType(MediaType.APPLICATION_JSON)
            .when().body(FavList)
            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
            .post(ADD_FAVLIST_URL);
    System.out.println("favorite list response" + response.prettyPrint());

      /* New flow:
      1) MSP Customer:
         GET /api/customer/v1/profile/createuser/isuser
      2) MSP Customer:
         GET /api/customer/v1/profile/isuser/guidbyuserid/201786759
      3) FCC:
         GET /api/catalog/v2/products/22805?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
      4) MSP Wishlist:
         POST /api/selection/list/v1/wishlists/items?userGuid=bdd7efc4-2653-4806-8276-cc9fd44f2713&guestUser=true&userId=201786759
       */
  }


  @Given("^a customer with listguid \"([^\"]*)\" and productid \"([^\"]*)\"$")
  public void a_customer_exits_with_userid(String listguid, String productId) {
    request = given().pathParam("listguid", listguid).queryParam("productId", productId);
  }

  @When("^customer delete product from fav list for a product$")
  public void a_customer_delete_fav_list_by() {

    response = request
            .contentType(MediaType.APPLICATION_JSON)
            .when()
            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
            .delete(DELETE_FAVLIST_URL);
    System.out.println("favorite list response" + response.prettyPrint());

      /*
      New flow:
      1) To MSP Wishlist:
         GET /api/selection/list/v1/wishlists/c3e6a1b9-092d-4ac5-8b09-2cbd9841e202
      2) To MSP Customer:
         GET /api/customer/v1/profile/guestUser?userid=55250007
      3) To FCC:
         GET /api/catalog/v2/products/22805?_fields=name%2Cactive%2Cattributes%2CprimaryPortraitSource%2CadditionalImageSource%2CprimaryImage%2CadditionalImages%2CcolorwayImages%2CreviewStatistics%2Clive%2CdisplayCode%2CdefaultCategoryId%2Cshipping%2Cprice%2Cavailable%2Cupcs%2CmemberProductIds&active=true
      4) MSP Wishlist
         DELETE /api/selection/list/v1/favorites/c3e6a1b9-092d-4ac5-8b09-2cbd9841e202?guestUser=false&upcId=52582&productId=22805
       */

  }

  @And("^I verify the favorites response with guid$")
  public void response_equal() {
    json.body("CustomerList.list[0].name",                                          equalTo("test's List"));
    json.body("CustomerList.list[0].listType",                                      equalTo("W"));
    json.body("CustomerList.list[0].defaultList",                                   equalTo(Boolean.TRUE));
    json.body("CustomerList.list[0].onSaleNotify",                                  equalTo(Boolean.FALSE));
    json.body("CustomerList.list[0].searchable",                                    equalTo(Boolean.FALSE));
    json.body("CustomerList.list[0].numberOfItems",                                 equalTo(4));
    json.body("CustomerList.list[0].showPurchaseInfo",                              equalTo(Boolean.TRUE));
    json.body("CustomerList.user.guestUser",                                      equalTo(Boolean.FALSE));

  }


  @And("^I verify the favorites response without guid$")
  public void response_equal_without_guid() {
    json.body("CustomerList.list[0].name",                                          equalTo("Guest List"));
    json.body("CustomerList.list[0].listType",                                      equalTo("W"));
    json.body("CustomerList.list[0].defaultList",                                   equalTo(Boolean.TRUE));
    json.body("CustomerList.list[0].onSaleNotify",                                  equalTo(Boolean.FALSE));
    json.body("CustomerList.list[0].searchable",                                    equalTo(Boolean.FALSE));
    json.body("CustomerList.list[0].numberOfItems",                                 equalTo(1));
    json.body("CustomerList.user.guestUser",                                      equalTo(Boolean.TRUE));
  }

}
