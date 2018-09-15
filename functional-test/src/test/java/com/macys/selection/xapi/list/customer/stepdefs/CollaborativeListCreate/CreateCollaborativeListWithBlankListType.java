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

public class CreateCollaborativeListWithBlankListType {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^user create collaborative list with blank type$")
	  public void collaborative_list_owner() {
	    request = given();
	  }

	@When("^customer create a collaborative list with blank list type$")
	  public void a_customer_create_collaborative_list_with_blank_list_type() {

	    String collaborativeListWithItems = "{\"list\":{\"user\":{\"userGuid\": \"90001003-90001003-90001003\"},\"list\":{\"name\": \"Collaborate list with blank list type\",\"listType\": null,\"defaultList\": false,\"onSaleNotify\": true,\"searchable\": true,\"numberOfItems\": 0,\"showPurchaseInfo\": true}}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeListWithItems)
				.post(getCollaborativeListEndpoint());
	    System.out.println("create list response" 	+ response.prettyPrint());
	  }

	  @Then("^the create collaborative list with blank list type status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^response contains corresponding list data$")
	  public void response_equal_collaborative_list_with_items() {
		  json.body("list.listGuid", equalTo("f338675e-c359-4de9-b083-900020011"));
	  }
}



