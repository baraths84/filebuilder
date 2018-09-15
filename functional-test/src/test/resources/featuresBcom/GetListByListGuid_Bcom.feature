# language: en
@common
Feature: Customer get list by list guid service calls for Bcom

  @bcomOnly
  Scenario: Get customer list by listGuid with filter=onSale,promotions
    Given a customer wants to retrieve list with filter=onSale,promotions and listguid "cbbe3d31-2c4c-4593-a501-fcdcf972f7ed"
    When customer retrieves list by listguid with filter options
    Then the get bcom list by list guid status code is "200"
    And get list by list guid with onSale,promotions filters response includes the following

  @bcomOnly
  Scenario: Get customer list by listGuid with filter=onSale
    Given a customer wants to retrieve list with filter=onSale and listguid "cbbe3d31-2c4c-4593-a501-fcdcf972f7ed"
    When customer retrieves list by listguid with filter options
    Then the get bcom list by list guid status code is "200"
    And get list by list guid with onSale filter response includes the following

  @bcomOnly
  Scenario: Get customer list by listGuid with filter=promotions
    Given a customer wants to retrieve list with filter=promotions and listguid "cbbe3d31-2c4c-4593-a501-fcdcf972f7ed"
    When customer retrieves list by listguid with filter options
    Then the get bcom list by list guid status code is "200"
    And get list by list guid with promotions filter response includes the following

  @bcomOnly
  Scenario: Get customer list by listGuid with filter=promotions,onSale
    Given a customer wants to retrieve list with filter=promotions,onSale and listguid "cbbe3d31-2c4c-4593-a501-fcdcf972f7ed"
    When customer retrieves list by listguid with filter options
    Then the get bcom list by list guid status code is "200"
    And get list by list guid with promotions,onSale filters response includes the following
