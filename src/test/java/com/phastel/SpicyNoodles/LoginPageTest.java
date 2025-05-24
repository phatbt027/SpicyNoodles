package com.phastel.SpicyNoodles;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoginPageTest {

    @LocalServerPort
    private int port;

    private WebDriver driver;
    private WebDriverWait wait;
    private String baseUrl;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // Run in headless mode
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testLoginPageLoads() {
        driver.get(baseUrl + "/login");
        
        // Verify page title
        assertEquals("Login - Spicy Noodles", driver.getTitle());
        
        // Verify login form elements are present
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        
        assertTrue(usernameField.isDisplayed());
        assertTrue(passwordField.isDisplayed());
        assertTrue(loginButton.isDisplayed());
    }

    @Test
    public void testInvalidLogin() {
        driver.get(baseUrl + "/login");
        
        // Enter invalid credentials
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        
        usernameField.sendKeys("invaliduser");
        passwordField.sendKeys("invalidpass");
        loginButton.click();
        
        // Verify error message is displayed
        WebElement errorMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error-message")));
        assertTrue(errorMessage.isDisplayed());
        assertTrue(errorMessage.getText().contains("Invalid username or password"));
    }

    @Test
    public void testValidLogin() {
        driver.get(baseUrl + "/login");
        
        // Enter valid credentials (you'll need to create a test user in your database)
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        
        usernameField.sendKeys("admin01"); // Replace with a valid test username
        passwordField.sendKeys("admin"); // Replace with a valid test password
        loginButton.click();
        
        // Verify redirect to dashboard
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        assertTrue(driver.getCurrentUrl().contains("/dashboard"));
    }

    @Test
    public void testLogout() {
        // First login
        driver.get(baseUrl + "/login");
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("username")));
        WebElement passwordField = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.className("btn-login"));
        
        usernameField.sendKeys("admin01"); // Replace with a valid test username
        passwordField.sendKeys("admin"); // Replace with a valid test password
        loginButton.click();
        
        // Wait for dashboard to load
        wait.until(ExpectedConditions.urlContains("/dashboard"));
        
        // Find the logout form and submit it
        WebElement logoutForm = wait.until(ExpectedConditions.presenceOfElementLocated(
            By.cssSelector("form[action*='/logout']")));
        logoutForm.submit();
        
        // Verify redirect to login page with logout message
        wait.until(ExpectedConditions.urlContains("/login?logout"));
        WebElement logoutMessage = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("error-message")));
        assertTrue(logoutMessage.getText().contains("You have been logged out"));
    }
} 