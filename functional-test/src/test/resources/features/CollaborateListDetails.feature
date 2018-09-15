# language: en
@common
Feature: Get collaborative list details


  @newFlow
  Scenario: Get list details with items and collaborators
    Given owner with listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001001" and userGuid "10001001-10001001-10001001" with items and collaborators
    When owner gets list details with items and collaborator
    Then get collaborative list details with items and collaborator status code is "200"
    And response contains list with items and collaborators details

  @newFlow
  Scenario: Get list details without items by collaborator
    Given collaborator with listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001002" and userGuid "10001002-10001002-10001002" without items and with collaborators
    When collaborator gets list details without items and with collaborators
    Then get collaborative list details without items and with collaborators status code is "200"
    And response contains list without items and with collaborators details

  @newFlow
  Scenario: Get list details with items without collaborators
    Given owner with listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001003" and userGuid "10001001-10001001-10001001" with items and without collaborators
    When owner gets list details with items and without collaborators
    Then get collaborative list details with items and without collaborators status code is "200"
    And response contains list with items and without collaborators details

 @newFlow
  Scenario: Get list details without items without collaborators
    Given owner with listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001004" and userGuid "10001001-10001001-10001001" without items and without collaborators
    When owner gets list details without items and without collaborators
    Then get collaborative list details without items and without collaborators status code is "200"
    And response contains list without items and without collaborators details

  @newFlow
  Scenario: To get list details by incorrect listGuid
    Given owner with incorrect listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001005" and userGuid "10001001-10001001-10001001" without items and without collaborators
    When owner gets list details by incorrect listGuid
    Then get collaborative list details by incorrect listGuid status code is "400"
    And response contains incorrect listGuid list details error

  @newFlow
  Scenario: To get list details by incorrect userGuid
    Given owner with listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001001" and incorrect userGuid "10008403-10008403-10008403" without items and without collaborators
    When owner gets list details by incorrect userGuid
    Then get collaborative list details by incorrect userGuid status code is "400"
    And response contains incorrect userGuid list details error


  @newFlow-in-progress
  Scenario: Get list details with items without productId
    Given owner with listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001007" and userGuid "10001001-10001001-10001001" without items without productId
    When owner gets list details with items without productId
    Then get collaborative list details with items without productId status code is "400"
    And response contains list details with items without productId details

  @newFlow
  Scenario: Get list details with collaborators without profile data
    Given owner with listGuid "hfs9df9-asd9v0-c359-4de9-b083-987-10001008" without collaborators profile data and userGuid "10001222-10001222-10001222"
    When owner gets list details without collaborators profile data
    Then get collaborative list details without collaborators profile data status code is "200"
    And response contains list details without collaborators profile data