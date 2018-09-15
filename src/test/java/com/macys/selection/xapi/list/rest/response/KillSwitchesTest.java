package com.macys.selection.xapi.list.rest.response;

import com.macys.selection.xapi.list.data.converters.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by m940030 on 11/14/17.
 */
@SpringBootTest
@JsonTest
public class KillSwitchesTest extends AbstractTestNGSpringContextTests {

    private static final String KS_JSON_FILE = "com/macys/selection/xapi/list/rest/response/killswitchList.json";

    @Autowired
    private JacksonTester<KillSwitches> json;

    private KillSwitches ks;

    @BeforeMethod
    public void setup() {
        ks = new KillSwitches();
        ks.setResponsiveWishlistEnabled(true);
        ks.setResponsiveWishlistFindEnabled(false);
        ks.setResponsiveWishlistPromotionsEnabled(true);
        ks.setFinalPriceDisplayEnabled(true);
        ks.setResponsiveCleanupExperimentEnabled(true);
        ks.setSeparateFindUsersEnabled(true);
        ks.setMspListEnabled(true);
        ks.setHeaderAsAServiceEnabled(true);
        ks.setProsPageNavigationEnabled(true);
        ks.setProsZonesAddToListEnabled(true);
        ks.setQuickViewProsZoneEnabled(true);
    }

    @Test
    public void killSwitchSerializeTest() throws ParseException, IOException {
        assertThat(this.json.write(ks)).isEqualToJson("killswitchList.json");
    }

    @Test
    public void killSwitchDeserializeTest() throws ParseException, IOException {
        String ksJson = TestUtils.readFile(KS_JSON_FILE);
        assertThat(this.json.parse(ksJson)).isEqualTo(ks);
    }

    @Test
    public void killSwitchEquaslsTest() {
        assertThat(ks.equals(null)).isFalse();
        assertThat(ks.equals(ks)).isTrue();
    }

    @Test
    public void killSwitchHashCodeTest() throws IOException {
        String ksJson = TestUtils.readFile(KS_JSON_FILE);
        KillSwitches ks = this.json.parseObject(ksJson);
        assertThat(ks.hashCode()).isNotNull();
    }

    @Test
    public void killSwitchToStringTest() throws IOException {
        String ksJson = TestUtils.readFile(KS_JSON_FILE);
        KillSwitches ks = this.json.parseObject(ksJson);
        assertThat(ks.toString()).isNotNull();
    }

}
