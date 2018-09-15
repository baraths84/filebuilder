# language: en
@common
Feature: Manage List service calls with negative scenarios

  @newFlow
  Scenario: To manage a list by userId and userGuid
    Given a customer with listGuid "71adefa2-183a-4a9e-aa26-a3b96c149hn7" with userId "201306456" and userGuid "02df7786-8e87-46cf-8954-fbe13ef8c50a"
    When a customer tries to manage list by userId and userGuid
    Then the manage list by userId and userGuid status code is "400"
    And manage list by userId and userGuid causes error

  @newFlow
  Scenario: To manage list by guest user
    Given a customer with listGuid "71adefa2-183a-4a9e-aa26-a3b96c149hn7" and userId "201306458"
    When guest user tries to manage list with guestUser true
    Then manage list by guest status code is "400"
    And manage list by guest causes error

  @newFlow
  Scenario: Manage list with updating with defaultList false
    Given a customer with userId "201306456" and with listGuid "8362659c-724e-40eb-a357-61b32ecfh59c"
    When customer tries to update defaultList with false
    Then update list with defaultList false status code is "400"
    And update list with defaultList false causes error

  @newFlow
  Scenario: Manage list by invalid listGuid
    Given a customer with invalid listGuid "18c7557f-264c-490d-8e9e-956830d49284" and userId "201306456"
    When customer tries to manage the list by invalid listGuid
    Then the manage list by invalid listGuid status code is "400"
    And manage list by invalid listGuid contains error

  @newFlow
  Scenario: Manage list by listGuid of different customer
    Given a customer with listGuid of different customer "18c7557f-264c-490d-8e9e-95683093k382" and userId "201306456"
    When customer tries to manage the list of different customer
    Then the manage list of different customer status code is "400"
    And manage list of different customer contains error

  @newFlow
  Scenario: Manage list with blank body
    Given a customer with listGuid "18c7557f-264c-490d-8e9e-95683093k3948" and with userId "201306456"
    When customer tries to manage the list with blank body
    Then the manage list with blank body status code is "400"
    And manage list with blank body contains error

  @newFlow
  Scenario: Manage list with blank list in the body
    Given a customer with the listGuid "18c7557f-264c-490d-8e9e-95683093k3493" and userId "201306456"
    When customer tries to manage the list with blank list in the body
    Then the manage list with blank list in the body status code is "400"
    And manage list with blank list in the body contains error