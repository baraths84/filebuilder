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

public class ManageListWithBlankListInTheBody {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with the listGuid \"([^\"]*)\" and userId \"([^\"]*)\"$")
	  public void a_customer_tries_to_update_list_with_blank_list(String listguid, String userid) {
		  request = given().pathParam("listGuid", listguid).queryParam("userId", userid);
	  }

	  @When("^customer tries to manage the list with blank list in the body$")
	  public void customer_manage_a_list_with_blank_list() {
	    String manageList = "{\"CustomerList\":{\"list\":[]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(manageList)
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .patch(getListByGuidEndpoint());
	    System.out.println("manage list response" + response.prettyPrint());

	  }
	  
	  @Then("^the manage list with blank list in the body status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^manage list with blank list in the body contains error$")
	  public void incorrect_request_message_in_the_response() {
		  json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
		  json.body("errors.error[0].errorCode", equalTo("50001"));
	  }

}
