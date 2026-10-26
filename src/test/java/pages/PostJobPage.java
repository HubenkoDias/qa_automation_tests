package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PostJobPage extends MenuPage {

    private static final By JOB_TITLE = By.xpath("//label[contains(normalize-space(),'Job title')]/following-sibling::input");
    private static final By JOB_DESCRIPTION = By.xpath("//label[contains(normalize-space(),'Description')]/following-sibling::textarea");
    private static final By PUBLISH_JOB_POSTING = By.xpath("//button[text()='Publish job posting']");

    protected PostJobPage(WebDriver driver) {
        super(driver);
    }

    public PostJobPage jobTitleFilling () {
        click(JOB_TITLE);
        type(JOB_TITLE, "Test text for Job Title information");
        return this;
    }
    public PostJobPage jobDescriptionFilling () {
        click(JOB_DESCRIPTION);
        type(JOB_DESCRIPTION, "Test text for the description information, test, test, test");
        return this;
    }
    public PostedJobPage jobPostPublishing () {
        scrollIntoViewAndClick(PUBLISH_JOB_POSTING);
        waitForUrlMatches("/jobs/[0-9a-f-]{36}");
        return new PostedJobPage(driver);
    }

}
