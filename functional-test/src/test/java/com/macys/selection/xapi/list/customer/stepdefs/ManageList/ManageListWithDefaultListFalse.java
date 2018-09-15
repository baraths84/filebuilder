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

public class ManageListWithDefaultListFalse {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with userId \"([^\"]*)\" and with listGuid \"([^\"]*)\"$")
	  public void a_customer_with_userid_andlistguid(String userid, String listguid) {
		  request = given().pathParam("listGuid", listguid).queryParam("userId", userid);
	  }

	  @When("^customer tries to update defaultList with false$")
	  public void customer_tries_update_defaultList_to_false() {
	    String manageList = "{\"CustomerList\":{\"list\":[]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(manageList)
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .patch(getListByGuidEndpoint());
	    System.out.println("manage list response" + response.prettyPrint());
	  }
	  
	  @Then("^update list with defaultList false status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^update list with defaultList false causes error$")
	  public void incorrect_request_message_in_the_response() {
		  json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
		  json.body("errors.error[0].errorCode", equalTo("50001"));
	  }

}
