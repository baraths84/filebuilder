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

public class CreateCollaborativeListWithCollaboratorsAndWithItemsByProductId {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with collaborators and list items with productId$")
	  public void owner_user_with_list_of_collaborators_and_items_with_productId() {
	    request = given();
	  }

	@When("^customer create a collaborative list with collaborators and items with productId$")
	  public void a_customer_create_collaborative_list_with_collaborators_and_items_by_productId() {

	    String collaborativeListWithItems = "{\"list\":{\"collaborators\": [{\"userId\": 2150901},{\"userId\": 2150902}], \"user\": {\"id\": 90001003},\"list\": {\"name\": \"Collaborative list with collaborators and products\",\"listType\": \"C\",\"defaultList\": false,\"onSaleNotify\": true,\"searchable\": true,\"numberOfItems\": 2,\"showPurchaseInfo\": true,\"items\": [{\"qtyRequested\": 1,\"priority\": \"H\",\"product\": {\"id\": 9000303}},{\"qtyRequested\": 2,\"priority\": \"H\",\"product\": {\"id\": 9000404}}]}}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeListWithItems)
				.post(getCollaborativeListEndpoint());
	    System.out.println("create list response" 	+ response.prettyPrint());
	  }

	@Then("^the create collaborative list with items by productId and collaborators status code is \"([^\"]*)\"$")
	public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	@And("^response contains product data and collaborator data$")
	public void response_equal_collaborative_list_with_items()
	{
		json.body("list.listGuid", equalTo("f338675e-c359-4de9-b083-9000100107"));
	}
}



