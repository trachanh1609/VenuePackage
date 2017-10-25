/* 
 * Copyright (C) 2017 Cocouz Ltd - All Rights Reserved
 */

package com.meetingpackage.TestSuite;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.meetingpackage.Pages.DashboardPage;
import com.meetingpackage.Pages.HomePage;

import utils.Email;

public class SupplierTests {
	private String baseUrl;
	private WebDriver driver;
	private HomePage homepage;
	private DashboardPage dp;
//	private String branch;
	
	/**
	 * Automatically fires before each test
	 * Opens browser and goes to meetingpackage.com
	 */
	@Before
	public void openBrowser() {
//		String branch = "not stage branch";
//		if(System.getenv("CIRCLE_BRANCH")!=null){
//			branch = System.getenv("CIRCLE_BRANCH");
//		}
//		org.junit.Assume.assumeTrue(branch.equals("stage"));
		baseUrl="http://mp:blahblahblah@stage.meetingpackage.com/";
		driver = new ChromeDriver();
		driver.manage().window().setSize(new Dimension(1280,720));
		driver.get(baseUrl);
		homepage = new HomePage(driver);
	}

	/**
	 * Automatically fires after each test case
	 * Closes all browser windows
	 */
	@After
	public void closeBrowser() {
//		driver.quit();
	}
	
	/**
	 * Test for choosing a plan as new supplier(only if triggered by stage branch
	 */
	@Test
	public void choosePlanTest(){
		if(System.getenv("CIRCLE_BRANCH")!=null){
			if(System.getenv("CIRCLE_BRANCH").equals("stage")){
				Email.deleteMessages();
				dp=homepage.goToRegisterPage().registerAsSupplier();
				dp.selectGoldPlan();
				
				Assert.assertTrue("Number of venues expected: "+dp.venues+". Shown: "+dp.getVenuesNum(), dp.getVenuesNum()==dp.venues);
				Assert.assertTrue("Wrond plan selected(Expected: Gold, Shown: "+driver.findElement(By.cssSelector("#account-partnership > b")).getText(), driver.findElement(By.cssSelector("#account-partnership > b")).getText().equals("Gold"));
				Assert.assertTrue("Total price("+dp.getTotal()+") not what it should be("+(dp.getPrice()*dp.venues)+")", (dp.getPrice()*dp.venues > dp.getTotal()-2)&&(dp.getPrice()*dp.venues < dp.getTotal()+2));
				Assert.assertTrue("Commission expected: 10 %. Shown: " + dp.getCommission(), dp.getCommission().equals("10 %"));
				dp.waitFor(5000);   //wait some time to allow emails to come to inbox
				Assert.assertTrue("Cannot assert email sent", Email.checkContractMail());
				

			}
		}
	}
}
