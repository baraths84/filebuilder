package com.macys.selection.xapi.list.customer.stepdefs.FavoritesDelete;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getFavoritesByListGuidEndpoint;
import static io.restassured.RestAssured.given;

public class DeleteFromFavoritesListByupcId {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^customer with listGuid \"([^\"]*)\" and upcId \"([^\"]*)\"$")
      public void customer_delete_item_from_favorites_by_upcId(String listguid, String upcId) {
          request = given().pathParam("listguid", listguid).queryParam("upcId", upcId);
      }

	  @When("^customer removes item from list by upcId$")
	  public void customer_removes_from_favorites_by_upcId() {
          response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .delete(getFavoritesByListGuidEndpoint());
	    System.out.println("Delete from Favorites response" + response.prettyPrint());

	  }
	  
	  @Then("^customer removes item from favorites by upcId status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

}
