# language: en
@common
Feature: Service to test deleting list

  @positive
  Scenario: Delete customer list by listGuid and userid without userGuid
    Given a customer with listguid "18c7557f-264c-490d-8e9e-956830d55622" and userid "201306333"
    When customer tries to delete the list by listguid and userid
    Then the delete status code with user is "204"

  @newFlow
  Scenario: Delete customer list by listGuid and userid with userGuid
     Given a customer with listguid "18c7557f-264c-490d-8e9e-956830d55622" and userid "201306456" with userGuid
     When customer tries to delete the list by listguid and userid with userGuid
     Then the delete status code with userid with userGuid is "204"

  @newFlow
  Scenario: Delete customer list by listGuid and userGuid with userid
     Given a customer with listguid "18c7557f-264c-490d-8e9e-956830d55633" and userGuid "02df7786-8e87-46cf-8954-fbe13ef8c50a"
     When customer tries to delete the list by listguid and userGuid
     Then the delete status code with userGuid is "204"

  @newFlow
  Scenario: Delete customer list by userId missed in customer
     Given a customer with listguid "18c7557f-264c-490d-8e9e-956830d55633" and missed userid "201306457"
     When customer tries to delete the list by missed userid
     Then the delete status code with missed userid is "204"



