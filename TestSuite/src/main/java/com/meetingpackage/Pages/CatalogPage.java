/* 
	 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
	 */
package com.meetingpackage.Pages;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Class for catalog page object
 */
public class CatalogPage extends BasePage {
	By addItemBtn = By.className("add-catalog-btn");
	By saveCatalogBtn = By.className("save-catalog-btn");
	By tabsLocator = By.cssSelector("#catalog-tabs > ul.nav-pills");
	By rowsLocator = By.cssSelector("#catalog-menu > tr");
	By deleteItemsBtn = By.className("delete-catalog-btn");
	By confirmBtn = By.cssSelector("div.jconfirm-buttons > button.btn.btn-blue");
	By confirmSaveBtn = By.cssSelector("div.jconfirm-buttons > button.btn-default");
	By csvUpload = By.cssSelector("div.table-footer > div > div > label > input.upload-csv-file-input");
	By menuNames = By.xpath("//input[contains(@class, 'menu-name')]");
	By venueSelect = By.xpath("//button[@data-id = 'venue-list']");
	By searchField = By.id("search-box");
	By searchButton = By.cssSelector("button.btn.search-item");
		
		/**
		 * Inbox page object constructor
		 * 
		 * @param driver	WebDriver to use
		 */
		public CatalogPage(WebDriver driver) {
			this.driver = driver;
			if (!checkPage(true,()->driver.getTitle().contains("Catalog"))) {
				throw new IllegalStateException("This is not the inbox page");
			}
		}
		
		public void clickSave() {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(saveCatalogBtn));
			driver.findElement(saveCatalogBtn).click();
			new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(confirmSaveBtn));
		}
		
		/**
		 * Adds line item for each tab(4) and saves it
		 */
		public void addItems() {
			String tab;
			new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
			waitFor(300);
			List<WebElement> addItem = driver.findElements(addItemBtn),
					saveBtn = driver.findElements(saveCatalogBtn),
					tabs=driver.findElement(tabsLocator).findElements(By.cssSelector("li"));
			for(int i=0;i<tabs.size()-1;i++) {
				new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(tabs.get(i)));
				tabs.get(i).click();
				new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
				waitFor(700);
//				tab = driver.findElement(By.cssSelector("#catalog-tabs > ul > li.active > a")).getAttribute("data-id");
				try{
				driver.findElement(By.cssSelector("div.tab-pane.active")).findElement(addItemBtn).click();
				} catch (Exception e){
					driver.findElement(confirmBtn).click();
					driver.findElement(By.cssSelector("div.tab-pane.active")).findElement(addItemBtn).click();
				}
				fillLastRowInfo("ItemTest"+i);
				saveBtn.get(i).click();
				new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(confirmSaveBtn));
				driver.findElement(confirmSaveBtn).click();
				waitFor(1000);
//				new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
//				new WebDriverWait(driver, 10).until(ExpectedConditions.attributeToBe(By.xpath("//table[@id = 'table-"+tab+"']/tbody/tr/td/input[contains(@value, 'ItemTest')]/../../td[4]/span/input"), "value", "0"));

			}
		}
		
		/**
		 * Deletes all added test items
		 */
		public void deleteItems() {
			String tab;
			List<WebElement> tabs=driver.findElement(tabsLocator).findElements(By.cssSelector("li")),
					checkboxes;
//			System.out.println(driver.findElements(By.xpath("//input[contains(@value, 'ItemTest')]")).size());
			for (int i = 0; i < tabs.size()-1; i++) {
//				waitFor(200);
				tabs.get(i).click();
				new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
				waitFor(1200);
				tab = driver.findElement(By.cssSelector("#catalog-tabs > ul > li.active > a")).getAttribute("data-id");
				checkboxes=driver.findElements(By.xpath("//table[@id = 'table-"+tab+"']/tbody/tr/td/input[contains(@value, 'ItemTest')]/../../td[1]/div/ins"));
				for (WebElement el : checkboxes) {
					el.click();
				}
				if(checkboxes.size()>0) {
					driver.findElements(deleteItemsBtn).get(i).click();
					waitFor(400);
//					new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI")));
					driver.findElement(confirmBtn).click();
//					tabs.get(i).click();
//					waitFor(1000);
					new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
				}
			}
			waitFor(1000);
		}
		
		public void selectTestVenue() {
			new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
			driver.findElement(venueSelect).click();
			new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[text() = 'test automation venue']")));
			driver.findElement(By.xpath("//span[text() = 'test automation venue']")).click();
			new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
			waitFor(500);
		}
		
		public void uploadCsv() {
			String path = System.getProperty("user.dir");
			driver.findElement(csvUpload).sendKeys(path+"/items.csv");
			new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
		}
		
		public List<String> getItemNames() {
			List<String> names = new ArrayList<String>();
			for (WebElement item : driver.findElements(menuNames)) {
				names.add(item.getAttribute("value"));
			}
			return names;
		}
		
		public boolean checkCsvUpload() {
			boolean flag = false;
			System.out.println(driver.findElement(menuNames).getAttribute("value"));
			List<WebElement> itemNames = driver.findElements(menuNames);
			for (WebElement item : itemNames) {
				if(item.getAttribute("value").contains("TotallyRandomName")){
					flag = true;
				}
			}
			return flag;
		}
		
		public void csvCleanUp() {
			List<WebElement> checkboxes;
			checkboxes=driver.findElements(By.xpath("//table[@id = 'table-menu']/tbody/tr/td/input[contains(@value, 'ItemTest')]/../../td[1]/div/ins"));
			for (WebElement el : checkboxes) {
				el.click();
			}
			if(checkboxes.size()>0) {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(deleteItemsBtn));
				driver.findElement(deleteItemsBtn).click();
				waitFor(900);
//				new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI")));
				driver.findElement(confirmBtn).click();
//				tabs.get(i).click();
//				waitFor(1000);
				new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
			}
			waitFor(3000);
		}
		
		/**
		 * Check if number of test items is equal to specified number
		 * @param num
		 * @return
		 */
		public boolean checkItems(int num) {
			int numOnPage = driver.findElements(By.xpath("//input[contains(@value, 'ItemTest')]")).size();
			System.out.println(numOnPage);
			return num==numOnPage;
		}
		
		public void searchFor(String searchTerm) {
			driver.findElement(searchField).sendKeys(searchTerm);
			driver.findElement(searchButton).click();
			new WebDriverWait(driver, 10).until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.blockUI.blockOverlay")));
		}
		
		public void fillLastRowInfo(String name) {
			String currentTab = driver.findElement(By.cssSelector("#catalog-tabs > ul > li.active > a")).getAttribute("data-id");
			List<WebElement> rows = driver.findElements(By.cssSelector("#table-"+currentTab+" > tbody > tr"));
			WebElement row = rows.get(rows.size()-1);
			By nameField = By.className(currentTab+"-name"),
					onlineCheckBox = By.className("online");
			row.findElement(nameField).sendKeys(currentTab+name);
//			if(online) {
//				row.findElement(onlineCheckBox).click();
//			}
		}
}
