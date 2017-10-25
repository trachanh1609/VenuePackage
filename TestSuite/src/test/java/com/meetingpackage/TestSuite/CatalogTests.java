package com.meetingpackage.TestSuite;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import com.meetingpackage.Pages.CatalogPage;
import com.meetingpackage.Pages.CheckoutPage;
import com.meetingpackage.Pages.HomePage;
import com.meetingpackage.Pages.OrderPage;
import com.meetingpackage.Pages.OrderPage.LineItem;
import com.meetingpackage.Pages.VenuePage;
import com.meetingpackage.Pages.VenueUtils.MeetingLength;
import com.meetingpackage.Pages.VenueUtils.Rooms;
import com.meetingpackage.Pages.VenueUtils.SeatingOrder;

import init.InitEnvironment;

public class CatalogTests {
	private String baseUrl;
	private WebDriver driver;
	private HomePage homepage;
	private CatalogPage catalog;
	private OrderPage op;
	private CheckoutPage cp;
	private VenuePage vp;
	private File scrFile;
	
	@Rule public TestName name = new TestName();
	
	/**
	 * Automatically fires before each test
	 * Opens browser and goes to meetingpackage.com
	 * 
	 * @param none
	 * @return none
	 */
	@Before
	public void openBrowser() {
		baseUrl=InitEnvironment.getUrl();
		
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1920,1080));
		driver.get(baseUrl);
		homepage = new HomePage(driver);
	}

	/**
	 * Automatically fires after each test case
	 * Closes all browser windows
	 * 
	 * @param none
	 * @return none
	 */
	@After
	public void closeBrowser() {
		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("target/site/screenshots/" + name.getMethodName() + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.quit();
	}
	
	@Test
	public void addItemsTest() {
		catalog = homepage.goToLoginPage().loginAsSupplier().goToCatalog();
		if(!catalog.checkItems(0)) {
			catalog.deleteItems();
		}
		catalog.addItems();
		Assert.assertTrue("Some line items weren't added to catalog", catalog.checkItems(4));
		catalog.deleteItems();
		Assert.assertTrue("Some items weren't deleted from catalog", catalog.checkItems(0));
	}
	
	@Test
	public void onlineItemsBookingTest() {
		HashSet<String> itemsOP = new HashSet<String>(), itemsCP;
		List<LineItem> lineItems;
		boolean itemNames = true;
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").fillinEnquiry(false, 10, LocalDate.now().plusDays(55), Rooms.TESTROOM4, MeetingLength.ThreeH, SeatingOrder.CABARET);
		itemsCP = cp.addAddons();
		op = cp.completeBooking().goToOrderManagement();
		lineItems = op.getLineItems();
		lineItems.remove(0);
		Assert.assertTrue("Number of items chosen on Checkout("+itemsCP.size()+") and shown on Order page("+op.getLineItems().size()+") does not match", op.getLineItems().size()-1==itemsCP.size());
		
		for (LineItem item : lineItems) {
			if(!itemsCP.toString().contains(item.getName())) {
				itemNames=false;
				System.out.println(item.getName());
			}
		}
		Assert.assertTrue("Item names on Checkout and in OM do not match", itemNames);
		op.cancelBooking();
	}
	
	//@Test
	public void offlineItemsBookingTest() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		List<LineItem> lineItemsShown;
		String bookingId = "";
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").selectHalfDayPackage().fillinEnquiry(false, 10, LocalDate.now().plusDays(50), Rooms.TESTROOM4, MeetingLength.ThreeH, SeatingOrder.CABARET);
		bookingId = cp.getBookingId();
		op = cp.completeBooking().goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		lineItems.add(new LineItem("breakfast", 5, 8.77, 10));
		lineItems.add(new LineItem("Beer", 5, 8, 10));
		lineItems.add(new LineItem("antijector", 2, 34.78, 40));
		lineItems.add(new LineItem("helicopter", 3, 1000, 1250));
		op.addCatalogItems(lineItems);
		lineItemsShown = op.getLineItems();
		lineItemsShown.remove(0);
		Assert.assertTrue("Line items set and saved are not the same\nExpected: "+lineItems.toString()+".\n Shown: "+lineItemsShown.toString(), lineItems.toString().equals(lineItemsShown.toString()));

		op.cancelBooking();
	}
	
	//@Test
	public void csvUploadTest() {
		catalog = homepage.goToLoginPage().loginAsSupplier().goToCatalog();
		catalog.selectTestVenue();
		catalog.csvCleanUp();
		catalog.uploadCsv();
		catalog.clickSave();
		catalog.waitFor(3000);
		Assert.assertTrue("Couldn't save items", driver.findElement(By.cssSelector("span.jconfirm-title")).getText().contains("Saved"));
		Assert.assertTrue("Some items weren't uploaded", catalog.checkCsvUpload());
	}
	
	@Test
	public void searchItemTest() {
		catalog = homepage.goToLoginPage().loginAsSupplier().goToCatalog();
		catalog.selectTestVenue();
		catalog.searchFor("searchTestItem");
		Assert.assertTrue(catalog.getItemNames().get(0), catalog.getItemNames().size()==1&&catalog.getItemNames().get(0).equals("searchTestItem"));
	}
	
	@Test
	public void onlineAvailableItemTests() {
		String bookingId;
		
		bookingId = onlineItemNotAvailableAfterBooking();	//Test No1
		System.out.println(bookingId);
		
		onlineItemDifferentDateTest();	//Test No2
		
		driver.get("https://meetingpackage.com/dashboard/inbox/"+bookingId);
		new OrderPage(driver).cancelBooking();
		
		onlineItemAfterCancelTest();	//Test No3
	}
	
	private String onlineItemNotAvailableAfterBooking() {
		String bookingId;
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").fillinEnquiry(false, 10, LocalDate.now().plusDays(5), Rooms.TESTROOM4, MeetingLength.ThreeH, SeatingOrder.CABARET);
		cp.addLimitedQuantityAddon();
		bookingId = cp.getBookingId();
		cp.completeBooking();
		driver.get("https://meetingpackage.com/venue/test-automation-venue");
		vp = new VenuePage(driver);
		cp = vp.fillinEnquiry(false, 10, LocalDate.now().plusDays(5), Rooms.TESTROOM5, MeetingLength.ThreeH, SeatingOrder.CABARET);
		Assert.assertFalse("Booked addon (of quantity 1) available for booking for same date", cp.hasAddon("OnlyOne"));
		return bookingId;
	}
	
	private void onlineItemDifferentDateTest() {
		driver.get("https://meetingpackage.com/venue/test-automation-venue");
		vp = new VenuePage(driver);
		cp = vp.fillinEnquiry(false, 10, LocalDate.now().plusDays(6), Rooms.TESTROOM5, MeetingLength.ThreeH, SeatingOrder.CABARET);
		Assert.assertTrue("Booked addon (of quantity 1) not available for booking for different date", cp.hasAddon("OnlyOne"));
	}
	
	private void onlineItemAfterCancelTest() {
		driver.get("https://meetingpackage.com/venue/test-automation-venue");
		vp = new VenuePage(driver);
		cp = vp.fillinEnquiry(false, 10, LocalDate.now().plusDays(5), Rooms.TESTROOM5, MeetingLength.ThreeH, SeatingOrder.CABARET);
		Assert.assertTrue("Addon (of quantity 1) not available after cancellation of previous booking", cp.hasAddon("OnlyOne"));
	}
}
