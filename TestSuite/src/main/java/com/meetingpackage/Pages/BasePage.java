/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for base page object
 */
public class BasePage {
	
	public static enum Languages {
		EN,
		FI,
		DA,
		FR,
		DE,
		IT,
		LV,
		RU,
		ES,
		SV
	}
	
	WebDriver driver;
	private Languages language = Languages.EN;
	//#main-nav > div.right > ul > li:nth-child(1) > a
	By topLogoLocator = By.id("top-logo");
	By loginButtonLocator = By.cssSelector("ul.menu.nav.navbar-nav > li:nth-child(1) > a");
	//By logoutButtonLocator = By.linkText("Log Out");
	By logoutButtonLocator = By.cssSelector("#main-nav > div.right > div.profilename.left > li > ul > li:nth-child(3) > a");
//	By registerButtonLocator = By.cssSelector("#main-nav > div.right > ul > li:nth-child(2) > a");
//	By registerButtonLocator = By.cssSelector("#block-menu-menu-footer-customers > ul > li.last.leaf > a");
	By registerButtonLocator = By.linkText("Register");
	By accountDropdownLocator = By.cssSelector("#main-nav > div.right > div.profilename.left > li > a");
//	By languageDropdownLocator = By.id("lang-dropdown-select-language");
	By languageDropdownLocator = By.cssSelector("div.region.region-language-switcher.current");
//	By accountDropdownLocator = By.className("dropdown-toggle");
	
	/**
	 * Navigates the browser to home page
	 * 
	 * @return HomePage object
	 */
	public HomePage goToHomePage() {
			driver.findElement(topLogoLocator).click();
			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		return new HomePage(driver);
	}
	
	/**
	 * Navigates the browser to login page
	 * 
	 * @return LoginPage object
	 */
	public LoginPage goToLoginPage() {
//		if(userLoggedIn()){
//			logOut();
//		}
		driver.findElement(loginButtonLocator).click();
	//	driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
	//	waitFor(300);
//		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
//		new Actions(driver).moveToElement(driver.findElement(loginButtonLocator)).click().perform();
		return new LoginPage(driver);
	}
	
	/**
	 * Navigates the browser to registration page
	 * 
	 * @return LoginPage object
	 */
	public RegisterPage goToRegisterPage() {
//		goToLoginPage();                     									//register button has been moved to login page
//		if (driver.findElement(registerButtonLocator).isDisplayed()) {
			driver.findElement(registerButtonLocator).click();
			driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
//		}
		return new RegisterPage(driver);
	}

	/**
	 * Logs out user if user logged
	 * 
	 * @return HomePage object
	 */
	public HomePage logOut() {
		if (driver.findElement(accountDropdownLocator).isDisplayed()) {
			driver.findElement(accountDropdownLocator).click();
			(new WebDriverWait(driver, 10))
			  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(logoutButtonLocator))));//.elementToBeClickable(logoutButtonLocator));
			try {
				driver.findElement(logoutButtonLocator).click();
			} catch(Exception e) {
				waitFor(3000);
				if(driver.findElement(logoutButtonLocator).isDisplayed())
					driver.findElement(logoutButtonLocator).click();
				else {
					driver.findElement(accountDropdownLocator).click();
					waitFor(3000);
					driver.findElement(logoutButtonLocator).click();
				}
			}
			waitFor(500);
		}
		return new HomePage(driver);
	}
	
	/**
	 * Method to change site language
	 * 
	 * @param Language enum
	 */
	public void changeLanguage(Languages lan){
		WebElement lans;
		driver.findElement(languageDropdownLocator).click();
		waitFor(400);
//		lans = driver.findElement(By.cssSelector("div.region.region-language-switcher.options > ul"));
		lans = driver.findElement(languageDropdownLocator).findElement(By.xpath("following-sibling::div[contains(@class, 'options')]/ul"));
//		lans.findElement(By.xpath("li/a/input[contains(@value, '"+lan.toString().toLowerCase()+"')]/..")).click();
		lans.findElement(By.xpath("li/a/span[text() = '"+lan.toString().toLowerCase()+"']/..")).click();
		language = lan;
		waitFor(500);
	}
	
	/**
	 * Returns current chosen language
	 * 
	 * @return Languages enum
	 */
	public Languages getCurrentLanguage(){
		return language;
	}

	/**
	 * Checks if user is logged in
	 * 
	 * @return true if logged in, false if not
	 */
	public boolean userLoggedIn() {
		if (driver.findElement(loginButtonLocator).isDisplayed())
			return false;
		return true;
	}

	/**
	 * Method to wait for the specified amount of time
	 * 
	 * @param ms How long to wait
	 */
	public void waitFor(int ms){
		try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}

	/**
	 * Method to check if current page is as expected(for page object constructors). Also waits some time to ensure page load
	 * @param control boolean variable against which check will be performed
	 * @param f function that's used to check if current page is what it's supposed to be
	 * @return true if current page is correct, false if not
	 */
	public boolean checkPage(boolean control, BooleanSupplier f) {
		waitFor(700);
		if(f.getAsBoolean()!=control) {
			waitFor(5000);
		}
		return f.getAsBoolean()==control;
	}
}
