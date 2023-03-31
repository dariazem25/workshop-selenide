package pages;

import io.qameta.allure.Step;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class CustomersPage {

    @Step("Input the customer to find")
    public void findCustomer(String customer) {
        $(byXpath("/html/body/div/div/div[2]/div/div[2]/div/form/div/div/input")).sendKeys(customer);
    }

    @Step("Delete the customer")
    public void deleteCustomer(Integer num) {
        $(byXpath("/html/body/div/div/div[2]/div/div[2]/div/div/table/tbody/tr[" + num + "]/td[5]/button")).click();
    }

    @Step("Sort the customers by first name")
    public void sortCustomersByFirstName() {
        $(byXpath("/html/body/div/div/div[2]/div/div[2]/div/div/table/thead/tr/td[1]/a")).click();
    }

    @Step("Get the existing customers")
    public List<Map<String, String>> getCustomers() {
        var customersSize = $$x("/html/body/div/div/div[2]/div/div[2]/div/div/table/tbody").texts().toString().split("\n").length;
        List<Map<String, String>> customers = new ArrayList<>();
        for (int i = 0; i < customersSize; i++) {
            Map<String, String> customer = new LinkedHashMap<>();
            var customerNum = i + 1;
            var firstName = $x("/html/body/div/div/div[2]/div/div[2]/div/div/table/tbody/tr[" + customerNum + "]/td[1]").text();
            var lastName = $x("/html/body/div/div/div[2]/div/div[2]/div/div/table/tbody/tr[" + customerNum + "]/td[2]").text();
            var postCode = $x("/html/body/div/div/div[2]/div/div[2]/div/div/table/tbody/tr[" + customerNum + "]/td[3]").text();
            customer.put("firstName", firstName);
            customer.put("lastName", lastName);
            customer.put("postCode", postCode);
            customers.add(customer);
        }
        return customers;
    }

    @Step("Delete all customers")
    public void deleteAllCustomers() {
        final int size = getCustomers().size();
        for (int i = 0; i < size; i++) {
            deleteCustomer(1);
        }
    }
}
