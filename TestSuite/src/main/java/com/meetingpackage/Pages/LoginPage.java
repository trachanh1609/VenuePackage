/* Copyright (C) 2017 Cocouz Ltd - All Rights Reserved */

package com.meetingpackage.Pages;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Class for login page object
 */
public class LoginPage extends BasePage {
	By usernameLocator = By.id("edit-name");
	By passwordLocator = By.id("edit-pass");
	By submitBtnLocator = By.id("edit-submit");

	/**
	 * Login page object constructor
	 * 
	 * @param driver
	 *            WebDriver to use
	 * @return none
	 */
	public LoginPage(WebDriver driver) {
		this.driver = driver;
//		if (!driver.getTitle().contains("User Login")) {
		if (!checkPage(true,()->driver.getCurrentUrl().contains("user/login"))) {
			throw new IllegalStateException("This is not the login page");
		}
	}

	/**
	 * Login as user specified by name and password parameters
	 * 
	 * @param username
	 *            Name of the user to log in as
	 * @param password
	 *            Password of the user to log in as
	 * @return dashboard page object
	 */
	public DashboardPage loginAs(String username, String password) {
		if (userLoggedIn()) {
			logOut();
		}
		driver.findElement(usernameLocator).sendKeys(username);
		driver.findElement(passwordLocator).sendKeys(password);
		driver.findElement(submitBtnLocator).click();
//		waitFor(2000);
//		(new WebDriverWait(driver, 10))
//		  .until(ExpectedConditions.visibilityOfElementLocated(By.id("top-logo")));//.presenceOfElementLocated(By.id("myDynamicElement")));
		return new DashboardPage(driver);
	}
	
	/**
	 * Login as test customer
	 * @return dashboard page object
	 */
	public DashboardPage loginAsCustomer() {
		return loginAs("testMPcustomer@example.com","blahblah");
	}
	
	/**
	 * Login as test supplier
	 * @return dashboard page object
	 */
	public DashboardPage loginAsSupplier() {
		return loginAs("test.villa@meetingpackage.com", "meetingpackage");
	}
}
