package io.intelligenta.communityportal.SeleniumTesting;

import io.intelligenta.communityportal.SeleniumTesting.Pages.LoginPage;
import io.intelligenta.communityportal.SeleniumTesting.Pages.NapPage;
import io.intelligenta.communityportal.SeleniumTesting.Pages.NapsPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class NapTestSelenium {

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

    @Test
    public void editNapSuccessfully() throws InterruptedException {
        NapPage napPage = new NapPage(driver);
        NapPage.open(driver);
        napPage.editNap("edited nap mk", "edited nap al",
                "", "",
                "edit desc mk", "desc al",
                "Во тек");
        String editedNapName = napPage.getNapName();
        assertEquals(editedNapName, "edited nap mk");
    }

    @Test
    public void editNapUnsuccessfully() throws InterruptedException {
        NapPage napPage = new NapPage(driver);
        NapPage.open(driver);
        napPage.editNap("edited nap mk", "edited nap al",
                "20.9.2021", "20.10.2021",
                "edit desc mk", "desc al",
                "Во тек");
        String editedNapName = napPage.getNapDate();
        assertNotEquals(editedNapName, "09.2021 - 10.2021");
    }

    @Test
    public void openNewEvaluation() throws InterruptedException {
        NapPage napPage = new NapPage(driver);
        NapPage.open(driver);
        napPage.openNewEvaluation("new selenium eval", "new selenium eval");
        assertTrue(napPage.searchForCloseEvalButton());
        napPage.closeEvaluation();
    }

    @Test
    public void openOldEvaluation() throws InterruptedException {
        NapPage napPage = new NapPage(driver);
        NapPage.open(driver);
        napPage.openOldEvaluation("Евал");
        assertTrue(napPage.searchForCloseEvalButton());
        napPage.closeEvaluation();
    }

    @Test
    public void countNumberOfNapAreas() throws InterruptedException {
        NapPage napPage = new NapPage(driver);
        NapPage.open(driver);
        assertEquals(napPage.countNumberOfNapAreas(), 2);
    }

}
