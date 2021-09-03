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

public class WebDevTesting {

	static WebDriver driver = null;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		// Create List object to store URLS and file object to store user-file.
		ArrayList<String> sites = new ArrayList<String>();
		File file;
		
		String site = "";
		String name = "url";
		
		// Go through the text file and store all of the addresses in the list.
		try {

			// Set the file from the users selection and update the path variable.
			file = new File("..//..//Documents//GitHub//webpage_screenshots//webtesting.rtf");

			Scanner myReader = new Scanner(file);

			// While there is another url, take the required pieces and add it to the lists.
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				// Check for the https portion of the string and print that line
				if (data.indexOf("https") != -1) {
					if(data.indexOf(".com") != -1) {
						System.out.println("Reached");
						site = data.substring(data.indexOf("//") + 2, data.indexOf(".com")+4);
						site = "https://" + site;
						sites.add(site);
					}
					else if(data.indexOf(".net") != -1) {
						site = data.substring(data.indexOf("//") + 2, data.indexOf(".net")+4);
						site = "https://" + site;
						sites.add(site);
					}
					else if(data.indexOf(".org") != -1) {
						site = data.substring(data.indexOf("//") + 2, data.indexOf(".org")+4);
						site = "https://" + site;
						sites.add(site);
					}
					else if(data.indexOf(".us") !=  -1) {
						site = data.substring(data.indexOf("//") + 2, data.indexOf(".us")+3);
						site = "https://" + site;
						sites.add(site);
					}
					else {
						continue;
					}	
				}
			}
	

			// For each loop to pass name, site, and date to capture object.
			for (int i = 0; i < sites.size(); i++) {
				testWebsite(sites.get(i), name);
			}

			myReader.close();
			
		} catch (FileNotFoundException e) {
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
	public static void testWebsite(String site, String name) throws IOException, InterruptedException {
		initDriver();

		// Set the site for the driver.
		driver.get("https://web.dev/measure/");

		// Find the text input element by its name
		WebElement element = driver.findElement(By.name(name));

		// Enter something to search for
		element.sendKeys(site);

		// Create a new web element for the button and press submit.
		//WebElement submitButton = driver.findElement(By.xpath("//button[contains(.,’Run Audit’)]"));
		WebElement submitButton = driver.findElement(By.id("run-lh-button"));
		submitButton.click();
		
		// Wait for 25 seconds to continue.
		TimeUnit.SECONDS.sleep(25);
		
		// Now submit the form. WebDriver will find the form for us from the element
		//element.submit();

		// Check the title of the page
		System.out.println("The site " +site+" has been submitted!");


		destroy();
	}
	
	// Kills the driver so that the webpage is closed.
	public static void destroy() {
		driver.quit();
	}
}