import org.example.configuration.ConfProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import pages.AddCustomerPage;
import pages.CustomersPage;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Task1Tests {

    private static WebDriver driver;
    private static CustomersPage customersPage;
    private static AddCustomerPage addCustomerPage;

    public static final String CUSTOMERS_LINK = ConfProperties.getProperty("customerspage");
    public static final String ADD_CUST_LINK = ConfProperties.getProperty("addcustpage");
    public static final String SORTING_LINK = ConfProperties.getProperty("sortingpage");

    public static TestHelper testHelper = new TestHelper();


    @BeforeEach
    public void setup() {
        driver = Driver.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        customersPage = new CustomersPage(driver);
        addCustomerPage = new AddCustomerPage(driver);
        testHelper.provideCustomers(customersPage, addCustomerPage, driver);
    }

    @AfterEach
    public void tearDown() {
        driver.get(CUSTOMERS_LINK);
        customersPage.deleteAllCreatedCustomers(testHelper.getAddedCustomersCount());
        driver.quit();
    }

    @Test
    public void addValidCustomerTest() {
        // Preconditions:

        // unique customer
        driver.get(CUSTOMERS_LINK);
        Map<String, String> newCustomer = testHelper.provideCustomer(customersPage);

        // Actions:
        driver.get(ADD_CUST_LINK);
        addCustomerPage.inputFirstName(newCustomer.get("firstName"));
        addCustomerPage.inputLastName(newCustomer.get("lastName"));
        addCustomerPage.inputPostCode(newCustomer.get("postCode"));
        addCustomerPage.clickAddCustomerBtn();

        // Result:
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        alert.accept();

        Assertions.assertTrue(text.contains("Customer added successfully with customer id :"), "Incorrect message was returned");

        String currentPage = driver.getCurrentUrl();
        Assertions.assertEquals(ADD_CUST_LINK, currentPage, "The page was changed");

        driver.get(CUSTOMERS_LINK);
        List<Map<String, String>> customers = customersPage.getCustomers();
        Assertions.assertTrue(customers.contains(newCustomer), "The customer was not created");
    }

    @Test
    public void sortingByFirstNameTest() {
        // Preconditions:
        driver.get(CUSTOMERS_LINK);
        List<Map<String, String>> customers = customersPage.getCustomers();

        // Actions:
        customersPage.sortCustomersByFirstName();

        // Result:
        String currentPage = driver.getCurrentUrl();
        List<Map<String, String>> reversedCustomers = customersPage.getCustomers();
        Assertions.assertEquals(SORTING_LINK, currentPage, "The page was not changed");

        customers.sort(testHelper.customerComparator.reversed());
        Assertions.assertEquals(customers, reversedCustomers, "The customers were not sorted in reverse order");

        customers.sort(testHelper.customerComparator);
        customersPage.sortCustomersByFirstName();

        List<Map<String, String>> alphabeticallyCustomers = customersPage.getCustomers();
        Assertions.assertEquals(customers, alphabeticallyCustomers, "The customers were not sorted alphabetically");
    }

    @Test
    public void findByFirstNameTest() {
        // Preconditions:
        driver.get(CUSTOMERS_LINK);
        List<Map<String, String>> customers = customersPage.getCustomers();
        List<Map<String, String>> expectedCustomers = customers.stream()
                .filter(cust -> cust.get("firstName").equals(customers.get(0).get("firstName")))
                .collect(Collectors.toList());

        // Actions:
        customersPage.findCustomer(customers.get(0).get("firstName"));

        // Result:
        String currentPage = driver.getCurrentUrl();
        List<Map<String, String>> actualCustomers = customersPage.getCustomers();
        Assertions.assertEquals(CUSTOMERS_LINK, currentPage, "The page was changed");

        Assertions.assertTrue(actualCustomers.containsAll(expectedCustomers), "Incorrect customers returned");
        Assertions.assertEquals(expectedCustomers.size(), actualCustomers.size(), "Incorrect amount of customers was returned");
    }

    @Test
    public void findByLastNameTest() {
        // Preconditions:
        driver.get(CUSTOMERS_LINK);
        List<Map<String, String>> customers = customersPage.getCustomers();
        var expectedCustomers = customers.stream()
                .filter(cust -> cust.get("lastName").equals(customers.get(0).get("lastName")))
                .collect(Collectors.toList());

        // Actions:
        customersPage.findCustomer(customers.get(0).get("lastName"));

        // Result:
        String currentPage = driver.getCurrentUrl();
        List<Map<String, String>> actualCustomers = customersPage.getCustomers();

        Assertions.assertEquals(CUSTOMERS_LINK, currentPage, "The page was changed");

        Assertions.assertTrue(actualCustomers.containsAll(expectedCustomers), "Incorrect customers returned");
        Assertions.assertEquals(expectedCustomers.size(), actualCustomers.size(), "Incorrect amount of customers was returned");
    }

    @Test
    public void findByPostCodeTest() {
        // Preconditions:
        driver.get(CUSTOMERS_LINK);
        List<Map<String, String>> customers = customersPage.getCustomers();
        var expectedCustomers = customers.stream()
                .filter(cust -> cust.get("postCode").equals(customers.get(0).get("postCode")))
                .collect(Collectors.toList());

        // Actions:
        customersPage.findCustomer(customers.get(0).get("postCode"));

        // Result:
        String currentPage = driver.getCurrentUrl();
        List<Map<String, String>> actualCustomers = customersPage.getCustomers();

        Assertions.assertEquals(CUSTOMERS_LINK, currentPage, "The page was changed");

        Assertions.assertTrue(actualCustomers.containsAll(expectedCustomers), "Incorrect customers returned");
        Assertions.assertEquals(expectedCustomers.size(), actualCustomers.size(), "Incorrect amount of customers was returned");
    }

    @Test
    public void addDuplicatedCustomerTest() {

        // Preconditions:
        driver.get(CUSTOMERS_LINK);
        List<Map<String, String>> existingCustomers = customersPage.getCustomers();
        driver.get(ADD_CUST_LINK);

        // Actions:
        addCustomerPage.inputFirstName(existingCustomers.get(0).get("firstName"));
        addCustomerPage.inputLastName(existingCustomers.get(0).get("lastName"));
        addCustomerPage.inputPostCode(existingCustomers.get(0).get("postCode"));
        addCustomerPage.clickAddCustomerBtn();

        // Result:
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();
        alert.accept();
        Assertions.assertEquals("Please check the details. Customer may be duplicate.", text, "Incorrect message was returned");
        String currentPage = driver.getCurrentUrl();

        Assertions.assertEquals(ADD_CUST_LINK, currentPage, "The page was changed");

        driver.get(CUSTOMERS_LINK);
        List<Map<String, String>> customers = customersPage.getCustomers().stream()
                .filter(c -> c.containsValue(existingCustomers.get(0).get("firstName"))
                                && c.containsValue(existingCustomers.get(0).get("lastName"))
                                && c.containsValue(existingCustomers.get(0).get("postCode")))
                .collect(Collectors.toList());

        Assertions.assertEquals(1, customers.size(), "The customer was created");
    }
}
