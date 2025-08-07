package Base;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class BaseTestsetup {
	
	
	protected  WebDriver driver;
	
	
	public void  freeSetup()
	{
        // Set path to chromedriver if needed (optional if chromedriver is in PATH)
        // System.setProperty("webdriver.chrome.driver", "path/to/chromedriver");

        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.get("https://opensource-demo.orangehrmlive.com/web/index.php/auth/login");
    }

    public void tearDown() 
    {
        if (driver != null) 
        
        {
           
        	driver.quit(); //Close all browser
       
        
        }
    }
	
	
	
	
	
	
	
	
	

}
