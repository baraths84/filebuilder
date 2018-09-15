# language: en
@common
Feature: Service to get all my requests and waiting approvals

  @newFlow
  Scenario: Get my requests for the several lists
    Given list owner with userGuid "99901-99901" with some requests for several lists
    When owner gets list of requests for several lists
    Then get requests for several lists status code is "200"
    And response contains requests for several lists data

  @newFlow
  Scenario: Get my requests with several statuses
    Given list owner with userGuid "10001001-10001001-10001001" with some requests with different statuses
    When owner gets list of requests with different statuses
    Then get requests with different statuses status code is "200"
    And response contains requests with different statuses data

  @newFlow
  Scenario: Get my requests for the one list
    Given list owner with userGuid "90001002-90001002-90001002" with some requests for one list
    When owner gets list of requests for one list
    Then get requests for one list status code is "200"
    And response contains requests for one list data

  @newFlow
  Scenario: Get my requests for user who doesn't have any requests
    Given list owner with userGuid "90001003-90001003-90001003" without any requests
    When owner gets blank list of request
    Then get requests for user without requests status code is "200"
    And response contains blank requests list data

  @newFlow
  Scenario: Get my requests for user by incorrect userGuid
    Given list owner with incorrect userGuid "99902-99902" without any requests
    When owner gets list of request by incorrect userGuid
    Then get requests for user with incorrect userGuid status code is "400"
    And response contains requests by incorrect userGuid error

  @newFlow
  Scenario: Get my requests for user by blank userGuid
    Given list owner with blank userGuid "" without any requests
    When owner gets list of request by blank userGuid
    Then get requests for user with blank userGuid status code is "400"
    And response contains requests by blank userGuid error

  @newFlow
  Scenario: Get my requests for user with offset and limit
    Given list owner with userGuid "99901-99901" with some requests and offset = 0 and limit = 100
    When owner gets full list of requests for several lists
    Then get requests for several lists with offset and limit status code is "200"
    And response contains all requests for several lists data

  @newFlow
  Scenario: Get my requests for user with incorrect offset
    Given list owner with userGuid "99901-99901" with some requests with incorrect offset
    When owner gets full list of requests with incorrect offset
    Then get requests for several lists with incorrect offset status code is "400"
    And response contains incorrect offset error

  @newFlow
  Scenario: Get my requests for user with incorrect limit
    Given list owner with userGuid "99901-99901" with some requests with incorrect limit
    When owner gets full list of requests with incorrect limit
    Then get requests for several lists with incorrect limit status code is "400"
    And response contains incorrect limit error

  @newFlow
  Scenario: Get my approvals for the several lists
    Given list owner with userGuid "99901-99901" with some approvals for several lists
    When owner gets list of approvals for several lists
    Then get approvals for several lists status code is "200"
    And response contains approvals for several lists data

  @newFlow
  Scenario: Get my approvals for one list
    Given list owner with userGuid "90001002-90001002" with some approvals for one list
    When owner gets list of approvals for one list
    Then get approvals for one list status code is "200"
    And response contains approvals for one list data

  @newFlow
  Scenario: Get my approvals for user who doesn't have any requests
    Given list owner with userGuid "90001003-90001003-90001003" without any approvals
    When owner gets blank list of approvals
    Then get requests for user without approvals status code is "200"
    And response contains blank approvals list data

  @newFlow
  Scenario: Get my approvals by incorrect userGuid
    Given list owner with incorrect userGuid "99902-99902"
    When owner gets list of approvals by incorrect userGuid
    Then get approvals for user by incorrect userGuid status code is "400"
    And response contains approvals incorrect userGuid error

  @newFlow
  Scenario: Get my approvals for user by blank userGuid
    Given list owner with blank userGuid "" without any approvals
    When owner gets list of approvals by blank userGuid
    Then get approvals for user with blank userGuid status code is "400"
    And response contains approvals by blank userGuid error

  @newFlow
  Scenario: Get my approvals for user with offset and limit
    Given list owner with userGuid "99901-99901" with some approvals and offset = 0 and limit = 100
    When owner gets full list of approvals for several lists
    Then get approvals for several lists with offset and limit status code is "200"
    And response contains all approvals for several lists data

  @newFlow
  Scenario: Get my approvals for user with incorrect offset
    Given list owner with userGuid "99901-99901" with some approvals with incorrect offset
    When owner gets full list of approvals with incorrect offset
    Then get approvals for several lists with incorrect offset status code is "400"
    And response contains approvals incorrect offset error

  @newFlow
  Scenario: Get my approvals for user with incorrect limit
    Given list owner with userGuid "99901-99901" with some approvals with incorrect limit
    When owner gets full list of approvals with incorrect limit
    Then get approvals for several lists with incorrect limit status code is "400"
    And response contains approvals incorrect limit error