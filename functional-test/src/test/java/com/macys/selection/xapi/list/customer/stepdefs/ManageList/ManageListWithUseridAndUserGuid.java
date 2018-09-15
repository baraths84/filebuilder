package com.macys.selection.xapi.list.customer.stepdefs.ManageList;

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

public class ManageListWithUseridAndUserGuid {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with listGuid \"([^\"]*)\" with userId \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
	  public void a_customer_with_userid_and_userguid(String listguid, String userid, String userGuid) {
		  request = given().pathParam("listGuid", listguid).queryParam("userId", userid).queryParam("userGuid", userGuid);
	  }

	  @When("^a customer tries to manage list by userId and userGuid$")
	  public void customer_manage_a_list_by_userid_and_userGuid() {
	    String manageList = "{\"CustomerList\":{\"list\":[{\"searchable\": \"false\"}]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(manageList)
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .patch(getListByGuidEndpoint());
	    System.out.println("manage list response" + response.prettyPrint());

		  /* New Flow:
		  1) MSP Customer:
		     GET /api/customer/v1/profile/guestUser?userid=55250007
		  1) MSP Wishlist:
		     PATCH /api/selection/list/v1/wishlists/18c7557f-264c-490d-8e9e-956830d55622?userId=201306333&guestUser=false
		   */
	  }
	  
	  @Then("^the manage list by userId and userGuid status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^manage list by userId and userGuid causes error$")
	  public void incorrect_request_message_in_the_response() {
		  json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
		  json.body("errors.error[0].errorCode", equalTo("50001"));
	  }

}
