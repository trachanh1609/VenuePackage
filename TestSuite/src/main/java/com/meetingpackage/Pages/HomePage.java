/* Copyright (C) 2017 Cocouz Ltd - All Rights Reserved */

package com.meetingpackage.Pages;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.meetingpackage.Pages.VenueUtils.MeetingLength;

/**
 * Class for home page object
 */
public class HomePage extends BasePage{
	
	By locationSearchFieldLocator = By.id("search-input");
	By datetimeLocator = By.id("datetime");
	By venueSearchButtonLocator = By.cssSelector("#search-form-wrapper > div > div:nth-child(2) > div.search-box-home > div > div > div.form-group.wrapper-where > div.toggle-search > div.venue");
	By venueSearchFieldLocator = By.id("venue");
	By listboxLocator = By.id("algolia-autocomplete-listbox-0");
	By testVenueSearchSuggestionLocator = By.cssSelector("a[href*='automation']");
	By meetingLengthLocator = By.id("edit-meeting-type");
	By submitButtonLocator = By.id("edit-submit");
//	By locationSearchContainerLocator = By.xpath("/html/body/div[4]");
	By locationSearchContainerLocator = By.cssSelector("body > div.pac-container.pac-logo.hdpi");
	
	By incDelegatesLocator = By.cssSelector(
			"#search-form-wrapper > div > div:nth-child(2) > div.search-box-home > div > div > div.form-group.wrapper-delegates2 > div > div.inc.button.buttoninc");
	By decDelegatesLocator = By.cssSelector(
			"#search-form-wrapper > div > div:nth-child(2) > div.search-box-home > div > div > div.form-group.wrapper-delegates2 > div > div.dec.button.buttoninc");
	
	private static final int DEF_DELEGATES_QUANTITY = 10;
	
	private String dateTime = "";
	

	/**
	 * Home page object constructor
	 * 
	 * @param driver	WebDriver to use
	 */
	public HomePage(WebDriver driver){
		boolean homepage;
		this.driver=driver;
		BooleanSupplier homepageCheck = ()->("Book meeting rooms, venues & packages | MeetingPackage.com".equals(driver.getTitle()) || driver.getTitle().contains("CWT"));
		if (!checkPage(true, homepageCheck)) {
			throw new IllegalStateException("This is not the main page");
		}
			
//		if (!"Book meeting rooms, venues & packages | MeetingPackage.com".equals(driver.getTitle())) {
//            throw new IllegalStateException("This is not the main page");
//        }
	}
	
	/**
	 * Get time and date selected in search field
	 * 
	 * @return String time
	 */
	public String getDateTime(){
		return dateTime;
	}
	
	/**
	 * Search for packages or rooms in location specified
	 * 
	 * @param String location to search packages in
	 * @param boolean pkg - True if searching for packages, false if for rooms
	 * @param int delegates - number of delegates to search for
	 * @return SearchPage
	 */
	public SearchPage locationSearch(String location, int delegates, boolean pkg){
		boolean newHomePage = getCurrentLanguage()==Languages.EN;
		meetingLengthLocator = newHomePage ? By.id("edit-meeting-type--2") : meetingLengthLocator;
		submitButtonLocator = newHomePage ? By.id("edit-submit--3") : submitButtonLocator;
		
		VenueUtils vu = new VenueUtils(driver);
		LocalDate date = LocalDate.now().plusDays(30);
		driver.findElement(locationSearchFieldLocator).clear();
		driver.findElement(locationSearchFieldLocator).sendKeys(location);
//		driver.findElement(datetimeLocator).sendKeys("14 September, 2017 @ 09:00");
		vu.setDateCommon(date, datetimeLocator);
		waitFor(1000);
		vu.setMeetingLengthUtil(MeetingLength.ThreeH, meetingLengthLocator);
		vu.setDelegatesNumCommon(delegates, DEF_DELEGATES_QUANTITY, incDelegatesLocator, decDelegatesLocator);
		Select roomORpkgSelect = new Select(driver.findElement(By.id(newHomePage ? "edit-index-id--2" : "edit-index-id")));
//		Select roomORpkgSelect = new Select(driver.findElement(By.id("edit-index-id")));

		if(pkg) {
			roomORpkgSelect.selectByValue("package_index");
		} else {
			roomORpkgSelect.selectByValue("venues_index");
		}
			
		dateTime = driver.findElement(datetimeLocator).getAttribute("value");
		driver.findElement(locationSearchFieldLocator).sendKeys(Keys.DOWN);
		try {
			(new WebDriverWait(driver, 10))
			  .until(ExpectedConditions.visibilityOfElementLocated(locationSearchContainerLocator));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			waitFor(1000);
		}
		driver.findElement(locationSearchFieldLocator).sendKeys(Keys.RETURN);
//		waitFor(1000);
		//		(new WebDriverWait(driver, 10))
//		  .until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("body > div.pac-container.pac-logo.hdpi")));
		
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.elementToBeClickable(submitButtonLocator));
		waitFor(2000);
		driver.findElement(submitButtonLocator).click();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		return new SearchPage(driver);
	}
	
	/**
	 * Check location search suggestions for duplicates
	 */
	public boolean checkSearchDuplicates(String location){
		boolean ret = false;
		List<WebElement> searchSuggestions;
		Set<String> uniques = new HashSet<String>();
		driver.findElement(locationSearchFieldLocator).clear();
		waitFor(300);
		driver.findElement(locationSearchFieldLocator).sendKeys(location);
//		driver.findElement(locationSearchFieldLocator).sendKeys(Keys.DOWN);
		try {
			waitFor(300);
			(new WebDriverWait(driver, 10))
			  .until(ExpectedConditions.visibilityOfElementLocated(locationSearchContainerLocator));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			waitFor(5000);
		}
//		driver.findElement(locationSearchFieldLocator).sendKeys(Keys.DOWN);
		searchSuggestions = driver.findElement(locationSearchContainerLocator).findElements(By.className("pac-item"));
		for (WebElement suggestion : searchSuggestions) {
			if(!uniques.add(suggestion.getText()))
				ret = true;
		}
//		driver.findElement(locationSearchFieldLocator).sendKeys(Keys.UP);
		driver.findElement(locationSearchFieldLocator).clear();
		ExpectedConditions.invisibilityOfElementLocated(locationSearchContainerLocator);
		return ret;
	}
	
	/**
	 * Search and go to venue by the name specified
	 * 
	 * @param name of the venue
	 * @return VenuePage
	 */
	public VenuePage venueSearch(String name){
		driver.findElement(venueSearchButtonLocator).click();
		driver.findElement(venueSearchFieldLocator).sendKeys(name);
		waitFor(500);
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.presenceOfNestedElementLocatedBy(listboxLocator, By.xpath("//a[contains(.,'"+name+"')]")));
		driver.findElement(listboxLocator).findElement(By.xpath("//a[contains(.,'"+name+"')]")).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabHandles = new ArrayList<String>(driver.getWindowHandles());
		tabHandles.remove(driver.getWindowHandle());
		driver.switchTo().window(tabHandles.iterator().next());
//		(new WebDriverWait(driver, 10))
//		  .until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(tabHandles.iterator().next()));
		return new VenuePage(driver);
	}
	
//	public VenuePage testVenueSearch(){
//		driver.findElement(venueSearchButtonLocator).click();
//		driver.findElement(venueSearchFieldLocator).sendKeys("automation");
//		
//		(new WebDriverWait(driver, 10))
//		  .until(ExpectedConditions.presenceOfElementLocated(testVenueSearchSuggestionLocator));
//		driver.switchTo().window(driver.getWindowHandle());
//		driver.findElement(testVenueSearchSuggestionLocator).click();
//		driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
//		waitFor(500);
//		
////		try {
////			Thread.sleep(2000);
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//		
//		ArrayList<String> tabHandles = new ArrayList<String>(driver.getWindowHandles());
////
////		String venuePageTitle = "Meeting Rooms and Venues";
////
////		for(String eachHandle : tabHandles)
////		{
////		    driver.switchTo().window(eachHandle);
////		    if(driver.getTitle().contains(venuePageTitle))
////		    {
////		        break;
////		    }
////		    else
////		    	driver.close();
////		}
//		tabHandles.remove(driver.getWindowHandle());
//		driver.switchTo().window(tabHandles.iterator().next());
//
//		return new VenuePage(driver);
//	}
}
