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

public class CreateCollaborativeListWithaLotOfCollaborators {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^list owner with userGuid and a lot of collaborators$")
	  public void owner_user_with_userid_and_list_collaborators() {
	    request = given();
	  }

	@When("^customer create a collaborative list with a lot of collaborators$")
	  public void a_customer_create_collaborative_list_collaborators() {

	    String collaborativeListWithItems = "{\"list\":{\"user\": {\"userGuid\": \"90001004-90001004-90001004\"},\"list\": {\"collaborators\": [{\"userId\": 2158695297},{\"userId\": 201336627},{\"userId\": 81000100},{\"userId\": 81000103}]},\"name\": \"Collaborative list with a lot of collaborators\",\"listType\": \"C\",\"defaultList\": false,\"onSaleNotify\": true,\"searchable\": true,\"numberOfItems\": 0,\"showPurchaseInfo\": true}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeListWithItems)
				.post(getCollaborativeListEndpoint());
	    System.out.println("create list response" 	+ response.prettyPrint());
	  }

	@Then("^the create collaborative list with a lot of collaborators status code is \"([^\"]*)\"$")
	public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	@And("^response contains error about exceeded collaborators$")
	public void response_equal_collaborative_list_with_items() {
		json.body("errors.error[0].developerMessage", equalTo("Maximum collaborators per list reached"));
		json.body("errors.error[0].errorCode", equalTo("50001"));
	}
}



