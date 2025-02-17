@JIRA_XYZ-1 @JIRA_XYZ-2 @smoke-test
Feature: REQ

  @REQ_1
  Scenario Outline: Delete created Phase with system and subsystem with tests at each level.
    Given User navigates to Requirement App
    And User navigates to "<level>"
    When User context click on folder "<folder>"
    And User click on "Delete" Button
    Then Folder "<folder>" with Requirement should be deleted successfully
    Examples:
      | level                                            | folder            |
      | Project Test : GlobalSystem : GlobalSubSystem    | GlobalSubSystem   |
      | Project Test : GlobalSystem                      | GlobalSystem      |
      | Project Test : GlobalPhase                       | GlobalPhase       |

  @REQ_2
  Scenario Outline: Export Requirement from grid in (All report and output type)
    Given User navigates to Requirement App
    And User context clicks Phase "Phase1"
    And user selects "Export Requirement"
    And User clicks "Requirement only" button
    And user clicks "select all" checkbox in Customize and Export Reports
    And selects report type "<report>", output as "<output>", versions "<version>" and title "<report name>"
    When User click on "Save" button
    And User verify "Success" status for export job on Export Progress window
    And User click on "Download" button on Download File window
    Then user verify that file downloaded successfully with name "<report name>"
    Examples:
      | report           | output | version | report name      |
      | Summary          | HTML   | Current | TestSummaryHTML  |
      | Summary          | PDF    | Current | TestSummaryPDF   |
      | Summary          | Word   | Current | TestSummaryWord  |
      | Detailed         | HTML   | Current | TestDetailedHTML |
      | Detailed         | PDF    | Current | TestDetailedPDF  |
      | Detailed         | Word   | Current | TestDetailedWord |
      | Data(Excel Only) | PDF    | Current | TestExcelPDF     |

  @REQ_3
  Scenario Outline: Update Requirement with all fields in detail view at release level
    Given User navigates to Requirement App
    And User navigates to "<level>"
    When User click on "Untitled" Requirement in REQ grid to open detail view
    And User enter name "<name>", description "<desc>", priority "<priority>", altId "<altid>"
    And User add attachments
    And User add custom field values
    Then Requirement should be updated with all data successfully
    And User click on Back to List button
    And User view Requirement name "<name>" in REQ grid
    Examples:
      | level                                                | name               | desc          | priority | altid  |
      | Release 1.0 : REQPhase2                              | REQPhase Test2     | REQPhase2     | P3       | tes-1  |
      | Release 1.0 : REQPhase2 : REQSystem2                 | REQSystem Test2    | REQSystem2    | P3       | tes-2  |
      | Release 1.0 : REQPhase2 : REQSystem2 : REQSubSystem2 | REQSubSystem Test1 | REQSubSystem2 | P3       | tes-3  |
