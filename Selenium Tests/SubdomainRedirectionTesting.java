package selenium;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * Created by Joshua Greenert 10/23/2020
 * 
 * This script will test our list of subdomains to check for errors.
 */
public class TestSubdomains {

	static WebDriver driver = null;
	static ArrayList<String> differences = new ArrayList<String>();
	Robot r;
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException {
		
		// Create List object to store URLS and file object to store user-file.
		ArrayList<String> domains = new ArrayList<String>();
		ArrayList<String> urls = new ArrayList<String>();
		
		File domainfile;
		File urlfile;
		
		String currentLine;
		
		domainfile = new File("..//..//Documents//Github//webpage_screenshots//subdomains.txt");
		urlfile = new File("..//..//Documents//Github//webpage_screenshots//redirects.txt");
		
		FileReader domainfr = new FileReader(domainfile);    
        BufferedReader domainbr = new BufferedReader(domainfr); 
        FileReader urlfr = new FileReader(urlfile);    
        BufferedReader urlbr = new BufferedReader(urlfr); 
		
		// Add every domain to the list.
		while ((currentLine = domainbr.readLine()) != null) {
			String data = currentLine;
			
			if(data.indexOf("cbdrx4u") != -1) {
				if(data.indexOf("strokec2") != -1) {
					data = data.substring(data.indexOf("strokec2") + 9, data.length());
				}
				
				data = data.replace("\\f0\\fs29\\fsmilli14667 ", "");
				data = "https://" + data;
				domains.add(data);
			}
		}
		
		// Add every url to the list.
		while ((currentLine = urlbr.readLine()) != null) {
			String data = currentLine;
			
			if(data.indexOf("cbdrx4u") != -1 || data.indexOf("truecbd") != -1) {
				if(data.indexOf("strokec2") != -1) {
					data = data.substring(data.indexOf("strokec2") + 9, data.length());
				}
				
				data = data.replace("\\f0\\fs29\\fsmilli14667 ", "");

				urls.add(data);
			}
			
		}

		// For each loop to pass name, site, and date to capture object.
		for (int i = 0; i < domains.size(); i++) {
			testWebsite(domains.get(i), urls.get(i));
		}

		// Print all of the list differences.
		for(int i = 0; i < differences.size(); i++) {
			System.out.println(differences.get(i));
		}
		
		domainbr.close();
		urlbr.close();
	}
	
	private static void testWebsite(String domain, String url) throws IOException, InterruptedException, AWTException {
		initDriver();
		
		// Set the site for the driver.
		driver.get(domain);
		
		// Wait for seconds to continue.
		TimeUnit.SECONDS.sleep(5);
		
		String redirection = driver.getCurrentUrl();
		
		if(!(url.equals(redirection))){
			System.out.println("URL: " + url + "\nREDIRECTION: " + redirection + "\n");
			differences.add("URL: " + url + "\nREDIRECTION: " + redirection + "\n");
		}
		
		destroy();
		
	}

	// Kills the driver so that the webpage is closed.
	public static void destroy() {
		driver.quit();
	}
	
	// Initializes the driver.
	public static void initDriver() throws IOException {
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();
	}
}