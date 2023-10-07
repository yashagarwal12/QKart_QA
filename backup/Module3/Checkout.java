package QKART_SANITY_LOGIN.Module1;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Checkout {
    RemoteWebDriver driver;
    String url = "https://crio-qkart-frontend-qa.vercel.app/checkout";

    public Checkout(RemoteWebDriver driver) {
        this.driver = driver;
    }

    public void navigateToCheckout() {
        if (!this.driver.getCurrentUrl().equals(this.url)) {
            this.driver.get(this.url);
        }
    }

    /*
     * Return Boolean denoting the status of adding a new address
     */
    public Boolean addNewAddress(String addresString) {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            /*
             * Click on the "Add new address" button, enter the addressString in the address
             * text box and click on the "ADD" button to save the address
             */
           WebElement parent=driver.findElement(By.xpath("//*[@class='shipping-container MuiBox-root css-vooagt']"));
            parent.findElement(By.xpath("//button[@id='add-new-btn']")).click();
            parent.findElement(By.xpath("//textarea[@placeholder='Enter your complete address']")).sendKeys(addresString);
            //Thread.sleep(2000);
            WebDriverWait wait=new WebDriverWait(driver,3);
            
            parent.findElement(By.xpath("//*[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root  css-177pwqq']")).click();
            wait.until(ExpectedConditions.textToBePresentInElementLocated(By.xpath("//*[@name='address']//following::p[1]"), addresString));
        
            return true;
           
        } catch (Exception e) {
            System.out.println("Exception occurred while entering address: " + e.getMessage());
            return false;

        }
    }

    /*
     * Return Boolean denoting the status of selecting an available address
     */
    public Boolean selectAddress(String addressToSelect) {
        try {
            /*
             * Iterate through all the address boxes to find the address box with matching
             * text, addressToSelect and click on it
             */

            if(driver.findElement(By.name("address")).isEnabled()){
                driver.findElement(By.name("address")).click();
                return true;
             }
             else{
            System.out.println("Unable to find the given address");
            return false;
             }
        } catch (Exception e) {
            System.out.println("Exception Occurred while selecting the given address: " + e.getMessage());
            return false;
        }
    }

    /*
     * Return Boolean denoting the status of place order action
     */
    public Boolean placeOrder() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 05: MILESTONE 4
            // Find the "PLACE ORDER" button and click on it
            driver.findElement(By.xpath("//*[@class='MuiButton-root MuiButton-contained MuiButton-containedPrimary MuiButton-sizeMedium MuiButton-containedSizeMedium MuiButtonBase-root  css-177pwqq']")).click();
            return true;

        } catch (Exception e) {
            System.out.println("Exception while clicking on PLACE ORDER: " + e.getMessage());
            return false;
        }
    
    }

    /*
     * Return Boolean denoting if the insufficient balance message is displayed
     */
    public Boolean verifyInsufficientBalanceMessage() {
        try {
            // TODO: CRIO_TASK_MODULE_TEST_AUTOMATION - TEST CASE 07: MILESTONE 6
            WebElement parent=driver.findElement(By.xpath("//*[@class='SnackbarItem-wrappedRoot css-vbcata']/div/div"));
            String text=parent.getText();
            if(text.trim().equals("You do not have enough balance in your wallet for this purchase")){
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println("Exception while verifying insufficient balance message: " + e.getMessage());
            return false;
        }
    }
}
