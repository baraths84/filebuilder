# language: en
@common
Feature: Move item from one list to another

  @newFlow
  Scenario: Moving item to other list without items
    Given a customer to move item to listguid "7dfa1ece-db69-4369-a222-8ecabc2ad0c9"
    When customer moves item to another list by userid without items
    Then the moveItem status code is "400"
    And i verify the move item error when no items provided

  @newFlow
  Scenario: Moving item to other list when both userId and userGuid provided
    Given a customer to move item to listguid "7dfa1ece-db69-4369-a222-8ecabc2ad0c9"
    When customer moves item to another list with both userId and userGuid
    Then the moveItem status code is "400"
    And i verify the move item error with both userId and userGuid

  @newFlow
  Scenario: Moving item to other list with userGuid
    Given a customer to move item to listguid "7dfa1ece-db69-4369-a222-8ecabc2ad0c9"
    When customer moves item to another list with userGuid
    Then the moveItem status code is "204"

