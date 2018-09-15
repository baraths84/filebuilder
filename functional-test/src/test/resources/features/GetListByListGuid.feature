# language: en
@common
Feature: Customer get list by list guid service calls

  @newFlow
  Scenario: Get customer list by listGuid with expands=user,items
    Given a customer wants to retrieve list with expands=user,items and listguid "8c15d7d8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with expands response includes the following

  @newFlow
  Scenario: Get customer list by listGuid without expands
    Given a customer wants to retrieve list without expands with listguid "8c15d7d8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer retrieves the list by listguid without expands
    Then the get list by list guid status code is "200"
    And get list by list guid without expands response includes the following



  @newFlow
  Scenario: Get customer list by listGuid with sortBy=retailPrice sortOrder=asc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "retailPrice" and sortOrder "asc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=retailPrice sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with sortBy=retailPrice sortOrder=desc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "retailPrice" and sortOrder "desc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=retailPrice sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with sortBy=avgReviewRating sortOrder=asc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "avgReviewRating" and sortOrder "asc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=avgReviewRating sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with sortBy=avgReviewRating sortOrder=desc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "avgReviewRating" and sortOrder "desc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=avgReviewRating sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with sortBy=topPick sortOrder=asc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "topPick" and sortOrder "asc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=topPick sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with sortBy=topPick sortOrder=desc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "topPick" and sortOrder "desc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=topPick sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with sortBy=addedDate sortOrder=asc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "addedDate" and sortOrder "asc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=addedDate sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with sortBy=addedDate sortOrder=desc
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "addedDate" and sortOrder "desc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid with sortBy=addedDate sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer list by listGuid with invalid sortBy
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "invalid" and sortOrder "desc"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "400"
    And i verify error on the get list by list guid with invalid sortBy

  @newFlow
  Scenario: Get customer list by listGuid with invalid sortOrder
    Given a customer wants to retrieve list with listguid "8c15d7d8-111" and sortBy "addedDate" and sortOrder "invalid"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "400"
    And i verify error on the get list by list guid with invalid sortOrder



  @newFlow
  Scenario: Get customer list by listGuid and check links when userGuid is present in list
    Given a customer wants to retrieve list without expands with listguid "8c15d7d8-d71c-4fe8-8724-eca38ef4bf6f"
    When customer retrieves the list by listguid without expands
    Then the get list by list guid status code is "200"
    And get list by list guid response includes links when userGuid is present

  @newFlow
  Scenario: Get customer list by listGuid and check links when userGuid is absent in list
    Given a customer wants to retrieve list without expands with listguid "6f522f8c-09c7-4306-b9e1-f331cf2244e6"
    When customer retrieves the list by listguid without expands
    Then the get list by list guid status code is "200"
    And get list by list guid response includes links when userGuid is absent



  @newFlow
  Scenario: Get customer list by listGuid and check fcc data including final price
    Given a customer wants to retrieve list with expands=user,items and listguid "a78a1ed1-111"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "200"
    And get list by list guid response includes fcc data and final price


  @newFlow
  Scenario: Get customer list by listGuid error when no product in fcc
    Given a customer wants to retrieve list with expands=user,items and listguid "a78a1ed1-222"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "400"
    And get list by list guid response includes error when no product in fcc

  @TemporaryDisabled
  Scenario: Get customer list by listGuid error when no upc in fcc
    Given a customer wants to retrieve list with expands=user,items and listguid "a78a1ed1-333"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "500"
    And get list by list guid response includes error when no upc in fcc

  @newFlow
  Scenario: Get customer list by listGuid error when product is Master
    Given a customer wants to retrieve list with expands=user,items and listguid "a78a1ed1-444"
    When customer retrieves list by listguid
    Then the get list by list guid status code is "400"
    And get list by list guid response includes error in case of Master product
