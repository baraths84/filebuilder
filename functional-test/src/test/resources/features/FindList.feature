# language: en
@common
Feature: Find list service calls

  @newFlow
  Scenario: Find customer list with first and last names and state
    Given a customer finds list with firstname "John" and lastname "White" and state "CA"
    When customer finds the list by first and last name
    Then the find list status code is "200"
    And find lists with state check lists order and random user returned


  @newFlow
  Scenario: Find customer list with invalid first and last name (that are missing in DB)
    Given a customer finds list with firstname "invalidFirstName" and lastname "invalidLastName"
    When customer finds the list by first and last name
    Then the find list status code is "200"

  @newFlow
  Scenario: Find customer list without first name
    Given a customer finds list without firstname and with lastname "Black"
    When customer finds the list by first and last name
    Then the find list status code is "400"
    And error response with missing firstname

  @newFlow
  Scenario: Find customer list without last name
    Given a customer finds list without lastname and with firstname "George"
    When customer finds the list by first and last name
    Then the find list status code is "400"
    And error response with missing lastname

  @newFlow
  Scenario: Find customer list with las name < 2 chars
    Given a customer finds list with firstname "George" and lastname "I"
    When customer finds the list by first and last name
    Then the find list status code is "400"
    And error response with lastname less than 2 chars




  @newFlow
  Scenario: Find customer list with fields error
    Given a customer finds list with fields
    When customer finds the list by first and last name
    Then the find list status code is "400"
    And error response incorrect request

  @newFlow
  Scenario: Find customer list with expand error
    Given a customer finds list with expand
    When customer finds the list by first and last name
    Then the find list status code is "400"
    And error response incorrect request

  @newFlow
  Scenario: Find customer list with userId error
    Given a customer finds list with userId
    When customer finds the list by first and last name
    Then the find list status code is "400"
    And error response incorrect request

  @newFlow
  Scenario: Find customer list with userGuid error
    Given a customer finds list with userGuid
    When customer finds the list by first and last name
    Then the find list status code is "400"
    And error response incorrect request


