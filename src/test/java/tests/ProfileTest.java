package tests;

import base.BasePage;
import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.DashboardPage;
import pages.HomePage;
import pages.LoginPage;
import utils.TestConfig;
import utils.TestUser;

public class ProfileTest extends BaseTest {

    @Test
    public void profileChangesArePersistedAfterSave() {
        String newBio = "Autotest bio " + System.currentTimeMillis();

        DashboardPage dashboardPage = new LoginPage(driver)
                .open()
                .loginAs(TestUser.QA)
                .setBio(newBio)
                .saveProfile()
                .reload();

        Assert.assertEquals(dashboardPage.getBioValue(), newBio,
                "Bio should stay changed after saving and reloading the page");
    }

    @DataProvider(name = "invalidYears")
    public Object[][] invalidYears() {
        return new Object[][]{{"51"}, {"600"}, {"-1"}};
    }

    @DataProvider(name = "validYears")
    public Object[][] validYears() {
        return new Object[][]{{"1"}, {"5"}, {"50"}};
    }

    @Test(dataProvider = "invalidYears", enabled = false,
            description = "years of experience has no validation")
    public void invalidYearsAreNotSaved(String years) {
        DashboardPage dashboardPage = saveYearsAndReload(years);

        Assert.assertNotEquals(dashboardPage.getYearsExpValue(), years,
                "Invalid value " + years + " must not be saved");
    }

    @Test(dataProvider = "validYears", enabled = false,
            description = "years of experience has no validation")
    public void validYearsAreSaved(String years) {
        DashboardPage dashboardPage = saveYearsAndReload(years);

        Assert.assertEquals(dashboardPage.getYearsExpValue(), years,
                "Valid value " + years + " should stay after save and reload");
    }

    @AfterMethod
    public void resetYearsExp() {
        if (driver.getCurrentUrl().contains("/dashboard")) {
            new DashboardPage(driver).setYearsExp("0").saveProfile();
        }
    }

    private DashboardPage saveYearsAndReload(String years) {
        return new LoginPage(driver)
                .open()
                .loginAs(TestUser.QA)
                .setYearsExp(years)
                .saveProfile()
                .reload();
    }

}