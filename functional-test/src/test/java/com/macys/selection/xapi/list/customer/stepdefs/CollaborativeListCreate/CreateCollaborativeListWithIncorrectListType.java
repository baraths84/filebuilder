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

public class CreateCollaborativeListWithIncorrectListType {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^user want to create collaborative list with incorrect type$")
	  public void customer_with_iserId() {
	    request = given();
	  }

	@When("^customer create a collaborative list with incorrect list type$")
	  public void a_customer_create_collaborative_list_with_incorrect_list_type() {

	    String collaborativeListWithItems = "{\"list\":{\"user\":{\"id\": 90001001},\"list\":{\"name\": \"Collaborate list with incorrect list type\",\"listType\": \"W\",\"defaultList\": false,\"onSaleNotify\": true,\"searchable\": true,\"numberOfItems\": 0,\"showPurchaseInfo\": true}}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeListWithItems)
				.post(getCollaborativeListEndpoint());
	    System.out.println("create list response" 	+ response.prettyPrint());
	  }

	  @Then("^the create collaborative list with incorrect list type status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	  @And("^collaborative response contains corresponding error")
	  public void response_equal_collaborative_list_with_items() {
		json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
		json.body("errors.error[0].errorCode", equalTo("50001"));
	  }
}



