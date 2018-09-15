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
        tags = "@security")
public class TestMspWishListAndFccFlowAndSecurityToken extends BasicTest {

    // set msp.wishlist.security.enabled=true in application.properties to enable security tokens
    // set wishlist.enabled=true in both tests and service

    static {
        LOGGER = LoggerFactory.getLogger(TestMspWishListAndFccFlowAndSecurityToken.class);
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