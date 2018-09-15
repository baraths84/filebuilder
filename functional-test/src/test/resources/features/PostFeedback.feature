# language: en
@common
Feature: Service to post collaborators feedback

  @newFlow
  Scenario: Collaborator add like feedback
    Given collaborator with userGuid "99901-99901" post like feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator post like feedback
    Then post like feedback by collaborator status code is "204"

  @newFlow
  Scenario: Collaborator add unlike feedback
    Given collaborator with userGuid "99901-99901" post unlike feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator post unlike feedback
    Then post unlike feedback by collaborator status code is "204"

  @newFlow
  Scenario: Collaborator add dislike feedback
    Given collaborator with userGuid "99901-99901" post dislike feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator post dislike feedback
    Then post dislike feedback by collaborator status code is "204"

  @newFlow
  Scenario: Collaborator add undislike feedback
    Given collaborator with userGuid "99901-99901" post undislike feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator post undislike feedback
    Then post undislike feedback by collaborator status code is "204"

  @newFlow
  Scenario: Collaborator with incorrect userGuid add feedback
    Given collaborator with incorrect userGuid "99902-99902" post feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator with incorrect userGuid post feedback
    Then post feedback by invalid collaborator status code is "400"
    And response contains invalid userGuid error

  @newFlow
  Scenario: Collaborator with userGuid add incorrect feedback
    Given collaborator with userGuid "99901-99901" post incorrect feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator with userGuid post incorrect feedback
    Then post incorrect feedback by collaborator status code is "400"
    And response contains invalid feedback error

  @newFlow
  Scenario: Collaborator with userGuid add feedback to incorrect item
    Given collaborator with userGuid "99901-99901" post like feedback for incorrect itemGuid "7dfa1ece-db69-4369-a222-99901-10" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator with userGuid post like feedback to incorrect item
    Then post feedback to incorrect item status code is "400"
    And response contains invalid itemGuid error

  @newFlow
  Scenario: Collaborator add LIKE feedback
    Given collaborator with userGuid "99901-99901" post LIKE feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator post LIKE feedback
    Then post LIKE feedback by collaborator status code is "204"

  @newFlow
  Scenario: Collaborator without permissions add feedback
    Given collaborator without permissions with userGuid "99905-99905" post feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator without permissions post like feedback
    Then post like feedback by collaborator without permissions status code is "400"
    And response contains incorrect permissions error

  @newFlow
  Scenario: Collaborator add LIKE feedback
    Given collaborator with userGuid "99901-99901" post LIKE feedback for itemGuid "7dfa1ece-db69-4369-a222-99901-01" in the collaborative listGuid "dfa1ece-db69-4369-a222-99901"
    When collaborator post LIKE feedback
    Then post LIKE feedback by collaborator status code is "204"
