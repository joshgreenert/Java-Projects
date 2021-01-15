package challenge;


import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * Created by Joshua Greenert on 12/08/2020
 * 
 * This program will pull up a website and collect a list of valid links
 * that it finds.  This will be done to test whether our clocking process is accurate or not
 * but may still not be the best method for checking.
 */
public class Website_Email_Test {

	static WebDriver driver = null;
	static Robot r;
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		
		// Call the function to test the website.
		testWebsite();
		
	}
	
	// Initializes the driver.
	public static void initDriver() throws IOException {
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
		//driver.manage().window().setPosition(new Point(-2000, 0));   Add this in when completed
	}

	// Creates the driver object, sends the parameter, waits, then destroys the driver.
	public static void testWebsite() throws IOException, InterruptedException, AWTException {
		initDriver();
		
		// Set the site for the driver.
		driver.get("https://truecbd4u.com/find-us/alabama");

		List<WebElement> list=driver.findElements(By.xpath("//*[@href or @src]"));

	       for(WebElement e : list){
	           String link = e.getAttribute("href");
	           if(null==link)
	               link=e.getAttribute("src");
	           System.out.println(e.getTagName() + " = "  + link);
	       }
		
		destroy();
	}
	
	// Kills the driver so that the webpage is closed.
	public static void destroy() {
		driver.quit();
	}
}