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

public class ManageListWithInvalidListGuid {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with invalid listGuid \"([^\"]*)\" and userId \"([^\"]*)\"$")
	  public void a_customer_with_userid_and_invalid_listguid(String listguid, String userid) {
		  request = given().pathParam("listGuid", listguid).queryParam("userId", userid);
	  }

	  @When("^customer tries to manage the list by invalid listGuid$")
	  public void customer_manage_a_list_by_invalid_listGuid() {
	    String manageList = "{\"CustomerList\":{\"list\":[{\"name\": \"test list\"}]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(manageList)
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .patch(getListByGuidEndpoint());
	    System.out.println("manage list response" + response.prettyPrint());
	  }
	  
	  @Then("^the manage list by invalid listGuid status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^manage list by invalid listGuid contains error$")
	  public void manage_list_by_incorrect_listGuid(){
	  	json.body("errors.error[0].developerMessage", equalTo("Invalid list guid"));
	  	json.body("errors.error[0].errorCode", equalTo("50001"));
	  }
}
