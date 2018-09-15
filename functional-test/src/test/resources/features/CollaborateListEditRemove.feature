# language: en
@common
Feature: Edit and Delete collaborative list

  @newFlow
  Scenario: To edit collaborative list name
    Given collaborative list with listGuid "7184c6f5-32c6-4e1f-b92f-90002001" and userGuid "90001001-90001001-90001001"
    When customer edit collaborative list name
    Then edit collaborative list name status code is "204"

  @newFlow
  Scenario: To edit collaborative list by incorrect listGuid
    Given collaborative list with incorrect listGuid "7184c6f5-32c6-4e1f-b92f-90002222" and userGuid "90001001-90001001-90001001"
    When customer edit collaborative list by incorrect listGuid
    Then edit collaborative list by incorrect listGuid status code is "400"
    And response contains error

  @TemporaryDisabled
  Scenario: Delete collaborative list by listGuid and userId with userGuid
    Given a collaborative list with listGuid "f338675e-c359-4de9-b083-9000100108" and userId "90001003"
    When customer tries to delete collaborative list by listGuid and userId
    Then the delete collaborative list status code with user is "204"

  @newFlow
  Scenario: Delete collaborative list by listGuid and userGuid
     Given a customer with collaborative listGuid "f338675e-c359-4de9-b083-9000100108" and userGuid "90001003-90001003-90001003"
     When customer tries to delete collaborative list by listGuid and userGuid
     Then the delete collaborative list by userGuid status code with userGuid is "204"
     
  @newFlow
  Scenario: Delete collaborative list by incorrect listGuid
    Given a collaborative list with incorrect listGuid "f338675e-c359-4de9-b083-9000100900" and userGuid "90001003-90001003-90001003"
    When customer tries to delete collaborative list by incorrect listGuid and userGuid
    Then the delete collaborative list by incorrect listGuid status code with user is "400"
    And response contains remove collaborative list by incorrect listGuid error

  @TemporaryDisabled
  Scenario: Delete collaborative list by guest
    Given a collaborative list with listGuid "f338675e-c359-4de9-b083-9000100120" and guest userId "81000105"
    When guest user tries to delete collaborative list
    Then the delete collaborative list by guest user status code with user is "400"
    And response contains remove collaborative list by guest user error

  @newFlow
  Scenario: Delete collaborative list by blank listGuid
    Given a collaborative list with blank listguid " " and userGuid "90001003-90001003-90001003"
    When customer tries to delete collaborative list by blank listguid
    Then the delete collaborative list status code by blank listGuid is "400"
    And response contains remove collaborative list by blank listGuid error

  @TemporaryDisabled
  Scenario: Delete collaborative list by userGuid and userGuid
    Given a customer with collaborative listguid "f338675e-c359-4de9-b083-900010010" userid "90001003" and userGuid "90001003-90001003-90001003"
    When customer tries to delete collaborative list by userId and userGuid
    Then the delete collaborative list status code by userId and userGuid is "400"
    And response includes the corresponding error

 