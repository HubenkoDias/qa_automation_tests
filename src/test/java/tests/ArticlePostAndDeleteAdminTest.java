package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.AdminPanelPage;
import pages.InsightsPage;
import pages.LoginPage;
import utils.TestUser;

public class ArticlePostAndDeleteAdminTest extends BaseTest {

    @Test (enabled = false, description = "WIP: no assertions, no cleanup")
    public void adminPanelPage () {
        InsightsPage createAndDeletePost = new LoginPage(driver)
                .open()
                .loginAs(TestUser.ADMIN)
                .openAdminPanel()
                .openBlogCreationPage()
                .createBlogArticle("Tester", "Tester", "Tester")
                .publishArticle("Tester")
                .openInsights()
                .isArticleDisplayed("Tester")
                .clickArticle("Tester");
    }

}
