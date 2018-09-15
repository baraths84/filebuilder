# language: en
@common
Feature: Service to get all lists of a customer


  @positive
  Scenario: To check all lists for a given user id
    Given a customer userid "201336627" for all lists
    When customer retrieves the list by userid
    Then the status code is "200"
    And GET all list response includes the following

  @newFlow
  Scenario: To check all lists are returned for invalid userId
    Given a customer wants to retrieve default list with userid "111"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get all list response with invalid user includes the following

  @newFlow
  Scenario: To check no lists are returned for invalid userGuid
    Given a customer wants to retrieve default list with userguid "111"
    When customer retrieves all lists
    Then get all lists the status code is "200"

  @newFlow
  Scenario: To check get an list with both userId and userGuid
    Given a customer wants to retrieve all lists with userid "111" and userguid "111"
    When customer retrieves all lists
    Then get all lists the status code is "400"
    And error response with both userId and userGuid includes the following

  @newFlow
  Scenario: To check get an list without user info
    Given a customer wants to retrieve all lists without user info
    When customer retrieves all lists
    Then get all lists the status code is "400"
    And error response without user includes the following




  @newFlow
  Scenario: To check error on default=false, expand=items
    Given a customer wants to retrieve any list with default=false and expand=items
    When customer retrieves all lists
    Then get all lists the status code is "400"
    And error response with default=false and expand=items

  @newFlow
  Scenario: To check get a list without default parameter
    Given a customer wants to retrieve any list without default parameter and userid "20130222"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get all list response with default parameter by default includes the following



  @newFlow
  Scenario: To check lists sorting
    Given a customer wants to retrieve all non-default lists for userid "201306458"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get all list sorting is as following

  @newFlow
  Scenario: Get customer default list with sortBy=retailPrice sortOrder=asc
    Given a customer wants to retrieve default list with sortBy "retailPrice" and sortOrder "asc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=retailPrice sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer default list with sortBy=retailPrice sortOrder=desc
    Given a customer wants to retrieve default list with sortBy "retailPrice" and sortOrder "desc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=retailPrice sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer default list with sortBy=avgReviewRating sortOrder=asc
    Given a customer wants to retrieve default list with sortBy "avgReviewRating" and sortOrder "asc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=avgReviewRating sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer default list with sortBy=avgReviewRating sortOrder=desc
    Given a customer wants to retrieve default list with sortBy "avgReviewRating" and sortOrder "desc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=avgReviewRating sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer default list with sortBy=topPick sortOrder=asc
    Given a customer wants to retrieve default list with sortBy "topPick" and sortOrder "asc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=topPick sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer default list with sortBy=topPick sortOrder=desc
    Given a customer wants to retrieve default list with sortBy "topPick" and sortOrder "desc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=topPick sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer default list with sortBy=addedDate sortOrder=asc
    Given a customer wants to retrieve default list with sortBy "addedDate" and sortOrder "asc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=addedDate sortOrder=asc response includes the following

  @newFlow
  Scenario: Get customer default list with sortBy=addedDate sortOrder=desc
    Given a customer wants to retrieve default list with sortBy "addedDate" and sortOrder "desc"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list with sortBy=addedDate sortOrder=desc response includes the following

  @newFlow
  Scenario: Get customer default list with invalid sortBy
    Given a customer wants to retrieve default list with sortBy "invalid" and sortOrder "asc"
    When customer retrieves all lists
    Then get all lists the status code is "400"
    And i verify error on the get default list with invalid sortBy

  @newFlow
  Scenario: Get customer default list with invalid sortOrder
    Given a customer wants to retrieve default list with sortBy "addedDate" and sortOrder "invalid"
    When customer retrieves all lists
    Then get all lists the status code is "400"
    And i verify error on the get default list with invalid sortOrder



  @newFlow
  Scenario: To check get default list response includes links when userGuid is present
    Given a customer wants to retrieve default list with userguid "4b61ee86-c46e-4f62-b97a-f52a20b512e8"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list response includes links when userGuid is present

  @newFlow
  Scenario: To check get default list response includes links when userGuid is absent
    Given a customer wants to retrieve default list with userid "111"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list response includes links when userGuid is absent


  @newFlow
  Scenario: To check imageUrlsList is returned in list
    Given a customer wants to retrieve non-default list with limit > 0 with userid "201306153"
    When customer retrieves all lists
    Then get all lists the status code is "200"
    And get default list response includes imageUrlsList
