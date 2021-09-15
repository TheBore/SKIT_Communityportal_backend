package io.intelligenta.communityportal.SeleniumTesting;

import io.intelligenta.communityportal.SeleniumTesting.Pages.LoginPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class LoginTestSelenium {
    private WebDriver driver;

    @BeforeTest
    public void setup() { driver = getDriver(); }

    private WebDriver getDriver() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\Boris\\Documents\\GitHub\\SKIT_Communityportal_backend\\src\\main\\resources\\driver\\chromedriver.exe");
        return new ChromeDriver();
    }

    @Test
    public void LoginWithInvalidCredentials() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        LoginPage.open(driver);
        loginPage.login("admin@gmail.com", "wrongpassword");
        String errorMessage = loginPage.getErrorMessage();
        System.out.println("Message: " + errorMessage);
        assertEquals(errorMessage, "Погрешна е-пошта или лозинка");
    }

    @Test
    public void LoginWithValidCredentials() throws InterruptedException {
        LoginPage loginPage = new LoginPage(driver);
        LoginPage.open(driver);
        loginPage.login("admin@gmail.com", "admin");
        String errorMessage = loginPage.getErrorMessage();
        System.out.println("Message: " + errorMessage);
        assertEquals(errorMessage, "No such element");
    }
}
