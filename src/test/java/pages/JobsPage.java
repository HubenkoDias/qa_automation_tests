package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class JobsPage extends MenuPage {

    private static final By JOBS_HEADING = By.tagName("h1");
    private static final By POST_A_JOB = By.cssSelector("a[href*='/jobs/post']");
    private static final By JOB_TITLE = By.cssSelector("input[placeholder='QA Engineer, Senior Test Automation Lead...']");
    private static final By JOB_DESCRIPTION = By.cssSelector("textarea[placeholder^='Describe the role']");
    private static final By PUBLISH_JOB_POSTING = By.xpath("//button[text()='Publish job posting']");
    private static final By SEARCH_JOBS = By.cssSelector("input[placeholder^='Search jobs or companies…']");
    private static final By FOUND_JOB = By.tagName("h3");

    public JobsPage(WebDriver driver) {
        super(driver);
    }

    public PostJobPage openPostJob () {
        click(POST_A_JOB);
        waitForUrlContains("/post");
        return new PostJobPage(driver);
    }

    public JobsPage searchForJobPost () {
        click(SEARCH_JOBS);
        type(SEARCH_JOBS, "Test text for Job Title information");
        return this;
    }

    public PostedJobPage openJobPage () {
        click(FOUND_JOB);
        return new PostedJobPage(driver);
    }

    public String getHeadingText() {
        return getText(JOBS_HEADING);
    }

}
