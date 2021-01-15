package challenge;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * Created by Joshua Greenert on 12/08/2020
 * 
 * This program will take all of the store locations for CBD to provide to a 
 * website that generates the review submission link.  These links will be stored
 * on a file that can then be copied to the Google sheet.
 */
public class CreateBusinessReviewList {

	static WebDriver driver = null;
	static Robot r;
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		
		// Create List object to store URLS and file object to store user-file.
		ArrayList<String> businesses = new ArrayList<String>();
		
		File businessListFile;
		
		// Go through the text file and store all of the addresses in the list.
		try {

			// Create a file chooser
			final JFileChooser jc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
			jc.setDialogTitle("Select the file with the websites");
			jc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

			// In response to a button click:
			int returnVal = jc.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				// Set the file from the users selection and update the path variable.
				businessListFile = jc.getSelectedFile();

				Scanner myReader = new Scanner(businessListFile);

				// While there is another url, take the required pieces and add it to the lists.
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();

					businesses.add(data);
				}

				// For each loop to pass name, site, and date to capture object.
				for (int i = 0; i < businesses.size(); i++) {
					testWebsite(businesses.get(i));
				}

				myReader.close();
			}
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
	public static void testWebsite(String business) throws IOException, InterruptedException, AWTException {
		initDriver();
		
		// Set the site for the driver.
		driver.get("https://whitespark.ca/google-review-link-generator/");

		// Find the textbox for the input and enter the input.
		WebElement businessInput = driver.findElement(By.id("pac-input"));
		businessInput.sendKeys(business);
		// Wait for 2 seconds to recognize options.
		TimeUnit.SECONDS.sleep(2);
		businessInput.sendKeys(Keys.ARROW_DOWN);
		businessInput.sendKeys(Keys.ENTER);
		
		// Wait for 2 seconds to recognize options.
		TimeUnit.SECONDS.sleep(2);
		
		// Get the review link and save it as a variable.
		WebElement reviewInput = driver.findElement(By.xpath("//input[@id='gp_link']"));
		String reviewText = reviewInput.getAttribute("value");
		
		// Set each item within a string for a new row in a text file.
		try(FileWriter fw = new FileWriter("../../Documents/Github/webpage_screenshots/reviewListFinal.txt", true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw))
		{

		    out.println(reviewText);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		// Check the title of the page
		System.out.println("The link for " +reviewText+" has been added!");


		destroy();
	}
	
	// Kills the driver so that the webpage is closed.
	public static void destroy() {
		driver.quit();
	}
}