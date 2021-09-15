package io.intelligenta.communityportal.SeleniumTesting;

import io.intelligenta.communityportal.SeleniumTesting.Pages.LoginPage;
import io.intelligenta.communityportal.SeleniumTesting.Pages.NapsPage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.core.annotation.Order;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class NapsTestSelenium {
    private WebDriver driver;

    @BeforeTest
    public void setupAndLogin() throws InterruptedException {
        driver = getDriver();
        LoginPage loginPage = new LoginPage(driver);
        LoginPage.open(driver);
        loginPage.login("admin@gmail.com", "admin");
    }

    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Boris\\Documents\\GitHub\\SKIT_Communityportal_backend\\src\\main\\resources\\driver\\chromedriver.exe");
        return new ChromeDriver();
    }

    // Always run the test in order so there is a user logged in.
    @Test
    @Order(1)
    public void countNumberOfNaps() {
        NapsPage napsPage = new NapsPage(driver);
        NapsPage.open(driver);
        assertEquals(napsPage.numberOfNaps(), 2);
    }

    @Test
    @Order(2)
    public void addNapSuccessfully() throws InterruptedException {
        NapsPage napsPage = new NapsPage(driver);
        NapsPage.open(driver);
        napsPage.addNap("nap mk", "nap al",
                "15.09.2021", "15.10.2021",
                "desc mk", "desc al",
                "Во тек");
        assertEquals(napsPage.numberOfNaps(), 2);
    }

    @Test
    @Order(3)
    public void addNapUnsuccessfully() throws InterruptedException {
        NapsPage napsPage = new NapsPage(driver);
        NapsPage.open(driver);
        napsPage.addNap(null, "nap al",
                "15.09.2021", "15.10.2021",
                "desc mk", "desc al",
                null);
        assertEquals(napsPage.numberOfNaps(), 2);
    }
}
