package QKART_TESTNG.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Home {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app";

    public Home(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToHome() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    public Boolean PerformLogout() throws InterruptedException {
        try {
            // Find and click on the Logout Button
            WebElement logout_button = driver.findElement(By.className("MuiButton-text"));
            logout_button.click();

            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.invisibilityOfElementWithText(By.className("css-1urhf6j"), "Logout"));

            return true;
        } catch (Exception e) {
            // Error while logout
            return false;
        }
    }

    /*
     * Returns Boolean if searching for the given product name occurs without any
     * errors
     */
    public Boolean searchForProduct(String product) {
        try {
            // Clear the contents of the search box and Enter the product name in the search
            // box
            WebElement searchBox = driver.findElement(By.xpath("//input[@name='search'][1]"));
            searchBox.clear();
            searchBox.sendKeys(product);

            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//div[contains(@class,'hero')]//following-sibling::div/div/div/div/p[1]"), product.trim()));
            Thread.sleep(3000);
            return true;
        } catch (Exception e) {
            //System.out.println("Error while searching for a product: " + e.getMessage());
            WebDriverWait wait=new WebDriverWait(driver, 10);
            wait.until(ExpectedConditions.textToBe(By.xpath("//*[@id='root']/div/div/div[3]/div[1]/div[2]/div/h4"),"No products found"));
            return false;
        }
    }

    /*
     * Returns Array of Web Elements that are search results and return the same
     */
    public List<WebElement> getSearchResults() {
        List<WebElement> searchResults = new ArrayList<WebElement>() {
        };
        try {
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults = driver.findElementsByClassName("css-1qw96cp");
            return searchResults;
        } catch (Exception e) {
            System.out.println("There were no search results: " + e.getMessage());
            return searchResults;

        }
    }

    /*
     * Returns Boolean based on if the "No products found" text is displayed
     */
    public Boolean isNoResultFound() {
        Boolean status = false;
        try {
            status = driver.findElementByXPath("//h4[text()=' No products found ']").isDisplayed();
            return status;
        } catch (Exception e) {
            return status;
        }
    }

    /*
     * Return Boolean if add product to cart is successful
     */
    public Boolean addProductToCart(String productName) {
        try {
           List<WebElement> gridContent = driver.findElementsByClassName("css-sycj1h");
            for (WebElement cell : gridContent) {

                if (productName.trim().equals(cell.findElement(By.className("css-yg30e6")).getText())) {
                    cell.findElement(By.tagName("button")).click();


                    
    WebDriverWait wait = new WebDriverWait(driver, 30);
     wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(
     String.format("//*[@class='MuiBox-root css-1gjj37g']/div[1][text()='%s']", productName))));
     return true;
                }
            }
            System.out.println("Unable to find the given product");
            return false;
        } catch (Exception e) {
            System.out.println("Exception while performing add to cart: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of clicking on the checkout button
     */
    public Boolean clickCheckout() {
        Boolean status = false;
        try {
            // Find and click on the the Checkout button
            WebElement checkoutBtn = driver.findElement(By.className("checkout-btn"));
            checkoutBtn.click();

            status = true;
            return status;
        } catch (Exception e) {
            System.out.println("Exception while clicking on Checkout: " + e.getMessage());
            return status;
        }
    }

    /*
     * Return Boolean denoting the status of change quantity of product in cart
     * operation
     */
    public Boolean changeProductQuantityinCart(String productName, int quantity) {
        /*try {

            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            int currentQty;
            for (WebElement item : cartContents) {
                if (productName.equals(
                        item.findElement(By.xpath("//*[@class='MuiBox-root css-1gjj37g']/div[1]")).getText())) {
                    currentQty = Integer.valueOf(item.findElement(By.className("css-olyig7")).getText());

                    while (currentQty != quantity) {
                        if (currentQty < quantity) {
                            item.findElements(By.tagName("button")).get(1).click();

                        } else if(currentQty > quantity){
                            item.findElements(By.tagName("button")).get(0).click();
                        }

                        synchronized (driver) {
                            driver.wait(2000);
                        }

                        currentQty = Integer
                                .valueOf(item.findElement(By.xpath("//div[@data-testid=\"item-qty\"]")).getText());
                    }

                    return true;
                }
            }

            return false;
        } catch (Exception e) {
            if (quantity == 0)
                return true;
            System.out.println(("exception occurred when updating cart"));
            return false;
        }
        
    }*/
    try {
        boolean status=false;
        Actions action=new Actions(driver);
    WebElement parent=driver.findElement(By.xpath("//div[@class='cart MuiBox-root css-0']"));
    WebElement child=parent.findElement(By.xpath("//div[@class='MuiBox-root css-zgtx0t']/div[@class='MuiBox-root css-1gjj37g']/div[text()='"+productName+"']"));
    if(child.isDisplayed()){
    String qnt= parent.findElement(By.xpath("//div[@class='MuiBox-root css-zgtx0t']/div[@class='MuiBox-root css-1gjj37g']/div[text()='"+productName+"']/following-sibling::div/div[1]/div")).getText();
      
     int int_qnt=Integer.parseInt(qnt);

    WebDriverWait wait=new WebDriverWait(driver,5);

    WebElement btn1= parent.findElement(By.xpath("//div[@class='MuiBox-root css-zgtx0t']/div[@class='MuiBox-root css-1gjj37g']/div[text()='"+productName+"']/following-sibling::div/div[1]/button[1]"));
     WebElement btn2=parent.findElement(By.xpath("//div[@class='MuiBox-root css-zgtx0t']/div[@class='MuiBox-root css-1gjj37g']/div[text()='"+productName+"']/following-sibling::div/div[1]/button[2]"));
     if(quantity<int_qnt){
        for(int j=1;j<=int_qnt-quantity;j++){    
        action.moveToElement(btn1).click(btn1).perform();
        wait.until(ExpectedConditions.textToBePresentInElement(parent.findElement(By.xpath("//div[@class='MuiBox-root css-zgtx0t']/div[@class='MuiBox-root css-1gjj37g']/div[text()='"+productName+"']/following-sibling::div/div[1]/div")), String.valueOf(int_qnt-j)));
       // Thread.sleep(1000);
        }
        return true;
    }
        else if(int_qnt<quantity){
        for(int j=1;j<=quantity-int_qnt;j++){   
            action.moveToElement(btn2).click(btn2).perform();
            wait.until(ExpectedConditions.textToBePresentInElement(parent.findElement(By.xpath("//div[@class='MuiBox-root css-zgtx0t']/div[@class='MuiBox-root css-1gjj37g']/div[text()='"+productName+"']/following-sibling::div/div[1]/div")), String.valueOf(int_qnt+j)));
            //Thread.sleep(1000);
        }
        return true;
    }
        else{
        return true;
        }
       
    }
    else{
        return false;
    }

    } catch (Exception e) {
        if (quantity == 0)
            return true;
        System.out.println("exception occurred when updating cart: " + e.getMessage());
        return false;
    }
}


    /*
     * Return Boolean denoting if the cart contains items as expected
     */
    public Boolean verifyCartContents(List<String> expectedCartContents) {
        try {
            List<WebElement> cartContents = driver.findElementsByXPath(
                    "//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-12 MuiGrid-grid-md-3 css-1q5pok1']//div[@class='MuiBox-root css-1gjj37g']/div[not(@class)]");
            ArrayList<String> actualCartContents = new ArrayList<String>() {

            };
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(cartItem.getText());
            }

            for (String expected : expectedCartContents) {
                // To trim as getText() trims cart item title
                if (!actualCartContents.contains(expected.trim())) {
                    return false;
                }
            }
            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }
}
