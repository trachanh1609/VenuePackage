/* Copyright (C) 2017 Cocouz Ltd - All Rights Reserved */

package com.meetingpackage.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Class for dashboard page object
 */
public class DashboardPage extends BasePage {
	By goToInboxBtn = By.cssSelector("body > div.main-container.basic-page.fluid > div > aside > ul > li:nth-child(2) > a");
	By goToCatalogBtn = By.cssSelector("body > div.main-container.basic-page.fluid > div > aside > ul > li:nth-child(8) > a");
	By standardPlanBtn = By.cssSelector("#hide-if-sign > div.table-responsive > table > tbody.select-buttons > tr > td:nth-child(2) > button");
	By goldPlanBtn = By.xpath("//*[@id=\"hide-if-sign\"]/div[4]/table/tbody[3]/tr/td[4]/button");
	By acceptAgreementBtn = By.cssSelector("#hide-if-sign > div.accept-description > button");
	By venueNum = By.id("venues");
	By currencySel = By.id("edit-field-venue-currency-und");
	By emailInput = By.id("email1");
	By vatInput = By.id("owner-vat-number");
	By regNumInput = By.id("owner-registration-number");
	By countrySelect = By.id("owner-country");
	By billingAddInput = By.id("owner-billing-address");
	By confirmBtn = By.cssSelector("div.jconfirm-buttons > button.btn.btn-blue");
	By goldPrice = By.id("GOLD-joining_fee");
	By totalFee = By.id("account-total-amount");
	By commission = By.id("account-commission");
//	By comfirmSuccess = By.cssSelector("body > div.jconfirm-buttons > button");
	private String price="0";
	public final int venues=3;

	/**
	 * Dashboard page object constructor
	 * 
	 * @param driver	WebDriver to use
	 */
	public DashboardPage(WebDriver driver) {
		this.driver = driver;
		if(!checkPage(true, ()->driver.findElement(By.cssSelector("body > div.main-container.basic-page.fluid > div > aside > ul > li.first.leaf > a")).getAttribute("class").contains("active-trail"))){
//		if (!driver.findElement(By.cssSelector("body > div.main-container.basic-page.fluid > div > aside > ul > li.first.leaf > a")).getAttribute("class").contains("active-trail")) {
			throw new IllegalStateException("This is not the dashboard page");
		}
	}
	
	/**
	 * Go to inbox page
	 * 
	 * @return InboxPage
	 */
	public InboxPage goToInbox(){
		driver.findElement(goToInboxBtn).click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.attributeToBe(By.id("loadingRooms"), "style", "display: none;"));
//		waitFor(4000);
		return new InboxPage(driver);
	}
	
	/**
	 * Go to catalog page
	 * 
	 * @return InboxPage
	 */
	public CatalogPage goToCatalog(){
		driver.findElement(goToCatalogBtn).click();
		waitFor(2000);
		
//		new WebDriverWait(driver, 10).until(ExpectedConditions.not(ExpectedConditions.visibilityOfElementLocated(By.className("fa-spin")))); // wait until catalog loads
		return new CatalogPage(driver);
	}
	
	/**
	 * Method to select gold plan
	 */
	public void selectGoldPlan(){
		driver.findElement(venueNum).clear();
		driver.findElement(venueNum).sendKeys(String.valueOf(venues));
		new Select(driver.findElement(currencySel)).selectByVisibleText("GBP");
//        new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(driver.findElement(goldPrice)));
		waitFor(2000);
		price = driver.findElement(goldPrice).getText();
		driver.findElement(goldPlanBtn).click();
		driver.findElement(acceptAgreementBtn).click();
//		waitFor(2000);
		new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(driver.findElement(vatInput)));
		driver.findElement(vatInput).sendKeys("5");
		driver.findElement(regNumInput).sendKeys("006");
		new Select(driver.findElement(countrySelect)).selectByVisibleText("Antarctica");
		driver.findElement(billingAddInput).sendKeys("South Pole");
		driver.findElement(confirmBtn).click();
		new WebDriverWait(driver, 10).until(ExpectedConditions.stalenessOf(driver.findElement(confirmBtn)));
		driver.findElement(confirmBtn).click();
	}
	
	/**
	 * Get price shown before accepting a plan
	 * @return double price
	 */
	public double getPrice(){
		price = price.replace("£", "");
		price = price.trim();
		return Double.parseDouble(price);
	}
	
	/**
	 * Get total price
	 * @return double total
	 */
	public double getTotal(){
		String total = "";
		total = driver.findElement(totalFee).getText();
		total = total.replace("£", "");
		total = total.trim();
		return Double.parseDouble(total);
	}
	
	/**
	 * Get commission shown on dashboard
	 * @return String commission
	 */
	public String getCommission(){
		return driver.findElement(commission).getText();
	}
	
	/**
	 * Get number of venues selected on dashboard
	 * @return in number of menues
	 */
	public int getVenuesNum(){
		return Integer.parseInt(driver.findElement(venueNum).getAttribute("value"));
	}
}
