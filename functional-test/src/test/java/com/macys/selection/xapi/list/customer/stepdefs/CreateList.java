package com.macys.selection.xapi.list.customer.stepdefs;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import javax.ws.rs.core.MediaType;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

public class CreateList {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with userid")
	  public void a_customer_with_userid() {
	    request = given();
	    System.out.println("Customer create list request" + request);
	  }
	  
	  @Given("^customer create a list$")
	  public void a_customer_create_a_list_by_userid() {

	    String createList = "{\"CustomerList\": {\"user\": {\"id\": 2158695297},\"list\": [{\"listType\": \"w\",\"name\": \"Test List\",\"searchable\": \"false\"}]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when().body(createList)
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .post(getListsEndpoint());
	    System.out.println("create list response" + response.prettyPrint());

	  }
	  
	  @Then("^the create list status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^I verify the create list response with guid")
	  public void response_equal() {
	    json.body("CustomerList.list[0].name",                                          equalTo("Test List"));
	    json.body("CustomerList.list[0].listType",                                      equalTo("W"));
	    json.body("CustomerList.list[0].defaultList",                                   equalTo(Boolean.FALSE));
	    json.body("CustomerList.list[0].onSaleNotify",                                  equalTo(Boolean.FALSE));
	    json.body("CustomerList.list[0].searchable",                                    equalTo(Boolean.FALSE));
	    json.body("CustomerList.user.guestUser",                                      equalTo(Boolean.FALSE));

		  /*
		  New Flow:
		  1) To MSP Customer
		     GET /api/customer/v1/profile/guestUser?userid=2158695297
		  2) To MSP Wishlist
		     POST /api/selection/list/v1/wishlists?guestUser=false
		   */
	  }

}
