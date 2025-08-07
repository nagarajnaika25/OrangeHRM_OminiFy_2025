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
        //Login page
        login = new LoginPage(driver);
        //PIM page
        pim = new PIMPage(driver);
        //Extent report
        report = ExtentReport.Myreport("PIMTestReport");
        //Screenshot
        scrshot = new ScreenShot(driver);
    }

    @Test
    public void testPIMFlow() throws InterruptedException 
    {
       //Create a test name
    	test = report.createTest("PIM Module - Add Employees and Verify");
    	//get screenshot
        scrshot.get_ScreenShot("PIM Module");
        test.info("Logging in...");
        //Login
        login.login("Admin", "admin123");
        //Test pass
        test.pass("Login successful");

        //Store Employee name
        String[][] employees = 
       {
            {"Jeniliya", "P", "Naik"},
            {"Arpita", "G", "Naik"},
            {"Anu", "M", "Naik"},
            
        };

        //Storing key-value pairs data
        Map<String, String> empMap = new LinkedHashMap<>();
        
         //Iterate employees data- stored above
        for (String[] emp : employees) 
        {
        	//Click on PIM
            pim.goToPIM();
            //Add F-M-Last name and stored created Employee Id
            String empId = pim.addEmployee(emp[0], emp[1], emp[2]);
            //Store as a fullname
            String fullName = emp[0] + " " + emp[1] + " " + emp[2];
            //Stored empMap variable 
            empMap.put(empId, fullName);
            //Print  full name and empId
            System.out.println("Employee Added: " + fullName + " | ID: " + empId);
            test.pass("Employee added: " + fullName + " | ID: " + empId);
        }

        Thread.sleep(3000);
        driver.navigate().refresh();
        Thread.sleep(2000);

        
        
        
        //Validation Employee date
        //Click on PIM
        pim.goToPIM();
        test.info("Navigated to Employee List");

        // Iterate empMap.put(empId, fullName); to entry
        for (Map.Entry<String, String>  entry : empMap.entrySet())  //entryset(EmployeeId and Name)
        {
            
        	//Store Individually  EmpId
        	String empId = entry.getKey();
        	//fullName
            String fullName = entry.getValue();
            
            //Print 
            System.out.println("Verifying: " + fullName + " | ID: " + empId);
            test.info("Verifying: " + fullName + " | ID: " + empId);
            
            
            //Verify -in EmployeeList
            boolean found = pim.verifyEmployeeById(empId, fullName, test);
            if (!found) 
            {
            	//If not found
                System.out.println("Not Found: " + fullName + " | ID: " + empId);
                test.fail("Not Found: " + fullName + " | ID: " + empId);
             }
          
             //Assert true or false
            Assert.assertTrue(found, "Employee not found: " + fullName + " | ID: " + empId);
          
        }

        
        //Logout
        pim.logout();
        test.pass("Logged out");
        //Validation or Asertion _for  the Logout
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