package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PostedJobPage extends MenuPage {

    private static final By TITLE = By.tagName("h1");
    private static final By APPLY_BUTTON = By.xpath("//button[text()='Apply now']");
    private static final By COVER_LETTER = By.cssSelector("textarea[placeholder='Why are you a good fit for this role?']");
    static final By SUBMIT_APPLICATION_BUTTON = By.xpath("//button[text()='Submit application']");
    private static final By APPLICATION_SENT_MESSAGE = By.xpath("//*[text()='Application sent!']");

    protected PostedJobPage(WebDriver driver) {
        super(driver);
    }

    public String getJobId() {
        String url = driver.getCurrentUrl();
        return url.substring(url.lastIndexOf('/') + 1);
    }

    public PostedJobPage applyProcess () {
        click(APPLY_BUTTON);
        waitForVisible(COVER_LETTER);
        click(COVER_LETTER);
        type(COVER_LETTER, "Test Text");
        click(SUBMIT_APPLICATION_BUTTON);
        waitForVisible(APPLICATION_SENT_MESSAGE);
        return this;
    }

    public String getApplicationMessageText () {
        return getText(APPLICATION_SENT_MESSAGE);
    }

    public String getTitleText() {
        return getText(TITLE);
    }

}
