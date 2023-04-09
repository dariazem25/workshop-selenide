import org.openqa.selenium.WebDriver;
import pages.AddCustomerPage;
import pages.CustomersPage;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class TestHelper {

    private int addedCustomersCount;

    public int getAddedCustomersCount() {
        return addedCustomersCount;
    }

    public void provideCustomers(CustomersPage customersPage, AddCustomerPage addCustomerPage, WebDriver webDriver) {
        webDriver.get(Task1Tests.CUSTOMERS_LINK);
        List<Map<String, String>> existingCustomers = customersPage.getCustomers();

        if (existingCustomers.isEmpty()) {
            webDriver.get(Task1Tests.ADD_CUST_LINK);
            addCustomerPage.addCustomer("Zoya", "Potapova", "456");
            addedCustomersCount++;
            addCustomerPage.addCustomer("Irina", "Zayceva", "912");
            addedCustomersCount++;
        } else if (existingCustomers.size() == 1) {
            webDriver.get(Task1Tests.ADD_CUST_LINK);
            Map<String, String> customer = provideCustomer(customersPage);
            addCustomerPage.addCustomer(customer);
            addedCustomersCount++;
        }
        webDriver.get(Task1Tests.CUSTOMERS_LINK);
    }

    public Map<String, String> provideCustomer(CustomersPage customersPage) {
        List<Map<String, String>> customers = customersPage.getCustomers();

        return customers.isEmpty() ? Map.of("firstName", "Anna", "lastName", "Zekunova", "postCode", "456")
                : Map.of("firstName", customers.get(0).get("firstName").repeat(2), "lastName",
                customers.get(0).get("lastName").repeat(2), "postCode", customers.get(0).get("postCode").repeat(2));
    }

    public Comparator<Map<String, String>> customerComparator = Comparator.comparing(m -> m.get("firstName"));
}
