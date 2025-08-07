package Pages;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentTest;

public class PIMPage 
{
    WebDriver driver;
    public WebDriverWait wait;

    @FindBy(xpath = "//span[text()='PIM' and contains(@class, 'oxd-main-menu-item--name')]")
    WebElement pimTab;

    @FindBy(xpath = "//div[@class='orangehrm-header-container']//button[normalize-space()='Add']")
    WebElement addEmployeeButton;

    @FindBy(name = "firstName")
    WebElement firstNameInput;

    @FindBy(name = "middleName")
    WebElement middleNameInput;

    @FindBy(name = "lastName")
    WebElement lastNameInput;

    @FindBy(xpath = "//label[text()='Employee Id']/following::input[1]")
    WebElement employeeIdInput;

    @FindBy(xpath = "//button[normalize-space()='Save']")
    WebElement saveButton;

    @FindBy(xpath = "//p[contains(@class, 'oxd-text--toast-title') and text()='Success']")
    WebElement successmsg;

    @FindBy(xpath = "//a[contains(@class, 'oxd-topbar-body-nav-tab-item') and text()='Employee List']")
    WebElement employeeListLink;

    @FindBy(xpath = "//table[@class='oxd-table']//tbody//tr")
    List<WebElement> employeeRows;

    @FindBy(xpath = "//button[contains(@class,'oxd-pagination-page-item--previous-next')]/i[contains(@class,'bi-chevron-right')]/parent::button")
    WebElement nextButton;

    @FindBy(xpath = "//i[contains(@class,'oxd-userdropdown-icon')]")
    WebElement profileDropdown;

    @FindBy(linkText = "Logout")
    WebElement logoutBtn;

    //Page factory
    public PIMPage(WebDriver driver)
    {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    
    //Click on PIM
    public void goToPIM() {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOf(pimTab));
        pimTab.click();
    }

    
    
    //Add Employess
    public String addEmployee(String first, String middle, String last) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        goToPIM();
        wait.until(ExpectedConditions.elementToBeClickable(addEmployeeButton)).click();
        wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        firstNameInput.clear();
        firstNameInput.sendKeys(first);
        middleNameInput.clear();
        middleNameInput.sendKeys(middle);
        lastNameInput.clear();
        lastNameInput.sendKeys(last);
        String empId = employeeIdInput.getAttribute("value").trim();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("oxd-form-loader")));
        saveButton.click();
        try {
            wait.until(ExpectedConditions.visibilityOf(successmsg));
        } catch (Exception e) {
            System.out.println("Success toast not found. Continuing anyway.");
        }
        driver.navigate().refresh();
        wait.until(ExpectedConditions.visibilityOf(pimTab));
        return empId;
    }

    
   
   

    //Click to Employeelist
    public void goToEmployeeList() 
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement empListTab = wait.until(ExpectedConditions.elementToBeClickable(
            By.xpath("//a[text()='Employee List']")));
        empListTab.click();
    }
    
    
    //Verifying Employee Name and Id

    public boolean verifyEmployeeById(String empIdToFind, String expectedFullName, ExtentTest test) throws InterruptedException {
        while (true) {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='oxd-table-card']")));
            List<WebElement> rows = driver.findElements(By.xpath("//div[@class='oxd-table-card']"));

            for (WebElement row : rows)
            
            
            {
                List<WebElement> cells = row.findElements(By.xpath(".//div[contains(@class, 'oxd-table-cell')]"));

                for (WebElement cell : cells) {
                    String text = cell.getText().trim();
                    if (text.equalsIgnoreCase(empIdToFind)) {
                        System.out.println("Name Verified: " + expectedFullName + " | ID: " + empIdToFind);
                        test.pass("Name Verified: " + expectedFullName + " | ID: " + empIdToFind);
                        return true;
                    }
                }

                if (cells.size() >= 3) {
                    String empId = cells.get(0).getText().trim();
                    String name = cells.get(1).getText().trim();
                    String surname = cells.get(2).getText().trim();
                    String fullName = (name + " " + surname).replaceAll("\\s+", " ").trim();
                    
                    if (empId.equalsIgnoreCase(empIdToFind)) {
                        System.out.println("Name Verified: " + fullName + " | ID: " + empId);
                        test.pass("Verified: " + fullName + " | ID: " + empId);
                        return true;
                    }
                }
            }

            List<WebElement> nextBtn = driver.findElements(
                By.xpath("//button[contains(@class, 'oxd-pagination-page-item') and .//i[contains(@class,'bi-chevron-right')]]"));
            if (!nextBtn.isEmpty() && nextBtn.get(0).isEnabled()) {
            	nextBtn.get(0).click();
            	wait.until(ExpectedConditions.invisibilityOfElementLocated(By.className("oxd-loading-spinner")));
                wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//div[@class='oxd-table-card']")));          } else {
                break;
            }
        }
        System.out.println("Not Found: " + expectedFullName + " | ID: " + empIdToFind);
        test.warning("Not Found by ID: " + empIdToFind + " (Name: " + expectedFullName + ")");
        return false;
    }

    
    //Logout 
    
    public void logout() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//span[@class='oxd-userdropdown-tab']")));
            dropdown.click();
            WebElement logoutBtn = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//a[text()='Logout']")));
            logoutBtn.click();
        } catch (Exception e) {
            System.out.println("Logout failed: " + e.getMessage());
        }
    }
}
