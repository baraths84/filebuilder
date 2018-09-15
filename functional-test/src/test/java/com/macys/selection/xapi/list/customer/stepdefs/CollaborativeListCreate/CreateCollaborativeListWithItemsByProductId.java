package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListCreate;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getCollaborativeListEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCollaborativeListWithItemsByProductId {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with userGuid and list items with productId$")
	  public void owner_user_with_productId_and_list_items() {
	    request = given();
	  }

	@When("^customer create a collaborative list with items by productId$")
	  public void a_customer_create_collaborative_list_with_items_by_productId() {

	    String collaborativeListWithItems = "{\"list\":{\"name\": \"Collaborative list with productId\",\"userGuid\": \"90001002-90001002-90001002\",\"items\":[{\"product\":{\"id\":9000101}},{\"product\":{\"id\":9000202 }}]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeListWithItems)
	            .post(getCollaborativeListEndpoint());
	    System.out.println("create list response" 	+ response.prettyPrint());
	  }

	  @Then("^the create collaborative list with items by productId status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^response contains collaborative list data with items")
	  public void response_equal_collaborative_list_with_items() {
		  json.body("list.listGuid", equalTo("f338675e-c359-4de9-b083-9000100107"));
	  }
}



