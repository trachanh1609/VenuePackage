package com.meetingpackage.TestSuite;

import java.io.File;
import java.io.IOException;

import org.junit.rules.TestName;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenShotRule extends TestWatcher {
	private WebDriver driver;
	private final TakesScreenshot takesScreenshot;
	private File scrFile;
	public TestName name = new TestName();
	
	 public ScreenShotRule(TakesScreenshot takesScreenshot, TestName name) {
	        this.takesScreenshot =  takesScreenshot;
	        this.name = name;
	    }
	 
	 @Override
	 protected void failed(Throwable e, Description description) {
   	  System.out.println(name.getMethodName());
//   	  scrFile = takesScreenshot.getScreenshotAs(OutputType.FILE);
// 		try {
// 			FileUtils.copyFile(scrFile, new File("target/site/screenshots/" + name.getMethodName() + "LOL.png"));
// 		} catch (IOException IOe) {
// 			// TODO Auto-generated catch block
// 			IOe.printStackTrace();
// 		}
     }
}
