# language: en
@common
Feature: Service to add item to the default customer list

  @positive
  Scenario: To add item to the customer default list
    Given a customer adding item to default list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
    When customer adds an item to the default list
    Then the addlist status code is "200"
    And i verify the add item to default list response

  @newFlow
    Scenario: To add item to the customer default list with both upc and product
      Given a customer adding item to default list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
      When customer adds an item with both upc and product
      Then the addlist status code is "400"
      And i verify error response

  @newFlow
      Scenario: To add item to the customer default list without user
        Given a customer adding item to default list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
        When customer adds an item without user
        Then the addlist status code is "200"
        And i verify the add item to default list response after user created

  @newFlow
      Scenario: To add item to the customer default list with invalid user id
        Given a customer adding item to default list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
        When customer adds an item with invalid user id
        Then the addlist status code is "200"
        And i verify the add item to default list after invalid user passed and new user created

  @newFlow
      Scenario: To add item to the customer default list with invalid upc
        Given a customer adding item to default list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
        When customer adds an item with invalid upc
        Then the addlist status code is "400"
        And i verify error response with invalid upc

  @newFlow
        Scenario: To add item to the customer default list with product only info (without upc)
          Given a customer adding item to default list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
          When customer adds an item with product only
          Then the addlist status code is "200"
          And i verify the add item to default list response with product only including fcc data and final price