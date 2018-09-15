package com.macys.selection.xapi.list.customer.stepdefs.FavoritesGet;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getFavoritesByUserGuidEndpoint;
import static com.macys.selection.xapi.list.util.TestUtils.getListByGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class GetFavoritesListByUserGuid {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with userGuid \"([^\"]*)\"$")
      public void customer_gets_favorites_list_by_userGuid(String userGuid) {
          request = given().pathParam("userguid", userGuid);
      }

	  @When("^customer receives list of favorites by userGuid$")
	  public void customer_gets_favorites_by_userguid() {
          response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .get(getFavoritesByUserGuidEndpoint());
	    System.out.println("Get Favorites response" + response.prettyPrint());

	  }
	  
	  @Then("^status code of getting list by userGuid is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^get favorites by userGuid contains following data$")
      public void response_equal_data(){
	      json.body("list.listGuid", equalTo("a78a1ed1-d7c0-4aae-80001000-1"));
	      json.body("list.products[0].pid", equalTo(10001));
      }
}
