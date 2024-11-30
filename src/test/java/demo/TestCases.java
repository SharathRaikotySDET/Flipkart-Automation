package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;


// import io.github.bonigarcia.wdm.WebDriverManager;
import demo.wrappers.Wrappers;
import demo.wrappers.Wrappers.ItemDetails;

public class TestCases {
    ChromeDriver driver;

    @Test
    public void testCase01() throws InterruptedException {
        System.out.println("Start Test Case: testCase01");
        driver.get("http://www.flipkart.com/");
       
        Wrappers.handleLoginPopUP(driver);
        // Search "Washing Machine"
        WebElement searchBox = driver.findElement(By.name("q"));
        Wrappers.searchAndEnter(searchBox, "Washing Machine", driver);

        

        WebElement popularityOption = driver.findElement(By.xpath("//div[text()='Popularity']"));
        Wrappers.click(popularityOption, driver);

        
        int count=0;

        for (int i = 0; i < 3; i++) { // Retry loop
    try {
        List<WebElement> ratings = driver.findElements(By.xpath("//span[contains(@id, 'productRating')]/div"));
        for (WebElement rating : ratings) {
            String ratingText = rating.getText();
            double ratingValue = Double.parseDouble(ratingText);
            if (ratingValue <= 4) {
                count++;
            }
        }
        break; 
    } catch (StaleElementReferenceException e) {
        System.out.println("Stale Element Exception occurred, retrying...");
    }
}
        
        System.out.println("Count of items with rating <= 4 stars: " + count);
        System.out.println("End Test Case: testCase01");
    }

    @Test
    public void testCase02() throws InterruptedException {
        System.out.println("Start Test Case: testCase02");
        driver.get("http://www.flipkart.com/");
        Thread.sleep(2000);

        Wrappers.handleLoginPopUP(driver);

        // Search "iPhone"
        WebElement searchBox = driver.findElement(By.name("q"));
        Wrappers.searchAndEnter(searchBox, "iPhone", driver);

        // Fetch titles and discounts with > 17% discount
        List<WebElement> items = driver.findElements(By.xpath("//div[@class='_75nlfW']"));
        for (WebElement item : items) {
            try{
            WebElement discountElement = item.findElement(By.xpath(".//div[@class='UkUFwK']"));
            WebElement titleElement = item.findElement(By.xpath(".//div[@class='KzDlHZ']"));
            if (discountElement != null && titleElement != null) {
                String discountText = discountElement.getText().replace("% off", "").trim();
                int discount = Integer.parseInt(discountText);
                if (discount > 17) {
                    System.out.println("Title: " + titleElement.getText() + ", Discount: " + discount + "%");
                }
            }}catch(Exception e){
                System.out.println("Discount element is null");
            }
        }
        System.out.println("End Test Case: testCase02");
    }

    @Test
    public void testCase03() throws InterruptedException {
        System.out.println("Start Test Case: testCase03");
        driver.get("http://www.flipkart.com/");
        Thread.sleep(2000);
       Wrappers.handleLoginPopUP(driver);
        

        // Search "Coffee Mug"
        WebElement searchBox = driver.findElement(By.name("q"));
        Wrappers.searchAndEnter(searchBox, "Coffee Mug", driver);

        Thread.sleep(2000);
        // Filter for 4 stars and above
        WebElement filter4Stars = driver.findElement(By.xpath("(//div[@class='XqNaEv'])[1]"));
        Wrappers.click(filter4Stars, driver);

   Thread.sleep(2000);
        // Fetch titles and image URLs of 5 items with highest reviews
        List<WebElement> items = driver.findElements(By.xpath("//div[@class='slAVV4']"));
         List<Wrappers.ItemDetails> topItems = Wrappers.getTopRatedItems(items, driver, 5);
         System.out.println("Top 5 items with 4★ & above and highest reviews:");
    for (Wrappers.ItemDetails item : topItems) {
        System.out.println("Title: " + item.getTitle() + ", Image URL: " + item.getImageURL() + ", Reviews: " + item.getReviews());
    }

    System.out.println("End Test Case: testCase03");
    }


    
    
    @BeforeTest
    public void startBrowser()
    {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        // NOT NEEDED FOR SELENIUM MANAGER
        // WebDriverManager.chromedriver().timeout(30).setup();

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log"); 

        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
    }

    @AfterTest
    public void endTest()
    {
        driver.close();
        driver.quit();

    }}
