package com.meetingpackage.TestSuite;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.base.Strings;
import com.meetingpackage.Pages.*;
import org.apache.commons.io.FileUtils;
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
import org.junit.*;
import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.meetingpackage.Pages.OrderPage.CustomerInfo;
import com.meetingpackage.Pages.OrderPage.LineItem;
import com.meetingpackage.Pages.OrderPage.MeetingDetails;
import com.meetingpackage.Pages.VenueUtils.MeetingLength;
import com.meetingpackage.Pages.VenueUtils.Rooms;
import com.meetingpackage.Pages.VenueUtils.SeatingOrder;

import init.InitEnvironment;

public class OrderManagementTests {
	private String baseUrl;
	private WebDriver driver;
	private HomePage homepage;
	private CheckoutPage cp;
	private OrderPage op;
	private File scrFile;
	private String bookingId ="";

	@Rule
	public TestWatcher watchman= new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			if(!Strings.isNullOrEmpty(bookingId)) {
				System.out.println(bookingId);
				try {
					driver.get(baseUrl);
					homepage=new HomePage(driver);
					if(!homepage.userLoggedIn()) {
						homepage.goToLoginPage().loginAsCustomer();
					}
					driver.get(baseUrl+"dashboard/inbox/"+bookingId);
					new OrderPage(driver).cancelBooking();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		@Override
		protected void skipped(AssumptionViolatedException e,
							   Description description) {
			if(!Strings.isNullOrEmpty(bookingId)) {
				System.out.println(bookingId);
				try {
					driver.get(baseUrl);
					homepage=new HomePage(driver);
					if(!homepage.userLoggedIn()) {
						homepage.goToLoginPage().loginAsCustomer();
					}
					driver.get(baseUrl+"dashboard/inbox/"+bookingId);
					new OrderPage(driver).cancelBooking();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}

		@Override
		protected void finished(Description description) {
			scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			try {
				FileUtils.copyFile(scrFile, new File("target/site/screenshots/" + name.getMethodName() + ".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			bookingId ="";
			driver.quit();
		}
	};
	
	@Rule public TestName name = new TestName();
	
	 /** Automatically fires before each test
	 * Opens browser and goes to meetingpackage.com
	 */
	@Before
	public void openBrowser() {
		baseUrl=InitEnvironment.getUrl();
		
		//change default download folder
		ChromeOptions co = new ChromeOptions();
		HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
		chromePrefs.put("download.default_directory", "/tmp");
		chromePrefs.put("plugins.always_open_pdf_externally", true);
		co.setExperimentalOption("prefs", chromePrefs);
		DesiredCapabilities cap = DesiredCapabilities.chrome();
		cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
		cap.setCapability(ChromeOptions.CAPABILITY, co);
		//
		
		driver = new ChromeDriver(cap);
		driver.manage().window().setSize(new Dimension(1280,720));
		driver.get(baseUrl);
		homepage = new HomePage(driver);
	}
	
//	/**
//	 * Automatically fires after each test case
//	 * Closes all browser windows
//	 */
//	@After
//	public void closeBrowser(){
//		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//		try {
//			FileUtils.copyFile(scrFile, new File("target/site/screenshots/" + name.getMethodName() + ".png"));
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		driver.quit();
//	}
	
	/**
	 * Test for enquiry(room) confirmation process and cancellation by supplier
	 */
	@Test
	public void supplierCancelsConfirmedOrderTest() {
		double totalEnqLineOP, totalEnqOP; //variables for total price for enquiry on order page for line item and resulted price
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
		.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.TwoH, SeatingOrder.BOARDROOM); //make a booking as customer
		bookingId = cp.getBookingId();
		homepage = cp.completeBooking().logOut();
		op = homepage.goToLoginPage().loginAs("test.villa@meetingpackage.com", "meetingpackage").goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Supplier should confirm order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Supplier should confirm"));
		op.confirmBooking();
//		op.waitFor(3000);
//		List<LineItem> lineItems = new ArrayList<LineItem>();
//		lineItems.add(op.new LineItem("Tea", 50, 0.8, 1));
//		lineItems.add(op.new LineItem("Snack", 30, 2.3, 2.5));
//		lineItems.add(op.new LineItem("Lunch", 40, 3.5, 4));
		totalEnqLineOP = Double.parseDouble(driver.findElements(op.lineItemsLocator).get(0).findElement(op.lineItemTotal).getText());
		totalEnqOP = Double.parseDouble(driver.findElement(op.resEnquiryTotal).getText());
		Assert.assertTrue("Enquiry price in line item field and resulted field are not the same", totalEnqLineOP==totalEnqOP);
		op = op.logOut().goToLoginPage().loginAsCustomer().goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Customer should confirm' order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Customer should confirm"));
		op.confirmBooking();
		Assert.assertTrue("'Confirmed order' order status expected. Shown: '"+op.getOrderStatus()+"' (Logged in as customer)", op.getOrderStatus().contains("Confirmed order"));
		op = op.logOut().goToLoginPage().loginAs("test.villa@meetingpackage.com", "meetingpackage").goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Confirmed order' order status expected. Shown: '"+op.getOrderStatus()+"' (Logged in as supplier)", op.getOrderStatus().contains("Confirmed order"));
//		op.addItems(lineItems);
		op.setCancelReason(true);
		op.cancelBooking();
		Assert.assertTrue("Booking cancelation fail", driver.findElements(op.orderCancelledLbl).size()!=0);
//		op.waitFor(5000);
	}
	
	/**
	 * Test for enquiry(package) confirmation process and cancellation by customer
	 */
	@Test
	public void customerCancelsConfirmedOrderTest() {
		double totalEnqLineOP, totalEnqOP; //variables for total price for enquiry on order page for line item and resulted price
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").selectFullDayPackage()
		.fillinEnquiry(false, 10, date, Rooms.TESTROOM4, MeetingLength.SixH, SeatingOrder.CLASSROOM); //make a booking as customer
		bookingId = cp.getBookingId();
		homepage = cp.completeBooking().logOut();
		op = homepage.goToLoginPage().loginAs("test.villa@meetingpackage.com", "meetingpackage").goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Supplier should confirm order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Supplier should confirm"));
		op.confirmBooking();
		totalEnqLineOP = Double.parseDouble(driver.findElements(op.lineItemsLocator).get(0).findElement(op.lineItemTotal).getText());
		totalEnqOP = Double.parseDouble(driver.findElement(op.resEnquiryTotal).getText());
		Assert.assertTrue("Enquiry price in line item field and resulted field are not the same", totalEnqLineOP==totalEnqOP);
		op = op.logOut().goToLoginPage().loginAsCustomer().goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Customer should confirm' order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Customer should confirm"));
		op.confirmBooking();
		Assert.assertTrue("'Confirmed order' order status expected. Shown: '"+op.getOrderStatus()+"' (Logged in as customer)", op.getOrderStatus().contains("Confirmed order"));
		op.setCancelReason(false);
		op.cancelBooking();
		op = op.logOut().goToLoginPage().loginAs("test.villa@meetingpackage.com", "meetingpackage").goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("Booking cancelation fail", driver.findElements(op.orderCancelledLbl).size()!=0);
	}
	
	/**
	 * Test for booking cancellation by customer before supplier confirmation
	 */
	@Test
	public void customerCancelsBeforeConfirmationTest() {
		LocalDate date = LocalDate.now().plusDays(50);
		op = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.TwoH, SeatingOrder.BOARDROOM)
				.completeBooking().goToOrderManagement();
		Assert.assertTrue("'Supplier should confirm order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Supplier should confirm"));
		op.setCancelReason(true);
		op.cancelBooking();
		Assert.assertTrue("Booking cancelation fail", driver.findElements(op.orderCancelledLbl).size()!=0);
	}
	
	/**
	 * Test for booking cancellation by customer after supplier confirmation
	 */
	@Test
	public void customerCancelsAfterSupplierConfirmTest() {
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.TwoH, SeatingOrder.BOARDROOM);
		bookingId = cp.getBookingId();
		homepage = cp.completeBooking().logOut();
		op = homepage.goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Supplier should confirm order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Supplier should confirm"));
		op.confirmBooking();
		op = op.logOut().goToLoginPage().loginAsCustomer().goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Customer should confirm' order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Customer should confirm"));
		op.cancelBooking();
		Assert.assertTrue("Booking cancelation fail", driver.findElements(op.orderCancelledLbl).size()!=0);
	}
	
	/**
	 * Test for new booking(not confirmed) cancellation by supplier
	 */
	@Test
	public void supplierCancelsBeforeConfirmationTest() {
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").selectHalfDayPackage()
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.TwoH, SeatingOrder.BOARDROOM); //make a booking as customer
		bookingId = cp.getBookingId();
//		homepage = cp.completeBooking().logOut();
		op = cp.completeBooking().logOut().goToLoginPage().loginAs("test.villa@meetingpackage.com", "meetingpackage").goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("'Supplier should confirm order status expected. Shown: '"+op.getOrderStatus()+"'", op.getOrderStatus().contains("Supplier should confirm"));
		op.setCancelReason(false);
		op.cancelBooking();
		op.logOut().goToLoginPage().loginAsCustomer().goToInbox().gotoOrderById(bookingId);
		Assert.assertTrue("Booking cancelation fail", driver.findElements(op.orderCancelledLbl).size()!=0);
	}
	
	/**
	 * Test for updating customer info on order page by customer
	 */
	@Test
	public void customerUpdateContactDetailsTest() {
		CustomerInfo info, loggedInfo;
		LocalDate date = LocalDate.now().plusDays(50);
		op = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.TwoH, SeatingOrder.BOARDROOM)
				.completeBooking().goToOrderManagement();
		info = op.new CustomerInfo("Not google", "Times square", "70200", "New York", "NY", "United States", "Jack", "Daniel", "04477766613", "jdfromny@notgmail.com", "111222333");
		op.changeCustomerInfo(info, true);
		loggedInfo = op.getLogCustomerInfo(true);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("messages")));
		Assert.assertTrue("Customer details set and logged are not the same\n Expected: "+info.toString()+". Shown: "+loggedInfo.toString(), info.toString().equals(loggedInfo.toString()));
		op.cancelBooking();
	}
	
	/**
	 * Test for updating number of delegates by customer on order page
	 */
	@Test
	public void customerChangesNumberOfDelegatesTest() {
		int delegates = 5;
		LocalDate date = LocalDate.now().plusDays(50);
		op = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").selectFullDayPackage()
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.SixH, SeatingOrder.BOARDROOM)
				.completeBooking().goToOrderManagement();
		op.changeDelegates(delegates);
		op.clickSaveBtn();
		Assert.assertTrue("Number of delegates logged not what it should be", delegates==op.getLogDelegates());
		op.cancelBooking();
	}
	
	/**
	 * Test for updating customer info on order page
	 */
	@Test
	public void supplierUpdateContactDetailsTest() {
		CustomerInfo info, loggedInfo;
		LocalDate date = LocalDate.now().plusDays(50);
		cp= homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").selectHalfDayPackage()
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.TwoH, SeatingOrder.BOARDROOM);
		bookingId = cp.getBookingId();
		homepage = cp.completeBooking().logOut();
		op = homepage.goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		info = op.new CustomerInfo("Not google", "Times square", "70200", "New York", "NY", "United States", "Jack", "Daniel", "04477766613", "", "111222333");
		op.changeCustomerInfo(info, false);
		loggedInfo = op.getLogCustomerInfo(false);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", driver.findElement(By.id("messages")));
		Assert.assertTrue("Customer details set and logged are not the same\n Expected: "+info.toString()+". Shown: "+loggedInfo.toString(), info.toString().equals(loggedInfo.toString()));
		op.cancelBooking();
	}
	
	/**
	 * Test for updating number of delegates by supplier on order page
	 */
	@Test
	public void supplierChangesNumberOfDelegatesTest() {
		int delegates = 3;
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.FiveH, SeatingOrder.BOARDROOM);
		bookingId = cp.getBookingId();
		op = cp.completeBooking().logOut().goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		op.changeDelegates(delegates);
		op.clickSaveBtn();
		Assert.assertTrue("Number of delegates logged not what it should be", delegates==op.getLogDelegates());
		op.cancelBooking();
	}
	
	/**
	 * Test for changing meeting details by supplier on order page
	 */
	//@Test
	public void supplierChangesMeetingDetailsTest() {
		MeetingDetails md, mdLog;
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue").selectHalfDayPackage()
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.FiveH, SeatingOrder.BOARDROOM);
		bookingId = cp.getBookingId();
		op = cp.completeBooking().logOut().goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		md = op.changeMeetingDetails();
		mdLog = op.getLogMeetingDetails();
		Assert.assertTrue("Meeting details set and logged are not the same\n Expected: "+md.toString()+". Shown: "+mdLog.toString(), md.toString().equals(mdLog.toString()));
		op.cancelBooking();
	}
	
	/**
	 * Test for changing meeting details by customer on order page
	 */
	@Test
	public void customerChangesMeetingDetailsTestForFail() {
		boolean haveChanged = true;
		MeetingDetails md, mdLog;
		LocalDate date = LocalDate.now().plusDays(50);
		op = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.FiveH, SeatingOrder.BOARDROOM)
				.completeBooking().goToOrderManagement();
		try {
			md = op.changeMeetingDetails();
			mdLog = op.getLogMeetingDetails();
		} catch (TimeoutException e) {
			haveChanged = false;
		}
		Assert.assertFalse("Customer was able to change meeting details in OM", driver.findElement(op.startDateField).isEnabled() || haveChanged);
		op.cancelBooking();
	}
	
	/**
	 * Test for adding line items by supplier on order page
	 */
	//@Test
	public void supplierAddsLineItemsTest() {
		List<LineItem> lineItems = new ArrayList<LineItem>();
		List<LineItem> lineItemsShown;
		LineItem updateEnq = new LineItem("Enquiry",7 , 90.6, 101);
		MeetingDetails md, mdLog;
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.FiveH, SeatingOrder.BOARDROOM);
		bookingId = cp.getBookingId();
		op = cp.completeBooking().logOut().goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		
		lineItems.add(new LineItem("Tea", 50, 0.8, 1));
		lineItems.add(new LineItem("Snack", 30, 2.3, 2.5));
		lineItems.add(new LineItem("Lunch", 40, 3.5, 4));
		op.editEnquiryLineItem(updateEnq);
		op.addItems(lineItems);
		lineItems.add(0, updateEnq);
		lineItemsShown = op.getLineItems();

		Assert.assertTrue("Line items set and saved are not the same\nExpected: "+lineItems.toString()+".\n Shown: "+lineItemsShown.toString(), assertLineItems(lineItems, lineItemsShown));
		op.cancelBooking();
	}
	
	/**
	 * Test for customer and supplier exchanging messages
	 */
	@Test
	public void sendMessagesTest() {
		HashSet<String> messages;
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.FiveH, SeatingOrder.BOARDROOM);
		bookingId = cp.getBookingId();
		op = cp.completeBooking().goToOrderManagement();
		op.sendMessage("yo");
		op.sendMessage("sup");
		op.waitFor(300);
		op = op.logOut().goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		
		messages = op.getMessages();
		Assert.assertTrue("Messages not received", messages.size()==2);
		Assert.assertTrue("Messages not same. Received: "+messages.toString(), messages.contains("yo")&&messages.contains("sup"));
		
		op.sendMessage("yay");
		op.sendMessage("wow");
		op.waitFor(300);
		op = op.logOut().goToLoginPage().loginAsCustomer().goToInbox().gotoOrderById(bookingId);
		
		messages = op.getMessages();
		Assert.assertTrue("Messages not received", messages.size()==4);
		Assert.assertTrue("Messages not same. Received: "+messages.toString(), messages.contains("yay")&&messages.contains("wow"));
		
		op.cancelBooking();
	}
	
	/**
	 * Test for file attachments in OM
	 */
	@Test
	public void fileAttachmentTest() {
		String testTxt = "Spicy jalapeno bacon ipsum dolor amet ham prosciutto bresaola jowl cillum porchetta nostrud short ribs swine qui beef ribs ipsum aliqua.",
		testSheet = "Name City Email Company Phone \n" +
				"Blake Wilmont Ut@sit.net Non Luctus Sit Ltd (02444) 9524868 \n" +
				"Solomon St. Austell id@Maurisvel.org Morbi Tristique Foundation (05236) 3473498 \n" +
				"Jorden Harrisburg et.ultrices@pellentesqueegetdictum.co.uk Nunc Commodo LLC (07052) 0621979 \n" +
				"Justina Sassocorvaro rhoncus.Proin@idanteNunc.edu Egestas Sed Pharetra Limited (088) 33346852 \n" +
				"Connor Aalbeke auctor.velit@cursus.org Vitae Industries (039435) 294058 \n" +
				"Holmes Fulda Donec.nibh@tellusjusto.com Enim Diam Vel Corp. (080) 45628218 ";
		LocalDate date = LocalDate.now().plusDays(50);
		cp = homepage.goToLoginPage().loginAsCustomer().goToHomePage().venueSearch("test automation venue")
				.fillinEnquiry(false, 10, date, Rooms.TESTROOM5, MeetingLength.FiveH, SeatingOrder.BOARDROOM);
		bookingId = cp.getBookingId();
		op = cp.completeBooking().goToOrderManagement();
//		op = homepage.goToLoginPage().loginAsCustomer().goToInbox().gotoOrderById("13646");
		op.attachFiles();
		op = op.logOut().goToLoginPage().loginAsSupplier().goToInbox().gotoOrderById(bookingId);
		HashSet<String> messages;
		messages = op.getMessages();
		Assert.assertTrue("Message not received correctly. Received: "+messages.toString(), messages.contains("here"));
		Assert.assertTrue("Not all files have been sent. No. of sent files: "+op.getSentFilesNum(), op.getSentFilesNum()==3);
		Assert.assertTrue("Check for .doc (1) file contents failed. Shown: '"+op.readDocFile()+"'", op.readDocFile().contains(testTxt));
		Assert.assertTrue("Check for .pdf (2) file contents failed. Shown: '"+op.readPdfFile()+"'", op.readPdfFile().replace("\n", "").contains(testTxt));
		Assert.assertTrue("Check for .xls (3) file contents failed. Shown: '"+op.readXlsFile()+"'\n"+testSheet, op.readXlsFile().contains(testSheet));

//		new OrderPage(driver).blahblah();
	}

	
	/**
	 * Utility method to assert line items are the same
	 * 
	 * @param input
	 * @param output
	 * @return
	 */
	public boolean assertLineItems (List<LineItem> input, List<LineItem> output) {
		boolean equal = true;
		for (int i = 0; i < output.size(); i++) {
			if(!output.get(i).toString().equals(input.get(i).toString())){
				equal = false;
				break;
			}
		}
		return equal;
	}
}
