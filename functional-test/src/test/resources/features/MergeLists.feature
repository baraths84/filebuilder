# language: en
@common
Feature: Merge Lists of signed user with guest user service calls

  @positive
  Scenario: Merge Lists
    Given a customer logs in and merge initiated
    When lists are merged successfully
    Then the merge status code is "200"

  @newFlow
  Scenario: Merge Lists With Error
    Given a customer logs in and merge initiated
    When lists are merged with error from customer
    Then the merge status code is "400"
    And the response contains merge error from customer

  @newFlow
  Scenario: Merge list of guest users
    Given guest user with list and another guest user with list
    When list of two guest users are merged
    Then merge lists of guest users status code is "200"

  @newFlow
  Scenario: Merge list of user without first name
    Given customer without first name and guest user
    When list of customer without first name and guest list are merged
    Then merge lists of customer without first name status code is "400"
    And the response contains error message

  @newFlow
  Scenario: Merge list of user without userGuid
    Given customer without userGuid and guest user
    When list of customer without userGuid and guest list are merged
    Then merge lists of customer without userGuid status code is "400"
    And the response of merge with customer without userGuid contains error message

  @newFlow
  Scenario: Merge lists with blank body
    Given customer with list and guest user
    When list of customer and guest list are merged without body
    Then merge lists of customer without body status code is "400"
    And the response of merge lists without body contains error message

  @newFlow
  Scenario: Merge lists when userId is blank
    Given customer with blank userID and guest user
    When list of customer with blank userId and guest list are merged
    Then merge lists of customer with blank userId status code is "400"
    And the response of merge without userId contains error message

  @newFlow
  Scenario: Merge lists when guestUserId is blank
    Given customer with blank guestUserId and user
    When list of customer with blank guestUserId and users list are merged
    Then merge lists of customer with blank guestUserId status code is "400"
    And the response of merge with blank guestUserId contains error message

  @newFlow
  Scenario: Merge list of guest and signed user
    Given guest user with list and signed user with list
    When list of guest user and signed user are merged
    Then merge lists of guest user and signed user status code is "200"

  @newFlow
  Scenario: Merge list of signed user and guest user
    Given signed user with list and guest user with list
    When list of signed user and guest user are merged
    Then merge lists of signed user and guest user status code is "200"
