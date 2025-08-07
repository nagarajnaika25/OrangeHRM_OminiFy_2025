package Pages;

import java.time.Duration;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {
	
	
	
    @FindBy(name = "username")
    WebElement username;

    @FindBy(name = "password")
    WebElement password;

    @FindBy(xpath = "//button[@type='submit']")
    WebElement loginBtn;
	
	
	@FindBy(xpath="//div[@class=\"oxd-alert oxd-alert--error\"]")
	WebElement errormsg;
	
	
	
	
	WebDriver driver;
	
	//Constrocter 
	public LoginPage(WebDriver  d)  
	{
		driver = d;//Stored webdriver object to here
		PageFactory.initElements(driver, this);// This refers to driver Object

	}
	
	
	
	//Valid Login 
	public void login(String user, String pass)
	{
		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(username));
		username.sendKeys(user);
        password.sendKeys(pass);
        loginBtn.click();
   
	}
	
	//Error meassgae
	public String ErrorMessage()
	
	{
	return errormsg.getText();
	
	}

	
	}


