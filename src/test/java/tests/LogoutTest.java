package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.HomePage;
import pages.LoginPage;
import utils.TestConfig;
import utils.TestUser;

public class LogoutTest extends BaseTest {

    @Test
    public void userHaveNoRightsWhenLogsOut() {
             new LoginPage(driver)
                .open()
                .loginAs(TestUser.QA)
                .logOut();
        LoginPage loginPage = new DashboardPage(driver).openWithoutLogin();


        Assert.assertEquals(loginPage.getHeadingText(), "Welcome back",
                "Name should be right");
    }
}