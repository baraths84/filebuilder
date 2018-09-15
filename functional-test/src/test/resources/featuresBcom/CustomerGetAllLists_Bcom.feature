# language: en
@common
Feature: Service to get all lists of a customer for Bcom


  @bcomOnly
  Scenario: To check all lists are returned for userId
    Given a customer wants to retrieve bcom default list with userid "201426549"
    When customer retrieves all lists with promotions
    Then get all lists bcom the status code is "200"
    And get all list response includes the following for Bcom