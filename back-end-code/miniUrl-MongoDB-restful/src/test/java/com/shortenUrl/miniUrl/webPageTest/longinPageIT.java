// package com.shortenUrl.miniUrl.webPageTest;

// import static org.junit.jupiter.api.Assertions.assertTrue;

// import org.junit.jupiter.api.AfterEach;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.openqa.selenium.By;
// import org.openqa.selenium.WebDriver;
// import org.openqa.selenium.WebElement;
// import org.openqa.selenium.chrome.ChromeDriver;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.boot.test.web.server.LocalServerPort;
// import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;


// @SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
// public class longinPageIT 
// {

//     private WebDriver driver;
    
//     @LocalServerPort
//     private Integer port;

//     private String baseUrl = "http://localhost:" + port;


//     @BeforeEach
//     void setup()
//     {
//         driver = new ChromeDriver();


//     }

//     @AfterEach
//     void cleanup()
//     {

//        driver.quit();
//     }

//     @Test
//     void verifyInputInvalidUserGetPromptMessage()
//     {
//         driver.get("http://localhost:" + port + "/login");

//         System.out.println("longinPageIT.verifyInputInvalidUserGetPromptMessage() baseUrl = " + baseUrl);
//         // driver.get(baseUrl + "/login");

//         WebElement userName = driver.findElement(By.id("username"));
//         userName.sendKeys("shouldNotHaveThisUser");

//         WebElement password = driver.findElement(By.id("password"));
//         userName.sendKeys("anypassword");

//         WebElement loginButton = driver.findElement(By.id("loginButton"));
//         loginButton.submit();

//         WebElement errorMessage = driver.findElement(By.id("error"));
        
//         assertTrue(errorMessage.getText().length() > 0);


//     }
    
// }
