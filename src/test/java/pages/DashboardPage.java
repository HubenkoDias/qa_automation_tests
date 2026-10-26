package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import utils.TestConfig;

public class DashboardPage extends MenuPage {

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    private static final By DASHBOARD_HEADING = By.tagName("h1");
    private static final By FULL_NAME = By.cssSelector("input[placeholder='John Smith']");
    private static final By JOB_TITLE = By.cssSelector("input[placeholder='Senior QA Engineer']");
    private static final By BIO = By.cssSelector("textarea");
    private static final By YEARS_EXP = By.cssSelector("input[type='number'][max='50']");
    private static final By SAVE_PROFILE = By.xpath("//button[normalize-space()='Save profile']");
    private static final By SAVED_BUTTON = By.xpath("//button[contains(normalize-space(), '✓ Saved')]");
    private static final By JOBS_LINK = By.cssSelector("a[href='/jobs']");


    public String getHeadingText() {
        return getText(DASHBOARD_HEADING);
    }

    public LoginPage openWithoutLogin() {
        driver.get(TestConfig.BASE_URL + "/dashboard");
        waitForUrlContains("/auth/login");
        return new LoginPage(driver);
    }

    public DashboardPage setBio(String text) {
        type(BIO, text);
        return this;
    }

    public DashboardPage saveProfile() {
        scrollIntoViewAndClick(SAVE_PROFILE);
        waitForVisible(SAVED_BUTTON);
        return this;
    }

    public DashboardPage reload() {
        driver.navigate().refresh();
        waitForVisible(BIO);
        return this;
    }

    public DashboardPage setYearsExp(String years) {
        type(YEARS_EXP, years);
        return this;
    }


    public String getYearsExpValue() {
        return getValue(YEARS_EXP);
    }

    public String getBioValue() {
        return getValue(BIO);
    }
}
