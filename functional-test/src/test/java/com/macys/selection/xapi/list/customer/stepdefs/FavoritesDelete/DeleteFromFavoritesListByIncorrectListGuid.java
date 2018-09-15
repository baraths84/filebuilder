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
import static com.macys.selection.xapi.list.util.TestUtils.getFavoritesByUserGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class DeleteFromFavoritesListByIncorrectListGuid {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^customer with incorrect listGuid \"([^\"]*)\" and productId \"([^\"]*)\"$")
      public void customer_delete_item_from_favorites_by_incorrect_listGuid(String listguid, String productId) {
          request = given().pathParam("listguid", listguid).queryParam("productId", productId);
      }

	  @When("^customer tries to remove item from list by incorrect listGuid$")
	  public void customer_removes_item_from_favorites_by_incorrect_listGuid() {
          response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .delete(getFavoritesByListGuidEndpoint());
	    System.out.println("Delete from Favorites response" + response.prettyPrint());

	  }
	  
	  @Then("^removing item from favorites by incorrect listGuid status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^removing item by incorrect listGuid response contains an error")
      public void response_equal_data(){
		  json.body("errors.error[0].developerMessage", equalTo("Invalid list guid"));
	      json.body("errors.error[0].errorCode", equalTo("50001"));
      }
}
