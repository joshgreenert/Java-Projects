package selenium;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


/*
 * Created by Joshua Greenert on 12/17/2020
 * 
 * This script will capture the information about each website to store a list of expiration dates.
 */

public class WhoIsExpirationDate {

	static WebDriver driver = null;
	
	public static void main(String[] args) throws IOException, InterruptedException, URISyntaxException, GitAPIException {
		
		
		// Create List object to store URLS and file object to store user-file.
		ArrayList<String> sites = new ArrayList<String>();
		
		File file;
		File deleteFile = new File("..//..//Documents//Github//webpage_screenshots//siteExpiryDate.txt");
		
		// Delete the file that is used to hold the gtmetrix scores
        if(deleteFile.delete()) 
        { 
            System.out.println("File deleted successfully"); 
        } 
        else
        { 
            System.out.println("Failed to delete the file"); 
        } 
		
		// Go through the text file and store all of the addresses in the list.
		try {

			// Set the file from the users selection and update the path variable.
			file = new File("..//..//Documents//GitHub//webpage_screenshots/fullwebsitelist.txt");

			Scanner myReader = new Scanner(file);

			// While there is another url, take the required pieces and add it to the lists.
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				sites.add(data);
				
			}
	

			// For each loop to pass name, site, and date to capture object.
			for (int i = 0; i < sites.size(); i++) {
				testWebsite(sites.get(i));
			}

			myReader.close();
			
			// Push commit
			sendToGit();
			
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
	public static void testWebsite(String site) throws InterruptedException, IOException {
		initDriver();

		String resultText = "";
		
		// Set the site for the driver.
		driver.get("https://godaddy.com/whois");

		// Look up the input field and enter the request.
		WebElement searchField = driver.findElement(By.name("domain"));
		searchField.sendKeys(site);
		searchField.sendKeys(Keys.RETURN);
		
		// Wait for 5 seconds to continue.
		TimeUnit.SECONDS.sleep(15);
		
		// Find the text from the results.
		WebElement results = driver.findElement(By.id("whois-data"));
		resultText = results.getText();
		
		// Separate the text from the rest.
		if(resultText.indexOf("Registrar Registration Expiration Date") != -1 && 
				resultText.indexOf("Registry Expiry Date") != -1) {
			resultText = resultText.substring(resultText.indexOf("Registry Expiry Date") + 22,
					resultText.indexOf("Registry Expiry Date") + 32);
		}
		else if (resultText.indexOf("Registrar Registration Expiration Date") != -1){
			resultText = resultText.substring(resultText.indexOf("Registrar Registration Expiration Date") + 40,
					resultText.indexOf("Registrar Registration Expiration Date") + 50);
		}
		else {
			resultText = resultText.substring(resultText.indexOf("Registry Expiry Date") + 22,
					resultText.indexOf("Registry Expiry Date") + 32);
		}

		// Set each item within a string for a new row in a text file.
		try(FileWriter fw = new FileWriter("..//..//Documents//Github//webpage_screenshots//siteExpiryDate.txt", true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw))
		{

		    out.println(resultText);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		destroy();
	}
	
	// Kills the driver so that the webpage is closed.
	public static void destroy() {
		driver.quit();
	}
	
	// Create a method to call that will send files to GitHub.
	public static void sendToGit() throws IOException, URISyntaxException, GitAPIException {
		
		// Open the path selected by user.
		File repo = new File("..//..//Documents//Github//webpage_screenshots");
		Git git = Git.open(repo);
		
		// Commit all items in the path.
		git.commit().setAll(true).setMessage("Automated push from Java Program").call();
		
		// push to remote:
	    PushCommand pushCommand = git.push();
	    pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("d8d10b373a560141099d6fc893755fa246e77a5c", ""));
	    pushCommand.call();
        
	    System.out.println("Pushed to GitHub!");
	}
}