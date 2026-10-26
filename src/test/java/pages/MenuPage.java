package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class MenuPage extends BasePage {

    private static final By JOBS_LINK = By.cssSelector("a[href='/jobs']");
    private static final By PROFILE_PHOTO = By.cssSelector("button:has(svg.lucide-chevron-down)");
    private static final By LOG_OUT = By.xpath("//button[normalize-space()='Log out']");


    protected MenuPage(WebDriver driver) {
        super(driver);
    }

    public JobsPage openJobs () {
        click(JOBS_LINK);
        waitForUrlContains("/jobs");
        return new JobsPage(driver);
    }

    public PublicPage openMenuItem(String path) {
        By link = By.cssSelector("a[href='" + path + "']");
        click(link);
        waitForUrlContains(path);
        return new PublicPage(driver);
    }

    public HomePage logOut() {
        click(PROFILE_PHOTO);
        waitForVisible(LOG_OUT);
        click(LOG_OUT);
        waitForInvisible(PROFILE_PHOTO);
        return new HomePage(driver);
    }


}
