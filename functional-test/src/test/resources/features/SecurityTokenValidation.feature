# language: en
@common
Feature: Validate security token

  @security
  Scenario: Moving item to other list with userGuid with a security token
    Given a customer to move item to listguid "7dfa1ece-db69-4369-a222-8ecabc2ad0c9"
    When customer moves item to another list with userGuid
    Then the moveItem status code is "204"

  @security
  Scenario: To manage a list on a userid with a security token
    When customer manage a list by listguid "835d1c33-52e8-4d25-8e88-3f1875e51acb" and userId "201306333"
    Then the manage list status code is "204"

  @security
  Scenario: Update list data by userId with a security token
    Given a customer with userId "201306456" and listGuid "8362659c-724e-40eb-a357-61b32ecfh48c"
    When customer tries to update list data
    Then update list data status code is "204"

  @security
  Scenario: To share a list with NO items by email with a security token
    Given customer is given a list with listGuid "8515dzd8-111-4fe8-8724-eca38ef4bf6f"
    When customer shares a list by email
    Then the sharing list by email status code is "204"

  @security
  Scenario: To create a list on a userid with a security token
    Given a customer with userid
    When customer create a list
    Then the create list status code is "200"
    And I verify the create list response with guid

  @security
  Scenario: Delete customer list by listGuid and userid with userGuid with a security token
    Given a customer with listguid "18c7557f-264c-490d-8e9e-956830d55622" and userid "201306456" with userGuid
    When customer tries to delete the list by listguid and userid with userGuid
    Then the delete status code with userid with userGuid is "204"

  @security
  Scenario: Merge list of guest and signed user with a security token
    Given guest user with list and signed user with list
    When list of guest user and signed user are merged
    Then merge lists of guest user and signed user status code is "200"

  @security
  Scenario: Update item priority with userId with a security token
    Given a customer to delete/update item with listGuid "6f522f8c-09c7-4306-b9e1-f331cf2244e6" and itemGuid "f83e486c-15ad-4a28-94dd-4a9bc6af3639" and userId "201306153"
    When customer updates item priority
    Then the delete/update item status code is "200"
