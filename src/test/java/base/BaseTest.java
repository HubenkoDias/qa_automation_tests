package base;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--no-sandbox",
                    "--disable-dev-shm-usage", "--window-size=1920,1080");
        } else {
            options.addArguments("--start-maximized");
        }
        driver = new ChromeDriver(options);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && driver != null) {
            System.out.println("FAILED: " + result.getName()
                    + " | URL at failure: " + driver.getCurrentUrl());
            try {
                File shot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                Files.copy(shot.toPath(), Path.of("target", result.getName() + ".png"),
                        StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
                System.out.println("Screenshot failed: " + e.getMessage());
            }
        }
        if (driver != null) {
            driver.quit();
        }
    }
}