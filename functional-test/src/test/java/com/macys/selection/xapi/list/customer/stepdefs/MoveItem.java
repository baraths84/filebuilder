package com.macys.selection.xapi.list.customer.stepdefs;

import com.macys.selection.xapi.list.util.TestUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import java.io.IOException;

import static com.macys.selection.xapi.list.util.TestUtils.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Step definition file for moving item from one list to another.
 **/
public class MoveItem {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^a customer to move item to listguid \"([^\"]*)\"$")
    public void a_customer_with_item_to_move_to_listguid(String listGuid) {
        request = given().pathParam("listGuid", listGuid);
        System.out.println("Move to list request" + request);
    }

    @When("^customer moves item to another list by userid without items$")
    public void customer_moves_item_to_another_list_without_items() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/moveItemNoItems.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when().body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg")
                .put(getMoveItemByListGuidEndpoint());
        System.out.println("Move to list response" + response.prettyPrint());
    }

    @When("^customer moves item to another list with both userId and userGuid$")
    public void customer_moves_item_to_another_list_with_both_userId_userGuid() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/moveItemUserIdAndUserGuid.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when().body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg")
                .put(getMoveItemByListGuidEndpoint());
        System.out.println("Move to list response" + response.prettyPrint());
    }

    @When("^customer moves item to another list with userGuid$")
    public void customer_moves_item_to_another_list_with_userGuid() throws IOException {
        String body = TestUtils.readFile("com/macys/selection/xapi/list/customer/stepdefs/moveItemWithUserGuid.json");

        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when().body(body)
                .cookie("secure_user_token", "13_bRM7cT4dR1hDSZLmddSuNqzDQ5gqG4nRYA5i5lYsDJGI6LZwqatRga2Bww4V7VDoAr8Z09/sS6p0SgVJp9bLzg==")
                .put(getMoveItemByListGuidEndpoint());
        System.out.println("Move to list response" + response.prettyPrint());

        /*
        New flow:
        1) To MSP Customer:
        GET /api/customer/v1/profile/guestUser?userguid=4b61ee86-c46e-4f62-b97a-f52a20b512e8
        2) To MSP Wishlist
        POST /api/selection/list/v1/wishlists/7dfa1ece-db69-4369-a222-8ecabc2ad0c9/items/move?itemGuid=3577fbc5-3385-4bc1-866f-36078928bbcd&userId=41&userGuid=4b61ee86-c46e-4f62-b97a-f52a20b512e8&guestUser=false
         */
    }

    @Then("^the moveItem status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^i verify the move item error when no items provided$")
    public void error_response_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Bad Json from UI, at least one item is required!"));
    }

    @And("^i verify the move item error with both userId and userGuid$")
    public void error_response_incorrect_request_equal() {
        json.body("errors.error[0].errorCode", equalTo("50001"));
        json.body("errors.error[0].developerMessage", equalTo("Incorrect request."));
    }
}
