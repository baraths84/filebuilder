# language: en
@common
Feature: Create List service calls

  @positive
  Scenario: To create a list on a userid
    Given a customer with userid
    When customer create a list
    Then the create list status code is "200"
    And I verify the create list response with guid
    