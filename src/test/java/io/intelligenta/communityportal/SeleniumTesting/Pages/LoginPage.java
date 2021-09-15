package io.intelligenta.communityportal.SeleniumTesting.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage{

    @FindBy(id = "email")
    private WebElement email;

    @FindBy(id = "password")
    private WebElement password;

    private WebElement login;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public static LoginPage open(WebDriver driver) {
        get(driver, "/login");
        return PageFactory.initElements(driver, LoginPage.class);
    }

    public boolean isLoaded() throws InterruptedException {
        Thread.sleep(5000);
        return wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.id("email"))).isDisplayed();
    }

    public void login(String user, String password) throws InterruptedException {
        driver.findElement(By.id("email")).clear();
        driver.findElement(By.id("email")).sendKeys(user);
        Thread.sleep(2000);
        driver.findElement(By.id("password")).sendKeys(password);
        Thread.sleep(2000);
        driver.findElement(By.id("kt_login_signin_submit")).click();
        Thread.sleep(2000);
    }

    public String getErrorMessage() {
        WebElement errorPage;
        try {
            errorPage = driver.findElement(By.cssSelector("div[class='ErrorPopup_container__3s6x9 undefined']"));
            return errorPage.getText();
        }
        catch (NoSuchElementException e){
            return "No such element";
        }
    }
}
