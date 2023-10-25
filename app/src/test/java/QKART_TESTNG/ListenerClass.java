 
package QKART_TESTNG;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;;
public class ListenerClass  implements ITestListener {

    public void onTestStart(ITestResult result){
        System.out.println("Test Case Sarted. Taking Screenshot"+result.getName());
    }
    public void onTestSuccess(ITestResult result){
        System.out.println("Test Case Success. Taking Screenshot"+result.getName());
    }
    public void onTestFailure(ITestResult result){
        System.out.println("Test Case Failure. Taking Screenshot"+result.getName());
    }
    
    
}