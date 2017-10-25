package com.meetingpackage.Pages;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.By.ByCssSelector;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.meetingpackage.Pages.VenueUtils.Rooms;
import com.meetingpackage.Pages.VenueUtils;
import com.meetingpackage.Pages.OrderPage.LineItem;

public class OrderPage extends BasePage {
	By bookingIdHeader = By.id("booking-id");
	By cancelDropdownBtn = By.cssSelector("#main > div > div.width-100.flex.justify--end.hideifcanceled > div > div:nth-child(1) > button");
	By cancelReasonSelect = By.id("reason-for-cancellation");
	By cancelReasonBtn = By.cssSelector("#order-cancellation > div.row > div > div > div > div > button");
	By cancelReasonDropdown = By.cssSelector("#order-cancellation > div.row > div > div > div > div > div > ul");
	By confirmBookingBtn = By.cssSelector("#floating-save > div > div.right-eleements > div.space-out__element.bottom-margin--10.confirm-block > button");
//	By confirmBookingBtn = By.cssSelector("#main > div.inner.inner--1170 > div:nth-child(4) > div > div.space-out__element.bottom-margin--10 > button");
	public By orderCancelledLbl = By.cssSelector("#main > div > div:nth-child(3) > ol > li > span");
	By addLineItemBtn = By.cssSelector("#drawer-uniquie-id-5 > div > div > div > div.space-out__element.bottom-margin--10 > button");
	By newLineItem = By.id("order-id-new");
	By itemName = By.id("title");
	By itemQuantity = By.id("quantity");
	By itemPriceNoVat = By.id("price");
	By itemPriceVat = By.id("vat");
	public By lineItemTotal = By.cssSelector("td.total");
	public By lineItemsLocator = By.id("line-items");
	By saveBtn = By.cssSelector("#floating-save > div > div.right-eleements > div.space-out__element.bottom-margin--10 > button");
	public By resEnquiryTotal = By.cssSelector("#memo > tbody > tr:nth-child(1) > td:nth-child(7)");
	By orderStatus = By.xpath("//ol[contains(@class, 'traffic-lights')]/li/span[contains(@class, 'traffic-lights__content--')]");
	By otherCancellationReason = By.id("other-cancellation-reason");
	By delegatesField = By.id("delegates");
	public By startDateField = By.id("start-date");
	By startTimeField = By.id("start-time");
	By endDateField = By.id("end-date");
	By endTimeField = By.id("end-time");
	By roomSelect = By.id("rooms");
	By layoutSelect = By.id("layout");
	By messageField = By.id("message");
	By sendMsgBtn = By.xpath("//*[@id='drawer-uniquie-id-1']//button[contains(@class, 'message-send')]");
	By messagesLog = By.id("messages");
	By messageLogText = By.cssSelector("div.message__info > div.message__body.simple-text > p");
	By attachFileInput = By.id("attach-file");
	By unsentFilesList = By.cssSelector("ul > li > div.js-input-file__files");
	By sentFilesList = By.cssSelector("#messages > div.message > div.message__info > li");
	
	//Client info fields:
	By companyField = By.id("company");
	By streetAddressField = By.id("street");
	By zipField = By.id("zip");
	By cityField = By.id("city");
	By stateField = By.id("state");
	By countryDropdown = By.id("country");
	By firstNameField = By.id("first-name");
	By lastNameField = By.id("family-name");
	By phoneField = By.id("phone");
	By emailField = By.id("email-address");
	By billingCodeField = By.id("billing-code");
	
	//Message log changes:
	By companyLog = By.xpath("//*[@id='messages']//span[text()='Company']/../span[2]/b[2]");
	By streetAddressLog = By.xpath("//*[@id='messages']//span[text()='Street Address']/../span[2]/b[2]");
	By zipLog = By.xpath("//*[@id='messages']//span[text()='Postal Code']/../span[2]/b[2]");
	By cityLog = By.xpath("//*[@id='messages']//span[text()='City']/../span[2]/b[2]");
	By stateLog = By.xpath("//*[@id='messages']//span[text()='State']/../span[2]/b[2]");
	By countryLog = By.xpath("//*[@id='messages']//span[text()='Country']/../span[2]/b[2]");
	By firstNameLog = By.xpath("//*[@id='messages']//span[text()='First Name']/../span[2]/b[2]");
	By lastNameLog = By.xpath("//*[@id='messages']//span[text()='Family Name']/../span[2]/b[2]");
	By phoneLog = By.xpath("//*[@id='messages']//span[text()='Phone']/../span[2]/b[2]");
	By emailLog = By.xpath("//*[@id='messages']//span[text()='Email']/../span[2]/b[2]");
	By billingCodeLog = By.xpath("//*[@id='messages']//span[text()='Billing Code']/../span[2]/b[2]");
	By delegatesLog = By.xpath("//*[@id='messages']//span[text()='Delegates']/../span[2]/b[2]");
	By startDateLog = By.xpath("//*[@id='messages']//span[text()='Start Date']/../span[2]/b[2]");
	By startTimeLog = By.xpath("//*[@id='messages']//span[text()='Start Time']/../span[2]/b[2]");
	By endDateLog = By.xpath("//*[@id='messages']//span[text()='End Date']/../span[2]/b[2]");
	By endTimeLog = By.xpath("//*[@id='messages']//span[text()='End Time']/../span[2]/b[2]");
	By roomLog = By.xpath("//*[@id='messages']//span[text()='Rooms']/../span[2]/b[2]");
	By layoutLog = By.xpath("//*[@id='messages']//span[text()='Layout']/../span[2]/b[2]");
	
	
	/**
	 * class for line item
	 * @author einonevolainen
	 *
	 */
	public static class LineItem {
		String name;
		int quantity;
		double priceNoVat, priceVat, amount;
//		Unit un;
//		
//		public enum Unit{
//			HOUR,
//			PERSON,
//			UNIT
//		}
		
		public LineItem(String name, int quantity, double priceNoVat, double priceVat) {
			this.name = name;
			this.quantity = quantity;
			this.priceNoVat = priceNoVat;
			this.priceVat = priceVat;
			amount = priceVat*quantity;
		}
		
		
		
		
		@Override
		public String toString() {
			return "LineItem [name=" + name + ", quantity=" + quantity + ", priceNoVat=" + priceNoVat + ", priceVat="
					+ priceVat + ", amount=" + amount + "]";
		}




		public String getName() {
			return name;
		}

		public int getQuantity() {
			return quantity;
		}

		public double getPriceNoVat() {
			return priceNoVat;
		}

		public double getPriceVat() {
			return priceVat;
		}

		public double getAmount() {
			return amount;
		}
	}
	
	/**
	 * class for customer info
	 * @author einonevolainen
	 *
	 */
	public class CustomerInfo {
		public String getCompany() {
			return company;
		}

		public String getStreet() {
			return street;
		}

		public String getZip() {
			return zip;
		}

		public String getCity() {
			return city;
		}

		public String getState() {
			return state;
		}

		public String getCountry() {
			return country;
		}

		public String getFirstname() {
			return firstname;
		}

		public String getLastname() {
			return lastname;
		}

		public String getPhone() {
			return phone;
		}

		public String getEmail() {
			return email;
		}

		public String getBillingCode() {
			return billingCode;
		}

		String company, street, zip, city, state, country, firstname, lastname, phone, email, billingCode;

		public CustomerInfo(String company, String street, String zip, String city, String state, String country,
				String firstname, String lastname, String phone, String email, String billingCode) {
			this.company = company;
			this.street = street;
			this.zip = zip;
			this.city = city;
			this.state = state;
			this.country = country;
			this.firstname = firstname;
			this.lastname = lastname;
			this.phone = phone;
			this.email = email;
			this.billingCode = billingCode;
		}
		
		@Override
		public String toString() {
			return "CustomerInfo [company=" + company + ", street=" + street + ", zip=" + zip + ", city=" + city
					+ ", state=" + state + ", country=" + country + ", firstname=" + firstname + ", lastname="
					+ lastname + ", phone=" + phone + ", email=" + email + ", billingCode=" + billingCode + "]";
		}
	}
	
	/**
	 * class for meeting details
	 * @author einonevolainen
	 *
	 */
	public class MeetingDetails {
		String startDate, startTime, endDate, endTime, delegates, room, layout;
		
		public MeetingDetails(String startDate, String startTime, String endDate, String endTime, String delegates,
				String room, String layout) {
			this.startDate = startDate;
			this.startTime = startTime;
			this.endDate = endDate;
			this.endTime = endTime;
			this.delegates = delegates;
			this.room = room;
			this.layout = layout;
		}

		public String getStartDate() {
			return startDate;
		}

		public String getStartTime() {
			return startTime;
		}

		public String getEndDate() {
			return endDate;
		}

		public String getEndTime() {
			return endTime;
		}

		public String getDelegates() {
			return delegates;
		}

		public String getRoom() {
			return room;
		}

		public String getLayout() {
			return layout;
		}

		@Override
		public String toString() {
			return "MeetingDetails [startDate=" + startDate + ", startTime=" + startTime + ", endDate=" + endDate
					+ ", endTime=" + endTime + ", delegates=" + delegates + ", room=" + room + ", layout=" + layout
					+ "]";
		}
		
	}
	
	/**
	 * Order page object constructor
	 * 
	 * @param driver	WebDriver to use
	 */
	public OrderPage (WebDriver driver) {
//		waitFor(2000);
		this.driver=driver;
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(bookingIdHeader));
		new WebDriverWait(driver, 10).until(ExpectedConditions.not(ExpectedConditions.textToBePresentInElement(bookingIdHeader, "BOOKING LOADING")));
		if (!checkPage(true,()->driver.findElement(bookingIdHeader).getText().contains("BOOKING #"))) {
            throw new IllegalStateException("This is not the order page");
        }
	}
	
	/**
	 * confirm this order
	 */
	public void confirmBooking() {
		driver.findElement(confirmBookingBtn).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.invisibilityOfElementLocated(confirmBookingBtn));
	}
	
	/**
	 * cancel this order
	 */
	public void cancelBooking() {
		if(!driver.findElement(By.id("submit-cancel")).isDisplayed()) {
			new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(cancelDropdownBtn));
			driver.findElement(cancelDropdownBtn).click();
			(new WebDriverWait(driver, 10))
			.until(ExpectedConditions.elementToBeClickable(By.id("submit-cancel")));
		}
		driver.findElement(By.id("submit-cancel")).click();
	}
	
	/**
	 * set reason for order cancellation
	 * @param other
	 */
	public void setCancelReason(boolean other) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(cancelDropdownBtn));
		driver.findElement(cancelDropdownBtn).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.not(ExpectedConditions.stalenessOf(driver.findElement(cancelReasonBtn))));
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.elementToBeClickable(cancelReasonBtn));
		waitFor(500);
		driver.findElement(cancelReasonBtn).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.visibilityOfElementLocated(cancelReasonDropdown));
//		Select reasonSelect = new Select(driver.findElement(cancelReasonSelect));
		if(other) {
			driver.findElement(cancelReasonDropdown).findElement(By.linkText("Other")).click();
			driver.findElement(otherCancellationReason).sendKeys("blahblahblah other cancellation reason");
		}
		else {
			driver.findElement(cancelReasonDropdown).findElement(By.xpath("//li[contains(@data-original-index, '1')]")).click();
//			reasonSelect.selectByValue("0");
		}
		waitFor(100);
	}
	
	/**
	 * edit enquiry(first line item)
	 * @param LineItem info to update
	 */
	public void editEnquiryLineItem(LineItem update) {
		WebElement item = driver.findElement(By.xpath("//*[@id='line-items']/tr[1]"));
		item.findElement(itemQuantity).clear();
		item.findElement(itemPriceNoVat).clear();
		item.findElement(itemPriceVat).clear();
		
		item.findElement(itemQuantity).sendKeys(String.valueOf(update.quantity));
		item.findElement(itemPriceNoVat).sendKeys(String.valueOf(update.priceNoVat));
		item.findElement(itemPriceVat).sendKeys(String.valueOf(update.priceVat));
	}
	
	/**
	 * add line items to the order
	 * @param lineItems
	 */
	public void addItems(List<LineItem> lineItems) {
//		List<LineItem> lineItems = new ArrayList<LineItem>();
//		LineItem l= new LineItem("",1,1.5,1.5);
		List<WebElement> lines;
//		lineItems.add(new LineItem("Tea", 50, 0.8, 1));
//		lineItems.add(new LineItem("Snack", 30, 2.3, 2.5));
//		lineItems.add(new LineItem("Lunch", 40, 3.5, 4));
		for (int i = 0; i<lineItems.size(); i++) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(addLineItemBtn));
			driver.findElement(addLineItemBtn).click();
//			System.out.println(lineItems.get(i).name);
		}
		lines = driver.findElements(newLineItem);
		for (int i = 0; i < lineItems.size(); i++) {
			lines.get(i).findElement(itemName).sendKeys(lineItems.get(i).name);
			lines.get(i).findElement(itemQuantity).sendKeys(String.valueOf(lineItems.get(i).quantity));
			lines.get(i).findElement(itemPriceNoVat).sendKeys(String.valueOf(lineItems.get(i).priceNoVat));
			lines.get(i).findElement(itemPriceVat).sendKeys(String.valueOf(lineItems.get(i).priceVat));
		}
		clickSaveBtn();
	}
	
	/**
	 * add offline catalog items to the order
	 * @param lineItems
	 */
	public void addCatalogItems(List<LineItem> lineItems) {
		List<WebElement> lines;
		for (int i = 0; i<lineItems.size(); i++) {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(addLineItemBtn));
			driver.findElement(addLineItemBtn).click();
		}
		lines = driver.findElements(newLineItem);
		for (int i = 0; i < lineItems.size(); i++) {
			lines.get(i).findElement(itemName).sendKeys(lineItems.get(i).name);
			(new WebDriverWait(driver, 10))
			  .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@id, 'ui-id-') and text() = '" + lineItems.get(i).name + "']")));
			lines.get(i).findElement(itemName).sendKeys(Keys.DOWN);
			lines.get(i).findElement(itemName).sendKeys(Keys.RETURN);
			lines.get(i).findElement(itemQuantity).sendKeys(String.valueOf(lineItems.get(i).quantity));
		}
	}
	
	/**
	 * returns line items for this order
	 */
	public List<LineItem> getLineItems() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		String name;
		int quantity;
		double priceNoVat, priceVat;
		List<WebElement> lines = driver.findElements(By.cssSelector("#memo > tbody > tr"));
		new WebDriverWait(driver, 10).until(ExpectedConditions.not(ExpectedConditions.stalenessOf(lines.get(0).findElement(By.cssSelector("td:nth-child(1)")))));
		for (WebElement line : lines) {
			new WebDriverWait(driver, 10).until(ExpectedConditions.not(ExpectedConditions.stalenessOf(line)));
//			System.out.println(line.findElement(itemName).getText()+" "+line.findElement(itemQuantity).getText()+" "+line.findElement(itemPriceNoVat).getText()+" "+line.findElement(itemPriceVat).getText());
			name = line.findElement(By.cssSelector("td:nth-child(1)")).getText();
			quantity = Integer.parseInt(line.findElement(By.cssSelector("td:nth-child(3)")).getText());
			priceNoVat = Double.parseDouble(line.findElement(By.cssSelector("td:nth-child(5)")).getText());
			priceVat = Double.parseDouble(line.findElement(By.cssSelector("td:nth-child(6)")).getText());
			lineItems.add(new LineItem(name, quantity, priceNoVat, priceVat));
		}
		
		return lineItems;
	}
	
	/**
	 * update customer contact info for the order
	 * @param info
	 */
	public void changeCustomerInfo(CustomerInfo info, boolean customer) {
		driver.findElement(companyField).clear();
		driver.findElement(streetAddressField).clear();
		driver.findElement(zipField).clear();
		driver.findElement(cityField).clear();
		driver.findElement(stateField).clear();
		driver.findElement(firstNameField).clear();
		driver.findElement(lastNameField).clear();
		driver.findElement(phoneField).clear();
		driver.findElement(billingCodeField).clear();
		
		driver.findElement(companyField).sendKeys(info.company);
		driver.findElement(streetAddressField).sendKeys(info.street);
		driver.findElement(zipField).sendKeys(info.zip);
		driver.findElement(cityField).sendKeys(info.city);
		driver.findElement(stateField).sendKeys(info.state);
		new Select(driver.findElement(countryDropdown)).selectByVisibleText(info.country);
		driver.findElement(firstNameField).sendKeys(info.firstname);
		driver.findElement(lastNameField).sendKeys(info.lastname);
		driver.findElement(phoneField).sendKeys(info.phone);
		driver.findElement(billingCodeField).sendKeys(info.billingCode);
		
		if(customer) {
			driver.findElement(emailField).clear();
			driver.findElement(emailField).sendKeys(info.email);
		}
		
		clickSaveBtn();
	}
	
	/**
	 * set new number of delegates
	 * @param num
	 */
	public void changeDelegates(int num) {
		driver.findElement(delegatesField).clear();
		driver.findElement(delegatesField).sendKeys(String.valueOf(num));
	}
	
	/**
	 * clicks save button
	 */
	public void clickSaveBtn() {
		driver.findElement(saveBtn).click();
		(new WebDriverWait(driver, 20))
		  .until(ExpectedConditions.not(ExpectedConditions.attributeToBe(saveBtn, "disabled", "true")));
	}
	
	/**
	 * Get new number of delegates shown in message log
	 * @return num of delegates
	 */
	public int getLogDelegates() {
		waitFor(5000);
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(delegatesLog));
		return Integer.parseInt(driver.findElement(delegatesLog).getText());
	}
	
	/**
	 * get updated customer info shown in message log
	 * @return customer info in log
	 */
	public CustomerInfo getLogCustomerInfo(boolean customer) {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(countryLog));
		String country = driver.findElement(countryLog).getText().equals("US")?"United States":driver.findElement(countryLog).getText();
		CustomerInfo logInfo = new CustomerInfo(driver.findElement(companyLog).getText(),
				driver.findElement(streetAddressLog).getText(),
				driver.findElement(zipLog).getText(),
				driver.findElement(cityLog).getText(),
				driver.findElement(stateLog).getText(),
				country,
				driver.findElement(firstNameLog).getText(),
				driver.findElement(lastNameLog).getText(),
				driver.findElement(phoneLog).getText(),
				customer?driver.findElement(emailLog).getText():"",
				driver.findElement(billingCodeLog).getText());
		return logInfo;
	}
	
	/**
	 * get updated customer info shown in message log
	 * @return customer info in log
	 */
	public MeetingDetails getLogMeetingDetails() {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#messages > div > div.message__info")));
		MeetingDetails meetingDetailsLog = new MeetingDetails(driver.findElement(startDateLog).getText(),
				driver.findElement(startTimeLog).getText(),
				driver.findElement(endDateLog).getText(),
				driver.findElement(endTimeLog).getText(),
				driver.findElement(delegatesLog).getText(),
				driver.findElement(roomLog).getText(),
				driver.findElement(layoutLog).getText());
		return meetingDetailsLog;
	}
	
	/**
	 * return current order status(supplier or customer should confirm or order confirmed)
	 * @return string order status
	 */
	public String getOrderStatus() {
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(orderStatus));
		return driver.findElement(orderStatus).getText();
	}
	
	/**
	 * change meeting details on order page
	 */
	public MeetingDetails changeMeetingDetails() {
		String startDate, startTime = "11:00", endDate, endTime = "13:00", delegates = "7", room, layout;
		MeetingDetails md;
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("meeting-id")));
//		//start date:
//		System.out.println(driver.findElement(startDateField).isEnabled());
		driver.findElement(startDateField).click();
		new WebDriverWait(driver, 5).until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div")));
		driver.findElement(By.cssSelector("#ui-datepicker-div > table > tbody > tr:nth-child(2) > td:nth-child(2) > a")).click(); //select monday on 2nd week of the month
		driver.findElement(startTimeField).clear();
		driver.findElement(startTimeField).sendKeys(startTime);
		
		//end date:
		driver.findElement(endDateField).click();
		new WebDriverWait(driver, 10).until(ExpectedConditions.visibilityOfElementLocated(By.id("ui-datepicker-div")));
		driver.findElement(By.cssSelector("#ui-datepicker-div > table > tbody > tr:nth-child(2) > td:nth-child(2) > a")).click(); //select monday on 2nd week of the month
		driver.findElement(endTimeField).clear();
		driver.findElement(endTimeField).sendKeys(endTime);
		
		changeDelegates(Integer.parseInt(delegates));
		new Select(driver.findElement(roomSelect)).selectByIndex(3);
		new Select(driver.findElement(layoutSelect)).selectByIndex(3);
		
		startDate = driver.findElement(startDateField).getAttribute("value");
		endDate = driver.findElement(endDateField).getAttribute("value");
		room = driver.findElement(roomSelect).findElement(By.xpath("..")).getText();
		room = room.substring(0, room.indexOf("\n"));
		layout = driver.findElement(layoutSelect).findElement(By.xpath("..")).getText();
		layout = layout.substring(0, layout.indexOf("\n"));

		md = new MeetingDetails(startDate, startTime, endDate, endTime, delegates, room, layout);
		clickSaveBtn();
		
		return md;
	}
	/**
	 * method for sending message
	 * 
	 * @param String msg Message to send
	 */
	public void sendMessage(String msg) {
		driver.findElement(messageField).sendKeys(msg);
		driver.findElement(sendMsgBtn).click();
	}
	
	/**
	 * Method to get set of messages on order page
	 * 
	 * @return HashSet<String>
	 */
	public HashSet<String> getMessages() {
		List<WebElement> els;
		HashSet<String> txts = new HashSet<String>();
		els = driver.findElement(messagesLog).findElements(messageLogText);
		for (WebElement el : els) {
			txts.add(el.getText());
		}
		return txts;
	}
	
	/**
	 * Method for attaching a file on order page
	 */
	public void attachFiles() {
		String path = System.getProperty("user.dir");
		
		driver.findElement(messageField).sendKeys("here");
		
		driver.findElement(attachFileInput).sendKeys(path+"/attachmentTest.doc");
		new WebDriverWait(driver, 10).until(ExpectedConditions.numberOfElementsToBe(unsentFilesList, 1));
		waitFor(2000);
		driver.findElement(attachFileInput).sendKeys(path+"/attachmentTest.pdf");
		new WebDriverWait(driver, 10).until(ExpectedConditions.numberOfElementsToBe(unsentFilesList, 2));
		waitFor(2000);
		driver.findElement(attachFileInput).sendKeys(path+"/attachmentTest.xlsx");
		new WebDriverWait(driver, 10).until(ExpectedConditions.numberOfElementsToBe(unsentFilesList, 3));
		waitFor(2000);
		driver.findElement(sendMsgBtn).click();
		(new WebDriverWait(driver, 30))
		  .until(ExpectedConditions.not(ExpectedConditions.attributeToBe(sendMsgBtn, "disabled", "disabled")));
		waitFor(3000);
	}
	
	/**
	 * Get number of attachments sent
	 */
	public int getSentFilesNum() {
		return driver.findElements(sentFilesList).size();
	}
	
	/**
	 * Method to download .doc attachment and read its contents
	 * 
	 * @return String first paragraph of the file
	 */
	public String readDocFile() {
		String content = "", fileName;
		WebElement fileLink = driver.findElement(By.partialLinkText(".doc"));
		fileName = fileLink.getText();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileLink);
		fileLink.click();
		waitFor(500);
		try {
			File file = new File("/tmp/"+fileName);
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			HWPFDocument doc = new HWPFDocument(fis);
			WordExtractor we = new WordExtractor(doc);
			content = we.getParagraphText()[0];
			
			fis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	/**
	 * Method to download PDF attachment and read its contents
	 * 
	 * @return String first paragraph of the file
	 */
	public String readPdfFile() {
		String content = "", fileName;
		WebElement fileLink = driver.findElement(By.partialLinkText(".pdf"));
		fileName = fileLink.getText();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileLink);
		fileLink.click();
		waitFor(500);
		try {
			PDFTextStripper pdfStripper = new PDFTextStripper();
			PDDocument doc = PDDocument.load(new File("/tmp/"+fileName));
			content = pdfStripper.getText(doc);
			doc.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	
	public String readXlsFile() {
		String content = "", fileName;
		WebElement fileLink = driver.findElement(By.partialLinkText(".xlsx"));
		fileName = fileLink.getText();
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fileLink);
		fileLink.click();
		waitFor(500);
		try {
			File f = new File("/tmp/"+fileName);
			XSSFWorkbook wb = new XSSFWorkbook(OPCPackage.open(f));
			XSSFSheet sheet = wb.getSheetAt(0);
			XSSFRow row;
			XSSFCell cell;

			int rows; // No of rows
			rows = sheet.getPhysicalNumberOfRows();
			int cols = 0; // No of columns
			int tmp = 0;
			for(int i = 0; i < 10 || i < rows; i++) {
				row = sheet.getRow(i);
				if(row != null) {
					tmp = sheet.getRow(i).getPhysicalNumberOfCells();
					if(tmp > cols) cols = tmp;
				}
			}

			for(int r = 0; r < rows; r++) {
				row = sheet.getRow(r);
				if(row != null) {
					for(int c = 0; c < cols; c++) {
						cell = row.getCell((short)c);
						if(cell != null) {
							// Your code here
							content+=cell.getStringCellValue();
						}
						content+=" ";
					}
					content+="\n";
				}
			}
		} catch(Exception ioe) {
			ioe.printStackTrace();
		}
		return content;
	}
	
	
	
}
