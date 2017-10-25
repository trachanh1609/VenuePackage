package com.meetingpackage.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public class OrderCompletePage extends BasePage {
	By gotoOrderLocator = By.cssSelector("#edit-checkout-completion-message > div > div > div:nth-child(1) > a");
	
	/**
	 * Order complete page object constructor
	 * 
	 * @param driver	WebDriver to use
	 */
	public OrderCompletePage (WebDriver driver){
		this.driver=driver;
		if (!checkPage(true,()->true||("Checkout complete | MeetingPackage".equals(driver.getTitle())))) {
            throw new IllegalStateException("This is not the checkout complete page");
        }
	}
	
	public OrderPage goToOrderManagement() {
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(gotoOrderLocator));
		driver.findElement(gotoOrderLocator).click();
		return new OrderPage(driver);
	}
}
