package io.intelligenta.communityportal.SeleniumTesting.Pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePage {
    protected WebDriver driver;
    public final WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 10);
    }

    static void get(WebDriver driver, String relativeUrl) {
        String url = System.getProperty("geb.build.baseUrl", "http://localhost:3000") + relativeUrl;
        driver.get(url);
    }
}
