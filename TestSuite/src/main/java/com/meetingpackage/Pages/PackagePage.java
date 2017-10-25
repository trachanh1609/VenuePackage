/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.meetingpackage.Pages.VenueUtils.MeetingLength;

/**
 * Class for package page object
 */
public class PackagePage extends VenueUtils{
	By dateTimeLocator = By.id("datetime");
	By roomDropdownLocator = By.id("edit-line-item-fields-field-commerce-room-und");
	By meetingLengthBtnLocator = By.cssSelector("*[data-id='edit-meeting-type']");
	By meetingLengthSelectLocator = By.id("edit-meeting-type");
	By seatingOrderLocator = By.id("edit-line-item-fields-field-commerce-seating-order-und");
	By submitBtnLocator = By.id("edit-submit");
	By delegatesLocator = By.id("edit-quantity");
	By incDelegatesLocator = By.cssSelector("div > div.form-item.form-item-quantity.form-type-textfield.form-group.inc-dec-field > div.inc.button.buttoninc");
	By decDelegatesLocator = By.cssSelector("div > div.form-item.form-item-quantity.form-type-textfield.form-group.inc-dec-field > div.dec.button.buttoninc");
	By priceLocator = By.cssSelector("div > div.form-item.form-item-price > div");
	By totalPriceLocator = By.cssSelector("div > div.form-item.form-item-total > div");
	By packageSelect = By.id("edit-product-id");
	private static final int DEF_DELEGATES_QUANTITY = 1;

	/**
	 * Constructor
	 * @param driver
	 */
	public PackagePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		(new WebDriverWait(driver, 20)).until(ExpectedConditions.elementToBeClickable(By.cssSelector("#breadcrumb > div > a.active")));
//		String txt = driver.findElement(By.cssSelector("#breadcrumb > div > a.active")).getText();
		if (!checkPage(true,()->(driver.findElement(By.cssSelector("#breadcrumb > div > a.active")).getText().contains("Package")||driver.findElement(By.cssSelector("#breadcrumb > div > a.active")).getText().contains("kokouspaketti")))) {
			throw new IllegalStateException("This is not the package page. Page title: " + driver.getTitle());
		}
	}
	
	/**
	 * Fill in enquiry with params specified
	 * 
	 * @param instantBooking
	 * @param numOfDelegates
	 * @param date
	 * @param room
	 * @param meetinglength
	 * @param seatingorder
	 * @return CheckoutPage
	 */
	public CheckoutPage fillinEnquiry(boolean instantBooking, int numOfDelegates, LocalDate date, Rooms room, MeetingLength meetinglength, SeatingOrder seatingorder) {
		setDelegatesNum(numOfDelegates);
		setDate(date);
		setMeetingLength(meetinglength);
		setRoom(room, instantBooking);
		setSeatingOrder(seatingorder);
//		if(!driver.findElement(submitBtnLocator).getCssValue("pointer-events").equals("none")) //check for clickable button, to prevent error for button not clickable
//			driver.findElement(submitBtnLocator).click();
//		else{
		waitFor(1200);
//		try {
//			driver.findElement(submitBtnLocator).click();
//			waitFor(500);
//			} catch (Exception e) {
//				System.out.println("submit button not clicked for the first time");
//				// TODO Auto-generated catch block
//				waitFor(5000);
//				driver.findElement(submitBtnLocator).click(); //Try again
//			}
//
//		if(!driver.findElements(submitBtnLocator).isEmpty())
//			driver.findElement(submitBtnLocator).click();
//			
//		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		clickSubmit();

		return new CheckoutPage(driver);
	}
	
	/**
	 * Get price shown on page
	 * 
	 * @return double price
	 */
	public double getPrice(){
		return Double.parseDouble(driver.findElement(priceLocator).getText().replace(" $", ""));
	}
	
	/**
	 * Get total price shown on page
	 * 
	 * @return double total
	 */
	public double getTotal() {
		return Double.parseDouble(driver.findElement(totalPriceLocator).getText().replace(" €", ""));
	}
	
	/**
	 * Click submit button and go to checkout page
	 * 
	 * @return CheckoutPage
	 */
	public CheckoutPage clickSubmit() {
		try {
			driver.findElement(submitBtnLocator).click();
		} catch (Exception e) {
			System.out.println("submit button not clicked for the first time");
			e.printStackTrace();
			// TODO Auto-generated catch block
			waitFor(10000);
			driver.findElement(submitBtnLocator).click(); //Try again
		}

		if(!driver.findElements(submitBtnLocator).isEmpty()) {
			waitFor(2000);
			if(driver.findElement(submitBtnLocator).isDisplayed()) {

				driver.findElement(submitBtnLocator).click();
			}
		}
		waitFor(800);
		return new CheckoutPage(driver);
	}
	
	/**
	 * Get date shown on page
	 * 
	 * @return String date
	 */
	public String getDate(){
		return driver.findElement(dateTimeLocator).getAttribute("value");
	}
	
	/**
	 * Get meeting length shown on page
	 * 
	 * @return String meeting length
	 */
	public String getMeetingLength(){
		return driver.findElement(meetingLengthSelectLocator).getAttribute("value");
	}
	
	/**
	 * Get number of delegates shown on page
	 * 
	 * @return String number of delegates
	 */
	public String getDelegates(){
		return driver.findElement(delegatesLocator).getAttribute("value");
	}
	
	/**
	 * Sets the date in enquiry
	 *
	 * @param date
	 */
	public void setDate(LocalDate date) {
		setDateCommon(date, dateTimeLocator);
	}
	
	/**
	 * Method to set number of delegates by clicking buttons
	 *
	 * @param quantity Number of delegates
	 */
	public void setDelegatesNum(int quantity) {
		setDelegatesNumCommon(quantity, DEF_DELEGATES_QUANTITY, incDelegatesLocator, decDelegatesLocator);
	}

	public PackagePage setPackage(String pkg) {
		Select pkgSel = new Select(driver.findElement(packageSelect));
		pkgSel.selectByVisibleText(pkg);
		return new PackagePage(driver);
	}
	
	/**
	 * Method to select the room in the enquiry
	 *
	 * @param room  Enum of the room to select
	 * @param instantBooking boolean true if booking for online available room
	 */
	public void setRoom(Rooms room, boolean instantBooking) {
		setRoomUtil(room, instantBooking, roomDropdownLocator);
	}
	
//	/**
//	 * Method to select meeting length in the enquiry
//	 *
//	 * @param meetingLen Enum of the meeting length to select
//	 */
//	public void setMeetingLength(MeetingLength meetingLen) {
//		setMeetingLengthUtil(meetingLen, meetingLengthLocator);
//	}
	
	/**
	 * Method to select meeting length in the enquiry
	 *
	 * @param meetingLen Enum of the meeting length to select
	 */
	public void setMeetingLength(MeetingLength meetingLen) {
//		setMeetingLengthUtil(meetingLen, meetingLengthLocator);
//		Select lenSelect = new Select(driver.findElement(meetingLengthLocator));
		int meetingLengthOption=1;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(meetingLengthBtnLocator));
		driver.findElement(meetingLengthBtnLocator).click();
		By setMeetingLengthLocator;
		switch (meetingLen){
		case TwoH :
//			lenSelect.selectByVisibleText("2 h");
			meetingLengthOption=2;
			break;
		case ThreeH :
			meetingLengthOption=3;
//			lenSelect.selectByVisibleText("3 h");
			break;
		case FourH :
//			lenSelect.selectByVisibleText("4 h");
			meetingLengthOption=4;
			break;
		case FiveH :
//			lenSelect.selectByVisibleText("5 h");
			meetingLengthOption=5;
			break;
		case SixH :
			meetingLengthOption=2;
//			lenSelect.selectByVisibleText("6 h");
			break;
		case SevenH :
			meetingLengthOption=3;
//			lenSelect.selectByVisibleText("7 h");
			break;
		case EightH :
			meetingLengthOption=4;
//			lenSelect.selectByVisibleText("8 h");
			break;
		case NineH :
			meetingLengthOption=5;
//			lenSelect.selectByVisibleText("9 h");
			break;
		case TenH :
			meetingLengthOption=6;
//			lenSelect.selectByVisibleText("10 h");
			break;
//		case Overnight :
//			meetingLengthOption=11;
////			lenSelect.selectByVisibleText("Overnight");
//			break;
//		case TwoDays :
//			meetingLengthOption=12;
////			lenSelect.selectByVisibleText("Two Days");
//			break;
		default:
			throw new IllegalArgumentException("Not defined meeting length");
		}
		setMeetingLengthLocator = By.xpath("//*[contains(@id, 'commerce-cart-add-to-cart-form')]/div/div[5]/div/div/ul/li["+meetingLengthOption+"]");
//		setMeetingLengthLocator = By.xpath("//*[@id=\"mp-enquiry-form\"]/div/div[1]/div[1]/div[6]/div/div/ul/li["+meetingLengthOption+"]");
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.visibilityOfElementLocated(setMeetingLengthLocator));
		driver.findElement(setMeetingLengthLocator).click();
	}
	
	/**
	 * Method to select the seating order in the enquiry
	 *
	 * @param seatingOrder
	 */
	public void setSeatingOrder(SeatingOrder seatingOrder) {
		setSeatingOrderUtil(seatingOrder, seatingOrderLocator);
	}
}
