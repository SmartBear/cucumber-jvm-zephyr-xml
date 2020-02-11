@JIRA_XYZ-1 @JIRA_XYZ-1 @smoke-test
Feature: Belly

  Scenario: a few cukes
    Given I have 42 cukes in my belly
    When I wait 1 hour
    Then my belly should growl
