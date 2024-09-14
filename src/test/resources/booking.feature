Feature: Booking endpoint
  Background: Booking endpoints should allow to get and create Bookings

  Scenario: Verify GET booking by id
    Given I perform a GET call to the booking endpoint with id "2"
    Then I verify that the status code is 200
    And The booking details are as follows:
      | firstname    | Mary          |
      | lastname     | Ericsson          |
      | totalprice   | 634            |
      | depositpaid  | false           |
      | checkin      | 2015-12-31     |
      | checkout     | 2020-09-05     |

  Scenario: Verifiy GET booking with invalid id
    Given I perform a GET call to the booking endpoint with id "hsdjfasd"
    Then I verify that the status code is 400
    And The message is "Bad Request"

  Scenario: Create a new booking POST
    Given I perform a POST call to the booking endpoint with the following data:
      | firstname       | Billie            |
      | lastname        | Eilish        |
      | totalprice      | 1000            |
      | depositpaid     | true          |
      | checkin         | 2001-12-18     |
      | checkout        | 2019-04-08     |
      | additionalneeds | Breakfast      |
    Then I verify that the status code is 200

  Scenario: Create a new booking with invalid fields POST
    Given I perform a POST call to the booking endpoint with the following invalid data:
      | firstname       | 1234           |
      | lastname        | !@#$%^         |
      | totalprice      | one hundred        |
      | depositpaid     | null           |
      | checkin         | random_date    |
      | checkout        | null_date      |
      | additionalneeds | InvalidRequest |
    Then I verify that the status code is 400
    And The message is "Bad Request"

  Scenario: Create a new booking with missing fields POST
    Given I perform a POST call to the booking endpoint with the following incomplete data:
      | firstname       | Jim            |
      | lastname        |                |
      | totalprice      | 800            |
      | depositpaid     | false          |
      | checkin         | 2018-03-09     |
      | checkout        | 2019-04-08     |
      | additionalneeds | Breakfast      |
    Then I verify that the status code is 400
    And The message is "Bad Request"
