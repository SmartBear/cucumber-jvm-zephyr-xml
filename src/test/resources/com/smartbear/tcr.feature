@JIRA_XYZ-1 @JIRA_XYZ-2 @smoke-test
Feature: TCR

  @TCR_1
  Scenario Outline: Export multi-selected ZQL search results in Testcases  (All Fields/Detailed/PDF)
    Given User navigates to Test Repository App
    And User navigate to Search Page
    And User enter text "<condition>" in "Advance Search" textbox
    And User click on "GO" button
    And User verify testcases satisfying "<condition>" condition are visible in TCR search grid
    When User click on "Export" Button
    And User click on "Select All" checkbox on Customize and Export Reports window
    And user select report type "<report>", output as "<output>", versions "<version>" and title "<report name>"
    And User click on "Save" button
    Then User verify "Success" status for export job on Export Progress window
    And User click on "Download" button on Download File window
    And user verify that file downloaded successfully with name "<report name>"
    Examples:
      | condition                                                      | report           | output | version | report name            |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") | Summary          | HTML   | Current | TestSearchSummaryHTML  |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") | Summary          | PDF    | Current | TestSearchSummaryPDF   |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") | Summary          | Word   | Current | TestSearchSummaryWord  |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") | Detailed         | HTML   | Current | TestSearchDetailedHTML |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") | Detailed         | PDF    | Current | TestSearchDetailedPDF  |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") | Detailed         | Word   | Current | TestSearchDetailedWord |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") | Data(Excel Only) | PDF    | Current | TestSearchExcelPDF     |


  @tcr_2
  Scenario: Ctrl+DnD (copy) 2-3 testcase from project release to local release
    Given User navigates to Test Repository App
    And User navigates to "Release 1.0 : TCRPhase : TCRSystem : TCRSubSystem"
    And User navigates to "Project Test Repository : GlobalPhase : GlobalSystem : GlobalSubSystem"
    When User click on "Select All" checkbox in TCR grid
    And User do Ctrl+DnD to "Release 1.0 : TCRPhase : TCRSystem : TCRSubSystem" folder
    Then User verify testcases copied successfully to "Release 1.0 : TCRPhase : TCRSystem : TCRSubSystem" folder


  @TCR_3
  Scenario Outline: Search testcase by advance search(single/multiple condition) with all supported fields
    Given User navigates to Test Repository App
    And User navigate to Search Page
    When User enter text "<condition>" in "Advance Search" textbox
    And User click on "GO" button
    Then User verify testcases satisfying "<condition>" condition are visible in TCR search grid
    Examples:
      | condition                                                      |
      | estimatedTime is not "empty"                                   |
      | estimatedTime = "11:11:11.000"                                 |
      | testcaseId in (1, 2, 3, 10) and creator != "test.manager"      |
      | release = "Project Test Repository" and priority is "empty"    |
      | altId ~ "test" or comment ~ "Phase"                            |
      | tag = "system" and altId ~ "test" and priority in ("P1", "P2") |
      | automated = true and version = 1                               |
      | folder = "TCRPhase" and name = "TCRPhase Test"                 |
      | versionId != 1                                                 |
      | CFCheckBox = "true" or CFText ~ "TCRPhase"                     |
      | contents ~ "TCRPhase Step"                                     |

  @TCR_4
  Scenario Outline: Import testcases(by id change/empty row/testcase name change) and view the folder name
    Given User navigates to Test Repository App
    When User click on "Import" button
    And User click on "Create Job" button for map "<map name>"
    And User enter Job details job name "<job name>" and select file "<file>" to import on create new import job window
    And User click on "Start Import" button
    Then User verify "Success" status for job "<job name>"
    And User close "Import Job Progress" window
    And User close "Import Test Cases" window
    And User navigates to "Imported :""<job name>" folder
    And User verify imported test cases in "<job name>" folder
    Examples:
      | map name       | job name       | file      |
      | Map_EmptyRow   | Job_EmptyRow   | test.xlsx |
      | Map_IdChange   | Job_IdChange   | test.xlsx |
      | Map_NameChange | Job_NameChange | test.xlsx |