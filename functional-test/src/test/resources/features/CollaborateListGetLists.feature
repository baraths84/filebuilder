# language: en
@common
Feature: Get Collaborative List service call

  @newFlow
  Scenario: To get lists by userGuid who is owner, collaborator, and have wish-list with items
    Given customer with userGuid "10001001-10001001-10001001" which has all types of lists with items
    When customer gets all type of lists
    Then get customer all type of lists status code is "200"
    And response contains corresponding data for most recent owner list and wish-list

  @newFlow
  Scenario: To get lists by userGuid who owns some collaborative list with one item with one collaborator
    Given customer with userGuid "10001002-10001002-10001002" which is collaborative list owner
    When customer collaborative list where he is owner
    Then get collaborative list as owner status code is "200"
    And response contains data of owner list with item and collaborator

  @newFlow
  Scenario: To get lists by userGuid who owns some collaborative list with several items with upcId
    Given customer with userGuid "10001003-10001003-10001003" which is collaborative list owner with several items
    When customer collaborative list with several items where he is owner
    Then get collaborative list with several items as owner status code is "200"
    And response contains data of first 3 items for owner

  @newFlow
  Scenario: To get collaborative list by userGuid with several items with productId
    Given customer with userGuid "10001010-10001010-10001010" which is collaborator of list with several items by productId
    When customer gets collaborative list with several items with productId
    Then get collaborative list with several items with productId status code is "200"
    And response contains data of first 3 items with productId for collaborator

  @newFlow
  Scenario: To get lists by userGuid who owns some collaborative list with several collaborators with sorting
    Given customer with userGuid "10001004-10001004-10001004" which is collaborative list owner with several collaborators
    When customer collaborative list with several collaborators where he is owner
    Then get collaborative list with first two collaborators status code is "200"
    And response contains corresponding data of collaborators

  @newFlow
  Scenario: To get lists by userGuid who who doesn't have any lists
    Given customer with userGuid "10001005-10001005-10001005" who doesn't have any lists
    When customer gets collaborative blank response
    Then get collaborative blank response status code is "200"
    And response contains blank response list for owner

  @newFlow
  Scenario: To get lists by userGuid who owns several lists and receive the most recent
    Given customer with userGuid "10001007-10001007-10001007" who owns several lists
    When customer receives data by several lists as owner
    Then get lists data for customer who owns several lists status code is "200"
    And response contains data of recent owner list and with-list

  @newFlow
  Scenario: To get lists by userGuid who is collaborator of some list with several collaborators
    Given customer with userGuid "10001008-10001008-10001008" who is collaborator of some list
    When customer receives data for some list as collaborator
    Then get lists data for customer who is collaborator of some list status code is "200"
    And response contains data of collaborative list with several collaborators

  @newFlow
  Scenario: To get lists by userGuid who is collaborator of several lists and get the most recent
    Given customer with userGuid "10001009-10001009-10001009" who is collaborator of several list
    When customer receives data for most recent list as collaborator
    Then get most recent data as collaborator status code is "200"
    And response contains data of most recent list for collaborator and wish-list

  @newFlow
  Scenario: To get lists by incorrect userGuid
    Given customer with incorrect userGuid "10008403-10008403-10008403" trying to get lists data
    When customer with incorrect userGuid trying to receive data about lists
    Then get lists data by incorrect userGuid status code is "400"
    And response contains incorrect userGuid for lists error message





