package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import java.util.Map;

public class AddCustomerPage {

    private WebDriver driver;

    @FindBy(xpath = "//input[@ng-model=\"fName\"]")
    private WebElement inputFirstName;

    @FindBy(xpath = "//input[@ng-model=\"lName\"]")
    private WebElement inputLastName;

    @FindBy(xpath = "//input[@ng-model=\"postCd\"]")
    private WebElement inputPostCode;

    @FindBy(xpath = "//button[@class=\"btn btn-default\"]")
    private WebElement customerBtn;

    public AddCustomerPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Input the first name")
    public void inputFirstName(String firstName) {
        inputFirstName.sendKeys(firstName);
    }

    @Step("Input the second name")
    public void inputLastName(String lastName) {
        inputLastName.sendKeys(lastName);
    }

    @Step("Input the post code")
    public void inputPostCode(String postCode) {
        inputPostCode.sendKeys(postCode);
    }

    @Step("Click 'Add Customer' button")
    public void clickAddCustomerBtn() {
        customerBtn.click();
    }

    @Step("Add the customer")
    public void addCustomer(String firstName, String lastName, String postCode) {
        inputFirstName(firstName);
        inputLastName(lastName);
        inputPostCode(postCode);
        clickAddCustomerBtn();
    }

    @Step("Add the customer")
    public void addCustomer(Map<String, String> customer) {
        inputFirstName(customer.get("firstName"));
        inputLastName(customer.get("lastName"));
        inputPostCode(customer.get("postCode"));
        clickAddCustomerBtn();
    }
}
