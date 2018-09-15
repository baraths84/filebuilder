# language: en
@common
Feature: Get Collaborative List by types service call

  @newFlow
  Scenario: To get collaborative list by userGuid who is owner and collaborator
    Given customer with userGuid "10001222-10001222-10001222" who is owner and collaborator
    When customer gets owner and collaborator list data
    Then get customer owner and collaborator list status code is "200"
    And response contains corresponding collaborator and owner list data

  @newFlow
  Scenario: To get collaborative list by userGuid who owns some collaborative list with several items with sorting
    Given customer with userGuid "10001012-10001012-10001012" who is owner and collaborator with several items
    When customer gets owner and collaborator list data with several items
    Then get customer owner and collaborator list  with several items status code is "200"
    And response contains corresponding collaborator and owner list data with recent items

  @newFlow
  Scenario: To get collaborative list by userGuid who owns some collaborative list with several collaborators with sorting
    Given customer with userGuid "10001013-10001013-10001013" who is owner and collaborator with several collaborators
    When customer gets owner and collaborator list data with several collaborators
    Then get customer owner and collaborator list  with several collaborators status code is "200"
    And response contains corresponding collaborator and owner list data with collaborators

  @newFlow
  Scenario: To get collaborative list by userGuid who didn't own any collaborative list and isn't a collaborator
    Given user with userGuid "100010014-100010014-100010014" who didn't own any collaborative list and isn't a collaborator
    When user gets owner and collaborative lists data
    Then get owner and collaborative blank lists data status code is "200"
    And response contains corresponding blank lists data

  @newFlow
  Scenario: To get collaborative list by userGuid who owns several lists and collaborator with several lists
    Given customer with userGuid "100010015-100010015-100010015" who is owner of several lists and collaborator in several lists
    When customer gets owner and collaborator lists data
    Then get collaborative and owner lists data status code is "200"
    And response contains corresponding owner and collaborator lists data

  @newFlow
  Scenario: To get collaborative list by userGuid who is collaborator of some list
    Given user with userGuid "100010016-100010016-100010016" who is collaborator of some list
    When user gets collaborator list data
    Then get collaborator list data status code is "200"
    And response contains corresponding collaborator list data

  @newFlow
  Scenario: To get collaborative list by userGuid who is collaborator of several lists
    Given customer with userGuid "100010017-100010017-100010017" who is collaborator of several lists
    When customer gets collaborator lists data
    Then get collaborator lists data status code is "200"
    And response contains corresponding collaborator lists data

  @newFlow
  Scenario: To get collaborative list by userGuid who is owner of several lists
    Given customer with userGuid "100010018-100010018-100010018" who is owner of several lists
    When customer gets owner lists data
    Then get owner lists data status code is "200"
    And response contains corresponding owner lists data

  @newFlow
  Scenario: To get only owner lists hideCollab=true
    Given user with userGuid "100010015-100010015-100010015" who has owners and collaborative lists and wants to see only owner lists
    When user gets only owner lists
    Then get owner lists only data by userGuid status code is "200"
    And response contains only owner lists data

  @newFlow
  Scenario: To get only collaborative lists hideOwner=true
    Given user with userGuid "100010015-100010015-100010015" who has collaborative and owners lists and wants to see only collaborative list
    When user gets only collaborative lists
    Then get collaborative lists data by userGuid status code is "200"
    And response contains collaborative lists data

  @newFlow
  Scenario: To get owner and collaborative lists hideOwner=false
    Given user with userGuid "100010015-100010015-100010015" who has collaborative and owners lists and wants to see both
    When user gets both collaborative and owner lists
    Then user gets his all owner and collaborative lists status code is "200"
    And response contains both collaborative and owner lists data for user





