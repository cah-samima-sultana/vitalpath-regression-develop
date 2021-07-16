Feature: Login
  Scenario: Login Functionality
    Given user navigates to cardinalhealth website
    And there user logs in through Login Window by using Username as "USER" and Password as "PASSWORD"
    Then user able to land in the home page
