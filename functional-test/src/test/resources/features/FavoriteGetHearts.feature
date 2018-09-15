# language: en
@common
Feature: Favorite get Hearts service calls

  @positive
  Scenario: To check get favorites having products
    Given a customer userguid "4b61ee86-c46e-4f62-b97a-f52a20b512e8"
    When customer retrieves the fav list by userid
    Then the fav status code is "200"
    And response includes the following productID

  @newFlow
  Scenario: Customer gets favorites list by userGuid
    Given a customer with userGuid "4e6cc696-b8c5-4caa-8691-f09f6f204b10"
    When customer receives list of favorites by userGuid
    Then status code of getting list by userGuid is "200"
    And get favorites by userGuid contains following data

  @newFlow
    Scenario: Customer gets blank favorites list
      Given customer with blank favorites list with userGuid "a78a1ed1-d7c0-4aae-80001000-2"
      When customer tries to receive blank favorites list
      Then status code of getting blank list is "200"
      And get blank favorites list by userGuid contains following data

  @newFlow
    Scenario: Customer gets favorites list by incorrect userGuid
      Given a customer with incorrect userGuid "a78a1ed1-d7c0-4aae-80001000-3"
      When customer receives list of favorites by incorrect userGuid
      Then status code of getting list by incorrect userGuid is "400"
      And get favorites by incorrect userGuid contains following error

