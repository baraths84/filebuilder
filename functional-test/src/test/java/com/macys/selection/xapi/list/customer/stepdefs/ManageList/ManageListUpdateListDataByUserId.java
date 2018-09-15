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

public class ManageListUpdateListDataByUserId {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with userId \"([^\"]*)\" and listGuid \"([^\"]*)\"$")
	  public void a_customer_with_userid_updates_list_date(String userid, String listguid) {
		  request = given().pathParam("listGuid", listguid).queryParam("userId", userid);
	  }

	  @When("^customer tries to update list data$")
	  public void customer_updates_list_data() {
	    String manageList = "{\"CustomerList\":{\"list\":[{\"name\":\"test\",\"defaultList\":true,\"onSaleNotify\":true,\"searchable\":true,\"showPurchaseInfo\":true}]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(manageList)
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .patch(getListByGuidEndpoint());
	    System.out.println("manage list response" + response.prettyPrint());

	  }
	  
	  @Then("^update list data status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	  		json = response.then().statusCode(stautsCode);
	  }

}
