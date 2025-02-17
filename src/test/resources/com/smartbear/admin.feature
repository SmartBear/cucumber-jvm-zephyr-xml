@JIRA_XYZ-1 @JIRA_XYZ-2 @smoke-test
Feature: Admin

  @Admin_1
  Scenario: Reset the Estimated time
    Given Navigate to "Administration -> Customization"
    And Click on Estimated Time Button
    When User Click on reset Estimated Time Button
    And User click on save button on "Default Estimated Time Editor" window
    And User click on "YES" button on "confirmation pop-up" window
    And Click on Estimated Time Button
    Then User view the default estimated time as "blank"

  @Admin_2
  Scenario Outline: Create Normal, Isolated and Restricted project with mandatory fields only
    Given Navigate to "Administration -> Project Setup"
    When User click on Add Project button
    And User enters project name "<projectname>" and startdate "<startdate>"
    And User selects project type as "<projecttype>"
    And User click on Add button
    Then Project should be created successfully with project name "<projectname>" and project type "<projecttype>"
    Examples:
      | projectname       | startdate  | projecttype |
      | NormalProject     | 05/18/2023 | Normal      |
      | IsolatedProject   | 05/18/2023 | Isolated    |
      | RestrictedProject | 05/18/2023 | Restricted  |

  @Admin_3
  Scenario Outline: User should be able to trigger full reindex for each entity individually
    Given Navigate to "Administration -> Customization"
    And User clicks on Indexing Button
    And User click on Full Reindexing button on Indexing window
    When User select "<entity>" entity from Select Entities to reindex dropdown
    And User click on Reindex button
    Then User verify job in indexing grid for "<entity>" entity with "Enqueue" status
    Examples:
    | entity           |
    | Testcase         |
    | Testcase Tree    |
    | Requirement      |
    | Execution        |
    | Requirement Tree |