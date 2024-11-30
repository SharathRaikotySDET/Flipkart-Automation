# Flipkart Automation Project

This project is an automation test suite designed to validate key functionalities on the Flipkart website. It leverages **Selenium WebDriver** with **TestNG** for testing and incorporates a reusable **Wrappers** class for handling common Selenium operations.

---

## Project Structure

- **`src/test/java`**: Contains TestNG test cases.
- **`Wrappers.java`**: Utility class for reusable Selenium methods like clicking, sending keys, and handling waits.
- **`TestCases.java`**: Includes the main test cases for automating Flipkart functionalities.

---

## Test Cases

### **1. Search for Washing Machines**
- **Objective:** Verify sorting by popularity and count the items with ratings ≤ 4 stars.
- **Steps:**
  1. Navigate to `www.flipkart.com`.
  2. Search for "Washing Machine".
  3. Sort results by popularity.
  4. Count and print the number of items with ratings ≤ 4 stars.

---

### **2. Search for iPhone**
- **Objective:** Print titles and discount percentages of items with more than 17% discount.
- **Steps:**
  1. Search for "iPhone".
  2. Filter items with a discount greater than 17%.
  3. Print the title and discount percentage for those items.

---

### **3. Search for Coffee Mugs**
- **Objective:** Fetch the top 5 items with the highest reviews, filtering by 4 stars and above.
- **Steps:**
  1. Search for "Coffee Mug".
  2. Filter results to show items with 4 stars and above.
  3. Select 5 items with the highest number of reviews.
  4. Print their titles and image URLs.

---

## Wrappers Usage

The **`Wrappers`** class simplifies repetitive Selenium tasks and ensures smooth browser interactions.

### **Sample Methods in `Wrappers`**

1. **`click(WebElement element, ChromeDriver driver)`**
   - Waits for the element to be clickable.
   - Scrolls into view and clicks using JavaScript if a direct click fails.

   **Example:**
   ```java
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

