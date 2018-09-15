package com.macys.selection.xapi.list.customer.stepdefs.ManageList;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getListByGuidEndpoint;
import static io.restassured.RestAssured.given;

public class ManageListUpdateListDataByUserGuid {
	  private Response response;
	  private ValidatableResponse json;
	  private RequestSpecification request;

	  @Given("^a customer with userGuid \"([^\"]*)\" and listGuid \"([^\"]*)\"$")
	  public void a_customer_with_userGuid_updates_list_date(String userGuid, String listguid) {
		  request = given().pathParam("listGuid", listguid).queryParam("userGuid", userGuid);
	  }

	  @When("^customer tries to update list data by userGuid$")
	  public void customer_updates_list_data_by_userGuid() {
	    String manageList = "{\"CustomerList\":{\"list\":[{\"name\":\"new test\",\"defaultList\":true,\"onSaleNotify\":false,\"searchable\":false,\"showPurchaseInfo\":false}]}}";
	    response = request
	            .contentType(MediaType.APPLICATION_JSON)
	            .when()
				.body(manageList)
	            .cookie("secure_user_token","13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
	            .patch(getListByGuidEndpoint());
	    System.out.println("manage list response" + response.prettyPrint());

	  }
	  
	  @Then("^update list data by userGuid status code is \"([^\"]*)\"$")
	  public void verify_status_code(int stautsCode) {
	  		json = response.then().statusCode(stautsCode);
	  }

}
