/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for checkout page object
 */
public class CheckoutPage extends BasePage {
By venueNameLocator = By.cssSelector("div.venue-details > div.venue-details-item.name-address > h3");
By bookingDateLocator = By.cssSelector("div.date-display-single");
By startTimeLocator = By.cssSelector("div.date-display-single > div.date-display-range > span.date-display-start");
By endTimeLocator = By.cssSelector("div.date-display-single > div.date-display-range > span.date-display-end");
By roomLocator = By.cssSelector("div.venue-details-item.full-info > div.full-info-item > div.info.room-detail");
//By delegatesLocator = By.cssSelector("#edit-cart-contents > div > div > div > div > div.views-field.views-field-field-commerce-seating-order > div > div.package-desc > div:nth-child(6) > div.content");
//By delegatesLocator = By.xpath("//div[contains(@class, 'quantity-label')]/../div[contains(@class, 'content')]");
By delegatesLocator = By.id("delegate_quantity");
By seatingOrderLocator = By.xpath("//div[contains(@class, 'full-info-item')]/div[contains(@class, 'info-label') and text()='Seating Order']/../div[@class = 'info']");
//By seatingOrderLocator = By.cssSelector("div.venue-details-item.full-info > div:nth-child(4) > div.info");
By priceLocator = By.cssSelector("div.total.full-info-item.totalPrice > div.info");

By emailField = By.id("edit-account-login-mail");
By firstNameField = By.id("edit-customer-profile-billing-field-customer-first-name-und-0-value");
By lastNameField = By.id("edit-customer-profile-billing-field-customer-last-name-und-0-value");
By phoneField = By.id("edit-customer-profile-billing-field-customer-phone-und-0-value");
By companyField = By.id("edit-customer-profile-billing-field-customer-company-name-und-0-value");
By termsCheckbox = By.id("edit-checkout-pane-custom-fields-terms");
By continueBookingBtn = By.id("edit-continue");
By validationErrorText = By.cssSelector("body > div.modal.fade.in > div.modal-dialog > div > div.modal-body > p");

By nextBtn = By.cssSelector("div.btn.btn-primary.next");

//private HashSet<String> addons;
//
//public HashSet<String> getAddons() {
//	return addons;
//}

/**
 * Constructor
 * @param driver
 */
public CheckoutPage(WebDriver driver) {
	this.driver=driver;
	if (!checkPage(true,()->driver.getCurrentUrl().contains("/checkout"))){
			//.getTitle().contains("Please add your contact details and proceed")) {
//		waitFor(15000);
		if (!driver.getCurrentUrl().contains("checkout")){
	        throw new IllegalStateException("This is not the checkout page");
		}
    }
}

/**
 * Fill info on checkout page and submit
 */
public void fillInInfo(){
	
	driver.findElement(emailField).sendKeys("testMPcustomer@example.com");
	driver.findElement(firstNameField).sendKeys("John");
	driver.findElement(lastNameField).sendKeys("Doe");
	driver.findElement(phoneField).sendKeys("0400777612");
	driver.findElement(companyField).sendKeys("Test");
	
	completeBooking();
}

	public void fillInInfoCustom(String firstName, String lastName, String phone){

//		driver.findElement(firstNameField).sendKeys(firstName);
//		driver.findElement(lastNameField).sendKeys(lastName);
		driver.findElement(phoneField).clear();
		driver.findElement(phoneField).sendKeys(phone);
		List<WebElement> nextStep = driver.findElements(nextBtn);
		nextStep.get(0).click();
		nextStep.get(1).click();

		driver.findElement(By.id("edit-commerce-payment-payment-details-invoice-company")).sendKeys("blahblah");
		driver.findElement(By.id("edit-commerce-payment-payment-details-invoice-booker")).sendKeys("blahblah");
		driver.findElement(By.id("edit-commerce-payment-payment-details-invoice-phone")).sendKeys(phone);
		driver.findElement(By.id("edit-commerce-payment-payment-details-invoice-cardnumber")).sendKeys("424242424242");
		driver.findElement(By.id("edit-commerce-payment-payment-details-invoice-card")).sendKeys("424");
		driver.findElement(By.id("edit-commerce-payment-payment-details-invoice-expiry")).sendKeys("10/23");
		driver.findElement(By.id("edit-commerce-payment-payment-details-invoice-address")).sendKeys("blahblahb");

		completeBooking();
	}

/**
 * submit booking request
 * @return
 */
public OrderCompletePage completeBooking() {
	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(termsCheckbox));
	driver.findElement(termsCheckbox).click();
	driver.findElement(continueBookingBtn).click();
	return new OrderCompletePage(driver);
}

/**
 * get current booking id
 */
public String getBookingId() {
	String bookingID = driver.getCurrentUrl();
	bookingID = bookingID.substring(bookingID.indexOf("checkout/")+9);
	return bookingID;
}

/**
 * Get venue name shown on checkout
 * 
 * @return String venue name
 */
public String getVenueName() {
	List<WebElement> venueName = driver.findElements(venueNameLocator);
	return venueName.get(0).isDisplayed() ? venueName.get(0).getText() : venueName.get(1).getText();
}

/**
 * Get venue name shown on checkout
 * 
 * @return int time selected
 */
public int getTime() {
	String startTime = driver.findElements(startTimeLocator).get(0).isDisplayed() ? driver.findElements(startTimeLocator).get(0).getText() : driver.findElements(startTimeLocator).get(1).getText();
	String endTime = driver.findElements(endTimeLocator).get(0).isDisplayed() ? driver.findElements(endTimeLocator).get(0).getText() : driver.findElements(endTimeLocator).get(1).getText();
	int start=0, end=0, len;
	start = Integer.parseInt(startTime.split(":")[0]);
	end = Integer.parseInt(endTime.split(":")[0]);
	len=end-start;
	return len;
	
}

/**
 * Get room shown on checkout
 * 
 * @return String room name
 */
public String getRoom() {
	List<WebElement> room = driver.findElements(roomLocator);
	return room.get(0).isDisplayed() ? room.get(0).getText() : room.get(1).getText();
}

/**
 * Get delegates number shown on checkout
 * 
 * @return int number of delegates
 */
public int getDelegates() {
	List<WebElement> del = driver.findElements(delegatesLocator);
	String ret;
	ret = del.get(0).isDisplayed() ? del.get(0).getText() : del.get(1).getText();
	return Integer.parseInt(ret);
}

/**
 * Get seating order shown on checkout
 * 
 * @return String seating order
 */
public String getSeatingOrder() {
	List<WebElement> so = driver.findElements(seatingOrderLocator);
	return so.get(0).isDisplayed() ? so.get(0).getText() : so.get(1).getText();
}

/**
 * Get date shown on checkout
 * 
 * @return String date
 */
public String getDate() {
	List<WebElement> date = driver.findElements(bookingDateLocator);
	return date.get(0).isDisplayed() ? date.get(0).getText() : date.get(1).getText();
}

/**
 * Get price shown on checkout
 * 
 * @return double price
 */
public double getPrice() {
	List<WebElement> price = driver.findElements(priceLocator);
	String total = price.get(0).isDisplayed() ? price.get(0).getText() : price.get(1).getText();
	total = total.replace("$", "");
	total = total.replace("€", "");
	total = total.replace("£", "");
	total = total.trim();
	total = total.replace(",", ".");
//	if(total.equals(""))
//		total = "0";
	return Double.parseDouble(total);
}

/**
 * Get error message on checkout
 * 
 * @return String validation message, if none : "No validation error message"
 */
public String getValidationMessage(){
	String ret="No validation error message";
	if(driver.findElement(validationErrorText).isDisplayed()){
		ret=driver.findElement(validationErrorText).getText();
	}
	return ret;
}

//public OrderCompletePage completeBookingNewCP(boolean addons) {
//	List<WebElement> nextStep = driver.findElements(nextBtn);
//	
//	int i=0;
//	nextStep.get(i).click();
//		waitFor(500);
//		if(nextStep.size()>2&&addons==true){
//		addAddons();
//		//		System.out.println(driver.findElement(By.cssSelector("#add_menu_chosen > div > ul > li:nth-child(3)")).getText());
//		//		driver.findElement(By.cssSelector("li:contains("+option+")")).click();
//		//		driver.findElement(By.xpath("//li[. = '"+option+"']")).click();
//		}
//		nextStep.get(++i).click();
////		new Select(driver.findElement(By.id("add-menu"))).selectByVisibleText(option);
////		nextStep.get(++i).click();
//		waitFor(1000);
//		driver.findElement(continueBookingBtn).click();
//		return new OrderCompletePage(driver);
//}

public HashSet<String> addAddons() {
List<WebElement> nextStep = driver.findElements(nextBtn);
	
	List <String> menuOptions = new ArrayList<String>();
	List <String> miscOptions = new ArrayList<String>();
	List<WebElement> menuItems=driver.findElements(By.xpath("//select[@id='add-menu']/optgroup[@label='MENU' or @label='MENU ITEMS']/option"));
	List<WebElement> miscItems=driver.findElements(By.xpath("//select[@id='add-misc']/optgroup[@label='MISCELLANEOUS' or @label='SERVICES']/option"));
	int i=0;
	
	nextStep.get(i++).click();
	
	for (WebElement webElement : menuItems) {
		menuOptions.add(webElement.getAttribute("text"));
	}
	
	for (WebElement webElement : miscItems) {
		if(!webElement.getAttribute("text").contains("OnlyOne")){
			miscOptions.add(webElement.getAttribute("text"));
		}	
	}
	
//	System.out.println(options);
	for (String option : menuOptions) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("add_menu_chosen")));
		driver.findElement(By.id("add_menu_chosen")).click();
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(), '"+option+"')]")));
//		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[text() = '"+option+"']")));
		driver.findElement(By.xpath("//li[contains(text(), '"+option+"')]")).click();
	}
//	waitFor(3000);
//	driver.findElement(By.id("add_misc_chosen")).click();
	for (String option : miscOptions) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("add_misc_chosen")));
//		(new Actions(driver)).moveToElement(driver.findElement(By.id("add_misc_chosen"))).click().perform();
		driver.findElement(By.id("add_misc_chosen")).click();
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(), '"+option+"')]")));
//		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[text() = '"+option+"']")));
		driver.findElement(By.xpath("//li[contains(text(), '"+option+"')]")).click();
	}
	
//	nextStep.get(i).click();
	waitFor(1000);
	
	List<String> newList = new ArrayList<String>();
	newList.addAll(menuOptions);
	newList.addAll(miscOptions);
	return new HashSet<String>(newList);
}

public void addLimitedQuantityAddon() {
	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElements(nextBtn).get(0));

	driver.findElements(nextBtn).get(0).click();

	new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(By.id("add_misc_chosen")));
	((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("add_misc_chosen")));
	driver.findElement(By.id("add_misc_chosen")).click();

	new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//li[contains(text(), 'OnlyOne')]")));
	driver.findElement(By.xpath("//li[contains(text(), 'OnlyOne')]")).click();
	}

public boolean hasAddon(String addon) {
	return driver.findElements(By.xpath("//select[contains(@id, 'add-')]//option[contains(text(), '"+addon+"')]")).size()>0;
}

//public void assertInfo(String room, MeetingLength meetingLength, SeatingOrder seatingOrder, int numOfDelegates, String formattedDate){
//	Assert.assertTrue("Checkout page. Venue name is not test automation",getVenueName().equals("test automation venue"));
//	Assert.assertTrue("Checkout page. Room expected: "+room+". Shown: "+getRoom(),getRoom().replaceAll("\\s+","").equalsIgnoreCase(room));
//	Assert.assertTrue("Checkout page. Number of delegates expected: "+ numOfDelegates+". Shown: "+getDelegates(),getDelegates()==numOfDelegates); 
//	Assert.assertTrue("Checkout page. Booking time expected: "+meetingLength.toString()+"ours. Time shown: "+getTime()+" hours",getTime()==(meetingLength.ordinal()+2));
//	Assert.assertTrue("Checkout page. Date expected: " + formattedDate + ". Date shown: " +getDate(),getDate().contains(formattedDate));
//	Assert.assertTrue("Checkout page. Seating order expected: " + seatingOrder + ". Shown: " +getSeatingOrder(), getSeatingOrder().equalsIgnoreCase(seatingOrder.toString()));
//
//}

}
