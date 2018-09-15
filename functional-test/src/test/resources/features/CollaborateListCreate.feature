# language: en
@common
Feature: Create Collaborative List service calls

  @newFlow
  Scenario: To create collaborative list without items and collaborators
    Given list owner with userGuid
    When customer create a collaborative list
    Then the create collaborative list status code is "200"
    And response contains corresponding collaborative data

  @newFlow
  Scenario: To create collaborative list with items by upcId
    Given list owner with userGuid and list items with upcId
    When customer create a collaborative list with items
    Then the create collaborative list with items status code is "200"
    And response contains corresponding collaborative list data with items

  @TemporaryDisabled
  Scenario: To create collaborative list with collaborators without items
    Given list owner with userGuid and list of collaborators
    When customer create a collaborative list with collaborators
    Then the create collaborative list with collaborators status code is "200"
    And response contains corresponding data with collaborators

  @newFlow
  Scenario: To create collaborative list with items by productId
    Given list owner with userGuid and list items with productId
    When customer create a collaborative list with items by productId
    Then the create collaborative list with items by productId status code is "200"
    And response contains collaborative list data with items

  @newFlow
  Scenario: To create collaborative list with items by productId and upcId
    Given list owner with userGuid and list items with productId and upcId
    When customer create a collaborative list with items by productId and upcId
    Then the create collaborative list with items by productId and upcId status code is "200"
    And response contains collaborative list data with product and upc data

  @TemporaryDisabled
  Scenario: To create collaborative list with collaborators and items by productId
    Given list owner with collaborators and list items with productId
    When customer create a collaborative list with collaborators and items with productId
    Then the create collaborative list with items by productId and collaborators status code is "200"
    And response contains product data and collaborator data

  @TemporaryDisabled
  Scenario: To create collaborative list with collaborators and items by upcId
    Given list owner with collaborators and list items with upcId
    When customer create a collaborative list with collaborators and items with upcId
    Then the create collaborative list with items by upcId and collaborators status code is "200"
    And response contains upc data and collaborator data

  @TemporaryDisabled
  Scenario: To create collaborative list with more collaborators that are allowed
    Given list owner with userGuid and a lot of collaborators
    When customer create a collaborative list with a lot of collaborators
    Then the create collaborative list with a lot of collaborators status code is "400"
    And response contains error about exceeded collaborators

  @newFlow-in-progress
  Scenario: To create collaborative list with more items that are allowed
    Given list owner with userGuid and big list of items
    When customer create a collaborative list with a lot of items
    Then the create collaborative list with a lot of items status code is "400"
    And response contains error about exceeded items number

  @TemporaryDisabled
  Scenario: Create collaborative list with incorrect list type
    Given user want to create collaborative list with incorrect type
    When customer create a collaborative list with incorrect list type
    Then the create collaborative list with incorrect list type status code is "400"
    And collaborative response contains corresponding error

  @TemporaryDisabled
  Scenario: Create collaborate list with blank list type
     Given user create collaborative list with blank type
     When customer create a collaborative list with blank list type
     Then the create collaborative list with blank list type status code is "200"
     And response contains corresponding list data

  @newFlow
  Scenario: To create collaborative list with blank body
    Given list owner with userGuid and list with blank body
    When customer create a collaborative list with blank body
    Then the create collaborative list with blank body status code is "400"
    And response contains error about blank body

  @newFlow
  Scenario: To create collaborative list with blank list
    Given list owner with userGuid and list with blank list
    When customer create a collaborative list with blank list
    Then the create collaborative list with blank list status code is "400"
    And response contains error about blank list

