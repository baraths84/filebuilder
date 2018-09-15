# language: en
@common
Feature: Favorite Add Hearts service calls

  @positive
  Scenario: To add favorite items for a given userGuid
    Given a customer with userguid "71adefa2-183a-4a9e-aa26-a3b96c11db24"
    When customer add products to fav list by userid
    Then the fav status code is "200"
    And I verify the favorites response with guid


  @positive
  Scenario: To add favorite items without userGuid
    Given a customer without userguid
    When customer add products to fav list by not having userid
    Then the fav status code is "200"
    And I verify the favorites response without guid