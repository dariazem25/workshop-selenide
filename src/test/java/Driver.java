import org.openqa.selenium.chrome.ChromeDriver;

public class Driver {

    public static ChromeDriver getDriver() {
        String chromeDriverPath = "src\\test\\resources\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);
        return new ChromeDriver();
    }
}
