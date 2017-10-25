/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.TestSuite;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runners.model.FrameworkMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.meetingpackage.Pages.CheckoutPage;
import com.meetingpackage.Pages.LoginPage;
import com.meetingpackage.Pages.OrderPage;
import com.meetingpackage.Pages.PackagePage;
import com.meetingpackage.Pages.VenuePage;
import com.meetingpackage.Pages.VenueUtils.MeetingLength;
import com.meetingpackage.Pages.VenueUtils.Rooms;
import com.meetingpackage.Pages.VenueUtils.SeatingOrder;

import org.openqa.selenium.TakesScreenshot;

import init.InitEnvironment;


public class VenueTests {
	private static String bookingNo="";
	private WebDriver driver;
	private VenuePage vp;
	private LoginPage lp;
	private CheckoutPage cp;
	private PackagePage pp;
	private OrderPage op;
	private String baseUrl;
	private File scrFile;
	
	@Rule public TestName name = new TestName();
	
	/**
	 * Automatically fires before each test case
	 * Opens browser
	 */
	@Before
	public void openBrowser(){
		baseUrl=InitEnvironment.getUrl();
		
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1920,1080));
	}
	
	/**
	 * Automatically fires after each test case
	 * Closes all browser windows
	 */
	@After
	public void closeBrowser(){
		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("target/site/screenshots/" + name.getMethodName() + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		driver.quit();
	}
	
	/**
	 * Tests if enquiry-venue exists
	 */
	@Test
	public void enquiryVenueExistsTest() {
		System.out.println("Going to 'enquiry venue don't delete' page");
		driver.get(baseUrl+"venue/enquiry-venue-dont-delete");
		vp = new VenuePage(driver);
		Assert.assertTrue("Venue doesn't exist", vp.getVenueName().equals("Enquiry Venue (Don't Delete)"));
		System.out.println("OK. Venue exists");
	}
	
	/**
	 * Tests for instant booking functionality
	 * 1. Tests if instant booking can be made
	 * 2. Tests if booking at the same time as previous(1) fails
	 * 3. Tests if booking can be deleted by supplier
	 */
	@Test
	public void instantBookingTests(){
		//Variables for test case. change if needed
		final int numOfDelegates = 6,
				daysUntilBooking = 107;
		LocalDate date = LocalDate.now().plusDays(daysUntilBooking);
		final Rooms room = Rooms.TESTROOM3;
		final MeetingLength meetingLength = MeetingLength.FourH;
		final SeatingOrder seatingOrder = SeatingOrder.COCKTAIL;
		
		instantBookTestForPass(numOfDelegates, date, room, meetingLength, seatingOrder);
		instantBookTestForFail(numOfDelegates, date, room, meetingLength, seatingOrder);
		cancelBookingTest(bookingNo);
	}
	
	/**
	 * Tests if instant booking can be made(part of the instantBookingTests)
	 * 
	 * @param numOfDelegates
	 * @param date
	 * @param room
	 * @param meetingLength
	 * @param seatingOrder
	 */
	private void instantBookTestForPass(int numOfDelegates, LocalDate date, Rooms room, MeetingLength meetingLength, SeatingOrder seatingOrder){
		String formattedDate = date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));

		driver.get(baseUrl+"venue/test-automation-venue");
		//hp = new HomePage(driver);
		//System.out.println("Instant booking test 1:\nTrying to search for MP test and go to venue page");
		//vp = hp.testVenueSearch();
		vp = new VenuePage(driver);
		Assert.assertTrue("Not the test automation venue page. Venue name says: "+vp.getVenueName(), vp.getVenueName().contains("automation"));
		System.out.println("OK");
		
		System.out.println("Filling in enquiry and trying to go to checkout page");
		cp=vp.fillinEnquiry(true, numOfDelegates, date, room, meetingLength, seatingOrder);
		System.out.println("OK");
		bookingNo = cp.getBookingId();
//		bookingNo = driver.getCurrentUrl();
//		bookingNo = bookingNo.substring(bookingNo.indexOf("checkout/")+9);
		System.out.println("Verifying info checkout page");
		
		assertInfo(room.toString(), meetingLength, seatingOrder, numOfDelegates, formattedDate, vp.getPrice());
//		Assert.assertTrue("Checkout page. Venue name is not test automation",cp.getVenueName().equals("test automation venue"));
//		Assert.assertTrue("Checkout page. Room expected: "+room+". Shown: "+cp.getRoom(),cp.getRoom().replaceAll("\\s+","").equalsIgnoreCase(room.toString()));
//		Assert.assertTrue("Checkout page. Number of delegates expected: "+ numOfDelegates+". Shown: "+cp.getDelegates(),cp.getDelegates()==numOfDelegates); 
//		Assert.assertTrue("Checkout page. Booking time expected: "+meetingLength.toString()+"ours. Time shown: "+cp.getTime()+" hours",cp.getTime()==(meetingLength.ordinal()+2));
//		Assert.assertTrue("Checkout page. Date expected: " + formattedDate + ". Date shown: " +cp.getDate(),cp.getDate().contains(formattedDate));
//		Assert.assertTrue("Checkout page. Seating order expected: " + seatingOrder + ". Shown: " +cp.getSeatingOrder(), cp.getSeatingOrder().equalsIgnoreCase(seatingOrder.toString()));
		System.out.println("OK");

		System.out.println("Filling in contact details and trying to complete booking");
		cp.fillInInfo();
		Assert.assertTrue("Thank you page not loaded(booking failed)" ,driver.getTitle().contains("Checkout complete"));
		System.out.println("OK");
	}
	
	/**
	 * Tests if instant booking for the same time as set by instantBookTestForPass fails (should not be used separately)
	 * 
	 * @param numOfDelegates
	 * @param date
	 * @param room
	 * @param meetingLength
	 * @param seatingOrder
	 */
	private void instantBookTestForFail(int numOfDelegates, LocalDate date, Rooms room, MeetingLength meetingLength, SeatingOrder seatingOrder){
		
		driver.get(baseUrl+"venue/test-automation-venue");
		vp = new VenuePage(driver);
		
		System.out.println("Instant booking test 2:\nFilling in enquiry and trying to go to checkout page");
		vp.setDelegatesNum(numOfDelegates);
		vp.setDate(date);
		vp.setRoom(room, true);
		vp.setMeetingLength(meetingLength);
		vp.setSeatingOrder(seatingOrder);
		vp.waitFor(100);
//		vp.clickSubmit();
		if(!driver.findElement(vp.submitBtnLocator).getCssValue("pointer-events").equals("none"))
			driver.findElement(vp.submitBtnLocator).click();
		//System.out.println("OK");
		
//		System.out.println("Filling in contact details and trying to complete booking");
//		cp.fillInInfo();
		Assert.assertFalse("Second booking(with same info) went to checkout page", driver.getCurrentUrl().contains("checkout"));
	}
	
	/**
	 * Tests if booking made by instantBookTestForPass can be cancelled (should not be used separately)
	 * 
	 * @param bookingNo ID number of the booking to cancel
	 */
	public void cancelBookingTest(String bookingNo){
		driver.get(baseUrl+"user/login");
		lp=new LoginPage(driver);
		lp.loginAs("test.villa@meetingpackage.com", "meetingpackage");
		driver.get(baseUrl + "dashboard/inbox/"+bookingNo);
		op = new OrderPage(driver);
		op.cancelBooking();
//		WebDriverWait wait = new WebDriverWait(driver, 10);
//		wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#main > div > div.width-100.flex.justify--end.hideifcanceled > div > div:nth-child(1) > button")));
//		driver.findElement(By.cssSelector("#main > div > div.width-100.flex.justify--end.hideifcanceled > div > div:nth-child(1) > button")).click();
////		(new WebDriverWait(driver, 10))
////		  .until(ExpectedConditions.elementToBeClickable(By.id("submit-cancel")));
//		driver.findElement(By.id("submit-cancel")).click();
		Assert.assertTrue("Booking cancelation fail", driver.findElements(By.cssSelector("#main > div > div:nth-child(3) > ol > li > span")).size()!=0);
	}
	
	/**
	 * Test for full-day package booking
	 */
	@Test
	public void fullDayPackageTest(){
		final int numOfDelegates = 4,
				daysUntilBooking = 107;
		LocalDate date = LocalDate.now().plusDays(daysUntilBooking);
		final Rooms room = Rooms.TESTROOM2;
		final MeetingLength meetingLength = MeetingLength.NineH;
		final SeatingOrder seatingOrder = SeatingOrder.THEATER;
		String formattedDate = date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
		driver.get(baseUrl+"venue/test-automation-venue");
		vp = new VenuePage(driver);
		pp = vp.selectFullDayPackage();
		double price=0;
		price = pp.getPrice();//Integer.parseInt(pp.getPrice().replace(" $", ""));
		price *= numOfDelegates;
		cp=pp.fillinEnquiry(true, numOfDelegates, date, room, meetingLength, seatingOrder);
		assertInfo(room.toString(), meetingLength, seatingOrder, numOfDelegates, formattedDate, price);
		bookingNo = driver.getCurrentUrl();
		bookingNo = bookingNo.substring(bookingNo.indexOf("checkout/")+9);
		cp.fillInInfo();
		Assert.assertTrue("Thank you page not loaded(booking failed)" ,driver.getTitle().contains("Checkout complete"));
		cancelBookingTest(bookingNo);
//		vp.waitFor(5000);
	}
	
	/**
	 * Test for half-day package booking
	 */
	@Test
	public void halfDayPackageTest(){
		final int numOfDelegates = 4,
				daysUntilBooking = 107;
		LocalDate date = LocalDate.now().plusDays(daysUntilBooking);
		final Rooms room = Rooms.TESTROOM4;
		final MeetingLength meetingLength = MeetingLength.FiveH;
		final SeatingOrder seatingOrder = SeatingOrder.CABARET;
		String formattedDate = date.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));
		driver.get(baseUrl+"venue/test-automation-venue");
		vp = new VenuePage(driver);
		pp = vp.selectHalfDayPackage();
		double price=0;
		price = pp.getPrice();
		price *= numOfDelegates;
		cp=pp.fillinEnquiry(false, numOfDelegates, date, room, meetingLength, seatingOrder);
		assertInfo(room.toString(), meetingLength, seatingOrder, numOfDelegates, formattedDate, price);
		bookingNo = driver.getCurrentUrl();
		bookingNo = bookingNo.substring(bookingNo.indexOf("checkout/")+9);
		cp.fillInInfo();
		Assert.assertTrue("Thank you page not loaded(booking failed)" ,driver.getTitle().contains("Checkout complete"));
		cancelBookingTest(bookingNo);
	}
	
	
	/**
	 * Utility method to assert info on checkout page
	 * 
	 * @param room
	 * @param meetingLength
	 * @param seatingOrder
	 * @param numOfDelegates
	 * @param formattedDate
	 * @param price
	 */
	private void assertInfo(String room, MeetingLength meetingLength, SeatingOrder seatingOrder, int numOfDelegates, String formattedDate, double price){
		Assert.assertTrue("Checkout page. Venue name is not test automation",cp.getVenueName().equals("test automation venue"));
		Assert.assertTrue("Checkout page. Room expected: "+room+". Shown: "+cp.getRoom(),cp.getRoom().replaceAll("\\s+","").equalsIgnoreCase(room));
		Assert.assertTrue("Checkout page. Number of delegates expected: "+ numOfDelegates+". Shown: "+cp.getDelegates(),cp.getDelegates()==numOfDelegates); 
		Assert.assertTrue("Checkout page. Booking time expected: "+meetingLength.toString()+" hours. Time shown: "+cp.getTime()+" hours",cp.getTime()==(meetingLength.ordinal()+2));
		Assert.assertTrue("Checkout page. Date expected: " + formattedDate + ". Date shown: " +cp.getDate(),cp.getDate().contains(formattedDate));
		Assert.assertTrue("Checkout page. Seating order expected: " + seatingOrder + ". Shown: " +cp.getSeatingOrder(), cp.getSeatingOrder().equalsIgnoreCase(seatingOrder.toString()));
		Assert.assertTrue("Checkout page. Total price expected: "+price+". Price shown: "+cp.getPrice(), cp.getPrice()==price);

	}
}
