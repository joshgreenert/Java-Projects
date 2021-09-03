package selenium;

/*
 *  Created by Joshua Greenert on 10/5/2020
 *  
 *  This script will read from a text file to create a list of links to 
 *  take screenshots of.  The screenshots will be saved in a folder to then
 *  be pushed to GitHub.
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class WebPageScreenShotTaker {

	static WebDriver driver = null;
	static int num = 1;

	public static void main(String[] args) throws IOException {

		// Start up the browser window and create screenshot object.
		WebPageScreenShotTaker ws = new WebPageScreenShotTaker();

		// Create List object to store URLS
		ArrayList<String> sites = new ArrayList<String>();
		ArrayList<String> names = new ArrayList<String>();

		String name = "";
		String site = "";
		String path = "";

		File file;

		SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
		String dateString = format.format(new Date());

		// Go through the text file and store all of the addresses in the list.
		try {
			// Set the file from the users selection and update the path variable.
			file = new File("..//..//Documents//GitHub//webpage_screenshots//website_map.txt");
			path = file.getAbsolutePath();
			path = path.substring(0, path.length() - 15);

			Scanner myReader = new Scanner(file);

			// While there is another url, take the required pieces and add it to the lists.
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				// Check for the https portion of the string and print that line
				if (data.indexOf("https") != -1) {
					site = data.substring(data.indexOf("//") + 2, data.length());
					site = "https://" + site;
					sites.add(site);

					// If the string has a www, then get the length up to last period; remove the 'www.'
					if(data.indexOf("www") == -1){
						name = data.substring(data.indexOf("//") + 2, data.indexOf(".", data.indexOf(".")));
						names.add(name);
					}
					else {
						name = data.substring(data.indexOf("//") + 2, data.lastIndexOf("."));
						name = name.substring(4, name.length());
						names.add(name);
					}
				}
			}
			
			// For each loop to pass name, site, and date to capture object.
			for (int i = 0; i < sites.size(); i++) {
				ws.capture(sites.get(i), names.get(i), dateString, path);
			}

			myReader.close();
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			sendToGit(path);
		} catch (IOException | URISyntaxException | GitAPIException e) {
			e.printStackTrace();
		}

	}

	public static void initDriver() throws IOException {
		System.setProperty("webdriver.chrome.driver", "driver/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		driver.manage().window().maximize();

	}

	public void capture(String site, String name, String date, String path) throws IOException {
		initDriver();

		// Set up path and increase screenshot number.
		path = path + name + "/" + date.substring(date.length() - 4, date.length()) + "/" + date.substring(0, 2) + "/";
		
		driver.get(site);

		// Remove all slashes from the site name
		site = site.replaceAll("/", "-");
		site = site.replaceAll(":", "_");

		// check if the file exists and delete it.
		File deleteFile = new File(path + site + num +".png");
		if(deleteFile.delete()) {
			System.out.println("File deleted");
		}
		
		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scrFile, new File(path + site + num +".png"));

		Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
				.takeScreenshot(driver);
		ImageIO.write(screenshot.getImage(), "PNG", new File(path + site + num + ".png"));

		System.out.println("Took Screenshot for " + site + " and saved as " + site + ".png");
		num++;
		
		destroy();
	}

	public static void destroy() {
		driver.quit();
	}
	
	// Create a method to call that will send files to GitHub.
	public static void sendToGit(String path) throws IOException, URISyntaxException, GitAPIException {
		
		// Open the path selected by user.
		File repo = new File(path);
		Git git = Git.open(repo);
		
		// Commit all items in the path.
		git.commit().setAll(true).setMessage("Automated push from Java Program").call();
		
		// push to remote:
	    PushCommand pushCommand = git.push();
	    pushCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider("d8d10b373a560141099d6fc893755fa246e77a5c", ""));
	    pushCommand.call();
	    
	    // State that the process was completed.
	    System.out.println("Sent to GitHub!");
        
	}
}