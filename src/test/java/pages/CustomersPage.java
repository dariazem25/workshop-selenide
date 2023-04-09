package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CustomersPage {

    private WebDriver driver;

    @FindBy(xpath = "//input[@ng-model=\"searchCustomer\"]")
    private WebElement search;

    @FindBy(xpath = "//tbody/tr[last()]/td[5]")
    private WebElement deleteBtn;

    @FindBy(xpath = "//thead/tr[1]/td[1]/a[1]")
    private WebElement sortByFirstNameBtn;

    public CustomersPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    @Step("Input the customer to find")
    public void findCustomer(String customer) {
        search.sendKeys(customer);
    }

    @Step("Delete the customer")
    public void deleteCustomer() {
        deleteBtn.click();
    }

    @Step("Sort the customers by first name")
    public void sortCustomersByFirstName() {
        sortByFirstNameBtn.click();
    }

    @Step("Delete all customers")
    public void deleteAllCreatedCustomers(int customers) {
        for (int i = 0; i < customers; i++) {
            deleteCustomer();
        }
    }

    @Step("Get the existing customers")
    public List<Map<String, String>> getCustomers() {
        List<Map<String, String>> customers = new ArrayList<>();
        int customersSize = driver.findElement(By.xpath("//tbody")).getText().split("\n").length;
        for (int i = 0; i < customersSize; i++) {
            Map<String, String> customer = new LinkedHashMap<>();
            int customerNum = i + 1;
            try {
                String firstName = driver.findElement(By.xpath("//tbody/tr[" + customerNum + "]/td[1]")).getText();
                String lastName = driver.findElement(By.xpath("//tbody/tr[" + customerNum + "]/td[2]")).getText();
                String postCode = driver.findElement(By.xpath("//tbody/tr[" + customerNum + "]/td[3]")).getText();
                customer.put("firstName", firstName);
                customer.put("lastName", lastName);
                customer.put("postCode", postCode);
                customers.add(customer);
            } catch (NotFoundException e) {
                return  customers;
            }
        }
        return customers;
    }
}
