Feature: As a happy holidaymaker I want to receive the correct weather forecast

  Scenario: A happy holidaymaker
    Given I request a weather forecast for my holiday
      And the forecast temperature is in "metric" units
      And the forecast request is from the city "Sydney" with country code "aus"
    When I receive the forecast correctly
    Then the temperature should be above 10 degrees
      And the forecast city should be "Sydney"
      And the forecast day should be on "Thursday"
