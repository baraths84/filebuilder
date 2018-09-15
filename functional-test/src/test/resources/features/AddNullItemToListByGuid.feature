# language: en
@common
Feature: Service to add null item to the default customer list

  @positive
  Scenario: To add item to the customer default list
    Given a customer adds null item to existing list with listguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
    When customer adds a null item to the existing list
    Then the add null item to existing list status code is "400"
    And i verify the add null item to existing list response
