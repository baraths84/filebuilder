# language: en
@common
Feature: Service to add item to the default customer list

  @newFlow
  Scenario: To add item to the customer any list
    Given a customer adds item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
    When customer adds an item to the existing list
    Then the add to existing list status code is "200"
    And i verify the add item to existing list response

  @newFlow
    Scenario: To add item to the customer any list with both upc and product
      Given a customer adds item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
      When customer adds an item to the existing list with upc and product
      Then the add to existing list status code is "400"
      And i verify error response on both upc and product

  @newFlow
    Scenario: To add item to the customer any list by user guid
        Given a customer adds item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
        When customer adds an item to the existing list by user guid
        Then the add to existing list status code is "200"
        And i verify the add item to existing list response for user guid

  @newFlow
    Scenario: To add item to the customer any list with userId in query and json (userId in query is ignored)
      Given a customer adds item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24" and userId "123"
      When customer adds an item to the existing list
      Then the add to existing list status code is "200"
      And i verify the add item to existing list response

  @newFlow
    Scenario: To add item to the customer any list with userGuid in query and userId in json (userGuid in query is ignored)
      Given a customer adds item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24" and userGuid "123"
      When customer adds an item to the existing list
      Then the add to existing list status code is "200"
      And i verify the add item to existing list response

  @newFlow
    Scenario: To add item to the customer any list with userId in query no user in json
      Given a customer adds item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24" and userId "201336579"
      When customer adds an item to the existing list without user in json
      Then the add to existing list status code is "200"
      And i verify the add item to existing list response

  @newFlow
    Scenario: To add item to the customer any list with userId in query no user in json
      Given a customer adds item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24" and userGuid "4b61ee86-c46e-4f62-b97a-f52a20b512e8"
      When customer adds an item to the existing list without user in json
      Then the add to existing list status code is "200"
      And i verify the add item to existing list response for user guid
