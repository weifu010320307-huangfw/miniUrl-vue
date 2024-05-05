package com.shortenUrl.miniUrl.webPageTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;

import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class indexPageIT 
{

    private WebDriver driver;
    
    @LocalServerPort
    private Integer port;




    @BeforeEach
    void setup()
    {
        driver = new ChromeDriver();


    }

    @AfterEach
    void cleanup()
    {

        if(driver != null)
        {
           driver.quit();

        }
    }

    @Test
    void verifyInputInvalidUrlGetPromtMessage()
    {
        driver.get("http://localhost:" + port);

        System.out.println("homePageTest.callPage() " + driver.getTitle());


        WebElement shortUrlInput = driver.findElement(By.id("shortUrlInput"));
        shortUrlInput.sendKeys("input<10");

        WebElement shortenNowButton = driver.findElement(By.id("shortenNowButton"));
        shortenNowButton.click();

        WebElement emptyError = new WebDriverWait(driver, Duration.ofSeconds(3))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("emptyError")));




        assertTrue(emptyError.getText().length() > 0);


    }

    @Test
    void verifyInputValidUrlGetStatstics()
    {
        driver.get("http://localhost:" + port);

        WebElement shortUrlInput = driver.findElement(By.id("shortUrlInput"));
        shortUrlInput.sendKeys("validInput.com");

        WebElement shortenNowButton = driver.findElement(By.id("shortenNowButton"));
        shortenNowButton.click();
        

        WebElement shortenResult = new WebDriverWait(driver, Duration.ofSeconds(3))
        .until(ExpectedConditions.presenceOfElementLocated(By.id("shortenResult")));


        WebElement shortenRatio = driver.findElement(By.id("shortenRatio"));
        WebElement accessRatio = driver.findElement(By.id("accessRatio"));

        assertTrue(shortenResult.getText().length() > 0);
        assertTrue(shortenRatio.getText().length() > 0);
        assertTrue(shortenRatio.getText().length() > 0);


    }

}
