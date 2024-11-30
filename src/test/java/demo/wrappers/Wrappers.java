package demo.wrappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class Wrappers {

    public static class ItemDetails {
        private String title;
        private String imageURL;
        private int reviews;
    
        public ItemDetails(String title, String imageURL, int reviews) {
            this.title = title;
            this.imageURL = imageURL;
            this.reviews = reviews;
        }
    
        public String getTitle() {
            return title;
        }
    
        public String getImageURL() {
            return imageURL;
        }
    
        public int getReviews() {
            return reviews;
        }
    }
    
    public static void handleLoginPopUP(ChromeDriver driver){
        try {
            WebElement closeButton = driver.findElement(By.xpath("//button[contains(text(),'✕')]"));
            Wrappers.click(closeButton, driver);
        } catch (Exception e) {
            System.out.println("Login popup not displayed.");
        }
    }

     public static void click(WebElement elementToClick, ChromeDriver driver) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.elementToBeClickable(elementToClick));
    
       
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            jsExecutor.executeScript("arguments[0].scrollIntoView({ behavior: 'smooth', block: 'center' });", elementToClick);
    
          
            try {
                elementToClick.click();
            } catch (Exception e) {
                jsExecutor.executeScript("arguments[0].click();", elementToClick);
            }
        } catch (Exception e) {
            System.err.println("Error clicking element: " + e.getMessage());
            throw e;
        }
    }
    

    public static void sendKeys(WebElement element, String textToEnter, ChromeDriver driver) {
        if (driver != null) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(element));
            if (element.isDisplayed()) {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
                element.clear();
                element.sendKeys(textToEnter);
            }
        }
    }
    public static void searchAndEnter(WebElement searchBox, String searchText, ChromeDriver driver) {
        if (driver != null) {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.visibilityOf(searchBox)); 
    
            if (searchBox.isDisplayed()) {
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("arguments[0].scrollIntoView(true);", searchBox); 
                searchBox.clear(); 
                searchBox.sendKeys(searchText); 
                searchBox.sendKeys(Keys.ENTER); 
            }
        }
    }
    public static List<ItemDetails> getTopRatedItems(List<WebElement> items, ChromeDriver driver, int limit) {
        List<ItemDetails> filteredItems = new ArrayList<>();
        for (WebElement item : items) {
            try {
                // Fetch title
                WebElement titleElement = item.findElement(By.xpath(".//a[contains(@class, 'wjcEIp')]"));
                String title = titleElement.getText();

                // Fetch image URL
                WebElement imageElement = item.findElement(By.xpath(".//img[@class='DByuf4']"));
                String imageURL = imageElement.getAttribute("src");

                // Fetch number of reviews
                WebElement reviewsElement = item.findElement(By.xpath(".//span[@class='Wphh3N']"));
                String reviewsText = reviewsElement.getText().split(" ")[0].replace(",", "");
                int numberOfReviews = Integer.parseInt(reviewsText);

                // Add to list
                filteredItems.add(new ItemDetails(title, imageURL, numberOfReviews));
            } catch (Exception e) {
               System.out.println("Invalid Iterm or No matcing item found");
               
            }
        }

        // Sort items by reviews in descending order
        Collections.sort(filteredItems, Comparator.comparingInt(ItemDetails::getReviews).reversed());

        // Return the top 'limit' items
        return filteredItems.subList(0, Math.min(limit, filteredItems.size()));
    }
}
    

