# language: en
@common
Feature: Get all collaborators from all lists

  @newFlow
  Scenario: To get all collaborators from all my lists
    Given owner with userGuid "10001374-10001374-10001374" who has several lists with collaborators
    When owner user gets all collaborators from several lists
    Then get collaborators from several lists status code is "200"
    And response contains data about collaborators from several lists

  @newFlow
  Scenario: To get all collaborators from one list
    Given owner with userGuid "100010018-100010018-100010018" who has one lists with collaborators
    When owner user gets collaborators from one list
    Then get collaborators from one list status code is "200"
    And response contains data about collaborators from one lists

  @newFlow
  Scenario: To get all collaborators from lists without collaborators
    Given owner with userGuid "100010017-100010017-100010017" who has several lists without collaborators
    When owner gets blank collaborators data from lists
    Then get blank collaborators data from lists status code is "200"
    And response contains blank collaborators data from several lists

  @newFlow
  Scenario: To get all collaborators from lists except one collaborator
    Given owner with userGuid "100010017-100010017-100010017" who has list with several collaborators
    When owner gets collaborators data except one from lists
    Then get collaborators data except one from lists status code is "200"
    And response contains collaborators data except one

  @newFlow
  Scenario: To get all collaborators from lists except several collaborators
    Given owner with userGuid "100010017-100010017-100010017" with list with several collaborators
    When owner gets collaborators data except several from lists
    Then get collaborators data except several from lists status code is "200"
    And response contains collaborators data except several

  @newFlow
  Scenario: To get all collaborators from lists except blank userGuid
    Given owner with userGuid "100010018-100010018-100010018" and blank except userGuid
    When owner gets collaborators data except blank userGuid from lists
    Then get collaborators data except blank userGuid from lists status code is "200"
    And response contains collaborators all data

  @newFlow
  Scenario: To get all collaborators from lists by incorrect userGuid
    Given owner user with incorrect userGuid "a78a1ed1-d7c0-4aae-80001000-3"
    When owner gets collaborators data by incorrect userGuid
    Then get collaborators data by incorrect userGuid status code is "400"
    And response contains incorrect owner userGuid error

  @newFlow
  Scenario: To get all collaborators without profile data from lists
    Given owner user with userGuid "100010016-100010016-100010016" and collaborators without profile data
    When owner gets collaborators without profile data by userGuid
    Then get collaborators without profile data by userGuid status code is "200"
    And response contains collaborators without profile data
