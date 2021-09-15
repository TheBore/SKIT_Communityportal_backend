package io.intelligenta.communityportal.SeleniumTesting.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class NapPage extends BasePage{

    public NapPage(WebDriver driver) {
        super(driver);
    }

    public static NapPage open(WebDriver driver) {
        get(driver, "/nap/35");
        return PageFactory.initElements(driver, NapPage.class);
    }

    public void editNap(String napNameMk, String napNameAl,
                        String startDate, String endDate,
                        String descriptionMk, String descriptionAl,
                        String status) throws InterruptedException {
        driver.findElement(By.className("editNapBtn")).click();
        Thread.sleep(2000);

        driver.findElement(By.name("nameMk")).clear();
        driver.findElement(By.name("nameMk")).sendKeys(napNameMk);
        driver.findElement(By.name("nameAl")).clear();
        driver.findElement(By.name("nameAl")).sendKeys(napNameAl);

        driver.findElement(By.name("startDate")).clear();
        driver.findElement(By.name("startDate")).click();
        driver.findElement(By.name("startDate")).clear();
        driver.findElement(By.name("startDate")).sendKeys(startDate);

        driver.findElement(By.name("end")).clear();
        driver.findElement(By.name("end")).click();
        driver.findElement(By.name("end")).clear();
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

    public String getNapName() {
        return driver.findElement(By.xpath("/html/body/div/div[2]/div/div[2]/div[2]/div/div[1]/div[1]/h2[1]/span")).getAttribute("title");
    }

    public String getNapDate() {
        return driver.findElement(By.cssSelector("#root > div:nth-child(2) > div > div:nth-child(2) > div.container-fluid > div > div.row > div.col-md-9 > h2:nth-child(6) > li")).getText();
    }

    public void openNewEvaluation(String descriptionMk, String descriptionAl) throws InterruptedException {
        driver.findElement(By.className("addNewEvalBtn")).click();

        Thread.sleep(2000);

        driver.findElement(By.name("descriptionMk")).clear();
        driver.findElement(By.name("descriptionMk")).click();
        driver.findElement(By.name("descriptionMk")).sendKeys(descriptionMk);

        driver.findElement(By.name("descriptionAl")).clear();
        driver.findElement(By.name("descriptionAl")).click();
        driver.findElement(By.name("descriptionAl")).sendKeys(descriptionAl);
        driver.findElement(By.xpath("/html/body/div[3]/div/div/div[3]/button[2]")).click();

        Thread.sleep(5000);
    }

    public void openOldEvaluation(String evaluationName) throws InterruptedException {
        driver.findElement(By.className("openOldEvalBtn")).click();

        Thread.sleep(2000);

        driver.findElement(By.xpath("/html/body/div[3]/div/div/div[2]/div/div/div/div/div/div[1]/div[2]/div/input")).sendKeys(evaluationName + Keys.TAB);
        driver.findElement(By.xpath("/html/body/div[3]/div/div/div[3]/button[2]")).click();

        Thread.sleep(5000);
    }

    public void closeEvaluation () throws InterruptedException {
        driver.findElement(By.className("closeEvalBtn")).click();

        Thread.sleep(2000);

        driver.findElement(By.xpath("/html/body/div[3]/div/div/div[3]/button[2]")).click();

        Thread.sleep(5000);
    }

    public int countNumberOfNapAreas() throws InterruptedException {
        return driver.findElements(By.id("accordion")).size();
    }

    public boolean searchForCloseEvalButton() {
        try {
            return driver.findElement(By.className("closeEvalBtn")).isDisplayed();
        }
        catch (NoSuchElementException e){
            return false;
        }
    }
}
