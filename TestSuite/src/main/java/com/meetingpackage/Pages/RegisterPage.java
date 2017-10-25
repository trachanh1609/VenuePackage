/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Class for registration page object
 */
public class RegisterPage extends BasePage {
	private static final String PASSWORD = "blahblah";

	By emailLocator = By.id("edit-mail");
	By passwordLocator = By.id("edit-pass-pass1");
	By confirmPasswordLocator = By.id("edit-pass-pass2");
	By firstNameLocator = By.id("edit-field-user-first-name-und-0-value");
	By lastNameLocator = By.id("edit-field-user-last-name-und-0-value");
	By companyNameLocator = By.id("edit-field-user-company-name-und-0-value");
	By registerAsCustomerLocator = By.cssSelector("#edit-field-registering-as-und > div:nth-child(1)");
	By registerAsSupplierLocator = By.cssSelector("#edit-field-registering-as-und > div:nth-child(2)");
	By submitBtnLocator = By.id("edit-submit");

	/**
	 * Register page object constructor
	 * 
	 * @param driver
	 *            WebDriver to use
	 * @return none
	 */
	public RegisterPage(WebDriver driver) {
		this.driver = driver;
		if (!checkPage(true,()->driver.getTitle().contains("User account"))) {
			throw new IllegalStateException("This is not the registration page");
		}
	}

	/**
	 * Fills in registration form (for internal use by registerAs.. functions)
	 * 
	 * @param email
	 *            Email address to fill in the form
	 * @param locator
	 *            Locator for the register as customer/venue partner button
	 * @return none
	 */
	private void fillInForm(String email, By locator) {
		driver.findElement(emailLocator).sendKeys("TestRegistrationCustomer_" + UUID.randomUUID() + "@example.com");
		driver.findElement(passwordLocator).sendKeys(PASSWORD);
		driver.findElement(confirmPasswordLocator).sendKeys(PASSWORD);
		driver.findElement(firstNameLocator).sendKeys("John");
		driver.findElement(lastNameLocator).sendKeys("Doe");
		driver.findElement(companyNameLocator).sendKeys("registerTestMP");
		driver.findElement(locator).click();
	}

	/**
	 * Register new customer
	 * 
	 * @param none
	 * @return none
	 * @throws none
	 */
	public DashboardPage registerAsCustomer(){
		if (userLoggedIn()) {
			logOut();
		}
		fillInForm("TestRegistrationCustomer_" + UUID.randomUUID() + "@example.com", registerAsCustomerLocator);
		waitFor(7000);
		driver.findElement(submitBtnLocator).click();

		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		return new DashboardPage(driver);
	}
	/**
	 * Register new venue partner
	 * 
	 * @param none
	 * @return none
	 * @throws none
	 */
	public DashboardPage registerAsSupplier(){
		if (userLoggedIn()) {
			logOut();
		}
		fillInForm("TestRegistrationSupplier_" + UUID.randomUUID() + "@example.com", registerAsSupplierLocator);
		waitFor(5000);
		driver.findElement(submitBtnLocator).click();

		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		return new DashboardPage(driver);
	}

}
