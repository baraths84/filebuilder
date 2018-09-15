package com.macys.selection.xapi.list.test;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ConsoleNotifier;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.macys.selection.xapi.list.util.TestUtilsBcom.checkWiremockModeOn;
import static com.macys.selection.xapi.list.util.TestUtilsBcom.getWiremockPort;
import static com.macys.selection.xapi.list.util.TestUtilsBcom.getWishlistAndFccFlowEnabled;
import static com.macys.selection.xapi.list.util.TestUtilsBcom.getXapiListBasicUrl;
import static com.macys.selection.xapi.list.util.TestUtilsBcom.getApplicationName;
/**
 * Main test runner file
 **/
//@CucumberOptions(
//    features = ("src/test/resources/featuresBcom"),
//    glue = {"com.macys.selection.xapi.list.customer"},
//    plugin = {"pretty"}, tags = "@bcomOnly")
public abstract class BasicTestBcom extends AbstractTestNGCucumberTests {
    protected static Logger LOGGER = LoggerFactory.getLogger(BasicTestBcom.class);

    protected static RequestSpecification xapiSpec;
    private static WireMockServer wireMockServer;

    public static void initSpec() {
        xapiSpec = new RequestSpecBuilder().setContentType(ContentType.JSON).setAccept(ContentType.JSON)
                .setBaseUri(getXapiListBasicUrl()).addFilter(new ResponseLoggingFilter())
                .addFilter(new RequestLoggingFilter()).build();
        LOGGER.info(String.format("Application Name : %s", getApplicationName()));
        // setting-up wiremock
        if (checkWiremockModeOn()) {
            WireMockConfiguration mockConfiguration = WireMockConfiguration.options();
            ConsoleNotifier notifier = new ConsoleNotifier(Boolean.valueOf(System.getProperty("verbose")));
            String mockRootDirectory = "src/test/resources/wiremock-war/WEB-INF/wiremock-bcom";
                LOGGER.info(String.format("Bcom flow is enabled in tests: "));


            // wiremock will look for __files and mappings folder inside the root directory
            mockConfiguration.port(getWiremockPort()).notifier(notifier).withRootDirectory(mockRootDirectory);
            LOGGER.info(String.format("Root wiremock directory: %s", mockConfiguration.filesRoot().getPath()));
            wireMockServer = new WireMockServer(mockConfiguration);

            wireMockServer.start();
            LOGGER.info(String.format("Started wiremock server on port %s.", getWiremockPort()));
        }
    }
    public static void tearDown() {
        if (checkWiremockModeOn()) {
            wireMockServer.stop();
            LOGGER.info("Stopped wiremock server.........");
        }
    }

}
