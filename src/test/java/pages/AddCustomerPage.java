package pages;

import io.qameta.allure.Step;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;

public class AddCustomerPage {

    @Step("Input the first name")
    public void inputFirstName(String firstName) {
        $(byXpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[1]/input")).sendKeys(firstName);
    }

    @Step("Input the second name")
    public void inputLastName(String lastName) {
        $(byXpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[2]/input")).sendKeys(lastName);
    }

    @Step("Input the post code")
    public void inputPostCode(String postCode) {
        $(byXpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/div[3]/input")).sendKeys(postCode);
    }

    @Step("Click 'Add Customer' button")
    public void clickAddCustomerBtn() {
        $(byXpath("/html/body/div/div/div[2]/div/div[2]/div/div/form/button")).click();
    }

    @Step("Create the customer")
    public void addCustomer(String firstName, String lastName, String postCode) {
        inputFirstName(firstName);
        inputLastName(lastName);
        inputPostCode(postCode);
        clickAddCustomerBtn();
    }
}
