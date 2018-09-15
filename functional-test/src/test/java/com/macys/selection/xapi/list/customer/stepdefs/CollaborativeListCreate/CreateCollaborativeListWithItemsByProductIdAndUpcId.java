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

public class CreateCollaborativeListWithItemsByProductIdAndUpcId {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with userGuid and list items with productId and upcId$")
	  public void owner_user_with_productId_and_upcId() {
	    request = given();
	  }

	@When("^customer create a collaborative list with items by productId and upcId$")
	  public void a_customer_create_collaborative_list_with_upcId_and_productId() {

	    String collaborativeListWithItems = "{\"list\":{\"name\": \"Collaborative list with productId and upcId\",\"userGuid\": \"90001003-90001003-90001003\",\"items\":[{\"upc\":{\"id\":36846878}},{\"product\":{\"id\":81003 }}]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeListWithItems)
	            .post(getCollaborativeListEndpoint());
	    System.out.println("create list response " 	+ response.prettyPrint());
	  }

	  @Then("^the create collaborative list with items by productId and upcId status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^response contains collaborative list data with product and upc data")
	  public void response_equal_collaborative_list_with_items() {
		  json.body("list.listGuid", equalTo("f338675e-c359-4de9-b083-9000100108"));
	  }
}



