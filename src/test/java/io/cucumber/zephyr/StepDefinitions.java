package io.cucumber.zephyr;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class StepDefinitions {
    @Given("I have {int} cukes in my belly")
    public void I_have_cukes_in_my_belly(int cukes) {
        Belly belly = new Belly();
        belly.eat(cukes);
    }

    @When("I wait {int} hour")
    public void i_wait_hour(Integer int1) {
        Belly belly = new Belly();
        belly.eat(int1);
    }

    @Then("my belly should growl")
    public void my_belly_should_growl() {
        Belly belly = new Belly();
        belly.drink();
    }

    @Given("I have {int} cakes in my belly")
    public void I_have_cakes_in_my_belly(Integer int1) {
        Belly belly = new Belly();
        belly.eat(int1);
    }

    @Given("Navigate to \"Administration -> Customization\"")
    public void Navigate_to_() {
        System.out.println("given_Admin");
    }

    @And("Click on Estimated Time Button")
    public void and_test() {
        System.out.println("and_Admin");
    }

    @When("User Click on reset Estimated Time Button")
    public void userClickOnResetEstimatedTimeButton() {
    }

    @And("User click on save button on {string} window")
    public void userClickOnSaveButtonOnWindow(String arg0) {
    }

    @And("User click on {string} button on {string} window")
    public void userClickOnButtonOnWindow(String arg0, String arg1) {
    }

    @Then("User view the default estimated time as {string}")
    public void userViewTheDefaultEstimatedTimeAs(String arg0) {
    }

    @Given("Navigate to \"Administration -> Project Setup\"")
    public void navigateTo() {
    }

    @When("User click on Add Project button")
    public void userClickOnAddProjectButton() {
    }

    @And("User enters project name {string} and startdate {string}")
    public void userEntersProjectNameAndStartdate(String arg0, String arg1) {
    }

    @And("User selects project type as {string}")
    public void userSelectsProjectTypeAs(String arg0) {
    }

    @And("User click on Add button")
    public void userClickOnAddButton() {
    }

    @Then("Project should be created successfully with project name {string} and project type {string}")
    public void projectShouldBeCreatedSuccessfullyWithProjectNameAndProjectType(String arg0, String arg1) {
    }

    @And("User clicks on Indexing Button")
    public void userClicksOnIndexingButton() {
    }

    @And("User click on Full Reindexing button on Indexing window")
    public void userClickOnFullReindexingButtonOnIndexingWindow() {
    }

    @When("User select {string} entity from Select Entities to reindex dropdown")
    public void userSelectEntityFromSelectEntitiesToReindexDropdown(String arg0) {
    }

    @And("User click on Reindex button")
    public void userClickOnReindexButton() {
    }

    @Then("User verify job in indexing grid for {string} entity with {string} status")
    public void userVerifyJobInIndexingGridForEntityWithStatus(String arg0, String arg1) {
    }

    @Given("User navigates to Requirement App")
    public void userNavigatesToRequirementApp() {
    }

    @And("User navigates to {string}")
    public void userNavigatesTo(String arg0) {
    }

    @Then("Folder {string} with Requirement should be deleted successfully")
    public void folderWithRequirementShouldBeDeletedSuccessfully(String arg0) {
    }

    @And("User click on {string} Button")
    public void userClickOnButton(String arg0) {
    }

    @When("User context click on folder {string}")
    public void userContextClickOnFolder(String arg0) {
    }

    @And("User context clicks Phase {string}")
    public void userContextClicksPhase(String arg0) {
    }

    @And("user selects {string}")
    public void userSelects(String arg0) {
    }

    @And("User clicks {string} button")
    public void userClickOnSaveButton(String arg0) {
    }

    @And("selects report type {string}, output as {string}, versions {string} and title {string}")
    public void selectsReportTypeOutputAsVersionsAndTitle(String arg0, String arg1, String arg2, String arg3) {
    }

    @And("user clicks {string} checkbox in Customize and Export Reports")
    public void userClicksCheckboxInCustomizeAndExportReports(String arg0) {
    }

    @When("User click on {string} button")
    public void userClickOnDeleteButton(String arg0) {
    }

    @And("User verify {string} status for export job on Export Progress window")
    public void userVerifyStatusForExportJobOnExportProgressWindow(String arg0) {
    }

    @And("User click on {string} button on Download File window")
    public void userClickOnButtonOnDownloadFileWindow(String arg0) {
    }

    @Then("user verify that file downloaded successfully with name {string}")
    public void userVerifyThatFileDownloadedSuccessfullyWithName(String arg0) {
    }

    @When("User click on {string} Requirement in REQ grid to open detail view")
    public void userClickOnRequirementInREQGridToOpenDetailView(String arg0) {
    }

    @And("User enter name {string}, description {string}, priority {string}, altId {string}")
    public void userEnterNameDescriptionPriorityAltId(String arg0, String arg1, String arg2, String arg3) {
    }

    @And("User add attachments")
    public void userAddAttachments() {
    }

    @Then("Requirement should be updated with all data successfully")
    public void requirementShouldBeUpdatedWithAllDataSuccessfully() {
    }

    @And("User add custom field values")
    public void userAddCustomFieldValues() {
    }

    @And("User click on Back to List button")
    public void userClickOnBackToListButton() {
    }

    @And("User view Requirement name {string} in REQ grid")
    public void userViewRequirementNameInREQGrid(String arg0) {
    }

    @Given("User navigates to Test Repository App")
    public void userNavigatesToTestRepositoryApp() {
    }

    @Then("User verify folder having text {string} in Folder name gets highlighted")
    public void userVerifyFolderHavingTextInFolderNameGetsHighlighted(String arg0) {
    }

    @When("User click on {string} checkbox in TCR grid")
    public void userClickOnCheckboxInTCRGrid(String arg0) {
    }

    @And("User do Ctrl+DnD to {string} folder")
    public void userDoCtrlDnDToFolder(String arg0) {
    }

    @Then("User verify testcases copied successfully to {string} folder")
    public void userVerifyTestcasesCopiedSuccessfullyToFolder(String arg0) {
    }

    @And("User enter map details map name {string}, row number <row num> and discriminator {string} on create new excel map window")
    public void userEnterMapDetailsMapNameRowNumberRowNumAndDiscriminatorOnCreateNewExcelMapWindow(String arg0, String arg1) {
    }

    @And("User enter zephyr field details testcase name {string} and altId {string}")
    public void userEnterZephyrFieldDetailsTestcaseNameAndAltId(String arg0, String arg1) {
    }

    @Then("Map should be created successfully with name {string}")
    public void mapShouldBeCreatedSuccessfullyWithName(String arg0) {
    }

    @And("User navigate to Search Page")
    public void userNavigateToSearchPage() {
    }

    @And("User click on {string} checkbox on Customize and Export Reports window")
    public void userClickOnCheckboxOnCustomizeAndExportReportsWindow(String arg0) {
    }

    @And("user select report type {string}, output as {string}, versions {string} and title {string}")
    public void userSelectReportTypeOutputAsVersionsAndTitle(String arg0, String arg1, String arg2, String arg3) {
    }

    @And("User click on {string} button for map {string}")
    public void userClickOnButtonForMap(String arg0, String arg1) {
    }

    @And("User enter Job details job name {string} and select file {string} to import on create new import job window")
    public void userEnterJobDetailsJobNameAndSelectFileToImportOnCreateNewImportJobWindow(String arg0, String arg1) {
    }

    @Then("User verify {string} status for job {string}")
    public void userVerifyStatusForJob(String arg0, String arg1) {
    }

    @And("User enter text {} in {string} textbox")
    public void userEnterTextInTextbox(String arg0, String arg1) {
    }

    @And("User verify testcases satisfying {} condition are visible in TCR search grid")
    public void userVerifyTestcasesSatisfyingConditionAreVisibleInTCRSearchGrid(String arg0) {
    }

    @And("User close {string} window")
    public void userCloseWindow(String arg0) {
    }

    @And("User navigates to {string}{string} folder")
    public void userNavigatesToFolder(String arg0, String arg1) {
    }

    @And("User verify imported test cases in {string} folder")
    public void userVerifyImportedTestCasesInFolder(String arg0) {
    }

    @When("I long {int} hour")
    public void iLongHour(int arg0) {
    }

    @Then("my chest should fill")
    public void myChestShouldFill() {
    }
}
