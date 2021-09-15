package io.intelligenta.communityportal.SeleniumTesting.Pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;

public class NapsPage extends BasePage{

    public NapsPage(WebDriver driver) {
        super(driver);
    }

    public static NapsPage open(WebDriver driver) {
        get(driver, "/naps");
        return PageFactory.initElements(driver, NapsPage.class);
    }

    public int numberOfNaps(){
        return driver.findElements(By.className("napDiv")).size();
    }

    public void addNap(String napNameMk, String napNameAl,
                       String startDate, String endDate,
                       String descriptionMk, String descriptionAl,
                       String status) throws InterruptedException {
        driver.findElement(By.className("addNapBtn")).click();
        Thread.sleep(2000);
        driver.findElement(By.name("nameMk")).clear();
        driver.findElement(By.name("nameMk")).sendKeys(napNameMk);
        driver.findElement(By.name("nameAl")).clear();
        driver.findElement(By.name("nameAl")).sendKeys(napNameAl);

        driver.findElement(By.name("startDate")).clear();
        driver.findElement(By.name("startDate")).click();
        driver.findElement(By.name("startDate")).sendKeys(startDate);

        driver.findElement(By.name("end")).clear();
        driver.findElement(By.name("end")).click();
        driver.findElement(By.name("end")).sendKeys(endDate);

        driver.findElement(By.name("descriptionMk")).clear();
        driver.findElement(By.name("descriptionMk")).click();
        driver.findElement(By.name("descriptionMk")).sendKeys(descriptionMk);
        driver.findElement(By.name("descriptionAl")).clear();
        driver.findElement(By.name("descriptionAl")).sendKeys(descriptionAl);

        driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div/div/div[6]/div/div/div[1]/div[2]/div/input")).sendKeys(status + Keys.TAB);
        driver.findElement(By.xpath("/html/body/div[3]/div/div/div[3]/button[2]")).click();

        Thread.sleep(5000);
    }
}
