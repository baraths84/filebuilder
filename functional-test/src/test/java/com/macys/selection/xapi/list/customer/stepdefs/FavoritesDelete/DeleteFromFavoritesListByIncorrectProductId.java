package com.macys.selection.xapi.list.customer.stepdefs.FavoritesDelete;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getFavoritesByListGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteFromFavoritesListByIncorrectProductId {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^customer with listGuid \"([^\"]*)\" and incorrect productId \"([^\"]*)\"$")
      public void customer_delete_item_from_favorites_by_incorrect_productId(String listguid, String productId) {
          request = given().pathParam("listguid", listguid).queryParam("productId", productId);
      }

	  @When("^customer tries to remove item from list by incorrect productId$")
	  public void customer_removes_item_from_favorites_by_incorrect_productId() {
          response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .delete(getFavoritesByListGuidEndpoint());
	    System.out.println("Delete from Favorites response" + response.prettyPrint());

	  }

	  /*
	  1. Get list data (with userId) by listGuid: /api/selection/list/v1/wishlists/a78a1ed1-d7c0-4aae-81000100-2?
	  2. Get user data (Guest or not) by userId: /api/customer/v1/profile/guestUser?userid=81000105&userguid=81000105-81000105
	  3. Get upcId by productId: /api/catalog/v2/products/81006
	  */

	  @Then("^removing item from favorites by incorrect productId status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^removing item by incorrect productId response contains an error")
      public void response_equal_data(){
		  json.body("errors.error[0].developerMessage", equalTo("Unable to find upc/product info from fcc."));
	      json.body("errors.error[0].errorCode", equalTo("50001"));
      }
}
