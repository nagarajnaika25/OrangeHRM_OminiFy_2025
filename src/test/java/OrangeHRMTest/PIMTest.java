package OrangeHRMTest;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import Base.ExtentReport;
import Base.ScreenShot;
import Pages.LoginPage;
import Pages.PIMPage;

public class PIMTest 
{
    WebDriver driver;
    LoginPage login;
    PIMPage pim;
     
    ExtentReports report;
    ExtentTest test;
    ScreenShot scrshot;
    
    @BeforeTest
    public void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
        login = new LoginPage(driver);
        pim = new PIMPage(driver);
        report = ExtentReport.Myreport("PIMTestReport");
        scrshot = new ScreenShot(driver);
    }

    @Test
    public void testPIMFlow() throws InterruptedException {
        test = report.createTest("PIM Module - Add Employees and Verify");
        scrshot.get_ScreenShot("PIM Module");

        test.info("Logging in...");
        login.login("Admin", "admin123");
        test.pass("Login successful");

        String[][] employees = {
            {"Jeniliya", "P", "Naik"},
            {"Arpita", "G", "Naik"},
            {"Anu", "M", "Naik"},
            
        };

        Map<String, String> empMap = new LinkedHashMap<>();

        for (String[] emp : employees) {
            pim.goToPIM();
            String empId = pim.addEmployee(emp[0], emp[1], emp[2]);
            String fullName = emp[0] + " " + emp[1] + " " + emp[2];
            empMap.put(empId, fullName);
            System.out.println("Employee Added: " + fullName + " | ID: " + empId);
            test.pass("Employee added: " + fullName + " | ID: " + empId);
        }

        Thread.sleep(3000);
        driver.navigate().refresh();
        Thread.sleep(2000);

        pim.goToPIM();
      
       

        test.info("Navigated to Employee List");

        for (Map.Entry<String, String> entry : empMap.entrySet()) {
            String empId = entry.getKey();
            String fullName = entry.getValue();
            System.out.println("Verifying: " + fullName + " | ID: " + empId);
            test.info("Verifying: " + fullName + " | ID: " + empId);
            boolean found = pim.verifyEmployeeById(empId, fullName, test);
            if (!found) {
                System.out.println("Not Found: " + fullName + " | ID: " + empId);
                test.fail("Not Found: " + fullName + " | ID: " + empId);
            }
            Assert.assertTrue(found, "Employee not found: " + fullName + " | ID: " + empId);
        }

        pim.logout();
        test.pass("Logged out");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.urlContains("/auth/login"));

        Assert.assertTrue(driver.getCurrentUrl().contains("/auth/login"), "Logout URL mismatch");
        Assert.assertTrue(driver.getTitle().toLowerCase().contains("orangehrm"), "Unexpected title");

        test.pass("Verified data & Logout Successfully");
  
    }


    @AfterTest
    public void tearDown() {
    	
    	if (report != null)
    	{
            report.flush();  
        }
        if (driver != null) {
            driver.quit();
        }
    }

}