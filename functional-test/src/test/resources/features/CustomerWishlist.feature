# language: en
@common
Feature: Customer list service calls

  @positive
  Scenario: To check user id returns customer list - Scenario1
    Given a customer userid "201306168"
    When customer retrieves the list by userid
    Then the status code is "200"
    And response includes the following
    And response includes user info

  @positive
  Scenario: Find customer list by first and last name
    Given a customer with firstname "myfirstname" and lastname "mylastname"
    When customer tries to find the list by first and last name
    Then the status code is "200x"
    And find lists response includes the following

  @positive
  Scenario: To check user guid returns customer list with pricing policy
    Given a customer with listguid "7184c6f5-32c6-4e1f-b92f-098c9c07b8ba"
    When customer retrieves the list by listguid
    Then the status code is "200"
    And response includes the following info about pricing policy

  @positive
  Scenario: Get customer list by listGuid
    Given a customer with listguid "8c15d7d8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer retrieves the list by listguid
    Then the status code is "200"
    And get list response by listGuid includes the following

  @positive
  Scenario: Delete item from customer list
    Given a customer with listGuid "6f522f8c-09c7-4306-b9e1-f331cf2244e6" and itemGuid "f83e486c-15ad-4a28-94dd-4a9bc6af3639"
    When customer deletes item from list
    Then the status code is "204"
  
  @positive
  Scenario: Moving item to targeted listGuid by itemGuid and userid
    Given a customer with item to move to listguid "7dfa1ece-db69-4369-a222-8ecabc2ad0c9"
    When customer moves item to another list by userid
    Then the status code is "204"

    