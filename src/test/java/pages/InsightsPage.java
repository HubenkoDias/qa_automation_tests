package pages;

import base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class InsightsPage extends BasePage {

    private static final By INSIGHTS_HEADING = By.tagName("h1");

    protected InsightsPage(WebDriver driver) {
        super(driver);
    }
    public String getHeadingText() {
        return getText(INSIGHTS_HEADING);
    }

    public InsightsPage isArticleDisplayed(String title) {
        By ARTICLE = By.xpath(
                "//a[contains(@class,'card-hover')]" +
                        "[.//h3[normalize-space()='" + title + "']]"
        );

        if (driver.findElements(ARTICLE).isEmpty()) {
            throw new AssertionError(
                    "Article was not found on Insights page: " + title
            );
        }

        return this;
    }
    public InsightsPage clickArticle(String title) {
        By article = By.xpath(
                "//a[contains(@class,'card-hover')]" +
                        "[.//h3[normalize-space()='" + title + "']]"
        );

        List<WebElement> articles = driver.findElements(article);

        if (articles.isEmpty()) {
            throw new AssertionError(
                    "Article was not found: " + title
            );
        }

        articles.get(0).click();
        return this;
    }
}
