import org.example.configuration.ConfProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.AddCustomerPage;
import pages.CustomersPage;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static com.codeborne.selenide.Selenide.webdriver;

public class Task1Tests {

    public static AddCustomerPage addCustomerPage = new AddCustomerPage();
    public static CustomersPage customersPage = new CustomersPage();
    public static final String CUSTOMERS_LINK = ConfProperties.getProperty("customerspage");
    public static final String ADD_CUST_LINK = ConfProperties.getProperty("addcustpage");
    public static final String SORTING_LINK = ConfProperties.getProperty("sortingpage");

    @BeforeAll
    public static void setup() {
        var chromeDriverPath = "src\\test\\resources\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        System.setProperty("selenide.browser", "Chrome");
        System.setProperty("chromeoptions.args", "--remote-allow-origins=*");
    }

    @BeforeEach
    public void tearDown() {
        open(CUSTOMERS_LINK);
        sleep(2500);
        customersPage.deleteAllCustomers();
    }

    @Test
    public void addValidCustomerTest() {
        // Preconditions:
        // Add customer page is opened
        open(ADD_CUST_LINK);

        // Actions:
        // Fill in first name
        addCustomerPage.inputFirstName("Ivan");
        // Fill in last name
        addCustomerPage.inputLastName("Ivanov");
        // Fill in post code
        addCustomerPage.inputPostCode("123");

        // Click on the 'Add Customer' button
        addCustomerPage.clickAddCustomerBtn();

        // Result:
        String currentPage = webdriver().driver().getWebDriver().getCurrentUrl();

        // The page was not changed
        Assertions.assertEquals(ADD_CUST_LINK, currentPage);

        //The customer was created successfully
        open(CUSTOMERS_LINK);
        customersPage.findCustomer("Ivanov");
        Assertions.assertTrue(webdriver().driver().getWebDriver().getPageSource().contains("Ivan"));
        Assertions.assertTrue(webdriver().driver().getWebDriver().getPageSource().contains("Ivanov"));
        Assertions.assertTrue(webdriver().driver().getWebDriver().getPageSource().contains("123"));
    }

    @Test
    public void sortingByFirstNameTest() {
        // Preconditions:
        // Some customers exist
        open(ADD_CUST_LINK);
        addCustomerPage.addCustomer("Anna", "Zekunova", "456");
        addCustomerPage.addCustomer("Kate", "Yakovleva", "123");
        addCustomerPage.addCustomer("Boris", "Ivanov", "456");
        addCustomerPage.addCustomer("Zhora", "Kovalev", "756");
        Map<String, String> customer1 = new LinkedHashMap<>();
        customer1.put("firstName", "Zhora");
        customer1.put("lastName", "Kovalev");
        customer1.put("postCode", "756");
        Map<String, String> customer2 = new LinkedHashMap<>();
        customer2.put("firstName", "Kate");
        customer2.put("lastName", "Yakovleva");
        customer2.put("postCode", "123");
        Map<String, String> customer3 = new LinkedHashMap<>();
        customer3.put("firstName", "Boris");
        customer3.put("lastName", "Ivanov");
        customer3.put("postCode", "456");
        Map<String, String> customer4 = new LinkedHashMap<>();
        customer4.put("firstName", "Anna");
        customer4.put("lastName", "Zekunova");
        customer4.put("postCode", "456");
        var reversedList = List.of(customer1, customer2, customer3, customer4);
        var alphabeticallyList = List.of(customer4, customer3, customer2, customer1);

        // customers page is opened
        open(CUSTOMERS_LINK);

        // Actions:
        // Click on the 'First Name' header
        customersPage.sortCustomersByFirstName();

        // Result:
        String currentPage = webdriver().driver().getWebDriver().getCurrentUrl();
        var reversedCustomers = customersPage.getCustomers();

        // The page was changed
        Assertions.assertEquals(SORTING_LINK, currentPage);

        // The customers are sorted in reverse way
        Assertions.assertEquals(reversedList, reversedCustomers);

        // Click the 'First Name' header again
        customersPage.sortCustomersByFirstName();
        var alphabeticallyCustomers = customersPage.getCustomers();

        // The customers are sorted alphabetically
        Assertions.assertEquals(alphabeticallyList, alphabeticallyCustomers);
    }

    @Test
    public void findByFirstNameTest() {
        // Preconditions:
        // Some customers exist
        open(ADD_CUST_LINK);
        addCustomerPage.addCustomer("Anna", "Zekunova", "456");
        addCustomerPage.addCustomer("Anna", "Petrova", "790");
        addCustomerPage.addCustomer("Irina", "Zayceva", "912");
        addCustomerPage.addCustomer("Valeria", "Zayceva", "912");
        addCustomerPage.addCustomer("Kate", "Yakovleva", "123");
        addCustomerPage.addCustomer("Boris", "Ivanov", "456");
        addCustomerPage.addCustomer("Zhora", "Kovalev", "756");
        addCustomerPage.addCustomer("Anastasiya", "Lomakina", "789");
        addCustomerPage.addCustomer("Anna", "Zekunova", "902");

        // customers page is opened
        open(CUSTOMERS_LINK);

        // Actions:
        // Input 'Anna' value in 'Search Customer' field
        customersPage.findCustomer("Anna");

        // Result:
        String currentPage = webdriver().driver().getWebDriver().getCurrentUrl();
        var customers = customersPage.getCustomers();

        // The page was not changed
        Assertions.assertEquals(CUSTOMERS_LINK, currentPage);

        // The following customers are returned
        Assertions.assertTrue(customers.containsAll(List.of(
                Map.of("firstName", "Anna", "lastName", "Zekunova", "postCode", "456"),
                Map.of("firstName", "Anna", "lastName", "Petrova", "postCode", "790"),
                Map.of("firstName", "Anna", "lastName", "Zekunova", "postCode", "902"))));
        Assertions.assertEquals(3, customers.size());
    }

    @Test
    public void findByLastNameTest() {
        // Preconditions:
        // Some customers exist
        open(ADD_CUST_LINK);
        addCustomerPage.addCustomer("Anna", "Zekunova", "456");
        addCustomerPage.addCustomer("Anna", "Petrova", "790");
        addCustomerPage.addCustomer("Irina", "Zayceva", "432");
        addCustomerPage.addCustomer("Valeria", "Zayceva", "912");
        addCustomerPage.addCustomer("Kate", "Yakovleva", "123");
        addCustomerPage.addCustomer("Boris", "Ivanov", "456");
        addCustomerPage.addCustomer("Zhora", "Kovalev", "756");
        addCustomerPage.addCustomer("Anastasiya", "Lomakina", "789");
        addCustomerPage.addCustomer("Anna", "Zekunova", "902");

        // customers page is opened
        open(CUSTOMERS_LINK);

        // Actions:
        // Input 'zayceva' value in 'Search Customer' field
        customersPage.findCustomer("zayceva");

        // Result:
        String currentPage = webdriver().driver().getWebDriver().getCurrentUrl();
        var customers = customersPage.getCustomers();

        // The page was not changed
        Assertions.assertEquals(CUSTOMERS_LINK, currentPage);

        // The following customers are returned
        Assertions.assertTrue(customers.containsAll(List.of(
                Map.of("firstName", "Irina", "lastName", "Zayceva", "postCode", "432"),
                Map.of("firstName", "Valeria", "lastName", "Zayceva", "postCode", "912"))));
        Assertions.assertEquals(2, customers.size());
    }

    @Test
    public void findByPostCodeTest() {
        // Preconditions:
        // Some customers exist
        open(ADD_CUST_LINK);
        addCustomerPage.addCustomer("Anna", "Zekunova", "456");
        addCustomerPage.addCustomer("Anna", "Petrova", "790");
        addCustomerPage.addCustomer("Irina", "Zayceva", "432");
        addCustomerPage.addCustomer("Valeria", "Zayceva", "912");
        addCustomerPage.addCustomer("Kate", "Yakovleva", "123");
        addCustomerPage.addCustomer("Boris", "Ivanov", "456");
        addCustomerPage.addCustomer("Zhora", "Kovalev", "756");
        addCustomerPage.addCustomer("Anastasiya", "Lomakina", "789");
        addCustomerPage.addCustomer("Anna", "Zekunova", "902");

        // customers page is opened
        open(CUSTOMERS_LINK);

        // Actions:
        // Input '456' value in 'Search Customer' field
        customersPage.findCustomer("456");

        // Result:
        String currentPage = webdriver().driver().getWebDriver().getCurrentUrl();
        var customers = customersPage.getCustomers();

        // The page was not changed
        Assertions.assertEquals(CUSTOMERS_LINK, currentPage);

        // The following customers are returned
        Assertions.assertTrue(customers.containsAll(List.of(
                Map.of("firstName", "Anna", "lastName", "Zekunova", "postCode", "456"),
                Map.of("firstName", "Boris", "lastName", "Ivanov", "postCode", "456"))));
        Assertions.assertEquals(2, customers.size());
    }
}
