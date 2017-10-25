/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for search page object
 */
public class SearchPage extends BasePage {
	By datetimeLocator = By.cssSelector("div.left-column > div.filters > div.filter.date-time > input");
	By meetingLengthLocator = By.cssSelector("div.left-column > div.filters > div.filter.meeting-length > div.btn-group.bootstrap-select > button");
	By delegatesLocator = By.id("people-amount");
	By searchForLocator = By.cssSelector("div.filter.search-for > div > button");
	By hitsLocator = By.id("hits");
	By searchWhenMoveMapToggle = By.id("search-toggle");
	By sortNearestLocator = By.cssSelector("div.left-column > div.sort-filter > solr-sort-facets > div.nearest");
	By sortCheapestLocator = By.cssSelector("div.left-column > div.sort-filter > solr-sort-facets > div.cheapest");
	By sortLargestLocator = By.cssSelector("div.left-column > div.sort-filter > solr-sort-facets > div.largest");
	By sortSmallestLocator = By.cssSelector("div.left-column > div.sort-filter > solr-sort-facets > div.smallest");
	By priceSliderLocator = By.cssSelector("#pips-range > div.noUi-base");
	By curMinPriceLocator = By.cssSelector("div.left-column > div.filters > div.filter.price-slider > div.filter-label > span.price-min");
	By incPriceSliderLocator = By.cssSelector("#pips-range > div.noUi-base > div.noUi-origin > div.noUi-handle-lower");
	By filtersBtnLocator = By.cssSelector("div.left-column > div.sort-filter.col-md-12 > solr-sort-facets > button.btn.btn-info.btn-sm.filters-button");
	By clearFiltersBtnLocator = By.cssSelector("div.left-column > div.sort-filter.col-md-12 > solr-sort-facets > button.btn.btn-danger.btn-sm.clear-filters-button");
	By inAirportFilterLocator = By.xpath("//*[@id='facets']//a[contains(@data-attribute, 'field_venue_environment') and contains(@data-value, 'In the Airport')]");
	By hitPriceLocator = By.cssSelector("div.hit-info-price");

	/**
	 * Constructor
	 * @param driver
	 */
	public SearchPage(WebDriver driver) {
		this.driver=driver;
//		String title = driver.getTitle();
		BooleanSupplier titleCheck = ()->driver.getTitle().contains("Meeting rooms in")||driver.getTitle().contains("Kokoustilat")||driver.getTitle().contains("Venues in")||driver.getTitle().contains("Juhlatilat");
//		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		if (!checkPage(true, titleCheck)) {
	        throw new IllegalStateException("This is not the search page");
	    }
	}
	
	/**
	 * Get meeting length shown on page
	 * 
	 * @return String meeting length
	 */
	public String getSelectedMLength() {
		return driver.findElement(meetingLengthLocator).getAttribute("title");
	}
	
	/**
	 * Get date shown on page
	 * 
	 * @return String date
	 */
	public String getSelectedDate() {
		return driver.findElement(datetimeLocator).getAttribute("value");
	}
	
	/**
	 * Get number of delegates shown on page
	 * 
	 * @return String number of delegates
	 */
	public String getSelectedDelegates() {
		return driver.findElement(delegatesLocator).getAttribute("value");
	}
	
	/**
	 * Returns what is searched for (rooms or packages)
	 * 
	 * @return String rooms or packages
	 */
	public String getSelectedSearchFor() {
		return driver.findElement(searchForLocator).getAttribute("title");
	}
	
	/**
	 * Get search results from current page
	 * @return List<WebElement> results
	 */
	public List<WebElement> getSearchHits() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		return driver.findElement(hitsLocator).findElements(By.cssSelector("div.imagebox > a:nth-child(3) > img"));
	}
	
	/**
	 * Get search result prices from current page
	 * @return List<WebElement> prices
	 */
	public List<WebElement> getSearchHitPrices() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		return driver.findElement(hitsLocator).findElements(By.cssSelector("div.hit-contentbox>div.hit-info-price.total"));
	}
	
	/**
	 * Get search result distance from current page
	 * @return List<WebElement> prices
	 */
	public List<WebElement> getSearchHitsDistance() {
		(new WebDriverWait(driver, 10))
	  .until(ExpectedConditions.visibilityOfElementLocated(hitsLocator));
		return driver.findElement(hitsLocator).findElements(By.cssSelector("div.hit-contentbox > div.text-right > p"));
	}
	
	/**
	 * Get search results venue names from current page
	 * @return List<WebElement> prices
	 */
	public List<WebElement> getSearchHitNames() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		return driver.findElement(hitsLocator).findElements(By.cssSelector("div.hit-contentbox > h3.hit-airport-code"));
	}
	
	/**
	 * Get first venue price from search results page
	 * @return double price
	 */
	public double getFirstResultPrice() {
		Double price;
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		price = Double.parseDouble(driver.findElement(hitsLocator).findElements(hitPriceLocator).get(0).getText().replace("€ ", "").replace(" /", "").replace("person","").trim());
		return price;
	}
	
	/**
	 * Get last venue price from search results page
	 * @return double price
	 */
	public double getLastResultPrice() {
		Double price;
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		price = Double.parseDouble(driver.findElement(hitsLocator).findElements(hitPriceLocator).get(getSearchHits().size()-1).getText().replace("€ ", "").replace(" /", ""));
		return price;
	}
	
	/**
	 * Get first venue name from search results page
	 * @return String venue name
	 */
	public String getFirstResultName() {
		return getSearchHitNames().get(0).getText();
	}
	
	/**
	 * Get last venue name from search results page
	 * @return String venue name
	 */
	public String getLastResultName() {
		List<WebElement> els = getSearchHitNames();
		return els.get(els.size()-1).getText();
	}
	
//	/**
//	 * Get number of venues shownon search results page
//	 * @return int number of venues
//	 */
//	public int getNumberOfVenues() {
//		return
//	}
	
	/**
	 * Method to increase minimum price filter higher than specified number
	 * @param minPrice
	 */
	public void incPriceFilter(int minPrice) {
		WebElement slider = driver.findElement(priceSliderLocator);
		WebElement minPriceFilter = driver.findElement(curMinPriceLocator);
		Actions moveSlider = new Actions(driver);
//		WebElement a = driver.findElement(By.cssSelector("body > div:nth-child(3) > section > div.left-column > div.filters > div.filter.price-slider > div.filter-label > span.price-min"));
//		JavascriptExecutor js = (JavascriptExecutor) driver;
//		WebElement a = driver.findElement(By.xpath("//*[@id='pips-range']/div[1]/div[1]"));
//		WebElement b = driver.findElement(By.xpath("//*[@id='pips-range']/div[1]/div[2]"));
//		js.executeScript("arguments[0].setAttribute('style', 'left: 30%;')",a);
//		js.executeScript("arguments[0].textContent = '18'",a);
//		js.executeScript("javascript:document.getElementsByClassName('noUi-connect').value=20;");
//		moveSlider.clickAndHold(slider).moveByOffset(100, 0).moveByOffset(90, 0).release().perform();
		int width = slider.getSize().width,
				percent = width/2/100; // 1 percent of half of the slider
		String price = "0 /";
		
		moveSlider.moveToElement(slider);
		moveSlider.moveByOffset(-(percent*100), 0);    // go to left side of slider
		for(int i = 0;i<19;i++){
			moveSlider.moveByOffset(percent*10, 0).click().perform(); //increase minimum price with increments of 5 percent
			waitFor(3000);
//			(new WebDriverWait(driver, 10))
//			  .until(ExpectedConditions.not(ExpectedConditions.textToBe(curMinPriceLocator, price)));
			price = minPriceFilter.getText();
//			System.out.println(price);
			if(minPrice<Integer.parseInt(price.replace(" /", "").replace("person","").trim())) {
				break;
			}
		}
		if(minPrice>=Integer.parseInt(minPriceFilter.getText().replace(" /", "").replace("person","").trim())) {
			throw new IllegalStateException("Could not increase filter for minimum price by "+minPrice);
		}
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		
//		waitFor(1000);
//		moveSlider.clickAndHold();
//		waitFor(1000);
//		moveSlider.moveByOffset(-20, 0);
//		waitFor(1000);
//		moveSlider.release();
//		waitFor(1000);
//		moveSlider.click().perform();
//		waitFor(1000);
//		moveSlider.moveToElement(slider);
//		moveSlider.moveByOffset(20, 0);
//		moveSlider.click().perform();
//		driver.get(driver.getCurrentUrl().replaceAll("price_min=0", "price_min=20"));
//		moveSlider.dragAndDropBy(slider, 90, 0).build().perform();
//		moveSlider.contextClick().perform();
//		moveSlider.dragAndDropBy(slider, 300, 0).perform();
//        action.perform();
//		action.perform();
//		waitFor(4000);
//		while(true)
//			moveSlider.moveToElement(slider).clickAndHold().moveByOffset(100, 0).release().perform();
//			moveSlider.dragAndDropBy(slider, 90, 0).build().perform();
		

//		waitFor(2000);
	}
	
	/**
	 * Method to reset minimum price filter to 0
	 */
	public void resetPriceFilter() {
		WebElement slider = driver.findElement(priceSliderLocator);
		WebElement minPriceFilter = driver.findElement(curMinPriceLocator);
		Actions moveSlider = new Actions(driver);
		int width = slider.getSize().width,
				percent = width/2/100; // 1 percent of half of the slider
		String price = "0 /";
		
		moveSlider.moveToElement(slider);
		moveSlider.moveByOffset(-(width/2-1), 0).click().perform();    // go to left side of slider
		waitFor(3000);
//		price = minPriceFilter.getText();
//		System.out.println(price);
		if(!minPriceFilter.getText().replace("person","").replace("/","").trim().equals("0")) {
			throw new IllegalStateException("failed to reset min price filter to 0");
		}
//		for(int i = 0;i<19;i++){
//			moveSlider.moveByOffset(percent*10, 0).click().perform(); //increase minimum price with increments of 5 percent
//			waitFor(3000);
//			price = minPriceFilter.getText();
//			System.out.println(price);
//			if(minPrice<Integer.parseInt(price.replace(" /", ""))) {
//				break;
//			}
//		}
//		if(minPrice>=Integer.parseInt(minPriceFilter.getText().replace(" /", ""))) {
//			throw new IllegalStateException("Could not increase filter for minimum price by "+minPrice);
//		}
//		(new WebDriverWait(driver, 10))
//		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
	}
	
	/**
	 * Set search filter and return number of venues that should be shown
	 * 
	 * @return int Number of venues that filter applies to
	 */
	public int setEnvFilter() {
		int venuesNum;
		
		driver.findElement(filtersBtnLocator).click();
		venuesNum = Integer.parseInt(driver.findElement(inAirportFilterLocator).getText().replaceAll("[^\\d.]", ""));
		driver.findElement(inAirportFilterLocator).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		waitFor(500);
		return venuesNum;
	}
	
	/**
	 * click clear filters button on SP
	 */
	public void clearFilters() {
		((JavascriptExecutor) driver).executeScript("window.scrollTo(0, 0);");
		driver.findElement(clearFiltersBtnLocator).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		waitFor(500);
	}
	
	/**
	 * Check if venues are sorted by nearest distance
	 * @return boolean true if sorted by distance ascending, false if not
	 */
	public boolean distanceAscending() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		
		List<WebElement> searchResDistances = driver.findElement(hitsLocator).findElements(By.cssSelector("div.hit-contentbox > div.text-right > p"));
		Double prev = 0.0, cur;
		for (WebElement webElement : searchResDistances) {
			cur = Double.parseDouble(webElement.getText().replace("km", "").replace("location_on","").trim());
			if(cur<prev)
				return false;
			prev=cur;
		}
		return true;
	}
	
	/**
	 * Check if venues are sorted cheapest first
	 * @return boolean true if sorted by price ascending, false if not
	 */
	public boolean priceAscending() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		
		List<WebElement> searchResPrices = getSearchHitPrices();
		double prev = 0.0, cur;
		if(String.valueOf(searchResPrices.get(0)).contains("css selector"))
			return false;
		for (WebElement webElement : searchResPrices) {
			cur = Double.parseDouble(webElement.getText().replace("£", "").replace("person","").trim());
			if(cur<prev)
				return false;
			prev=cur;
		}
		return true;
	}
	
	/**
	 * Check if venues are sorted largest first
	 * @return boolean true if sorted by price ascending, false if not
	 */
	public boolean sizeDescending() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		
		List<WebElement> searchResMaxDelegates = driver.findElement(hitsLocator).findElements(By.cssSelector("div.imagebox > a:nth-child(5) > div > div.hit-info-delegates"));
		int prev = 99999999, cur;
		for (WebElement webElement : searchResMaxDelegates) {
//			System.out.println(webElement.getText());
			cur = Integer.parseInt(webElement.getText().substring(webElement.getText().lastIndexOf(" - ")+3));
			if(cur>prev)
				return false;
			prev=cur;
		}
		return true;
	}
	
	/**
	 * Check if venues are sorted smallest first
	 * @return boolean true if sorted by price ascending, false if not
	 */
	public boolean sizeAscending() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(hitsLocator))));
		
		List<WebElement> searchResMaxDelegates = driver.findElement(hitsLocator).findElements(By.cssSelector("div.imagebox > a:nth-child(5) > div > div.hit-info-delegates"));
		int prev = 0, cur;
		for (WebElement webElement : searchResMaxDelegates) {
//			System.out.println(webElement.getText());
			cur = Integer.parseInt(webElement.getText().substring(webElement.getText().lastIndexOf(" - ")+3));
			if(cur<prev)
				return false;
			prev=cur;
		}
		return true;
	}
	
	/**
	 * returns state of 'Search when I move map checkbox'
	 * @return true if checked, false if not
	 */
	public boolean searchWhenMoveChecked() {
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.visibilityOfElementLocated(searchWhenMoveMapToggle));
		return driver.findElement(searchWhenMoveMapToggle).isSelected();
	}
	
	/**
	 * returns sort nearest button
	 * @return WebElement sort nearest button
	 */
	public WebElement sortNearest() {
		return driver.findElement(sortNearestLocator);
	}

	/**
	 * returns sort cheapest button
	 * @return WebElement sort cheapest button
	 */
	public WebElement sortCheapest() {
		return driver.findElement(sortCheapestLocator);
	}

	/**
	 * returns sort largest button
	 * @return WebElement sort largest button
	 */
	public WebElement sortLargest() {
		return driver.findElement(sortLargestLocator);
	}

	/**
	 * returns sort smallest button
	 * @return WebElement sort smallest button
	 */
	public WebElement sortSmallest() {
		return driver.findElement(sortSmallestLocator);
	}
}
