package OrangeHRMTest;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import Base.BaseTestsetup;
import Base.ExtentReport;
import Base.ScreenShot;
import Pages.LoginPage;

public class LoginTest extends BaseTestsetup {
    ExtentReports report;
    ExtentTest test;
    LoginPage lgPage;
    ScreenShot scrshot;
    
    
    @BeforeTest
    public void initReport() {
    	report = ExtentReport.Myreport("LoginTestReport");
    }
    
    
    @BeforeMethod
    public void setupTest() {

        freeSetup();// from BaseTestsetup
        lgPage = new LoginPage(driver);
        scrshot=new ScreenShot(driver);
    }

    //Valid Login
    @Test(priority=1)
    public void validLoginTest() {
        test = report.createTest("Valid Login Test");
        scrshot.get_ScreenShot("Before  Validlogin");
        test.info("Attempting login with valid credentials");
        lgPage.login("Admin", "admin123");
        scrshot.get_ScreenShot("After ValidLogin_Dashboard");
        String currentUrl = driver.getCurrentUrl();
        boolean isLoggedIn = currentUrl.contains("dashboard");

        Assert.assertTrue(isLoggedIn, "Expected to be on dashboard after login");
        test.pass("Login successful - redirected to dashboard");
        
      
        
    }

    
    //Invalid Login
    @Test(priority=2)
    public void invalidLoginTest() throws InterruptedException {
        
    	driver.navigate().refresh();
        test = report.createTest("Invalid Login Test");
        scrshot.get_ScreenShot("Before InValidlogin");

        test.info("Attempting login with invalid credentials");
        lgPage.login("wronguser", "wrongpass");  // Fixed typo
        Thread.sleep(2000);
        String actualMessage = lgPage.ErrorMessage();
       String expectedMessage = "Invalid credentials";
      
       Assert.assertEquals(actualMessage, expectedMessage, "Expected error message not shown");
       System.out.println("Error Message is : "+ actualMessage);
        test.pass("Error message displayed correctly for invalid login");
        scrshot.get_ScreenShot("After InValidlogin");
    }

    @AfterMethod
    public void tearDownTest() {
        report.flush(); // Save ExtentReport
        tearDown();     // Close browser
    }
    
    
    
    @AfterTest
    public void flushReport() {
        report.flush();  
    }
}
