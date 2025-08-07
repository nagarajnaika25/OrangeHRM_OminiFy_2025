package Base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReport {
	
	private static ExtentSparkReporter  reportsetup;
	private static ExtentReports  report;
	private static ExtentTest  test;
	
	
	public  static ExtentReports  Myreport(String reportname)
	{
		String path = System.getProperty("user.dir") + "/Reports/"+reportname+".html";
		reportsetup=new ExtentSparkReporter(path);
		
				
		reportsetup.config().setReportName("WebApplication Testing"+reportname);
		reportsetup.config().setDocumentTitle("SauceDemo Application testing"+reportname);
		reportsetup.config().setTheme(Theme.STANDARD);
		
		
		//Report
		report=new ExtentReports();
		 
		report.attachReporter(reportsetup);
		report.setSystemInfo("Automation Engineer", "Nagaraj naik");
		report.setSystemInfo("Environment", "QA");
		report.setSystemInfo("Language", "Java");
		report.setSystemInfo("OS", "Windows 11");
		
		
		return report;
		
		

	}
	
	public static ExtentTest craeteTest(String testName)
	{
		
	    test=report.createTest(testName);
		return test;
	}
	

}
