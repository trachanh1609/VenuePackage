/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for venue page object
 */
public class VenuePage extends VenueUtils {
	
	By venueNameLocator = By.className("cc-title");
//	By venueNameLocator = By.cssSelector("#node-25465 > div.top-content > header > h1 > a");
	By meetingRoomLocator = By.cssSelector(
			"#venue-information > div > div.view.view-venue-rooms-all.view-id-venue_rooms_all.view-display-id-block.view-dom-id-64aaa9c0ad37d470500e69af2c004e92 > div.view-content > div > ul > li > div > div > div.field.field-name-field-room-pictures.field-type-image.field-label-hidden > div > div > img");
	By delegatesLocator = By.id("edit-quantity");
	By incDelegatesLocator = By.cssSelector(
			"#mp-enquiry-form > div > div.enquiry-cart > div.enquiry-cart-content > div.form-item.form-item-quantity.form-type-textfield.form-group.inc-dec-field > div.inc.button.buttoninc");
	By decDelegatesLocator = By.cssSelector(
			"#mp-enquiry-form > div > div.enquiry-cart > div.enquiry-cart-content > div.form-item.form-item-quantity.form-type-textfield.form-group.inc-dec-field > div.dec.button.buttoninc");
	By dateTimeLocator = By.id("edit-date-time-date");
	By seatingOrderLocator = By.id("edit-seating-order");
	//By pickerMonthLocator = By.className("picker__month");
	//By navNextMonthBtnLocator = By.className("picker__nav--next");
	//By navNextMonthBtnLocator = By.cssSelector("div.picker__header > div.picker__nav--next");

	//div.picker__header > div.picker__nav--next
	//By pickerDateLocator = By.xpath("//*[contains(@class, 'picker__day--infocus') and text() = '17']");
	//By nineAmTimeLocator = By.cssSelector("li[aria-label='09:00']");
	//By roomDropdownLocator = By.cssSelector("*[data-id='edit-room']");
	By roomDropdownLocator = By.id("edit-room");

	By instantBookableRoomLocator = By.cssSelector("span");//[class='bolt']");
	By meetingLengthLocator = By.cssSelector("*[data-id='edit-meeting-length']");
	By meetingLengthLocatorSel = By.id("edit-meeting-length");
	By fulldayPackageLocator = By.linkText("Full-day package");
	By halfdayPackageLocator = By.linkText("Half-day package");
	By totalLocator = By.id("edit-room-total-vat");
//	By priceLocator = By.className("price");
	By priceLocator = By.id("edit-room-price-vat");
//	By priceLocator = By.xpath("//*[contains(@id, 'commerce-cart-add-to-cart-form')]//div[contains(@class, 'price')]");
	public By sidebarTitleLocator = By.id("bookthis");
	public By submitBtnLocator = By.id("edit-actions-submit");
	private String price="";
	private String total="";

	WebDriver driver;
	private static final int DEF_DELEGATES_QUANTITY = 10;
	
	
	/**
	 * Venue page constructor
	 * 
	 * @param driver
	 *            WebDriver
	 */
	public VenuePage(WebDriver driver) {
		super(driver);
		this.driver = driver;
		String title = driver.getTitle().toLowerCase();
		waitFor(2000);
		if (!checkPage(true, ()->true||((driver.getTitle().toLowerCase().contains("meeting rooms and venues") || driver.getTitle().toLowerCase().contains("kokoustilat")) && driver.getCurrentUrl().contains("venue")))) {
			throw new IllegalStateException("This is not the venue page. Page title: " + driver.getTitle());
		}
	}

	/**
	 * Returns current venue name
	 * 
	 * @return String Name of the venue
	 */
	public String getVenueName() {
		return driver.findElement(venueNameLocator).getText();
	}
	
	/**
	 * Returns current price per hour
	 * 
	 * @return String Name of the venue
	 */
	public double getPrice(){
		if(price.equals("")) {
			System.out.println(driver.findElement(priceLocator).getAttribute("value"));
			price = driver.findElement(priceLocator).getAttribute("value");
		}
		price = price.replace("$", "");
		price = price.replace("€", "");
		price = price.replace("£", "");
		price = price.replace(",", ".");
		price = price.trim();
		if(price.equals(""))
			price="0";
		return Double.parseDouble(price);
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
		 * Get meeting length shown on page
		 * 
		 * @return String meeting length
		 */
		public String getMeetingLength(){
			return driver.findElement(meetingLengthLocatorSel).getAttribute("value");
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
	 * Returns current total price
	 * 
	 * @return String Name of the venue
	 */
	public double getTotal(){
		if(total.equals("")) {
			System.out.println(driver.findElement(totalLocator).getAttribute("value"));
			price = driver.findElement(totalLocator).getAttribute("value");
		}
		total = total.replace("$", "");
		total = total.replace("€", "");
		total = total.replace("£", "");
		total = total.replace(",", ".");
		price = price.trim();
		if(total.equals(""))
			total="0";
		return Double.parseDouble(price);
	}
	
	/**
	 * Method to fill in enquiry on the venue page and proceed to checkout
	 * 
	 * @param instantBooking
	 * @param numOfDelegates
	 * @param date
	 * @param room
	 * @param meetinglength
	 * @param seatingorder
	 * @return CheckoutPage object
	 */
	public CheckoutPage fillinEnquiry(boolean instantBooking, int numOfDelegates, LocalDate date, Rooms room, MeetingLength meetinglength, SeatingOrder seatingorder) {
		setRoom(room, instantBooking);
		setDelegatesNum(numOfDelegates);
		waitFor(500);
		setMeetingLength(meetinglength);
		setDate(date);
		setSeatingOrder(seatingorder);
		waitFor(1200);
		System.out.println(driver.findElement(totalLocator).getAttribute("value"));
		price = driver.findElement(priceLocator).getAttribute("value");
		//price *= Integer.parseInt(driver.findElement(priceLocator).getText().replace(" $", ""));
		//(new Actions(driver)).moveToElement(driver.findElement(submitBtnLocator)).click().perform();
		//StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		//System.out.println(stackTraceElements[i].getMethodName());
		//System.out.println(driver.findElement(submitBtnLocator).getCssValue("pointer-events"));
		//		if(!driver.findElement(submitBtnLocator).getCssValue("pointer-events").equals("none")) //check for clickable button, to prevent error for button not clickable
		//			driver.findElement(submitBtnLocator).click();
		//		else{
		
//		try {
//			driver.findElement(submitBtnLocator).click();
//			waitFor(500);
//		} catch (Exception e) {
//			System.out.println("submit button not clicked for the first time");
//			// TODO Auto-generated catch block
//			waitFor(5000);
//			driver.findElement(submitBtnLocator).click(); //Try again
//		}
//		if(!driver.findElements(submitBtnLocator).isEmpty())
//			driver.findElement(submitBtnLocator).click();
//		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
//		waitFor(100);
//
//		return new CheckoutPage(driver);
		return clickSubmit();
	}
	
	/**
	 * Select full-day package
	 * 
	 * @return PackagePage
	 */
	public PackagePage selectFullDayPackage(){
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(fulldayPackageLocator))));
		driver.findElement(fulldayPackageLocator).click();
		return new PackagePage(driver);
	}
	
	/**
	 * Select half-day package
	 * 
	 * @return PackagePage
	 */
	public PackagePage selectHalfDayPackage(){
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(halfdayPackageLocator))));
		driver.findElement(halfdayPackageLocator).click();
		return new PackagePage(driver);
	}
	
	public void setFirstRoom() {
		Select roomSelect = new Select(driver.findElement(roomDropdownLocator));
		roomSelect.selectByIndex(1);
	}

	/**
	 * Method to select the room in the enquiry
	 *
	 * @param room  Enum of the room to select
	 * @param instantBooking TODO
	 */
	public void setRoom(Rooms room, boolean instantBooking) {
		setRoomUtil(room, instantBooking, roomDropdownLocator);
//		WebElement roomDropdown = driver.findElement(roomDropdownLocator);
//		Select roomSelect = new Select(roomDropdown);
////		WebElement roomOption;
////		List<WebElement> roomOptions;
////		System.out.println(roomOptions.get(4).findElements(By.className("bolt")).size());
////		for(int i= 0;i<roomOptions.size();i++){
////			System.out.println(roomOptions.get(i).getAttribute("innerText"));
////		}
//
//		//roomsDropdown.click();
//		//waitFor(1000);
//		
//
////		roomsDropdown = driver.findElement(roomDropdownLocator);
////		roomOptions = roomsDropdown.findElements(By.cssSelector("li>a"));
//
//		//roomOptions = driver.findElements(By.cssSelector("#venue-information > div > div.view.view-venue-rooms-all.view-id-venue_rooms_all > div.view-content > div > ul > li.views-row.views-row-3.views-row-odd.enabled"));
//		switch(room) {
//		case TESTROOM1 :
//			roomSelect.selectByVisibleText("test room 1");
////			roomOption = roomOptions.get(1);
////			}
//			//(new Actions(driver)).moveToElement(roomOption).click().perform();
////			roomOption.click();
//			break;
//
//		case TESTROOM2 :
//			roomSelect.selectByVisibleText("test room 2");
////			roomOption = roomOptions.get(5);
////			//(new Actions(driver)).moveToElement(roomOption).click().perform();
////			roomOption.click();
//			break;
//
//		case TESTROOM3 :
//			roomSelect.selectByVisibleText("test room 3");
////			roomOption = driver.findElement(By.cssSelector("#venue-information > div > div.view.view-venue-rooms-all.view-id-venue_rooms_all > div.view-content > div > ul > li.views-row.views-row-3.views-row-odd.enabled"));
////			/*if(instantBooking){
////				if(roomOption.findElements(By.className("bolt")).size()==0)
////					throw new IllegalArgumentException("Room '"+roomOption.getText()+"' is not instant bookable");			}*/
////			//(new Actions(driver)).moveToElement(roomOption).click().perform();
////			roomOption.click();
////			(new WebDriverWait(driver, 10)).until(
////					ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#room2 > div.modal-dialog.modal-lg > div > button")));
////			driver.findElement(By.cssSelector("#room2 > div.modal-dialog.modal-lg > div > button")).click();
//			break;
//
//		case TESTROOM4 :
//			roomSelect.selectByVisibleText("test room 4");
////			roomOption = roomOptions.get(4);
////			if(instantBooking){
////				if(roomOption.findElements(By.className("bolt")).size()==0)
////					throw new IllegalArgumentException("Room '"+roomOption.getText()+"' is not instant bookable");			}
////			//(new Actions(driver)).moveToElement(roomOption).click().perform();
////			roomOption.click();
//			break;
//
//		case TESTROOM5 :
//			roomSelect.selectByVisibleText("test room 5");
////			roomOption = roomOptions.get(5);
////			if(instantBooking){
////				if(roomOption.findElements(By.className("bolt")).size()==0)
////					throw new IllegalArgumentException("Room '"+roomOption.getText()+"' is not instant bookable");			}
////			//(new Actions(driver)).moveToElement(roomOption).click().perform();
////			roomOption.click();
//			break;
//
//		default:
//			throw new IllegalArgumentException("Not defined room");
//		}
//		
//		if(instantBooking)
//			if(roomSelect.getFirstSelectedOption().findElements(By.className("bolt")).size()==0)
//				throw new IllegalArgumentException("Room '"+roomSelect.getFirstSelectedOption().getText()+"' is not instant bookable");

	}

	/**
	 * Method to select meeting length in the enquiry
	 *
	 * @param meetingLen Enum of the meeting length to select
	 */
	public void setMeetingLength(MeetingLength meetingLen) {
//		setMeetingLengthUtil(meetingLen, meetingLengthLocator);
//		Select lenSelect = new Select(driver.findElement(meetingLengthLocator));
		int meetingLengthOption=1;
		waitFor(400);
		driver.findElement(meetingLengthLocator).click();
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
			meetingLengthOption=6;
//			lenSelect.selectByVisibleText("6 h");
			break;
		case SevenH :
			meetingLengthOption=7;
//			lenSelect.selectByVisibleText("7 h");
			break;
		case EightH :
			meetingLengthOption=8;
//			lenSelect.selectByVisibleText("8 h");
			break;
		case NineH :
			meetingLengthOption=9;
//			lenSelect.selectByVisibleText("9 h");
			break;
		case TenH :
			meetingLengthOption=10;
//			lenSelect.selectByVisibleText("10 h");
			break;
		case Overnight :
			meetingLengthOption=11;
//			lenSelect.selectByVisibleText("Overnight");
			break;
		case TwoDays :
			meetingLengthOption=12;
//			lenSelect.selectByVisibleText("Two Days");
			break;
		default:
			throw new IllegalArgumentException("Not defined meeting length");
		}
		setMeetingLengthLocator = By.xpath("//*[@id=\"mp-enquiry-form\"]/div/div[1]/div[1]/div[6]/div/div/ul/li["+meetingLengthOption+"]");
		driver.findElement(setMeetingLengthLocator).click();
	}


	/**
	 * Method to select the seating order in the enquiry
	 *
	 * @param seatingOrder
	 */
	public void setSeatingOrder(SeatingOrder seatingOrder) {
		setSeatingOrderUtil(seatingOrder, seatingOrderLocator);
//		Select dropdown = new Select(driver.findElement(seatingOrderLocator));
//		String optText = "- Select -";
//		List<WebElement> options = dropdown.getOptions();
//		for (WebElement option : options){
//			if (seatingOrder.toString().toLowerCase().contains(option.getText().toLowerCase().substring(0, 4)) && option.isEnabled()){
//				optText=option.getText();
//			}
//		}
//		dropdown.selectByVisibleText(optText);
	}

	/**
	 * Method to set number of delegates by clicking buttons
	 *
	 * @param quantity Number of delegates
	 */
	public void setDelegatesNum(int quantity) {
		waitFor(500);
		setDelegatesNumCommon(quantity, DEF_DELEGATES_QUANTITY, incDelegatesLocator, decDelegatesLocator);
//		int diff = quantity - DEF_DELEGATES_QUANTITY;
//		if (diff > 0) {
//			for (int i = 0; i < diff; i++) {
//				driver.findElement(incDelegatesLocator).click();
//			}
//		} else if (diff < 0) {
//			for (int i = 0; i > diff; i--) {
//				driver.findElement(decDelegatesLocator).click();
//			}
//		}
	}

	/**
	 * Sets the date in enquiry
	 *
	 * @param date
	 */
	public void setDate(LocalDate date) {
		setDateCommon(date, dateTimeLocator);
//		String month = date.format(DateTimeFormatter.ofPattern("MMM"));
//		String day = date.format(DateTimeFormatter.ofPattern("d"));
//		By pickerDateLocator = By.xpath("//*[contains(@class, 'picker__day--infocus') and text() = '" + day + "']");
//
//		driver.findElement(dateTimeLocator).click();
////		new WebDriverWait(driver, 20).until(ExpectedConditions.visibilityOfElementLocated(pickerMonthLocator));
//		waitFor(300);
//		for(int i=0;i<12;i++){
//				waitFor(100);
//
//			if(driver.findElement(pickerMonthLocator).getText().contains(month)){
//				break;
//			}
//
//			(new Actions(driver)).moveToElement(driver.findElement(navNextMonthBtnLocator)).click().perform();
//			//driver.findElement(navNextMonthBtnLocator).click();
//		}
//		driver.findElement(pickerDateLocator).click();
//		// (new
//		// Actions(driver)).moveToElement(driver.findElement(nineAmTimeLocator));//.click().perform();
//		waitFor(500);
////		(new Actions(driver)).moveToElement(driver.findElement(nineAmTimeLocator)).click().perform();
//		driver.findElement(nineAmTimeLocator).click();
	}
}
