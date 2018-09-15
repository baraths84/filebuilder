# language: en
@common
Feature: Service to add/get/delete avatar image

  @newFlow
  Scenario: Create avatar image for user by userGuid
    Given a customer add avatar image by userGuid
    When customer add avatar image by userGuid
    Then create avatar image status code is "204"

  @newFlow
  Scenario: Create avatar image for user by incorrect userGuid
    Given a customer add avatar image by incorrect userGuid
    When customer add avatar image by incorrect userGuid
    Then create avatar image by incorrect incorrect status code is "400"
    And response contains add avatar by incorrect userGuid error

  @newFlow
  Scenario: Create avatar image for user by userId
    Given a customer add avatar image by userId
    When customer add avatar image by userId
    Then create avatar image by userId status code is "400"
    And response contains add avatar by missed userGuid error

  @newFlow
  Scenario: Create blank avatar image for user by userGuid
    Given a customer add blank avatar image by userGuid
    When customer add blank avatar image by userGuid
    Then create blank avatar image status code is "204"

  @newFlow
  Scenario: Create avatar image with blank body
    Given a customer add avatar image with blank body
    When customer add avatar image with blank body
    Then create avatar image with blank body status code is "400"
    And response contains blank avatar body error

  @newFlow
  Scenario: Get avatar image by userGuid
    Given a customer get avatar image by userGuid "10001001-10001001-10001001"
    When customer get avatar image by userGuid
    Then get avatar image by userGuid status code is "200"
    And response contains avatar image data

  @newFlow
  Scenario: Get avatar image by incorrect userGuid
    Given a customer get avatar image by incorrect userGuid "a78a1ed1-d7c0-4aae-80001000-3"
    When customer get avatar image by incorrect userGuid
    Then get avatar image by incorrect userGuid status code is "400"
    And response contains get avatar incorrect userGuid error

  @newFlow
  Scenario: Get blank avatar image
    Given a customer get blank avatar image by userGuid "10001002-10001002-10001002"
    When customer get blank avatar image by userGuid
    Then get blank avatar image by userGuid status code is "200"
    And response contains blank avatar image data

  @newFlow
  Scenario: Get null avatar response
    Given a customer get null avatar image response by userGuid "10001003-10001003-10001003"
    When customer get null avatar image response
    Then get null avatar image status code is "200"
    And response contains null avatar image data

  @newFlow
  Scenario: Delete user avatar by userGuid
    Given a customer delete avatar image by userGuid "10001001-10001001-10001001"
    When customer delete avatar image by userGuid
    Then delete avatar image by userGuid status code is "204"

  @newFlow
  Scenario: Delete user avatar by incorrect userGuid
    Given a customer delete avatar image by incorrect userGuid "a78a1ed1-d7c0-4aae-80001000-3"
    When customer delete avatar image by incorrect userGuid
    Then delete avatar image by incorrect userGuid status code is "400"
    And response contains delete avatar incorrect userGuid error

  @newFlow
  Scenario: Delete already removed user avatar
    Given a customer delete avatar image again by userGuid "10001003-10001003-10001003"
    When customer delete already removed avatar image
    Then delete already remove avatar image status code is "400"
    And response contains delete already remove avatar image error



