package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class MenuPage extends BasePage {

    private static final By JOBS_LINK = By.cssSelector("a[href='/jobs']");
    private static final By PROFILE_PHOTO = By.cssSelector("button:has(svg.lucide-chevron-down)");
    private static final By LOG_OUT = By.xpath("//button[normalize-space()='Log out']");
    private static final By ADMIN_PANEL_PROFILE = By.cssSelector("a[href='/admin']");
    private static final By INSIGHTS_LINK = By.cssSelector("a[href='/insights']");

    protected MenuPage(WebDriver driver) {
        super(driver);
    }

    public JobsPage openJobs () {
        click(JOBS_LINK);
        waitForUrlContains("/jobs");
        return new JobsPage(driver);
    }

    public InsightsPage openInsights () {
        click(INSIGHTS_LINK);
        waitForUrlContains("/insights");
        return new InsightsPage(driver);
    }


    public PublicPage openMenuItem(String path) {
        By link = By.cssSelector("a[href='" + path + "']");
        click(link);
        waitForUrlContains(path);
        return new PublicPage(driver);
    }

    public AdminPanelPage openAdminPanel  () {
        click(PROFILE_PHOTO);
        click(ADMIN_PANEL_PROFILE);
        waitForUrlContains("/admin");
        return new AdminPanelPage(driver);
    }


    public HomePage logOut() {
        click(PROFILE_PHOTO);
        waitForVisible(LOG_OUT);
        click(LOG_OUT);
        waitForInvisible(PROFILE_PHOTO);
        return new HomePage(driver);
    }


}
