package wingify;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
Sushant Chauhan
QA intern assignment
 */
public class AutomationQaTest {


    public ChromeDriver driver;
    private static ExtentReports extentReports;
    ExtentTest extentTest;
    // Method to set up test environment before each testCase
    @BeforeMethod
    public void setup() {
                             //path set Up for the chromeDriver
        System.setProperty("webdriver.chrome,driver", "C:\\BrowserDriver\\chromedriver.exe");
        // Initialize ChromeDriver and navigate to the login page
        driver = new ChromeDriver();
        driver.get("https://sakshingp.github.io/assignment/login.html");
        // maximize the window
        driver.manage().window().maximize();

        // Set up ExtentReports for test reporting
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter("test-output/ExtentReport.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(htmlReporter);

        extentReports.createTest("setUp");

    }
     //Test 1 for the Successful Login
    @Test(priority = 1)
    public void successfulLogin() throws InterruptedException {
        extentTest = extentReports.createTest("Successful Login");
        // Perform the login operation
        Thread.sleep(1000);
        loginOperation("username", "password");
          //Successful login testing
        Thread.sleep(1000);
        Reporter.log("Successful Login Testing");

        // click on AMOUNT to sort
        driver.findElement(By.id("amount")).click();
        List<WebElement> elements = driver.findElements(By.cssSelector(".text-right.bolder.nowrap"));

        List<Double> AmountList = new ArrayList<>();
           //Extract amount from the table
        for (WebElement ele : elements) {
            String amount = ele.getText();
            double amt = convertStringToDouble(amount);
            AmountList.add(amt);
        }

        // before sorting
        for (double value : AmountList) {
            System.out.println(value);
        }

        // check for sorting

        List<Double> newList = new ArrayList<>(AmountList);

        Collections.sort(newList);

        boolean sorted = newList.equals(AmountList);

        System.out.println("Values sorted successfully: " + sorted);

    }
    // Test 2 for empty userNameField
    @Test(priority = 2)
    public void emptyUsernameField() throws InterruptedException {
        extentTest = extentReports.createTest("Empty User Field.");
        // Performing the  login operation with empty username
        Thread.sleep(1000);
        loginOperation("", "password");
        assertMessage("Username must be present");
 //log empty username testing
        Reporter.log("Empty UserName Field Testing");
    }


  //Test 3 for empty passWord Field
    @Test(priority = 3)
    public void emptyPasswordField() throws InterruptedException {
        extentTest = extentReports.createTest("Empty Password Field.");
        //Performing the login Operation with empty passWord
        Thread.sleep(1000);
        loginOperation("username", "");
        assertMessage("Password must be present");
//log empty password testing
        Reporter.log("Empty Password Field Testing");
    }

    // Test 4 for both(userName and passWord) empty Fields
    @Test(priority = 4)
    public void bothEmptyField() throws InterruptedException {
        extentTest = extentReports.createTest("Empty Both Fields.");
        Thread.sleep(1000);
        loginOperation("", "");
        assertMessage("Both Username and Password must be present");
        // Log empty both fields testing
        Reporter.log("Empty Both Field Testing");
    }


    // Method to close browser after each test
    public void tearDown() {
        if (driver != null)
            driver.quit();
    }
  //Method for login Operation
    private void loginOperation(String username, String password) {
        driver.findElement(By.id("username")).sendKeys(username);
        driver.findElement(By.id("password")).sendKeys(password);

        driver.findElement(By.id("log-in")).click();
    }

    //Method to assert Error Message
    private void assertMessage(String expectedMessage) {
        try {
            WebElement message = driver.findElement(By.cssSelector(".alert.alert-warning"));

            Assert.assertEquals(message.getText(), expectedMessage);
        } catch (NoSuchElementException e) {
            throw new RuntimeException("Error message not found");
        }
    }



    //Method to convert String to Double
    private static double convertStringToDouble(String amount) {

        Pattern pattern;
        pattern = Pattern.compile("([-+]?)\\s*(\\d{1,3}(,\\d{3})*(\\.\\d+)?)");

        Matcher matcher = pattern.matcher(amount);
        if (matcher.find()) {
            // Combine sign and numeric part, remove commas and spaces before parsing
            String sign = matcher.group(1);
            String numericPart = matcher.group(2).replace(",", "").replace(" ", "");

            double parsedAmount = Double.parseDouble(numericPart);
            // adding "-" sign if no. is negative
            return (sign.equals("-")) ? -parsedAmount : parsedAmount;
        } else {
            throw new NumberFormatException("Unable to parse amount: " + amount);
        }
    }

}
