package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;
// import static org.junit.Assert.assertTrue;
import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.checkerframework.common.reflection.qual.NewInstance;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import QKART_TESTNG.ListenerClass;
import org.testng.annotations.Listeners;
import QKART_TESTNG.ListenerClass;

// @Listeners(ListenerClass.class)
public class QKART_Tests {

    static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

    @BeforeSuite(alwaysRun = true)
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
    @Test(description = "Verify registration happens correctly", priority = 1, groups = {"sanity"})
    @Parameters({"user", "pass"})
    public void TestCase01(String user, String pass) throws InterruptedException {
        takeScreenshot(driver, "TestCase01", "Verify User Registration");
        Boolean status;
        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser(user, pass, true);
        takeScreenshot(driver, "TestCase01", "Testing registration & login");
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, pass);
        takeScreenshot(driver, "TestCase01", "Testing registration & login");
        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();
        takeScreenshot(driver, "TestCase01", "Verify User Registration");
        assertTrue(status, "Unable to logout successfully");


    }

    @Test(description = "Verify re-registering an already registered user fails", priority = 2,
            groups = {"sanity"})
    public void TestCase02() throws InterruptedException {
        Boolean status;
        takeScreenshot(driver, "TestCase02", " Verify User Registration with an existing username");
        SoftAssert softassert = new SoftAssert();
        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        takeScreenshot(driver, "TestCase02", " Verify User Registration with an existing username");
        softassert.assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the Registration page and try to register using the previously
        // registered user's credentials
        registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);

        // If status is true, then registration succeeded, else registration has
        // failed. In this case registration failure means Success
        takeScreenshot(driver, "TestCase02", " Verify User Registration with an existing username");
        softassert.assertFalse(status, "Failed to register new user");
        softassert.assertAll();

    }

    @Test(description = "Verify the functionality of search text box", priority = 3,
            groups = {"sanity"})
    public void TestCase03() throws InterruptedException {
        takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
        // logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;

        // Visit the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for the "yonex" product

        status = homePage.searchForProduct("YONEX");
        takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
        assertTrue(status, "Test Case Failure. Unable to search for given product");


        // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();

        // Verify the search results are available
        takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
        assertFalse(searchResults.size() == 0,
                "Test Case Failure. There were no results for the given search string");


        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");

            assertTrue(elementText.toUpperCase().contains("YONEX"),
                    "Test Case Failure. Test Results contains un-expected values: " + elementText);
        }
        // Search for product
        status = homePage.searchForProduct("Gesundheit");
        takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
        assertFalse(status, "Test Case Failure. Invalid keyword returned results");

        // Verify no search results are found
        searchResults = homePage.getSearchResults();
        takeScreenshot(driver, "TestCase03", "Verify functionality of search box ");
        assertTrue(searchResults.size() == 0, "Products found for invalid string");
        assertTrue(homePage.isNoResultFound(), "No products found message is not displayed");

    }

    @Test(description = "Verify the existence of size chart for certain items and validate contents of size chart",
            priority = 4, groups = {"regression"})
    public void TestCase04() throws InterruptedException {
        takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
        // logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
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
            takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
            assertTrue(result.verifySizeChartExists(),
                    "Test Case Fail. Size Chart Link does not exist");

            // Verify if size dropdown exists
            status = result.verifyExistenceofSizeDropdown(driver);
            takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
            assertTrue(status, "Drop down not found");

            // Open the size chart
            takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
            assertTrue(result.openSizechart(), "Test Case Fail. Failure to open Size Chart");


            takeScreenshot(driver, "TestCase04", "Verify the presence of size Chart ");
            assertTrue(result.validateSizeChartContents(expectedTableHeaders, expectedTableBody,
                    driver), "Failure while validating contents of Size Chart Link");

            // Close the size chart modal
            status = result.closeSizeChart(driver);

        }

    }

    @Test(description = "Verify that a new user can add multiple products in to the cart and Checkout",
            priority = 5, groups = {"sanity"})
    @Parameters({"user", "pass", "tc5_prod1", "tc5_prod2", "address"})
    public void TestCase05(String user, String pass, String tc5_prod1, String tc5_prod2,
            String address) throws InterruptedException {
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        Boolean status;
        // logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");

        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();

        // Register a new user
        status = registration.registerUser(user, pass, true);
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        assertTrue(status, "Unable to register");

        // Save the username of the newly registered user
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Go to the login page
        Login login = new Login(driver);
        login.navigateToLoginPage();

        // Login with the newly registered user's credentials
        status = login.PerformLogin(lastGeneratedUserName, pass);
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        assertTrue(status, "Unable to login");

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct(tc5_prod1);
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        assertTrue(status, "No product found");
        homePage.addProductToCart(tc5_prod1);
        status = homePage.searchForProduct(tc5_prod2);
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        assertTrue(status, "No product found");
        homePage.addProductToCart(tc5_prod2);

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress(address);

        // Place the order
        checkoutPage.placeOrder();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();

        // Log out the user
        homePage.PerformLogout();

        // logStatus("End TestCase", "Test Case 5: Happy Flow Test Completed : ", status ? "PASS" :
        // "FAIL");
        takeScreenshot(driver, "TestCase05", "Verify Happy Flow of buying products ");
        assertTrue(status, "Test Case 5: Happy Flow Test Failed");

    }

    @Test(description = "Verify that the contents of the cart can be edited", priority = 6,
            groups = {"regression"})
    @Parameters({"user", "pass", "tc6_prod1", "tc6_prod2", "address"})
    public void TestCase06(String user, String pass, String tc6_prod1, String tc6_prod2,
            String address) throws InterruptedException {
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        Boolean status;
        // logStatus("Start TestCase", "Test Case 6: Verify that cart can be edited", "DONE");
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser(user, pass, true);
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        assertTrue(status, "Unable to register");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, pass);
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        assertTrue(status, "Unable to login");

        homePage.navigateToHome();
        status = homePage.searchForProduct(tc6_prod1);
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        assertTrue(status, "No product found");
        homePage.addProductToCart(tc6_prod1);

        status = homePage.searchForProduct(tc6_prod2);
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        assertTrue(status, "No product found");
        homePage.addProductToCart(tc6_prod2);

        // update watch quantity to 2
        homePage.changeProductQuantityinCart(tc6_prod1, 2);

        // update table lamp quantity to 0
        Thread.sleep(2000);
        homePage.changeProductQuantityinCart(tc6_prod2, 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart(tc6_prod1, 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress(address);

        checkoutPage.placeOrder();

        try {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(
                    ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));
        } catch (TimeoutException e) {
            System.out.println("Error while placing order in: " + e.getMessage());
            takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
            assertFalse(false, "Test Case 6: Verify that cart can be edited failed");
        }

        status = driver.getCurrentUrl().endsWith("/thanks");

        homePage.navigateToHome();
        homePage.PerformLogout();

        // logStatus("End TestCase", "Test Case 6: Verify that cart can be edited: ", status ?
        // "PASS" : "FAIL");
        takeScreenshot(driver, "TestCase06", " Verify that cart can be edited");
        assertTrue(status, "Test Case 6: Verify that cart can be edited failed");
    }

    @Test(description = "Verify that insufficient balance error is thrown when the wallet balance is not enough",
            priority = 7, groups = {"sanity"})
    @Parameters({"user", "pass", "tc7_prod1", "address"})
    public void TestCase07(String user, String pass, String tc7_prod1, String address)
            throws InterruptedException {
        takeScreenshot(driver, "TestCase07",
                "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
        Boolean status;
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser(user, pass, true);

        takeScreenshot(driver, "TestCase07",
                "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
        assertTrue(status, "Unable to register");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, pass);

        takeScreenshot(driver, "TestCase07",
                "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
        assertTrue(status, "Unable to login");

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct(tc7_prod1);

        takeScreenshot(driver, "TestCase07",
                "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
        assertTrue(status, "No product found");

        homePage.addProductToCart(tc7_prod1);

        homePage.changeProductQuantityinCart(tc7_prod1, 10);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress(address);
        checkoutPage.selectAddress(address);

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        takeScreenshot(driver, "TestCase07",
                "  Verify that insufficient balance error is thrown when the wallet balance is not enough");
        assertTrue(status,
                "Test Case 7: Verify that insufficient balance error is thrown when the wallet balance is not enough failed");


    }

    @Test(description = "Verify that a product added to a cart is available when a new tab is added",
            priority = 8, groups = {"regression"})
    public void TestCase08() throws InterruptedException {
        takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
        Boolean status = false;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
        assertTrue(status, "Unable to register");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
        assertTrue(status, "Unable to login");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
        assertTrue(status, "No product found");

        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);

        Thread.sleep(2000);
        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        takeScreenshot(driver, "TestCase08", "Verify New tab shows added product");
        assertTrue(status,
                "Test Case 8: Verify that product added to cart is available when a new tab is opened failed");
    }

    @Test(description = "Verify that privacy policy and about us links are working fine",
            priority = 9, groups = {"regression"})
    public void TestCase09() throws InterruptedException {
        Boolean status = false;
        SoftAssert sa = new SoftAssert();
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        assertTrue(status, "Unable to register");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        assertTrue(status, "unable to login");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);
        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        sa.assertFalse(status,
                "Verifying parent page url didn't change on privacy policy link click failed");

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");

        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        sa.assertTrue(status, "Verifying new tab opened has Privacy Policy page heading failed");

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        driver.findElement(By.linkText("Terms of Service")).click();
        status = driver.getCurrentUrl().equals(basePageURL);

        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        sa.assertFalse(status,
                "Verifying parent page url didn't change on privacy policy link click failed");

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");

        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        sa.assertTrue(status, "Verifying new tab opened has Terms Of Service page heading failed");

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        takeScreenshot(driver, "TestCase09", "Verify privacy Policy & Terms of Service Link");
        assertTrue(status,
                "Test Case 9: Verify that the Privacy Policy, About Us are displayed correctly failed");
    }

    @Test(description = "Verify that the contact us dialog works fine", priority = 10,
            groups = {"regression"})
    public void TestCase10() throws InterruptedException {
        takeScreenshot(driver, "TestCase10", "Verify that the contact link works");
        Boolean status;

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        WebElement contactUs = driver.findElement(By.xpath("//*[text()='Contact us']"));
        status = contactUs.isDisplayed();
        takeScreenshot(driver, "TestCase10", "Verify that the contact link works");
        assertTrue(status, "Unable to find contactUs");
        contactUs.click();

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys("crio user");
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys("criouser@gmail.com");
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys("Testing the contact us page");

        WebElement submit = driver.findElement(By.xpath(
                "/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));
        submit.click();


        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf(submit));
        takeScreenshot(driver, "TestCase10", "Verify that the contact link works");

    }

    @Test(description = "Ensure that the Advertisement Links on the QKART page are clickable",
            priority = 11, groups = {"sanity"})
    public void TestCase11() throws InterruptedException {
        Boolean status = false;
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        assertTrue(status, "Unable to Register");

        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        assertTrue(status, "Unable to login");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        assertTrue(status, "No product found");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
        status = checkoutPage.placeOrder();
        Thread.sleep(3000);
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        assertTrue(status, "unable to place order");

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        assertTrue(Advertisements.size() == 3, "Verify that 3 Advertisements are available fail");

        WebElement Advertisement1 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        assertTrue(status, "Verify that Advertisement 1 is clickable fail");


        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 =
                driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        takeScreenshot(driver, "TestCase11", "Check for advertisement");
        assertTrue(status, "Verify that Advertisement 2 is clickable fail");

    }


    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s",
                String.valueOf(java.time.LocalDateTime.now()), type, message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType,
                    description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

