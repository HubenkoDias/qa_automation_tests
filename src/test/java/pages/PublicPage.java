package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PublicPage extends MenuPage {

    private static final By HEADING = By.tagName("h1");

    public PublicPage(WebDriver driver) {
        super(driver);
    }
    public String getHeadingText() {
        return getText(HEADING);
    }
}
