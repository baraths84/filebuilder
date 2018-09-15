# language: en
@common
Feature: Share email service call

  @positive
  Scenario: To share a list by email
    Given customer is given a list with listGuid "8515dzd8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer shares a list by email
    Then the sharing list by email status code is "204"

  @newFlow
  Scenario: To share a list by email
    Given customer is given a list with listGuid "8515dzd8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer shares a list by email
    Then the sharing list by email status code is "204"

  @newFlow
  Scenario: To share a list with NO items by email
    Given customer is given a list with listGuid "8515dzd8-111-4fe8-8724-eca38ef4bf6f"
    When customer shares a list by email
    Then the sharing list by email status code is "204"

  @newFlow
  Scenario: To share a list by email with invalid input (email to)
    Given customer is given a list with listGuid "8515dzd8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer shares a list with invalid input _to_ by email
    Then the sharing list by email status code is "400"
    And The error response contains an error message "Invalid to email address" and errorCode "50001"

  @newFlow
  Scenario: To share a list by email with invalid input (message)
    Given customer is given a list with listGuid "8515dzd8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer shares a list with invalid input _message_ by email
    Then the sharing list by email status code is "400"
    And The error response contains an error message "Invalid input" and errorCode "50001"