/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.Pages;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for inbox page object
 */
public class InboxPage extends BasePage {
	By ordersLocator = By.xpath("//tbody/tr");
	By orderIdLocator = By.xpath("td[contains(@class, 'order-order_id')]/a");
	By venueNameLocator = By.xpath("td[contains(@class, 'order-venue')]");
	By testVenueNameLocator = By.xpath("td[contains(@class, 'order-venue') and text()='test automation venue']");
	By orderStatusLocator = By.xpath("td[contains(@class, 'order-status')]/span");
	By goToOrder = By.xpath("td[contains(@class, 'order-action')]/a");
	By nextPageLocator = By.id("pager_next");
	
	/**
	 * Inbox page object constructor
	 * 
	 * @param driver	WebDriver to use
	 */
	public InboxPage(WebDriver driver) {
		this.driver = driver;
		if (!checkPage(true,()->driver.getTitle().contains("Inbox"))) {
			throw new IllegalStateException("This is not the inbox page");
		}
	}
	
	public OrderPage gotoOrderById(String id) {
		List<WebElement> orderIds = driver.findElements(ordersLocator).stream().map((o)->o.findElement(orderIdLocator)).collect(Collectors.toList());
		WebElement orderFound = null;
		for (WebElement order : orderIds) {
			if (order.getText().equals(id)) {
				orderFound = order;
			}
		}
		if (orderFound != null) {
			orderFound.click();
		} else {
			throw new NoSuchElementException("Order with the specified id(" + id + ") not found");
		}
		return new OrderPage(driver);
	}

	public List<WebElement> getTestBookings(){
		Stream<WebElement> bookings = driver.findElements(ordersLocator).stream();
		return bookings.filter((b)->b.findElement(venueNameLocator).getText().equalsIgnoreCase("test automation venue")).collect(Collectors.toList());
//		return bookings.filter((b)->b.findElement(testVenueNameLocator).isDisplayed()).collect(Collectors.toList());
	}

	public void cancelBookings(List<WebElement> bookings){
		int j=0;
		bookings = bookings.stream().filter((b)->(!b.findElement(orderStatusLocator).getText().equals("Cancelled"))).collect(Collectors.toList()); //get all not cancelled bookings
		for (WebElement booking:bookings) {
//			booking.findElement(orderIdLocator).click();
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", booking);
			new Actions(driver)
					.keyDown(Keys.COMMAND)
					.click(booking.findElement(goToOrder))
					.keyUp(Keys.COMMAND)
					.build().perform();
		}
		ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
		for(int i=1;i<tabs.size();i++) {
			driver.switchTo().window(tabs.get(i));
			new OrderPage(driver).cancelBooking();
			driver.close();
		}
		driver.switchTo().window(tabs.get(0));
	}

	public void gotoNextPage(){
		driver.findElement(nextPageLocator).click();
		new WebDriverWait(driver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='loadingRooms'][contains(@style, 'display: none;')]")));
	}
}
