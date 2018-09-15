package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListEditRemove;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getGetCollaborativeListByGuidEndpoint;
import static io.restassured.RestAssured.given;

public class EditCollaborativeListChangeListName {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	@Given("^collaborative list with listGuid \"([^\"]*)\" and userGuid \"([^\"]*)\"$")
	  public void collaborative_list_with_listGuid(String lisgGuid, String userGuid) {
	    request = given().pathParam("listguid", lisgGuid).queryParam("userGuid", userGuid);
	  }

	@When("^customer edit collaborative list name")
	  public void a_customer_edit_collaborative_list_name() {

	    String collaborativeList = "{\"list\":{\"name\": \"Edited list name\"}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(collaborativeList)
	            .put(getGetCollaborativeListByGuidEndpoint());
	    System.out.println("create list response" + response.prettyPrint());
	  }

	  @Then("^edit collaborative list name status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	    json = response.then().statusCode(stautsCode);
	  }

}
