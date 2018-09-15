# language: en
@common
Feature: Delete and update item service calls

  @newFlow
  Scenario: Delete item with invalid user succeeds
    Given a customer to delete/update item with listGuid "6f522f8c-09c7-4306-b9e1-111" and itemGuid "f83e486c-15ad-4a28-94dd-111"
    When customer deletes item with invalid user
    Then the delete/update item status code is "204"

  @newFlow
  Scenario: Update item priority with userId
    Given a customer to delete/update item with listGuid "6f522f8c-09c7-4306-b9e1-f331cf2244e6" and itemGuid "f83e486c-15ad-4a28-94dd-4a9bc6af3639" and userId "201306153"
    When customer updates item priority
    Then the delete/update item status code is "200"

  @newFlow
  Scenario: Update item priority with userGuid
    Given a customer to delete/update item with listGuid "6f522f8c-09c7-4306-b9e1-f331cf2244e6" and itemGuid "f83e486c-15ad-4a28-94dd-4a9bc6af3639" and userGuid "4b61ee86-c46e-4f62-b97a-f52a20b512e8"
    When customer updates item priority
    Then the delete/update item status code is "200"

  @newFlow
  Scenario: Update item priority without userId and userGuid
    Given a customer to delete/update item with listGuid "6f522f8c-09c7-4306-b9e1-f331cf2244e6" and itemGuid "f83e486c-15ad-4a28-94dd-4a9bc6af3639"
    When customer updates item priority
    Then the delete/update item status code is "400"
    And i verify the update item without userId and userGuid

  @newFlow
  Scenario: Update item priority with both userId and userGuid
    Given a customer to delete/update item with listGuid "6f522f8c-09c7-4306-b9e1-f331cf2244e6" and itemGuid "f83e486c-15ad-4a28-94dd-4a9bc6af3639" and userId "201306153" and userGuid "4b61ee86-c46e-4f62-b97a-f52a20b512e8"
    When customer updates item priority
    Then the delete/update item status code is "400"
    And i verify the update item with both userId and userGuid

  @newFlow
  Scenario: Update item priority with invalid userId
    Given a customer to delete/update item with listGuid "6f522f8c-09c7-4306-b9e1-f331cf2244e6" and itemGuid "f83e486c-15ad-4a28-94dd-4a9bc6af3639" and userId "111"
    When customer updates item priority
    Then the delete/update item status code is "200"
