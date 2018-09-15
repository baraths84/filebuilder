package com.macys.selection.xapi.list.test;

import cucumber.api.CucumberOptions;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

/**
 * Main test runner file
 **/
@CucumberOptions(
        features = ("src/test/resources/features"),
        glue = {"com.macys.selection.xapi.list.customer"},
        plugin = {"pretty", "html:target/cucumber-html-report",
                "json:target/cucumber.json"},
        tags = "@newFlow")
public class TestMspWishListAndFccFlow extends BasicTest {


    // TO RUN TESTS WITH OLD FLOW (XAPI-WISHLIST -> MSP CUSTOMER )
    // mvn -Dtest=TestCustomerListIT test
    // (set wishlist.enabled=false in both tests and service)

    // TO RUN TESTS WITH NEW FLOW (XAPI-WISHLIST -> MSP WISHLIST + FCC + MSP CUSTOMER(profile) )ava
    // mvn -Dtest=TestCustomerListIT,TestMspWishListAndFccFlow test
    // (set wishlist.enabled=true in both tests and service)

    static {
        LOGGER = LoggerFactory.getLogger(TestMspWishListAndFccFlow.class);
    }

    @BeforeClass
    public static void initSpec() {
        BasicTest.initSpec();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        BasicTest.tearDown();
    }
}
