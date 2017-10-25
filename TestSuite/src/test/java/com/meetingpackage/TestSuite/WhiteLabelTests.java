//package com.meetingpackage.TestSuite;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.commons.io.FileUtils;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.rules.TestName;
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.chrome.ChromeDriver;
//
//import com.meetingpackage.Pages.HomePage;
//
//import init.InitEnvironment;
//import utils.ImageCompare;
//
//public class WhiteLabelTests {
//	private String baseUrl;
//	private WebDriver driver;
//	private HomePage homepage;
//	private File scrFile;
//	
//@Rule public TestName name = new TestName();
//	
//	/**
//	 * Automatically fires before each test case
//	 * Opens browser
//	 */
//	@Before
//	public void openBrowser(){
////		baseUrl=InitEnvironment.getUrl();
//		driver = new ChromeDriver();
//		driver.manage().window().setSize(new Dimension(1920,1080));
//		driver.get("https://cwteasymeetings.com");
//		homepage = new HomePage(driver);
//	}
//	
//	/**
//	 * Automatically fires after each test case
//	 * Closes all browser windows
//	 */
//	@After
//	public void closeBrowser(){
//		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//		try {
//			FileUtils.copyFile(scrFile, new File("target/site/screenshots/" + name.getMethodName() + ".jpg"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////		driver.quit();
//	}
//	
//	@Test
//	public void checkHomePage() {
////		ImageComparison imageComparison = new ImageComparison(10,10,0.1);
////		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
////		try {
////			FileUtils.copyFile(scrFile, new File("target/site/screenshots/" + name.getMethodName() + ".png"));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
////		try {
////			System.out.println(imageComparison.fuzzyEqual("target/site/screenshots/checkHomePage.png", "checkHomePage.png", "imageDiffs.png"));
////		} catch (IOException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		ImageCompare ic = new ImageCompare("test1.jpg", "test2.jpg");
//		ic.setParameters(8, 6, 5, 10);
//		ic.setDebugMode(2);
//		ic.compare();
//		System.out.println("Match: " + ic.match());
//		
////		homepage.venueSearch("test automation venue");
//	}
//}
