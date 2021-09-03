package selenium;

import java.awt.AWTException;
import java.awt.Robot;
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
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

/*
 * Created by Joshua Greenert on 10/13/2020
 * 
 * This program will pull up the gtmetrix site and submit a web page hyperlink.
 * After waiting until the page has loaded (unsure about time frame) the elements will be captured
 * from the pagespeed, yslow, fully loaded, size request, suggestion 1, suggestion 2, and suggestion 3
 * fields.
 */
public class GTMetrixTesting {

	static WebDriver driver = null;
	static Robot r;
	
	static String pagespeed = "";
	static String yslow = "";
	static String loadtime = "";
	static String pagesize = "";
	static String requests = "";
	static String performance = "";
	static String details = "";
	
	public static void main(String[] args) throws IOException, InterruptedException, AWTException, URISyntaxException, GitAPIException {
		
		// Create List object to store URLS and file object to store user-file.
			ArrayList<String> sites = new ArrayList<String>();
			ArrayList<String> names = new ArrayList<String>();
			
			File deleteFile = new File("..//..//Documents//Github//webpage_screenshots//gtmetrixdata.txt"); 
			File siteFile;
			
			String webname = "";
			String site = "";
			String name = "url";
			String username = "dang@dang-designs.com";
			String password = "vaMjyt-qixbu6-pydcok";
			
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
				siteFile = new File("..//..//Documents//GitHub//webpage_screenshots//webtesting.rtf");

				Scanner myReader = new Scanner(siteFile);

				// While there is another url, take the required pieces and add it to the lists.
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();

					// Check for the https portion of the string and print that line
					if (data.indexOf("https") != -1) {
						if(data.indexOf(".com") != -1) {
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
						
						// If the string has a www, then get the length up to last period; remove the
						// 'www.'
						if (data.indexOf("www") == -1) {
							webname = data.substring(data.indexOf("//") + 2, data.indexOf(".", data.indexOf(".")));
							names.add(webname);
						} else {
							webname = data.substring(data.indexOf("//") + 2, data.lastIndexOf("."));
							webname = webname.substring(4, webname.length());
							names.add(webname);
						}
					}
				}

				// For each loop to pass name, site, and date to capture object.
				for (int i = 0; i < sites.size(); i++) {
					testWebsite(sites.get(i), name, username, password, names.get(i));
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
	public static void testWebsite(String site, String name, String user, String pass, String webname) throws IOException, InterruptedException, AWTException {
		initDriver();
		
		// Reset the variables for each value.
		pagespeed = "";
		yslow = "";
		
		// Set the site for the driver.
		driver.get("https://gtmetrix.com/");

		// GTMetrix updated their amount of sites to use per user.  
		// Attempting to run without user specified
		/*
		// Find the login button and authenticate.
		WebElement loginButton = driver.findElement(By.className("js-auth-widget-link"));
		loginButton.click();
		WebElement username = driver.findElement(By.id("li-email"));
		username.sendKeys(user);
		
		TimeUnit.SECONDS.sleep(5);
		
		WebElement password = driver.findElement(By.id("li-password"));
		password.sendKeys(pass);
		
		// Submit the credentials through robot and wait for page to load up.
		r = new Robot();
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);
		r.keyPress(KeyEvent.VK_ENTER);
		r.keyRelease(KeyEvent.VK_ENTER);
		TimeUnit.SECONDS.sleep(5);
		*/
		
		// Find the URL field and enter the name of the site.
		WebElement element = driver.findElement(By.name(name));
		element.sendKeys(site);

		// Create a new web element for the button and press submit.
		WebElement submitButton = driver.findElement(By.className("analyze-form-button"));
		submitButton.click();
		
		// Wait for 25 seconds to continue.
		TimeUnit.SECONDS.sleep(195);
		
		// After the testing is complete, grab the fields needed.
		WebElement performscores = driver.findElement(By.className("report-scores"));
		WebElement pagedetails = driver.findElement(By.className("report-page-details"));
		
		performance = performscores.getText();
		details = (String)pagedetails.getText();
		
		int count = 0;
		// Use string operations to separate the scores out.
		for(int i = 0; i < performance.length(); i++) {
			if(Character.isDigit(performance.charAt(i))) {
				if(count < 2) {
					pagespeed += performance.charAt(i);
					count++;
				}
				else if(count >= 2) {
					yslow += performance.charAt(i);
					count++;
				}
			}
		}
		
		count = 0;
		for(int i = 0; i < details.length(); i++) {
			
			if(count ==3) {
				break;
			}
			
			if(Character.isDigit(details.charAt(i))) {
				if(count < 1) {
					details = details.substring(i, details.length());
					loadtime = details.substring(0, details.indexOf("s"));
					count++;
					i = details.indexOf("s")+1;
				}
				else if(count == 1) {
					details = details.substring(i, details.length());
					
					if(details.indexOf("MB") != -1) {
						pagesize = details.substring(0, details.indexOf("MB")+2);
						count++;
						i = details.indexOf("MB")+2;
					}
					else {
						pagesize = details.substring(0, details.indexOf("KB")+2);
						count++;
						i = details.indexOf("KB")+2;
					}
				}
				else if(count == 2) {
					details = details.substring(i, details.length());
					requests = details.substring(0, details.length());
					count++;
					break;
				}
			}
		}
		
		details = webname + "," + pagespeed + "," + yslow + "," + loadtime + "," + pagesize + "," + requests;
		
		// Set each item within a string for a new row in a text file.
		try(FileWriter fw = new FileWriter("..//..//Documents//Github//webpage_screenshots//gtmetrixdata.txt", true);
		    BufferedWriter bw = new BufferedWriter(fw);
		    PrintWriter out = new PrintWriter(bw))
		{

		    out.println(details);
		    
		} catch (IOException e) {
			e.printStackTrace();
		}
		

		// Check the title of the page
		System.out.println("The site " +site+" has been tested!");


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
	    pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("joshdangdesigns", "queen-KING-spade-1"));
	    pushCommand.call();
        
	    System.out.println("Pushed to GitHub!");
	}
}