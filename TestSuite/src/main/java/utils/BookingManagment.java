package utils;

import com.meetingpackage.Pages.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.LocalDate;

public class BookingManagment {
    private WebDriver driver;
    private InboxPage inbox;
    private VenuePage venue;
    private PackagePage packagePage;

    public BookingManagment(WebDriver driver){
        this.driver=driver;
    }

    public void deleteTestBookings(){
        int pages = 10;
        driver.get("https://meetingpackage.com/user/login");
        inbox = new LoginPage(driver).loginAsCustomer().goToInbox();
        for(int i=0;i<pages;i++) {
            inbox.cancelBookings(inbox.getTestBookings());
            inbox.gotoNextPage();
        }
    }

    public void makeBookings(int num){
        driver.get("https://mp:blahblahblah@stage.meetingpackage.com/user/login");
        new LoginPage(driver).loginAsCustomer();
        new DashboardPage(driver).waitFor(3000);
//        driver.get("https://mp:blahblahblah@stage.meetingpackage.com/venue/mp-test");
//        venue = new VenuePage(driver);
        for(int i=0;i<num;i++) {
            try {
                driver.get("https://mp:blahblahblah@stage.meetingpackage.com/venue/mp-test");
                venue = new VenuePage(driver);
                venue.fillinEnquiry(true, 10, LocalDate.now().plusDays(34+i), VenueUtils.Rooms.CUSTOM, VenueUtils.MeetingLength.TwoH, VenueUtils.SeatingOrder.NONE).completeBooking();
            } catch (Exception e) {
                try {
                    driver.findElement(By.cssSelector("body > div.jconfirm.jconfirm-material.jconfirm-open > div.jconfirm-scrollpane > div > div > div > div > div.jconfirm-closeIcon")).click();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                e.printStackTrace();
            }
        }
    }

    public void makePackageBookings(int num){
        int pages = 10;
        driver.get("https://mp:blahblahblah@stage.meetingpackage.com/user/login");
        new LoginPage(driver).loginAs("1quandm1195@gmail.com","minhquan");
        new DashboardPage(driver).waitFor(3000);
        for(int i=0;i<num;i++) {
            try {
                driver.get("https://mp:blahblahblah@stage.meetingpackage.com/venue/mp-test/full-day-package");
                packagePage = new PackagePage(driver);
                packagePage.setPackage("0 price").fillinEnquiry(true, 10, LocalDate.now().plusDays(20+i), VenueUtils.Rooms.CUSTOM, VenueUtils.MeetingLength.TwoH, VenueUtils.SeatingOrder.NONE);//.completeBooking();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
