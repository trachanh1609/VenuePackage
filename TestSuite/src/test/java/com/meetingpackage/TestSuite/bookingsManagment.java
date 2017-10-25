package com.meetingpackage.TestSuite;

import com.meetingpackage.Pages.HomePage;
import com.meetingpackage.Pages.RegisterPage;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import utils.BookingManagment;

public class bookingsManagment {
        private String baseUrl;
        private WebDriver driver;
//
        //@Test Uncomment
        public void makeBookings() {
            driver = new ChromeDriver();
//            new BookingManagment(driver).makeBookings(9);
                new BookingManagment(driver).makePackageBookings(3);
        }
//
//    @Test
//    public void deleteBookings() {
//        driver = new ChromeDriver();
//        new BookingManagment(driver).deleteTestBookings();
//    }
}
