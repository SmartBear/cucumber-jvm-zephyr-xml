@JIRA_XYZ-1 @JIRA_XYZ-2 @smoke-test
Feature: Belly

  @JIRA_XYZ-3
  Scenario: a few cukes
    Given I have 42 cukes in my belly
    When I wait 1 hour
    Then my belly should growl

  @JIRA_XYZ-4
  Scenario: a few cukes 2
    Given I have 42 cukes in my belly
    When I wait 1 hour
    Then my belly should growl
