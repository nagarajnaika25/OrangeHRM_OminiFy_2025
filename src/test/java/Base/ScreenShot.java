package Base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;

public class ScreenShot {
	
	
	 WebDriver driver;
	


	  
	   public ScreenShot (WebDriver driver)
	   {
		   
	     this.driver=driver; 
	   }
	   
	   
	   
		public  void get_ScreenShot  (String scnamme)
		
		{
		try {
		TakesScreenshot screen=(TakesScreenshot)driver;
		
		File file=screen.getScreenshotAs(OutputType.FILE);
		
		
	    String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    String fileName = scnamme + "_" + timestamp + ".png";
	    
	    
	    //File path
	    String fpath= System.getProperty("user.dir")+"/Reports/Screenshots/" + fileName;
		File scfath=new File(fpath);
		scfath.getParentFile().mkdirs();

		FileHandler.copy(file, scfath);
		
		System.out.println("ScreenShot taken"+scfath);
		}
		catch(Exception e)
		{
		 System.out.println(""+e.getMessage());	
		}
		
		}
	}



