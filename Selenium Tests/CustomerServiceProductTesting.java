package selenium;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/*
 * Created by Joshua Greenert on 10/12/2020
 * 
 * The goal of this script is to take a list, provided by the user, of websites and
 * cycle through the list by testing each one on the web.dev/measure website.
 */

public class CustomerServiceProductTesting {

	static WebDriver driver = null;
	static ArrayList<String> failedProducts = new ArrayList<String>();
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		// Create List object to store names from text file.
		ArrayList<String> productNames = new ArrayList<String>();
		File file;
		int indexCount = 1;
		
		String site = "https://cs.sunflora.org";
		String fileName = "CustomerServiceWebsiteTesting.txt";
		
		try {

			// Set the file from the users selection and update the path variable.
			file = new File("..//..//Documents//GitHub//webpage_screenshots//"+fileName);

			Scanner myReader = new Scanner(file);

			// While there is another url, take the required pieces and add it to the lists.
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				productNames.add(data);

			}
			
			
			// Run each product through the test and confirm working as intended.
			for(int i = 0; i < productNames.size()-3; i++) {
				testWebsite(site, productNames.get(i), indexCount);
				indexCount++;
			}
			
			//System.out.println(productNames.size());
			
			// Show all of the items that did not receive true.
			for(int i = 0; i < failedProducts.size();i++) {
				System.out.println(failedProducts.get(i));
			}
			
			// Close the reader object
			myReader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}


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
	public static void testWebsite(String site, String name, int count) throws IOException, InterruptedException {
		initDriver();

		// Set the site for the driver.
		driver.get(site);
		String addButtonID = (String)"add"+count;

		// Find the text input element by its name
		WebElement emailField = driver.findElement(By.className("input_text"));
		emailField.sendKeys("joshg@dang-designs.com");
		
		WebElement passField = driver.findElement(By.name("password"));
		passField.sendKeys("6SF?%DwGRem_kxYH");

		// Create a new web element for the button and press submit.
		//WebElement submitButton = driver.findElement(By.xpath("//button[contains(.,’Run Audit’)]"));
		WebElement submitButton = driver.findElement(By.className("button"));
		submitButton.click();
		
		// Wait for 5 seconds to continue.
		TimeUnit.SECONDS.sleep(2);
		
		WebElement cartFormButton = driver.findElement(By.className("button3"));
		cartFormButton.click();
		
		// Wait for 5 seconds to continue.
		TimeUnit.SECONDS.sleep(2);
		
		WebElement addIDButton = driver.findElement(By.id(addButtonID));
		addIDButton.click();
		
		WebElement continueButton = driver.findElement(By.id("Continue"));
		continueButton.click();
		
		// Wait for 10 seconds to continue.
		TimeUnit.SECONDS.sleep(2);
		
		String result = (String)driver.findElement(By.xpath("//table/tbody/tr[3]/td[2]")).getAttribute("innerHTML");
		
		if(!name.equals(result)) {
			if(name.equals("") || result.equals("")) {
				failedProducts.add("Test Product: " + result + "  Actual Product: " + name);
			}
		}

		destroy();
	}
	
	// Kills the driver so that the webpage is closed.
	public static void destroy() {
		driver.quit();
	}
}