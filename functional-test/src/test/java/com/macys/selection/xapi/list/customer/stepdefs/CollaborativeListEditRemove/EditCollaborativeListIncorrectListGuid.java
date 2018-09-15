package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListEditRemove;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getGetCollaborativeListByGuidEndpoint;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class EditCollaborativeListIncorrectListGuid {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^collaborative list with incorrect listGuid \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
	  public void collaborative_list_with_incorrect_listGuid(String lisgGuid, String userGuid) {
	    request = given().pathParam("listguid", lisgGuid).queryParam("userGuid", userGuid);
	  }

	@When("^customer edit collaborative list by incorrect listGuid")
	  public void a_customer_edit_collaborative_list_by_incorrect_incorrect() {

	    String collaborativeList = "{\"list\":{\"name\": \"Incorrect list guid\"}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeList)
	            .put(getGetCollaborativeListByGuidEndpoint());
	    System.out.println("create list response" + response.prettyPrint());
	  }

	  @Then("^edit collaborative list by incorrect listGuid status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

	@And("^response contains error$")
	public void manage_list_by_incorrect_listGuid(){
		json.body("errors.error[0].developerMessage", equalTo("Invalid list guid"));
		json.body("errors.error[0].errorCode", equalTo("50001"));
	}
}
