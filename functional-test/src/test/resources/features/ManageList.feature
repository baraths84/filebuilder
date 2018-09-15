# language: en
@common
Feature: Manage List service calls

  @positive
  Scenario: To manage a list on a userid
    When customer manage a list by listguid "835d1c33-52e8-4d25-8e88-3f1875e51acb" and userId "201306333"
    Then the manage list status code is "204"

  @newFlow
  Scenario: Update list data by userId
    Given a customer with userId "201306456" and listGuid "8362659c-724e-40eb-a357-61b32ecfh48c"
    When customer tries to update list data
    Then update list data status code is "204"

  @newFlow
  Scenario: Update list data by userGuid
    Given a customer with userGuid "e2f748bc-a4d8-4b3a-aa8b-4e9d869fc6b5" and listGuid "8362659c-724e-40eb-a357-61b32ecfh49c"
    When customer tries to update list data by userGuid
    Then update list data by userGuid status code is "204"

  @newFlow
  Scenario: Manage list by userId missed in customer
    Given a customer with missed userid "201306457" and listguid "18c7557f-264c-490d-8e9e-956830d55691"
    When customer tries to manage the list by missed userid
    Then the manage list status code with missed userid is "204"



