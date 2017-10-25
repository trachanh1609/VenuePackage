/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Utility class with methods common for venue (just the room) and package booking
 *
 */
public class VenueUtils extends BasePage {
	
	public static enum Rooms {
		TESTROOM1,
		TESTROOM2,
		TESTROOM3,
		TESTROOM4,
		TESTROOM5,
		CUSTOM
	}
	public static enum MeetingLength {
		TwoH(2),
		ThreeH(3),
		FourH(4),
		FiveH(5),
		SixH(6),
		SevenH(7),
		EightH(8),
		NineH(9),
		TenH(10),
		Overnight(0),
		TwoDays(0);
		
		public final int value;

		  MeetingLength(final int value) {
		     this.value = value;
		  }
	}
	
	public enum SeatingOrder{
		COCKTAIL,
		U_SHAPE,
		BOARDROOM,
		THEATER,
		CLASSROOM,
		BANQUET,
		CABARET,
		NONE
	}
	
	/**
	 * Constructor
	 * @param driver
	 */
	public VenueUtils(WebDriver driver){
		this.driver = driver;
	}
	
	/**
	 * date picker not working properly on first use
	 * @param date
	 * @param dateTimeLocator
	 */
	private void clearDatePicker(LocalDate date, By dateTimeLocator) {
		driver.findElement(dateTimeLocator).click();
		
		String month = date.format(DateTimeFormatter.ofPattern("MMM"));
		String day = date.format(DateTimeFormatter.ofPattern("d"));
		By pickerDateLocator = By.xpath("//*[contains(@class, 'picker__day--infocus') and text() = '" + day + "']");
		By pickerMonthLocator = By.className("picker__month");
		By nineAmTimeLocator = By.cssSelector("ul.picker__list > li[aria-label='09:00']");
		By navNextMonthBtnLocator = By.cssSelector("div.picker__header > div.picker__nav--next");
		
		for(int i=0;i<12;i++){
			waitFor(100);

		if(driver.findElement(pickerMonthLocator).getText().contains(month)){
			break;
		}
//		driver.findElement(navNextMonthBtnLocator).click();
		(new Actions(driver)).moveToElement(driver.findElement(navNextMonthBtnLocator)).click().perform();
	}
	driver.findElement(pickerDateLocator).click();
	(new WebDriverWait(driver, 10))
	  .until(ExpectedConditions.visibilityOfElementLocated(nineAmTimeLocator));
	  waitFor(500);
//	  (new Actions(driver)).moveToElement(driver.findElement(nineAmTimeLocator)).click().perform();
	driver.findElement(nineAmTimeLocator).click();
	}
	
	/**
	 * Utility method to set the date
	 *
	 * @param date
	 */
	public void setDateCommon(LocalDate date, By dateTimeLocator) {
//		clearDatePicker(date, dateTimeLocator);
		String month = date.format(DateTimeFormatter.ofPattern("MMM"));
		String day = date.format(DateTimeFormatter.ofPattern("d"));
		By pickerDateLocator = By.xpath("//*[contains(@class, 'picker__day--infocus') and text() = '" + day + "']");
		By pickerMonthLocator = By.className("picker__month");
		By nineAmTimeLocator = By.cssSelector("ul.picker__list > li[aria-label='09:00']");
		By navNextMonthBtnLocator = By.cssSelector("div.picker__header > div.picker__nav--next");
		driver.findElement(dateTimeLocator).click();
		waitFor(300);
		for(int i=0;i<12;i++){
				waitFor(100);

			if(driver.findElement(pickerMonthLocator).getText().contains(month)){
				break;
			}
			driver.findElement(navNextMonthBtnLocator).click();
//			(new Actions(driver)).moveToElement(driver.findElement(navNextMonthBtnLocator)).click().perform();
		}
		driver.findElement(pickerDateLocator).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.visibilityOfElementLocated(nineAmTimeLocator));
		  waitFor(500);
//		  (new Actions(driver)).moveToElement(driver.findElement(nineAmTimeLocator)).click().perform();
		driver.findElement(nineAmTimeLocator).click();
	}
	
	/**
	 * Utility method to set number of delegates by clicking buttons
	 *
	 * @param quantity Number of delegates
	 */
	public void setDelegatesNumCommon(int quantity, int DEF_DELEGATES_QUANTITY, By incDelegatesLocator, By decDelegatesLocator) {
		waitFor(500);
		
		int diff = quantity - DEF_DELEGATES_QUANTITY;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(decDelegatesLocator));
		if (diff > 0) {
			for (int i = 0; i < diff; i++) {
				driver.findElement(incDelegatesLocator).click();
			}
		} else if (diff < 0) {
			for (int i = 0; i > diff; i--) {
				driver.findElement(decDelegatesLocator).click();
			}
		}
	}
	
	/**
	 * Utility method to select the room
	 *
	 * @param room  Enum of the room to select
	 * @param instantBooking TODO
	 */
	public void setRoomUtil(Rooms room, boolean instantBooking, By roomDropdownLocator) {
		Select roomSelect = new Select(driver.findElement(roomDropdownLocator));
		switch(room) {
			case TESTROOM1 :
				roomSelect.selectByVisibleText("test room 1");
				break;

			case TESTROOM2 :
				roomSelect.selectByVisibleText("test room 2");
				break;

			case TESTROOM3 :
				roomSelect.selectByVisibleText("test room 3");
				break;

			case TESTROOM4 :
				roomSelect.selectByVisibleText("test room 4");
				break;

			case TESTROOM5 :
				roomSelect.selectByVisibleText("test room 5");
				break;
			case CUSTOM:
				roomSelect.selectByVisibleText("Test 0 price");
				break;

			default:
				throw new IllegalArgumentException("Not defined room");
		}
		waitFor(500);
//		if(instantBooking)
//			if(roomSelect.getFirstSelectedOption().findElements(By.className("bolt")).size()==0)
//				throw new IllegalArgumentException("Room '"+roomSelect.getFirstSelectedOption().getText()+"' is not instant bookable");
	}
	
	/**
	 * Utility method to select meeting length
	 *
	 * @param meetingLen Enum of the meeting length to select
	 */
	public void setMeetingLengthUtil(MeetingLength meetingLen, By meetingLengthLocator) {
//		driver.findElement(By.cssSelector("*[data-id='edit-meeting-length']")).click();
//		By setMeetingLengthLocator = By.xpath("//*[@id=\"mp-enquiry-form\"]/div/div[1]/div[1]/div[6]/div/div/ul/li["+7+"]");
//		driver.findElement(setMeetingLengthLocator).click();
//		driver.findElement(meetingLengthLocator).click();
//		(new Actions(driver)).moveToElement(driver.findElement(meetingLengthLocator)).click().perform();
		Select lenSelect = new Select(driver.findElement(meetingLengthLocator));
//		lenSelect.getOptions().get(2).click();
		switch (meetingLen){
		case TwoH :
			lenSelect.selectByVisibleText("2 h");
			break;
		case ThreeH :
			lenSelect.selectByVisibleText("3 h");
			break;
		case FourH :
			lenSelect.selectByVisibleText("4 h");
			break;
		case FiveH :
			lenSelect.selectByVisibleText("5 h");
			break;
		case SixH :
			lenSelect.selectByVisibleText("6 h");
			break;
		case SevenH :
			lenSelect.selectByVisibleText("7 h");
			break;
		case EightH :
			lenSelect.selectByVisibleText("8 h");
			break;
		case NineH :
			lenSelect.selectByVisibleText("9 h");
			break;
		case TenH :
			lenSelect.selectByVisibleText("10 h");
			break;
		case Overnight :
			lenSelect.selectByVisibleText("Overnight");
			break;
		case TwoDays :
			lenSelect.selectByVisibleText("Two Days");
			break;
		default:
			throw new IllegalArgumentException("Not defined meeting length");
		}
	}
	
	/**
	 *
	 * Util method to select the seating order
	 *
	 * @param seatingOrder
	 */
	public void setSeatingOrderUtil(SeatingOrder seatingOrder, By seatingOrderLocator) {
		Select dropdown = new Select(driver.findElement(seatingOrderLocator));
		String optText;
		switch (seatingOrder){
		case COCKTAIL :
			optText="Cocktail";
			//dropdown.selectByVisibleText("Cocktail");
			break;
		case U_SHAPE :
			optText="U-Shape";
//			dropdown.selectByVisibleText("U-Shape");
			break;
		case BOARDROOM :
			optText="Boardroom";
//			dropdown.selectByVisibleText("Boardroom");
			break;
		case THEATER :
			optText="Theater";
//			dropdown.selectByVisibleText("Theater");
			break;
		case CLASSROOM :
			optText="Classroom";
//			dropdown.selectByVisibleText("Classroom");
			break;
		case BANQUET :
			optText="Banquet";
//			dropdown.selectByVisibleText("Banquet");
			break;
		case CABARET :
			optText="Cabaret";
//			dropdown.selectByVisibleText("Cabaret");
			break;
			case NONE :
				return;
		default:
			throw new IllegalArgumentException("Not defined seating order");
		}
		
		List<WebElement> options = dropdown.getOptions();
		for (WebElement option : options){
			if (option.getText().contains(optText)){
				dropdown.selectByVisibleText(option.getText());
			}
		}
		//dropdown.selectByVisibleText(optText);
	}
}
