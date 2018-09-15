# language: en
@common
Feature: Service to get activity log

  @newFlow
  Scenario: Get activity log of like, unlike, dislike, undislike
    Given list owner with userGuid "99901-99901" wants to get activity log of list "dfa1ece-db69-4369-a222-99990" with like, unlike, dislike, undislike activities
    When owner gets like, unlike, dislike, undislike activities in activity log
    Then get like, unlike, dislike, undislike activity log status code is "200"
    And response contains like, unlike, dislike, undislike activities data

  @newFlow
  Scenario: Get activity log item added and item removed
    Given list owner with userGuid "99901-99901" wants to get activity log of list "dfa1ece-db69-4369-a222-99991" with item added and item removed activities
    When owner gets item added and item removed activities in activity log
    Then get item added and item removed activity log status code is "200"
    And response contains item added and item removed activities data

  @newFlow
  Scenario: Get activity log with activity for single item by several collaborators
    Given list owner with userGuid "99901-99901" wants to get activity log of list "dfa1ece-db69-4369-a222-99996" with activity from several collaborators about one item
    When owner gets activity log with one item and several collaborators
    Then get activity log with one item and several collaborators status code is "200"
    And response contains activity of several collaborators about one item data

  @newFlow
  Scenario: Get activity log of list without activities
    Given list owner with userGuid "99901-99901" wants to get blank activity log of list "dfa1ece-db69-4369-a222-99999"
    When owner gets blank activities in activity log
    Then get blank activity log status code is "200"
    And response contains blank activities data

  @newFlow
  Scenario: Get activity log of list by incorrect userGuid
    Given list owner with incorrect userGuid "99904-99904" wants to get activity log of list "dfa1ece-db69-4369-a222-99990"
    When user with incorrect userGuid gets activity log
    Then get activity log by incorrect userGuid status code is "400"
    And response contains incorrect userGuid error

  @newFlow
  Scenario: Get activity log by incorrect listGuid
    Given list owner with userGuid "99901-99901" wants to get activity log of incorrect list "dfa1ece-db69-4369-a222-99994"
    When owner gets activities by incorrect listGuid
    Then get activity log by incorrect listGuid status code is "400"
    And response contains incorrect listGuid error

  @newFlow
  Scenario: Get activity log with single activity
    Given list owner with userGuid "99901-99901" wants to get activity log of list "dfa1ece-db69-4369-a222-99995" with single activity
    When owner gets single activity in activity log
    Then get single activity log status code is "200"
    And response contains dislike activity data

  @newFlow
  Scenario: Get activity log of user without first and last name
    Given list owner with userGuid "99901-99901" wants to get activity log of list "dfa1ece-db69-4369-a222-99998" with collaborator without first name last name
    When owner gets single activity of list with collaborator without first name last name
    Then get single activity log with collaborator without first name last name status code is "200"
    And response contains corresponding data without first name last name