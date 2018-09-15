package com.macys.selection.xapi.list.test;

import static com.macys.selection.xapi.list.util.TestUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;

/**
 * Main test runner file
 **/
@CucumberOptions(
        features = ("src/test/resources/featuresBcom"),
        glue = {"com.macys.selection.xapi.list.customer"},
        plugin = {"pretty", "html:target/cucumber-html-report",
                "json:target/cucumber.json"},
        tags = "@bcomOnly")
public class TestCustomerListITBcom extends BasicTestBcom {

    // TO RUN TESTS WITH OLD FLOW (XAPI-WISHLIST -> MSP CUSTOMER )
    // mvn -Dtest=TestCustomerListITBcom test
    // (set wishlist.enabled=false in both tests and service)

    // TO RUN TESTS WITH NEW FLOW (XAPI-WISHLIST -> MSP WISHLIST + FCC + MSP CUSTOMER(profile) )
    // mvn -Dtest=TestCustomerListITBcom,TestMspWishListAndFccFlowBcom test
    // (set wishlist.enabled=true in both tests and service)

    static {
        LOGGER = LoggerFactory.getLogger(TestCustomerListITBcom.class);
    }

    @BeforeClass
    public static void initSpec() {
        BasicTestBcom.initSpec();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void tearDown() {
        BasicTestBcom.tearDown();
    }

}
