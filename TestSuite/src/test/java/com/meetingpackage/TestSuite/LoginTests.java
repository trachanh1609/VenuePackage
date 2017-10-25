/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.TestSuite;

import com.meetingpackage.Pages.LoginPage;
import com.meetingpackage.Pages.RegisterPage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.meetingpackage.Pages.HomePage;

import init.InitEnvironment;

import java.util.concurrent.TimeUnit;

public class LoginTests {
	private String baseUrl;
	private WebDriver driver;
	private HomePage homepage;
	private RegisterPage loginpage;

	/**
	 * Automatically fires before each test
	 * Opens browser and goes to meetingpackage.com
	 * 
	 * @param none
	 * @return none
	 */
	@Before
	public void openBrowser() {
		baseUrl=InitEnvironment.getUrl();
		
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1280,720));
		driver.get(baseUrl);
		homepage = new HomePage(driver);
//		driver.get("https://meetingpackage.com/user/register");
//		loginpage = new RegisterPage(driver);
		//homepage.waitFor(15000);
		//driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		/*baseUrl = "https://meetingpackage.com/";
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1280,720));
		driver.get(baseUrl);
		homepage = new HomePage(driver);*/
	}

	/**
	 * Automatically fires after each test case
	 * Closes all browser windows
	 * 
	 * @param none
	 * @return none
	 */
	@After
	public void closeBrowser() {
		driver.quit();
	}

	/**
	 * Tests if customer can login successfully
	 * 
	 * @param none
	 * @return none
	 */
	@Test
	public void loginAsCustomerTest() {
		homepage.goToLoginPage().loginAs("testMPcustomer@example.com", "blahblah");
		Assert.assertTrue("User failed to login",
				driver.getCurrentUrl().contains("/dashboard")
//				driver.getTitle().equals("Welcome to your Dashboard | MeetingPackage")
//				driver.findElements(By.cssSelector("#main-nav > div.right > div.profilename.left")).get(0).isDisplayed()
		);
	}

	/**
	 * Tests if supplier can login successfully
	 * 
	 * @param none
	 * @return none
	 */
	@Test
	public void loginAsSupplierTest() {
		homepage.goToLoginPage().loginAs("testMPsupplier@example.com", "blahblah");
		Assert.assertTrue("User failed to login",
				driver.getCurrentUrl().contains("/dashboard"));
	}

	/**
	 * Tests if customer can logout successfully
	 * 
	 * @param none
	 * @return none
	 */
	@Test
	public void logoutCustomerTest() {
		if (!homepage.userLoggedIn()) {
			homepage.goToLoginPage().loginAs("testMPcustomer@example.com", "blahblah");

		}
		homepage.logOut();
		Assert.assertTrue("User failed to logout", driver.findElement(By.linkText("Login")).isDisplayed());
	}
	
	/**
	 * Tests if customer can logout successfully
	 * 
	 * @param none
	 * @return none
	 */
	@Test
	public void logoutSupplierTest() {
		if (!homepage.userLoggedIn()) {
			homepage.goToLoginPage().loginAs("testMPsupplier@example.com", "blahblah");

		}
		homepage.logOut();
		Assert.assertTrue("User failed to logout", driver.findElement(By.linkText("Login")).isDisplayed());
	}
}
