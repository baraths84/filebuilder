# language: en
@common
Feature: Service to test deleting list negative scenarios

  @positive
  Scenario: Delete customer list by listGuid and userid when user is null
    Given a customer with null user and listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
    When customer tries to delete the list by listguid and null user
    Then the delete status code is "400"

  @newFlow
  Scenario: Delete customer list by blank listGuid
    Given a customer with blank listguid " "
    When customer tries to delete the list by blank listguid
    Then the delete status code by blank listGuid is "400"

  @newFlow
  Scenario: Delete customer list by userid and userGuid
    Given a customer with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24" userid "201306456" and userGuid "02df7786-8e87-46cf-8954-fbe13ef8c50a"
    When customer tries to delete the list by userId and userGuid
    Then the delete status code by userId and userGuid is "400"
    And response includes the following error

  @newFlow
  Scenario: Delete customer list with guest user
    Given a customer with with listGuid "71adefa2-183a-4a9e-aa26-a3b96c149hn7" and guest userId "201306458"
    When guest user tries to delete list
    Then delete list by guest status code is "400"
    And delete list by guest causes error