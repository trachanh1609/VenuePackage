/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.TestSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.meetingpackage.Pages.CheckoutPage;
import com.meetingpackage.Pages.HomePage;
import com.meetingpackage.Pages.PackagePage;
import com.meetingpackage.Pages.SearchPage;
import com.meetingpackage.Pages.VenuePage;
import com.meetingpackage.Pages.BasePage.Languages;
import com.meetingpackage.Pages.VenueUtils.MeetingLength;

import init.InitEnvironment;
import junit.framework.TestResult;

public class SearchTests {
	private String baseUrl;
	private WebDriver driver;
	private HomePage homepage;
	private VenuePage vp;
	private CheckoutPage cp;
	private SearchPage sp;
	private PackagePage pp;
	private double price;
	private int delegates;
	private boolean pkg;
	private File scrFile;
	
	@Rule public TestName name = new TestName();
//	@Rule public ScreenShotRule screenShootRule = new ScreenShotRule((TakesScreenshot) driver, name);
//	@Rule
//	public TestRule watcher = new TestWatcher() {
//		@Override
//	   protected void failed (Description description) {
//	      System.out.println("Starting test: " + driver.getTitle());
//	   }
//	};
	
	/**
	 * Automatically fires before each test
	 * Opens browser and goes to meetingpackage.com
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
	 */
	@After
	public void closeBrowser() {
//		TestResult tr = new TestResult();
//		this.r
//		homepage.waitFor(10000);
//		System.out.println(tr.failureCount());
//		if(tr.wasSuccessful())
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
	 * Test for venue search in Finland in finnish language (checks for correct language and currency)
	 */
	@Test
	public void venueFinlandSearchTest() {
		price = 0;
		homepage.changeLanguage(Languages.FI);
		
		//check for header on home page:
		Assert.assertTrue("Home page. Expected: Varaa kokoustiloja & -paketteja; Shown: "+driver.findElement(By.cssSelector("#search-form-wrapper > div > div.row.text-header > h1")).getText(), driver.findElement(By.cssSelector("#search-form-wrapper > div > div.row.text-header > h1")).getText().contains("Varaa kokoustiloja"));
		vp = homepage.venueSearch("SEA LIFE Helsinki");
		assertVenuePageLanguage(Languages.FI);
//		//check for enquiry form texts:
//		Assert.assertTrue("Venue page enquiry form. Expected: TARJOUSPYYNTÖ & Jatka; Shown: "+driver.findElement(vp.sidebarTitleLocator).getText()+" & "+driver.findElement(vp.submitBtnLocator).getText(), driver.findElement(vp.sidebarTitleLocator).getText().equalsIgnoreCase("TARJOUSPYYNTÖ")&&driver.findElement(vp.submitBtnLocator).getText().equalsIgnoreCase("jatka"));
//		//check for venue page facilities text:
//		Assert.assertTrue("Venue page. Expected: Palvelut; Shown: "+driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")), driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")).getText().equals("Palvelut"));
		vp.setFirstRoom();
		vp.setMeetingLength(MeetingLength.ThreeH);
//		vp.setMeeting
		vp.waitFor(500);
		price=3*vp.getPrice();  //total for 3 hour booking
		vp.clickSubmit();
		driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		cp = new CheckoutPage(driver);
		
		Assert.assertTrue("Checkout page. Total price expected: "+price+". Price shown: "+cp.getPrice(), cp.getPrice()==price);
		//check for checkout page header:
		assertCheckoutPageLanguage(Languages.FI);
//		Assert.assertTrue("Checkout page. Expected: Tietojen lähetys; Shown: "+driver.findElement(By.cssSelector("body > div.main-container.basic-page.fluid > div > section > h1")).getText(), driver.findElement(By.cssSelector("body > div.main-container.basic-page.fluid > div > section > h1")).getText().equalsIgnoreCase("Tietojen lähetys"));
//		System.out.println(driver.findElement(vp.sidebarTitleLocator).getText()+" "+driver.findElement(vp.submitBtnLocator).getText());
	}
	
	/**
	 * Test for venue search in England in english language (checks for correct language and currency)
	 */
	@Test
	public void venueEnglandSearchTest() {
		price = 0;
		homepage.changeLanguage(Languages.EN);
		
		//check for header on home page:
		Assert.assertTrue("Home page. Expected: Book meeting rooms & packages; Shown: "+driver.findElement(By.cssSelector("#search-form-wrapper > div > div.row.text-header > h1")).getText(), driver.findElement(By.cssSelector("#search-form-wrapper > div > div.row.text-header > h1")).getText().contains("Book meeting rooms"));
		vp = homepage.venueSearch("Bloomsburry Way - The Office Group");
		
		assertVenuePageLanguage(Languages.EN);
//		//check for enquiry form texts:
//		Assert.assertTrue("Venue page enquiry form. Expected: ENQUIRY & Send enquiry; Shown: "+driver.findElement(vp.sidebarTitleLocator).getText()+" & "+driver.findElement(vp.submitBtnLocator).getText(), driver.findElement(vp.sidebarTitleLocator).getText().equalsIgnoreCase("ENQUIRY")&&driver.findElement(vp.submitBtnLocator).getText().equalsIgnoreCase("Send enquiry"));
//		//check for venue page facilities text:
//		Assert.assertTrue("Venue page. Expected: Facilities; Shown: "+driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")), driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")).getText().equals("Facilities"));
		
		vp.setFirstRoom();
		vp.setMeetingLength(MeetingLength.ThreeH);
		vp.waitFor(500);
		price=3*vp.getPrice();  //total for 3 hour booking
		vp.clickSubmit();
		cp = new CheckoutPage(driver);
		
		
		
		Assert.assertTrue("Checkout page. Total price expected: "+price+". Price shown: "+cp.getPrice(), cp.getPrice()==price);
		//check for checkout page header:
		assertCheckoutPageLanguage(Languages.EN);
//		Assert.assertTrue("Checkout page. Expected: Please add your contact details and proceed; Shown: "+checkoutText, checkoutText.equalsIgnoreCase("Please add your contact details and proceed")||checkoutText.equalsIgnoreCase("checkout"));
	}
	
	/**
	 * Test for venue search in Italy in italian language (checks for correct language and currency)
	 */
	@Test
	public void venueItalySearchTest() {
		price = 0;
		String enq = "";
		homepage.changeLanguage(Languages.IT);
		
		//check for header on home page:
		Assert.assertTrue("Home page. Expected: Prenota sale riunioni e pacchetti standardizzati; Shown: "+driver.findElement(By.cssSelector("#search-form-wrapper > div > div.row.text-header > h1")).getText(), driver.findElement(By.cssSelector("#search-form-wrapper > div > div.row.text-header > h1")).getText().contains("Prenota sale riunioni"));
		vp = homepage.venueSearch("ADI Doria Grand Hotel");
		enq = driver.findElement(vp.sidebarTitleLocator).getText();
		
		//check for enquiry form texts:
		Assert.assertTrue("Venue page enquiry form. Expected: RICHIESTA & Invia richiesta; Shown: "+driver.findElement(vp.sidebarTitleLocator).getText()+" & "+driver.findElement(vp.submitBtnLocator).getText(), (enq.equalsIgnoreCase("RICHIESTA")||enq.equalsIgnoreCase("INCHIESTA"))&&driver.findElement(vp.submitBtnLocator).getText().equalsIgnoreCase("Invia richiesta"));
		//check for venue page facilities text:
		Assert.assertTrue("Venue page. Expected: Servizi; Shown: "+driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")), driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")).getText().equals("Servizi"));
		
		vp.setFirstRoom();
		vp.setMeetingLength(MeetingLength.ThreeH);
		vp.waitFor(500);
//		price=3*vp.getPrice();  //total for 3 hour booking
		vp.clickSubmit();
		cp = new CheckoutPage(driver);
		
//		Assert.assertTrue("Checkout page. Total price expected: "+price+". Price shown: "+cp.getPrice(), cp.getPrice()==price);
		assertCheckoutPageLanguage(Languages.IT);
//		Assert.assertTrue("Checkout page. Expected: Informazioni Personali; Shown: "+driver.findElement(By.cssSelector("body > div.main-container.basic-page.fluid > div > section > h1")).getText(), driver.findElement(By.cssSelector("#edit-customer-profile-billing > div > div:nth-child(1) > h3 > span")).getText().equalsIgnoreCase("Informazioni Personali"));
	}
	
	/**
	 * Test for packages search in Helsinki in finnish
	 */
	@Test
	public void searchForPackagesFinnishTest() {
		pkg = true;
		delegates = 8;
		boolean havePrices = true;
		price = 0;
		homepage.changeLanguage(Languages.FI);
		sp = homepage.locationSearch("Helsinki", 8, pkg);
		assertSearchPageInfo(Languages.FI);
		List<WebElement> searchResultPrices = sp.getSearchHitPrices(); // get package prices from 1st page of search results
		for (WebElement webElement : searchResultPrices) {
			if(webElement.getText().equals(""))
				havePrices=false;
		}
		
		Assert.assertTrue("Some venues in search results don't have prices", havePrices);
		List<WebElement> searchResults = sp.getSearchHits(); //get search results from 1st page
		price = Double.parseDouble(searchResultPrices.get(0).getText().replace("€ ", ""));
		searchResults.get(0).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabHandles = new ArrayList<String>(driver.getWindowHandles());
		tabHandles.remove(driver.getWindowHandle());
		driver.switchTo().window(tabHandles.iterator().next());
		pp = new PackagePage(driver);
		assertPackagePageInfo();
		cp = pp.clickSubmit();
	}
	
	/**
	 * Search for rooms test in Helsinki in finnish
	 */
	//@Test
	public void searchForRoomsFinnishTest() {
		pkg = false;
		delegates = 12;
		boolean haveZeroPrices = false;
		String wrongPrice = null;
		price = 0;
		homepage.changeLanguage(Languages.FI);
		Assert.assertFalse("Homepage. There are duplicates in locations search suggestions", homepage.checkSearchDuplicates("Helsinki"));
		sp = homepage.locationSearch("Helsinki", 12, pkg);
		assertSearchPageInfo(Languages.FI);
		List<WebElement> searchResultPrices = sp.getSearchHitPrices(); // get room prices from 1st page of search results
		for (WebElement webElement : searchResultPrices) {             //check for zero valued prices in results
			try {
				if(Double.parseDouble(webElement.getText().replace("€", "").trim())==0) {
					haveZeroPrices=true;
					wrongPrice = webElement.getText();
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				if(!e.getMessage().equals("empty String"))
					throw e;
			}
		}
		Assert.assertFalse("Some venues in search results show 0 value prices("+wrongPrice+")", haveZeroPrices);
		List<WebElement> searchResults = sp.getSearchHits(); //get search results from 1st page
		searchResults.get(0).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabHandles = new ArrayList<String>(driver.getWindowHandles());
		tabHandles.remove(driver.getWindowHandle());
		driver.switchTo().window(tabHandles.iterator().next());
		vp = new VenuePage(driver);
		vp.waitFor(4000);
		assertVenuePageInfo();
		assertVenuePageLanguage(Languages.FI);
		cp = vp.clickSubmit();
		assertCheckoutPageLanguage(Languages.FI);
	}
	
	/**
	 * Search for rooms test in London in english
	 */
	@Test
	public void searchForRoomsEnglishTest() {
		pkg = false;
		delegates = 12;
		boolean haveZeroPrices = false;
		String wrongPrice = null;
		price = 0;
		homepage.changeLanguage(Languages.EN);
		Assert.assertFalse("Homepage. There are duplicates in locations search suggestions", homepage.checkSearchDuplicates("London, UK"));
		sp = homepage.locationSearch("London, UK", 12, pkg);
		assertSearchPageInfo(Languages.EN);
		List<WebElement> searchResultPrices = sp.getSearchHitPrices(); // get room prices from 1st page of search results
		for (WebElement webElement : searchResultPrices) {             //check for zero valued prices in results
			try {
				if(Double.parseDouble(webElement.getText().replace("£", "").trim())==0) {
					haveZeroPrices=true;
					wrongPrice = webElement.getText();
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				if(!e.getMessage().equals("empty String"))
					throw e;
			}
		}
		Assert.assertFalse("Some venues in search results show 0 value prices("+wrongPrice+")", haveZeroPrices);
		List<WebElement> searchResults = sp.getSearchHits(); //get search results from 1st page
		searchResults.get(0).click();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.numberOfWindowsToBe(2));
		ArrayList<String> tabHandles = new ArrayList<String>(driver.getWindowHandles());
		tabHandles.remove(driver.getWindowHandle());
		driver.switchTo().window(tabHandles.iterator().next());
		vp = new VenuePage(driver);
		vp.waitFor(5000);
		assertVenuePageInfo();
		assertVenuePageLanguage(Languages.EN);

		cp = vp.clickSubmit();
		assertCheckoutPageLanguage(Languages.EN);
	}
	
	
	/**
	 * search criteria test in finnish language
	 */
	@Test
	public void searchSortOrderFinnishTest() {
		homepage.changeLanguage(Languages.FI);
		searchSortOrder("Helsinki");
	}
	
	/**
	 * search criteria test in english language
	 */
	@Test
	public void searchSortOrderEnglishTest() {
		searchSortOrder("London, UK");
	}
	
	/**
	 * tests if map marker on search page functions as supposed to
	 */
	@Test
	public void mapSearchTest() {
		boolean hasTestVenue = false;
		WebElement map;
		int height, width, percent = 49;
		sp = homepage.locationSearch("Ivalontie 4, Inari", 10, true);
		sp.waitFor(500);
		List<WebElement> venueNames = sp.getSearchHitNames();
		for (WebElement venueName : venueNames) {
			if(venueName.getText().contains("test automation venue"))
				hasTestVenue = true;
		}
		Assert.assertTrue("Venue for test automation not displayed on search page", hasTestVenue);
//		driver.findElement(By.cssSelector("img[src*='markerTransparent.png']")).click();
		(new WebDriverWait(driver, 10)).until(ExpectedConditions.visibilityOfElementLocated(By.id("map")));
		map = driver.findElement(By.id("map"));
		height = map.getSize().height;
		width = map.getSize().width;
		System.out.println(map.getSize().width+" "+map.getSize().height);
		(new Actions(driver)).moveToElement(driver.findElement(By.id("map")), width*percent/100, height*percent/100).click().perform();
//		(new Actions(driver)).moveToElement(driver.findElement(By.id("map"))).moveByOffset(-23, 0).click().perform();
		(new WebDriverWait(driver, 10))
		  .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#popup-infowindow > div.pop-up-info-content")));
		System.out.println(driver.findElement(By.cssSelector("#popup-infowindow > div.pop-up-info-content")).getText());
		Assert.assertTrue("Venue info didn't pop up when clicked on map marker",driver.findElement(By.cssSelector("#popup-infowindow > div.pop-up-info-content > a")).getText().contains(" - Inari"));

	}
	
	@Test
	public void searchFiltersTest() {
		String firstVenueName, lastVenueName;
		double firstVenuePrice;
		int venueNumExpected;
		pkg = true;
		delegates = 10;
		sp = homepage.locationSearch("Helsinki", delegates, pkg);
		assertSearchPageInfo(Languages.EN);
		firstVenueName = sp.getFirstResultName();
		lastVenueName = sp.getLastResultName();
		firstVenuePrice = +sp.getFirstResultPrice();
//		lastVenuePrice = +sp.getLastResultPrice
		sp.incPriceFilter((int)firstVenuePrice);
		Assert.assertTrue("First search result has higher price than minimum price set on filter", firstVenuePrice<sp.getFirstResultPrice());
		sp.resetPriceFilter();
		Assert.assertTrue("Search results are not same after resetting price filter back to default. Expected: "+firstVenueName+ " & " +lastVenueName+". Shown: "+sp.getFirstResultName()+" & "+sp.getLastResultName(), sp.getFirstResultName().equals(firstVenueName)&&sp.getLastResultName().equals(lastVenueName));
		venueNumExpected = sp.setEnvFilter();
		Assert.assertTrue("Number of venues on search page("+sp.getSearchHits().size()+") not what filter was suggesting("+venueNumExpected+")", sp.getSearchHits().size()==venueNumExpected);
		sp.clearFilters();
		Assert.assertTrue("Search results are not same after resetting environment filter back to default. Expected: "+firstVenueName+ ". Shown: "+sp.getFirstResultName(), sp.getFirstResultName().equals(firstVenueName));

//		Assert.assertTrue("Search results are not same after resetting environment filter back to default. Expected: "+firstVenueName+ " & " +lastVenueName+". Shown: "+sp.getFirstResultName()+" & "+sp.getLastResultName(), sp.getFirstResultName().equals(firstVenueName)&&sp.getLastResultName().equals(lastVenueName));
	}
	
	/**
	 * method to test search criteria(sorting order, map)
	 */
	private void searchSortOrder(String location) {
		sp = homepage.locationSearch(location, 10, false);
		Assert.assertTrue("'Search when I move the map' is not selected", sp.searchWhenMoveChecked());
		Assert.assertTrue("Sort by nearest is not selected by default", sp.sortNearest().getAttribute("class").contains("btn-warning"));
		Assert.assertTrue("Venues not sorted by distance", sp.distanceAscending());
		sp.sortCheapest().click();
		Assert.assertTrue("Venues not sorted by ascending price (cheapest first)", sp.priceAscending());
		sp.sortLargest().click();
		Assert.assertTrue("Venue not sorted by descending size", sp.sizeDescending());
		sp.sortSmallest().click();
		Assert.assertTrue("Venue not sorted by ascending size", sp.sizeAscending());
		
		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(scrFile, new File("target/site/screenshots/MAP-" + name.getMethodName() + ".png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		System.out.println(sp.sizeDescending());
	}
	
	/**
	 * Method to assert information on search page
	 */
	private void assertSearchPageInfo(Languages lan) {
		
		String searchFor = sp.getSelectedSearchFor(),
				header = driver.findElement(By.cssSelector("div.left-column > h1")).getText(),
				mLengthLabel = driver.findElement(By.cssSelector("div.left-column > div.filters > div.filter.meeting-length > div.filter-label")).getText(),
				dateLabel = driver.findElement(By.cssSelector("div.left-column > div.filters > div.filter.date-time > div.filter-label")).getText(),
				delegatesLabel = driver.findElement(By.cssSelector("div.left-column > div.filters > div.filter.persons > div")).getText(),
				searchForLabel = driver.findElement(By.cssSelector("div.left-column > div.filters > div.filter.search-for > div.filter-label")).getText();
		Assert.assertTrue("Search page. Date expected: "+homepage.getDateTime()+". Shown: "+sp.getSelectedDate(), sp.getSelectedDate().equals(homepage.getDateTime()));
		Assert.assertTrue("Search page. Meeting length expected: 3 h. Shown: "+sp.getSelectedMLength(), sp.getSelectedMLength().equals("3 h"));
		Assert.assertTrue("Search page. Number of delegated expected: "+delegates+". Shown: "+sp.getSelectedDelegates(), sp.getSelectedDelegates().equals(String.valueOf(delegates)));
		if(lan.toString().equals("FI")){
			if(pkg) {
				Assert.assertTrue("Search page. Search for packages not selected (or not in finnish)", searchFor.equals("Kokouspaketit"));
				Assert.assertTrue("Search page. Header not in finnish", header.contains("Kokoustilat Helsinki"));
			}
			else {
				Assert.assertTrue("Search page. Search for rooms not selected (or not in finnish)", searchFor.equals("Kokouspaikat"));
				Assert.assertTrue("Search page. Header not in finnish", header.contains("Juhlatilat Helsinki"));
			}
			Assert.assertTrue("Search page. Meeting length label not in finnish", mLengthLabel.contains("Kokouksen kesto"));
			Assert.assertTrue("Search page. Date label not in finnish", dateLabel.contains("Aloitusajankohta"));
			Assert.assertTrue("Search page. Delegates label not in finnish", delegatesLabel.contains("Osallistujamäärä"));
			Assert.assertTrue("Search page. Search for label not in finnish", searchForLabel.contains("Näytä"));
		}
		else if(lan.toString().equals("EN")){
			if(pkg) {
				Assert.assertTrue("Search page. Search for packages not selected (or not in english)", searchFor.equals("Packages"));
				Assert.assertTrue("Search page. Header not in english", header.contains("Meeting rooms in "));
			}
			else {
				Assert.assertTrue("Search page. Search for rooms not selected (or not in english)", searchFor.equals("Meeting rooms"));
				Assert.assertTrue("Search page. Header not in english", header.contains("Meeting rooms in London")||header.contains("Venues in London"));
			}
			Assert.assertTrue("Search page. Meeting length label not in english", mLengthLabel.contains("Meeting length"));
			Assert.assertTrue("Search page. Date label not in english", dateLabel.contains("Date & Time"));
			Assert.assertTrue("Search page. Delegates label not in english", delegatesLabel.contains("Persons"));
			Assert.assertTrue("Search page. Search for label not in english", searchForLabel.contains("Search for"));
		}
	}
	
	/**
	 * Method to assert information on package page
	 */
	private void assertVenuePageInfo() {
		Assert.assertTrue("Venue page. Date expected: "+homepage.getDateTime()+". Shown: "+vp.getDate(), vp.getDate().equals(homepage.getDateTime()));
		Assert.assertTrue("Venue page. Meeting length expected: 3 h. Shown: "+vp.getMeetingLength(), vp.getMeetingLength().equals("3"));
		Assert.assertTrue("Venue page. Number of delegated expected: "+delegates+". Shown: "+vp.getDelegates(), vp.getDelegates().equals(String.valueOf(delegates)));
		
	}
	
	/**
	 * Method to assert information on package page
	 */
	private void assertPackagePageInfo() {
		Assert.assertTrue("Package page. Date expected: "+homepage.getDateTime()+". Shown: "+pp.getDate(), pp.getDate().equals(homepage.getDateTime()));
		Assert.assertTrue("Package page. Meeting length expected: 3 h. Shown: "+pp.getMeetingLength(), pp.getMeetingLength().equals("3"));
		Assert.assertTrue("Package page. Number of delegated expected: "+delegates+". Shown: "+pp.getDelegates(), pp.getDelegates().equals(String.valueOf(delegates)));
		Assert.assertTrue("Package page. Price for package("+pp.getTotal()+") does not match price from search page("+price+")", pp.getTotal()==price);
	}
	
	/**
	 * Method to assert language information on venue page
	 */
	private void assertVenuePageLanguage(Languages lan) {
		String enquiryExpected = null,
				sendEnquiryExpected = null,
				facilitiesExpected = null;
		if(lan.toString().equals("EN")){
			enquiryExpected = "ENQUIRY";
					sendEnquiryExpected = "Send enquiry";
					facilitiesExpected = "Facilities";
		}
		else if(lan.toString().equals("FI")){
			enquiryExpected = "TARJOUSPYYNTÖ";
					sendEnquiryExpected = "Jatka";
					facilitiesExpected = "Palvelut";
		}
		//check for enquiry form texts:
		Assert.assertTrue("Venue page enquiry form. Expected: "+ enquiryExpected + " "+sendEnquiryExpected+"; Shown: "+driver.findElement(vp.sidebarTitleLocator).getText()+" & "+driver.findElement(vp.submitBtnLocator).getText(), driver.findElement(vp.sidebarTitleLocator).getText().equalsIgnoreCase(enquiryExpected)&&driver.findElement(vp.submitBtnLocator).getText().equalsIgnoreCase(sendEnquiryExpected));
		//check for venue page facilities text:
		Assert.assertTrue("Venue page. Expected: "+facilitiesExpected+"; Shown: "+driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")), driver.findElement(By.cssSelector("#venue-information > div > a:nth-child(1)")).getText().equals(facilitiesExpected));
	}
	
//	private void assertCheckoutPageInfo() {
//		Assert.assertTrue("Checkout page. Date expected: "+homepage.getDateTime()+". Shown: "+cp.getDate(), cp.getDate().contains(homepage.getDateTime()));
////		Assert.assertTrue("Venue page. Meeting length expected: 3 h. Shown: "+cp.getMeetingLength(), vp.getMeetingLength().equals("3"));
//		Assert.assertTrue("Venue page. Number of delegated expected: "+delegates+". Shown: "+cp.getDelegates(), vp.getDelegates().equals(String.valueOf(delegates)));
//	}
	
	/**
	 * Method to assert language information on checkout page
	 */
	private void assertCheckoutPageLanguage(Languages lan) {
		String checkoutText = driver.findElement(By.cssSelector("#checkout-template > div:nth-child(1) > h1")).getText();
		//check for checkout page header:
		if(lan.toString().equals("EN"))
			Assert.assertTrue("Checkout page. Expected: Please add your contact details and proceed; Shown: "+checkoutText, checkoutText.equalsIgnoreCase("Please add your contact details and proceed")||checkoutText.equalsIgnoreCase("checkout"));
		else if(lan.toString().equals("FI"))
			Assert.assertTrue("Checkout page. Expected: Tietojen lähetys; Shown: "+checkoutText, checkoutText.equalsIgnoreCase("Tietojen lähetys"));
		else if(lan.toString().equals("IT"))
			Assert.assertTrue("Checkout page. Expected: Check out; Shown: "+checkoutText, checkoutText.equalsIgnoreCase("Check out"));
//			Assert.assertTrue("Checkout page. Expected: Informazioni Personali; Shown: "+checkoutText, checkoutText.equalsIgnoreCase("Informazioni Personali"));
	}

}


