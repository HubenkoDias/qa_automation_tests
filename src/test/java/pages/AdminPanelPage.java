package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class AdminPanelPage extends MenuPage {

    private static final By BLOG_BUTTON = By.xpath("//button[normalize-space()='Blog']");
    private static final By BLOG_TITLE = By.cssSelector("input[placeholder='Post title']");
    private static final By BLOG_EXCERPT = By.cssSelector("input[placeholder='Short description for cards']");
    private static final By BLOG_CONTENT = By.cssSelector("textarea[placeholder^='Write your post']");
    private static final By CREATE_DRAFT_BUTTON = By.xpath("//button[normalize-space()='Create draft']");

    protected AdminPanelPage(WebDriver driver) {
        super(driver);
    }

    public AdminPanelPage openBlogCreationPage () {
        click(BLOG_BUTTON);
        return this;
    }
    public AdminPanelPage createBlogArticle (String title, String excerpt, String content) {
        click(BLOG_TITLE);
        type(BLOG_TITLE, title);
        type(BLOG_EXCERPT, excerpt);
        type(BLOG_CONTENT, content);
        click(CREATE_DRAFT_BUTTON);
        return this;
    }

    public AdminPanelPage publishArticle (String title) {
        By article = By.xpath(
                "//div[contains(@class,'card')]" +
                        "[.//*[normalize-space()='" + title + "']]"
        );

        List<WebElement> articles = driver.findElements(article);

        if (articles.isEmpty()) {
            throw new AssertionError(
                    "Article not found: " + title
            );
        }

        articles.get(0).findElement(
                By.xpath(".//button[normalize-space()='Publish']")
        ).click();

        return this;
    }

}
