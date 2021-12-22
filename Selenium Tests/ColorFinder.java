package capitalcityshoes;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
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
 * The goal of this script is to obtain the color hex variables for all of the unknown
 * colors that we need for the color swatches pro plugin.
 */

public class ColorFinder {

	static WebDriver driver = null;
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		
		// Create List object to store URLS and file object to store user-file.
		ArrayList<String> hexColors = new ArrayList<String>();
		File file;
		
		// Go through the text file and store all of the addresses in the list.
		try {

			// Set the file from the users selection and update the path variable.
			file = new File("..//..//Documents//GitHub//webpage_screenshots//colors.txt");

			Scanner myReader = new Scanner(file);

			// While there is another url, take the required pieces and add it to the lists.
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();

				hexColors.add(data);
			}
	

			// For each loop to pass name, site, and date to capture object.
			for (int i = 0; i < hexColors.size(); i++) {
				getColorHex(hexColors.get(i));
			}

			myReader.close();
			
			System.out.println("All colors submitted!");
			
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
	}
	
	// Creates the driver object, sends the parameter, waits, then destroys the driver.
	public static void getColorHex(String color) throws IOException, InterruptedException {
		initDriver();

		// Set the site for the driver.
		driver.get("https://google.com");
		
		// Create new color string
		String colorString = color + " color hex";
		String filename = "..//..//Documents//GitHub//webpage_screenshots//colorHex.txt";

		WebElement searchElement = driver.findElement(By.cssSelector("[aria-label='Search']"));
		searchElement.sendKeys(colorString);
		searchElement.submit();
		
		try {
			WebElement contextElement = driver.findElement(By.xpath("//*[@id=\"rcnt\"]"));
			
			// Convert data from Element to string
			String colorHexString = contextElement.getText();
			String subHexString = colorHexString.substring(colorHexString.indexOf("#"), colorHexString.indexOf("#")+7);
			
			System.out.println(subHexString);
			
			FileWriter fw = new FileWriter(filename, true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(subHexString);
			bw.newLine();
			bw.close();

			destroy();
		}
		catch(Exception XPathException) {
			
		}
		
	}
	
	// Kills the driver so that the webpage is closed.
	public static void destroy() {
		driver.quit();
	}
}