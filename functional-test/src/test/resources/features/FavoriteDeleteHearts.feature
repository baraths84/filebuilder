# language: en
@common
Feature: Favorite delete hearts service calls

  @positive
  Scenario: To delete favorite items from given list
    Given a customer with listguid "c3e6a1b9-092d-4ac5-8b09-2cbd9841e202" and productid "22805"
    When customer delete product from fav list for a product
    Then the fav status code is "204"


#  @wip
#  Scenario: To delete favorite items from given list with unavailable product
#    Given a customer with listguid "c3e6a1b9-092d-4ac5-8b09-2cbd9841e202" and productid "3309"
#    When customer delete product from fav list for a product
#    Then the fav status code is "400"

  @newFlow
  Scenario: Delete item from favorites by incorrect listGuid
    Given customer with incorrect listGuid "a78a1ed1-d7c0-4aae-81000100-1" and productId "81001"
    When customer tries to remove item from list by incorrect listGuid
    Then removing item from favorites by incorrect listGuid status code is "400"
    And removing item by incorrect listGuid response contains an error

  @newFlow
  Scenario: Delete item from favorites signed user by prodId with upcId
    Given customer with listGuid "a78a1ed1-d7c0-4aae-81000100-2" with productId "8100201"
    When customer tries to remove item from list by productId
    Then removing item from favorites by productIdd status code is "204"

  @newFlow
  Scenario: Delete item from favorites guest user by prodId with upcId
    Given guest user with listGuid "a78a1ed1-d7c0-4aae-81000100-3" with productId "81003"
 	When guest user tries to remove item from list by productId
 	Then guest user removes item from favorites by productIdd status code is "204"

  @newFlow
  Scenario: Delete item from favorites by upcId without productId
    Given customer with listGuid "a78a1ed1-d7c0-4aae-81000100-3" and upcId "8100401"
   	When customer removes item from list by upcId
   	Then customer removes item from favorites by upcId status code is "204"

  @newFlow
  Scenario: Guest user trying to delete item from not default list
    Given guest ser with not default listGuid "a78a1ed1-d7c0-4aae-81000100-5" and upcId "8100501"
    When guest user removes favorite item from not default list
    Then guest user removes favorites item from not default list status code is "400"
    And response contains error message

  @newFlow
  Scenario: Delete item from favorites by incorrect productId
    Given customer with listGuid "a78a1ed1-d7c0-4aae-81000100-2" and incorrect productId "81006"
    When customer tries to remove item from list by incorrect productId
    Then removing item from favorites by incorrect productId status code is "400"
    And removing item by incorrect productId response contains an error

  @newFlow
  Scenario: Delete item from favorites by incorrect upcId
    Given customer with listGuid "a78a1ed1-d7c0-4aae-81000100-2" and incorrect upcId "8100601"
    When customer tries to remove item from list by incorrect upcId
    Then removing item from favorites by incorrect upcId status code is "400"
    And removing item by incorrect upcId response contains an error

  @newFlow
  Scenario: Delete item from favorites by blank productId
    Given customer with listGuid "a78a1ed1-d7c0-4aae-81000100-2" and blank productId ""
    When customer tries to remove item from list by blank productId
    Then removing item from favorites by blank productId status code is "400"
    And removing item by blank productId response contains an error

  @newFlow
  Scenario: Delete master item from favorites
    Given customer with listGuid "a78a1ed1-d7c0-4aae-81000100-2" and master productId "81666"
    When customer tries to remove item from list by master productId
    Then removing item from favorites by master productId status code is "400"
    And removing item by master productId response contains an error

