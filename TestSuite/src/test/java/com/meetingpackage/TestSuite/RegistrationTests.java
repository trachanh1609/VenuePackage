/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.TestSuite;

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

public class RegistrationTests {
	private String baseUrl;
	private WebDriver driver;
	private HomePage homepage;

	/**
	 * Automatically fires before each test case
	 * Opens browser and goes to meetingpackage.com
	 *
	 * @return none
	 */
	@Before
	public void openBrowser(){
		baseUrl=InitEnvironment.getUrl();
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1280,720));
		driver.get(baseUrl);
		homepage = new HomePage(driver);
	}
	
	/**
	 * Automatically fires after each test case
	 * Closes all browser windows
	 *
	 * @return none
	 */
	@After
	public void closeBrowser(){
		driver.quit();
	}
	
	/**
	 * Tests if customer can register successfully
	 *
	 * @return none
	 */
	@Test
	public void registerAsCustomerTest(){
		homepage.goToRegisterPage().registerAsCustomer();
		Assert.assertTrue("Customer registration failed",
				driver.getTitle().equals("Welcome to your Dashboard | MeetingPackage"));
	}
	
	/**
	 * Tests if supplier can register successfully
	 *
	 * @return none
	 */
	@Test
	public void registerAsSupplierTest(){
		homepage.goToRegisterPage().registerAsSupplier();
		Assert.assertTrue("Supplier registration failed",
				driver.getCurrentUrl().contains("/dashboard"));
//				driver.findElements(By.cssSelector("#main-nav > div.right > div.profilename.left")).size()>0);
				//driver.findElements(By.id("plan")).size()>0);
	}
}
