/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package QKART_SANITY_LOGIN.Module1;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class QkartSanity {

    public static String lastGeneratedUserName;
    

    public static RemoteWebDriver createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
       

        return driver;
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        // TODO: CRIO_TASK_MODULE_SYNCHRONISATION - Implement method using below steps
        /*
         * 1. Check if the folder "/screenshots" exists, create if it doesn't
         * 2. Generate a unique string using the timestamp
         * 3. Capture screenshot
         * 4. Save the screenshot inside the "/screenshots" folder using the following
         * naming convention: screenshot_<Timestamp>_<ScreenshotType>_<Description>.png
         * eg: screenshot_2022-03-05T06:59:46.015489_StartTestcase_Testcase01.png
         */
        try{
            File theDir=new File("/screenshots");
            if(!theDir.exists()){
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
			String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);

			TakesScreenshot scrShot = ((TakesScreenshot) driver);
			File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

			File DestFile = new File("screenshots/" + fileName);
			FileUtils.copyFile(SrcFile, DestFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /*
     * Testcase01: Verify the functionality of Login button on the Home page
     */
    public static Boolean TestCase01(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "TestCase01", "Verify User Registration");
        logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "FAIL");
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");

            // Return False as the test case Fails
            takeScreenshot(driver, "TestCase01", "Testing registration & login");
            takeScreenshot(driver, "TestCase01", "Verify User Registration");
            return false;
        } else {
            logStatus("TestCase 1", "Test Case Pass. User Registration Pass", "PASS");
        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "TestCase01", "Testing registration & login");
            takeScreenshot(driver, "TestCase01", "Verify User Registration");
            return false;
        }

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();
        logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status ? "PASS" : "FAIL");
        takeScreenshot(driver, "TestCase01", "Verify User Registration");
        return status;
    }

    /*
     * Verify that an existing user is not allowed to re-register on QKart
     */
    public static Boolean TestCase02(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "TestCase02", " Verify User Registration with an existing username");
        logStatus("Start Testcase", "Test Case 2: Verify User Registration with an existing username ", "DONE");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        logStatus("Test Step", "User Registration : ", status ? "PASS" : "FAIL");
        if (!status) {
            logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "PASS" : "FAIL");
            takeScreenshot(driver, "TestCase02", " Verify User Registration with an existing username");
            return false;

        }

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status ? "FAIL" : "PASS");
        takeScreenshot(driver, "TestCase02", " Verify User Registration with an existing username");
        return !status;
    }

    /*
     * Verify the functinality of the search text box
     */
    public static Boolean TestCase03(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
        logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product
        status = homePage.searchForProduct("yonex");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
            takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
            return false;
        }
        

        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        if (searchResults.size() == 0) {
            logStatus("TestCase 3", "Test Case Failure. There were no results for the given search string", "FAIL");
            takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
            return false;
        }

        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            if (!elementText.toUpperCase().contains("YONEX")) {
                logStatus("TestCase 3", "Test Case Failure. Test Results contains un-expected values: " + elementText,
                        "FAIL");
                 takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");    
                return false;
            }
        }

        logStatus("Step Success", "Successfully validated the search results ", "PASS");
        // SLEEP_STMT_02
        //Thread.sleep(2000);

        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        if (!status) {
            logStatus("TestCase 3", "Test Case Failure. Unable to search for given product", "FAIL");
            takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
            return false;
        }
       

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        if (searchResults.size() == 0) {
            if (homePage.isNoResultFound()) {
                logStatus("Step Success", "Successfully validated that no products found message is displayed", "PASS");
            }
            logStatus("TestCase 3", "Test Case PASS. Verified that no search results were found for the given text",
                    "PASS");
        } else {
            logStatus("TestCase 3", "Test Case Fail. Expected: no results , actual: Results were available", "FAIL");
            takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
            return false;
        }
        takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
        return true;
    }

    /*
     * Verify the presence of size chart and check if the size chart content is as
     * expected
     */
    public static Boolean TestCase04(RemoteWebDriver driver) throws InterruptedException {
        takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
        logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        //Thread.sleep(2000);
        List<WebElement> searchResults = homePage.getSearchResults();

        // Create expected values
        List<String> expectedTableHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTableBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));

        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists()) {
                takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
                logStatus("Step Success", "Successfully validated presence of Size Chart Link", "PASS");

                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
                logStatus("Step Success", "Validated presence of drop down", status ? "PASS" : "FAIL");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTableHeaders, expectedTableBody, driver)) {
                        logStatus("Step Success", "Successfully validated contents of Size Chart Link", "PASS");
                    } else {
                        logStatus("Step Failure", "Failure while validating contents of Size Chart Link", "FAIL");
                    }
                   // Thread.sleep(2000);
                    // Close the size chart modal
                    status = result.closeSizeChart(driver);
                   // Thread.sleep(2000);

                } else {
                    logStatus("TestCase 4", "Test Case Fail. Failure to open Size Chart", "FAIL");
                    takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
                    return false;
                }

            } else {
                logStatus("TestCase 4", "Test Case Fail. Size Chart Link does not exist", "FAIL");
                takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
                return false;
            }
        }
        logStatus("TestCase 4", "Test Case PASS. Validated Size Chart Details", "PASS");
        takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
        return status;
    }

    /*
     * Verify the complete flow of checking out and placing order for products is
     * working correctly
     */
    public static Boolean TestCase05(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
            logStatus("TestCase 5", "Test Case Failure. Happy Flow Test Failed", "FAIL");

        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase", "Test Case 5: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
       // Thread.sleep(2000);
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();
        // SLEEP_STMT_04: Wait for place order to succeed and navigate to Thanks page
        WebDriverWait wait=new WebDriverWait(driver, 3);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();
       // Thread.sleep(2000);

        // Log out the user
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" : "FAIL");
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        return status;
    }

    /*
     * Verify the quantity of items in cart can be updated
     */
    public static Boolean TestCase06(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 06: MILESTONE 5

        // TODO: Register a new user
       
        registration.navigateToRegisterPage();

        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("TestCase 6", "Test Case Failure. Happy Flow Test Failed", "FAIL");
            takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        }
        lastGeneratedUserName=registration.lastGeneratedUsername;
        // TODO: Login using the newly registed user
      
       login.navigateToLoginPage();

       status = login.PerformLogin(lastGeneratedUserName, "abc@123");
       if (!status) {
           logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
           logStatus("End TestCase", "Test Case 6: Happy Flow Test Failed : ", status ? "PASS" : "FAIL");
           takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
       }
    
        // TODO: Add "Xtend Smart Watch" to cart
        status=homePage.searchForProduct("watch");
        homePage.addProductToCart("Xtend Smart Watch");

        // TODO: Add "Yarine Floor Lamp" to cart
        status=homePage.searchForProduct("lamp");
        homePage.addProductToCart("Yarine Floor Lamp");
         Thread.sleep(2000);
        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 3);
        Thread.sleep(2000);
        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);
        Thread.sleep(2000);
        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);
        Thread.sleep(2000);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        
        WebDriverWait wait=new WebDriverWait(driver,2);
        wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@id='notistack-snackbar']"), "Order placed successfully!"));

        status = driver.getCurrentUrl().endsWith("/thanks");

        homePage.navigateToHome();
        
        homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ? "PASS" : "FAIL");
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        return status;
    }


    public static Boolean TestCase07(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "TestCase07", "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
        logStatus("Start TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough",
                "DONE");
                takeScreenshot(driver, "TestCase07", "  Verify that insufficient balance error is thrown when the wallet balance is not enough");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            logStatus("Step Failure", "User Perform Registration Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
                    takeScreenshot(driver, "TestCase07", "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
            return false;
        }
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            logStatus("Step Failure", "User Perform Login Failed", status ? "PASS" : "FAIL");
            logStatus("End TestCase",
                    "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                    status ? "PASS" : "FAIL");
                    takeScreenshot(driver, "TestCase07", "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
            return false;
        }

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set");
        //Thread.sleep(3000);

        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set ", 12);
       // Thread.sleep(2000);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        logStatus("End TestCase",
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough: ",
                status ? "PASS" : "FAIL");
                takeScreenshot(driver, "TestCase07", "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
        return status;
    }

    public static Boolean TestCase08(RemoteWebDriver driver) throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
        logStatus("Start TestCase", "Test Case 8: Verify New tab shows added product", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            takeScreenshot(driver, "TestCase08", "Verify New tab shows added product ");
            logStatus("TestCase 8", "Test Case Failure. New tab shows added product Test Failed", "FAIL");

        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
            logStatus("Step Failure", "User Perform Login Failed: ", "FAIL");
            logStatus("End TestCase", "Test Case 8: New tab shows added product Test Failed : ", "FAIL");
            return false;
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        status = homePage.searchForProduct("Stylecon");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set");

        String parent=driver.getWindowHandle();

        driver.switchTo().newWindow(WindowType.TAB);
        driver.get("https://crio-qkart-frontend-qa.vercel.app");

       
        
        status=homePage.cart("YONEX Smash Badminton Racquet");
        status=homePage.cart("Stylecon 9 Seater RHS Sofa Set");
        if (!status) {
            takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
            logStatus("Step Failure", "Unable to find the product in cart: ",  "FAIL");
            logStatus("End TestCase", "Test Case 8: New tab shows added product Test Failed : ", "FAIL");
            return false;
        }
       
        driver.close();
        
        driver.switchTo().window(parent);

       status= homePage.PerformLogout();

        logStatus("End TestCase", "Test Case 8: New tab shows added product Test Completed : ", status ? "PASS" : "FAIL");
        takeScreenshot(driver, "TestCase08", "Verify New tab shows added product ");
        return status;
    }

    public static boolean TestCase09(RemoteWebDriver driver) throws InterruptedException{
        boolean status;
        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        logStatus("Start TestCase", "Test Case 9: Verify privacy Policy & Terms of Service Link", "DONE");
       
        Home homepage=new Home(driver);
        homepage.navigateToHome();

        //String parent=driver.getWindowHandle();

        homepage.footer(driver,"//a[@href='privacy-policy']");


         status=driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/");
         if (!status) {
            takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
            logStatus("Step Failure", "Focus on Home Page URL: ", "FAIL");
            logStatus("End TestCase", "Test Case 9: Verify privacy Policy & Terms of Service Link : ","FAIL");
            return false;
        }
        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        status=homepage.switchWindow(driver,"https://crio-qkart-frontend-qa.vercel.app/privacy-policy","//*[@id='root']/div/div[2]/h2","Privacy Policy");

        
            logStatus("Test Step", "Content Found: ", status ? "PASS" : "FAIL");
          

        homepage.footer(driver,"//a[@href='terms-of-service']");

        status=driver.getCurrentUrl().equals("https://crio-qkart-frontend-qa.vercel.app/");
         if (!status) {
            takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
            logStatus("Step Failure", "Focus on Home Page URL: ", "FAIL");
            logStatus("End TestCase", "Test Case 9: Verify privacy Policy & Terms of Service Link : ","FAIL");
            return false;
        }
     
        
        status=homepage.switchWindow(driver, "https://crio-qkart-frontend-qa.vercel.app/terms-of-service","//*[@id='root']/div/div[2]/h2","Terms of Service");

          takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
            logStatus("Test Step", "Content Found: ", status ? "PASS" : "FAIL");
          
    
       
        homepage.closeTab(driver);
        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        logStatus("End TestCase", "Test Case 9: Verify privacy Policy & Terms of Service Link", status?"Pass":"Fail");
        return status;
    }


public static boolean TestCase10(RemoteWebDriver driver) throws InterruptedException{
    takeScreenshot(driver, "TestCase10","Verify that the contact link works");
    logStatus("Start TestCase", "Test Case 10: Verify that the contact link works", "DONE");
boolean status;
Home homepage=new Home(driver);
homepage.navigateToHome();
status=homepage.checkAds(driver);
    takeScreenshot(driver, "TestCase10", "Verify that the contact link works");
    logStatus("End TestCase", "Test Case 10: Verify that the contact link works", status?"PASS":"FAIL");
   return status;

}
public static boolean TestCase11(RemoteWebDriver driver, int i, int j) throws InterruptedException{
    Boolean status;
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        logStatus("Start TestCase", "Test Case 11:Check for advertisement", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser("testUser", "abc@123", true);
        if (!status) {
            takeScreenshot(driver, "TestCase11", "Check for advertisement ");
            logStatus("TestCase 11", "Test Case Failure. Check for advertisement Test Failed", "FAIL");

        }

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        if (!status) {
            takeScreenshot(driver, "TestCase11", "Check for advertisement");
            logStatus("Step Failure", "User Perform Login Failed: ", "FAIL");
            logStatus("End TestCase", "Test Case 11:Check for advertisement Test Failed : ", "FAIL");
            return false;
        }

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("Yonex");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

       homePage.clickCheckout();

       Checkout checkoutPage=new Checkout(driver);

    checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
    checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

    checkoutPage.placeOrder();
    
    WebDriverWait wait=new WebDriverWait(driver,5);
    wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

    status = driver.getCurrentUrl().endsWith("/thanks");

    String baseURL=driver.getCurrentUrl();

   int size=driver.findElements(By.xpath("//iframe")).size();
    status=size==3;

    if (!status) {
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        logStatus("Step Failure", "Desired Iframes Not present: ", "FAIL");
    }

        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/iframe[1]")));
       // status=driver.findElement(By.xpath("//button[text()='Buy Now']")).isEnabled();
        //logStatus("Test Step", "Buy Now button is clickable: ", status?"PASS":"FAIL");
        status=driver.findElement(By.xpath("//button[text()='View Cart']")).isEnabled();
        logStatus("Test Step", "View Cart button is clickable: ", status?"PASS":"FAIL");
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();

        driver.switchTo().parentFrame();
        status=!driver.getCurrentUrl().equals(baseURL);
        logStatus("Test Step", "Back to Parent Frame: ", status?"PASS":"FAIL");

        driver.get(baseURL);
        Thread.sleep(3000);

        driver.switchTo().frame(driver.findElement(By.xpath("//*[@id='root']/div/div[2]/div/iframe[2]")));
        status=driver.findElement(By.xpath("//button[text()='View Cart']")).isEnabled();
        logStatus("Test Step", "View Cart button is clickable: ", status?"PASS":"FAIL");
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
       // logStatus("Test Step", "Buy Now button is clickable: ", status?"PASS":"FAIL");
        driver.switchTo().parentFrame();
        status=!driver.getCurrentUrl().equals(baseURL);
        logStatus("Test Step", "Back to Parent Frame: ", status?"PASS":"FAIL");

        driver.get(baseURL);
        Thread.sleep(3000);

    homePage.navigateToHome();
    
    homePage.PerformLogout();

    logStatus("End TestCase", "Test Case 11: Check for advertisement Test: ", status ? "PASS" : "FAIL");
    takeScreenshot(driver, "End Test Case", "TestCase11");
    return status;
}


    public static void main(String[] args) throws InterruptedException, MalformedURLException {
        int totalTests = 0;
        int passedTests = 0;
        Boolean status;
        // Maximize and Implicit Wait for things to initailize
        RemoteWebDriver driver=createDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        

        try {
            // Execute Test Case 1
             totalTests += 1;
             status = TestCase01(driver);
            if (status) {
                passedTests += 1;
            }

            System.out.println("");

           // Execute Test Case 2
            totalTests += 1;
            status = TestCase02(driver);
            if (status) {
                passedTests += 1;
            }
       
          System.out.println("");
            // Execute Test Case 3
              totalTests += 1;
            status = TestCase03(driver);
            if (status) {
            passedTests += 1;
             }

   
          System.out.println("");
     // Execute Test Case 4
            totalTests += 1;
            status = TestCase04(driver);
            if (status) {
            passedTests += 1;
             }
    
            System.out.println("");

            // Execute Test Case 5
            totalTests += 1;
            status = TestCase05(driver);
            if (status) {
             passedTests += 1;
             }

        System.out.println("");
            
             // Execute Test Case 6
             totalTests += 1;
            status = TestCase06(driver);
            if (status) {
            passedTests += 1;
            }

       System.out.println("");

          // Execute Test Case 7
         totalTests += 1;
            status = TestCase07(driver);
            if (status) {
            passedTests += 1;
             }

            System.out.println("");

            totalTests += 1;
            status = TestCase08(driver);
            if (status) {
            passedTests += 1;
             }

             System.out.println("");

             totalTests += 1;
             status = TestCase09(driver);
             if (status) {
             passedTests += 1;
              }

            System.out.println("");

             totalTests += 1;
             status = TestCase10(driver);
             if (status) {
             passedTests += 1;
                } 

            System.out.println("");

           totalTests += 1;
             status = TestCase11(driver, 0, 0);
             if (status) {
             passedTests += 1;
                } 
 
        } 
        catch (Exception e) {
            throw e;
        } finally {
            // quit Chrome Driver
            driver.quit();

            System.out.println(String.format("%s out of %s test cases Passed ", Integer.toString(passedTests),
                    Integer.toString(totalTests)));
        }

    }
}