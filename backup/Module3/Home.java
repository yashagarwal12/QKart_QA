package QKART_SANITY_LOGIN.Module1;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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

            // SLEEP_STMT_10: Wait for Logout to complete
            // Wait for Logout to Complete
            Thread.sleep(3000);
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Clear the contents of the search box and Enter the product name in the search
            // box
            driver.findElement(By.name("search")).clear();
            driver.findElement(By.name("search")).sendKeys(product);
            WebDriverWait wait=new WebDriverWait(driver,3);
            wait.until(ExpectedConditions.or(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[contains(@class,'hero MuiBox-root')]/following-sibling::div/div/h4"), "No products found"),
               ExpectedConditions.elementToBeClickable(By.xpath("//*[@class='MuiCardActions-root MuiCardActions-spacing card-actions css-3zukih']/button"))));
            return true;
        }   catch (Exception e) {
            System.out.println("Error while searching for a product: " + e.getMessage());
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Find all webelements corresponding to the card content section of each of
            // search results
            searchResults=driver.findElements(By.xpath("//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-6 MuiGrid-grid-md-3 css-sycj1h']"));
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 03: MILESTONE 1
            // Check the presence of "No products found" text in the web page. Assign status
            // = true if the element is *displayed* else set status = false
            status=driver.findElement(By.xpath("//*[contains(@class,'hero MuiBox-root')]/following-sibling::div/div/h4")).isDisplayed();
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
            boolean status=false;
            List<WebElement> element= driver.findElements(By.xpath("//div[@class='MuiGrid-root MuiGrid-item MuiGrid-grid-xs-6 MuiGrid-grid-md-3 css-sycj1h']"));
            for(WebElement e:element){
            String text=e.findElement(By.xpath("//div[@class='MuiCardContent-root css-1qw96cp']/p")).getText();
            if(text.trim().equals(productName)){
             WebElement product=e.findElement(By.xpath("//*[@class='MuiCardActions-root MuiCardActions-spacing card-actions css-3zukih']/button"));
                
                Actions action=new Actions(driver);
                action.moveToElement(product).click(product).perform();
               status= true;
             }
             else{
                System.out.println("Unable to find the given product");
                status= false;
                }
            
            }
           return status;
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
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find and click on the the Checkout button
        driver.findElement(By.xpath("//div[@class='cart MuiBox-root css-0']/div[@class='cart-footer MuiBox-root css-1bvc4cc']/button")).click();
        status=true;
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
            WebElement cartParent = driver.findElement(By.className("cart"));
            List<WebElement> cartContents = cartParent.findElements(By.className("css-zgtx0t"));

            ArrayList<String> actualCartContents = new ArrayList<String>() {
            };
            for (WebElement cartItem : cartContents) {
                actualCartContents.add(cartItem.findElement(By.className("css-1gjj37g")).getText().split("\n")[0]);
            }

            for (String expected : expectedCartContents) {
                if (!actualCartContents.contains(expected)) {
                    return false;
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("Exception while verifying cart contents: " + e.getMessage());
            return false;
        }
    }

    public boolean cart(String productName){
        boolean status=false;
        WebDriverWait wait=new WebDriverWait(driver,3);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='cart MuiBox-root css-0']")));
        WebElement parent=driver.findElement(By.xpath("//div[@class='cart MuiBox-root css-0']"));
        List<WebElement> child=parent.findElements(By.xpath("//div[@class='MuiBox-root css-zgtx0t']/div[1]"));
        for(WebElement E:child){
            if(E.getText().equals(productName)){
                return true; 
            }
        }
        return true;
       
    }

    public void footer(RemoteWebDriver driver, String xpath){
        driver.findElement(By.xpath(xpath)).click();
    }

    public boolean switchWindow(RemoteWebDriver driver, String URL , String x_path, String content ) throws InterruptedException{
        boolean status=false;
        try{
       
        String mainWindow = driver.getWindowHandle();
        Set<String> openedWindows = driver.getWindowHandles();
        if(openedWindows.size() > 0)
        {
            for(String newWindow : openedWindows)
            {
                driver.switchTo().window(newWindow);
                //Thread.sleep(2000);        
                if(driver.getCurrentUrl().equals(URL)) {
                
               String text= driver.findElement(By.xpath(x_path)).getText();
               status=text.equalsIgnoreCase(content);
               Thread.sleep(2000);
            }
            }

        }
        driver.switchTo().window(mainWindow);
       return status;
    }
    catch(Exception E){
        return status;
    }
}
public void closeTab(RemoteWebDriver driver) throws InterruptedException {
    String mainWindow = driver.getWindowHandle();
    Set<String> openedWindows = driver.getWindowHandles();
   
        for(String newWindow : openedWindows)
        {
            driver.switchTo().window(newWindow);
            if(!newWindow.equals(mainWindow)){
            driver.close();
            }
            
        }
        driver.switchTo().window(mainWindow);
 
}

public boolean checkAds(RemoteWebDriver driver){
   try{driver.findElement(By.xpath("//p[text()='Contact us']")).click();
    //Thread.sleep(5000);
    
    WebElement parent=driver.findElement(By.xpath("//*[@class='card-block']"));
    
    WebElement name=parent.findElement(By.xpath("//div[@class='col-md-6']/div/input[@placeholder='Name']"));
    name.sendKeys("crio user");
    //Thread.sleep(2000);
    WebElement email=parent.findElement(By.xpath("//div[@class='col-md-6']/div/input[@placeholder='Email']"));
    email.sendKeys("criouser@gmail.com");
    //Thread.sleep(2000);
    WebElement mssg=parent.findElement(By.xpath("//input[@placeholder='Message']"));
    mssg.sendKeys("Testing the contact us page");
    //Thread.sleep(2000);
    parent.findElement(By.xpath("//div[@class='col-md-12']/button")).click();
    return true;
}
catch(Exception E){
    return false;
}

}
}
