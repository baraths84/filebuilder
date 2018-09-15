package com.macys.selection.xapi.list.customer.stepdefs.CollaborativeListsGetByTypes;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;

import javax.ws.rs.core.MediaType;

import static com.macys.selection.xapi.list.util.TestUtils.getCollaborativeListsByTypes;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


/**
 * Step definition file for get list by list guid.
 **/
public class GetOwnerAndCollaborativeSeveralLists {

    private Response response;
    private ValidatableResponse json;
    private RequestSpecification request;

    @Given("^customer with userGuid \"([^\"]*)\" who is owner of several lists and collaborator in several lists$")
    public void a_customer_with_userGuid_with_collaborative_and_owner_lists(String userGuid) {
        request = given().queryParam("userGuid", userGuid);
        System.out.println("Customer lists request" + request);
    }

    @When("^customer gets owner and collaborator lists data$")
    public void a_customer_retrieves_owner_and_colalborator_list() {
        response = request
                .contentType(MediaType.APPLICATION_JSON)
                .when()
                .get(getCollaborativeListsByTypes());
        System.out.println("Customer list response" + response.prettyPrint());
    }

    @Then("^get collaborative and owner lists data status code is \"([^\"]*)\"$")
    public void verify_status_code(int statusCode) {
        json = response.then().statusCode(statusCode);
    }

    @And("^response contains corresponding owner and collaborator lists data$")
    public void contains_owner_and_collaborator_several_lists_data() {
        json.body("listsPresentation.lists.listOwner[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o100010071"));
        json.body("listsPresentation.lists.listOwner[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listOwner[0].name", equalTo("Owner list 4"));
        json.body("listsPresentation.lists.listOwner[0].userGuid", equalTo("10001007-10001007-10001007"));
        json.body("listsPresentation.lists.listOwner[0].numberOfItems", equalTo(0));

        json.body("listsPresentation.lists.listOwner[1].userGuid", equalTo("10001007-10001007-10001007"));
        json.body("listsPresentation.lists.listOwner[1].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o100010072"));
        json.body("listsPresentation.lists.listOwner[1].listType", equalTo("C"));
        json.body("listsPresentation.lists.listOwner[1].numberOfItems", equalTo(0));

        json.body("listsPresentation.lists.listOwner[2].userGuid", equalTo("10001007-10001007-10001007"));
        json.body("listsPresentation.lists.listOwner[2].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o100010073"));
        json.body("listsPresentation.lists.listOwner[2].listType", equalTo("C"));
        json.body("listsPresentation.lists.listOwner[2].numberOfItems", equalTo(0));

        json.body("listsPresentation.lists.listOwner[3].userGuid", equalTo("10001007-10001007-10001007"));
        json.body("listsPresentation.lists.listOwner[3].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-o100010074"));
        json.body("listsPresentation.lists.listOwner[3].listType", equalTo("C"));
        json.body("listsPresentation.lists.listOwner[3].numberOfItems", equalTo(0));

        json.body("listsPresentation.lists.listCollaborator[0].userGuid", equalTo("10002008-10002008-10002008"));
        json.body("listsPresentation.lists.listCollaborator[0].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c100010091"));
        json.body("listsPresentation.lists.listCollaborator[0].listType", equalTo("C"));
        json.body("listsPresentation.lists.listCollaborator[0].name", equalTo("Collaborator List 1"));
        json.body("listsPresentation.lists.listCollaborator[0].numberOfItems", equalTo(2));

        json.body("listsPresentation.lists.listCollaborator[1].userGuid", equalTo("10002008-10002008-10002008"));
        json.body("listsPresentation.lists.listCollaborator[1].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c100010092"));
        json.body("listsPresentation.lists.listCollaborator[1].listType", equalTo("C"));
        json.body("listsPresentation.lists.listCollaborator[1].name", equalTo("Collaborator List 2"));

        json.body("listsPresentation.lists.listCollaborator[2].userGuid", equalTo("10002009-10002009-10002009"));
        json.body("listsPresentation.lists.listCollaborator[2].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c100010093"));
        json.body("listsPresentation.lists.listCollaborator[2].listType", equalTo("C"));
        json.body("listsPresentation.lists.listCollaborator[2].name", equalTo("Collaborator List 3"));

        json.body("listsPresentation.lists.listCollaborator[3].userGuid", equalTo("10002009-10002009-10002009"));
        json.body("listsPresentation.lists.listCollaborator[3].listGuid", equalTo("d965e5ff-cc88-4fa9-8226-c100010094"));
        json.body("listsPresentation.lists.listCollaborator[3].listType", equalTo("C"));
        json.body("listsPresentation.lists.listCollaborator[3].name", equalTo("Collaborator List 4"));
    }

}
