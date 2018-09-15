package com.macys.selection.xapi.list.client.response.fcc;

import com.macys.selection.xapi.list.data.converters.TestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.TimeZone;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@JsonTest
public class ProductResponseTest extends AbstractTestNGSpringContextTests {

    private static final String JSON_FILE = "com/macys/selection/xapi/list/client/response/fcc/fcc_product_response.json";


    @Autowired
    private JacksonTester<ProductResponse> json;

    private ProductResponse productResponse;

    private DateFormat dateFormat;

    @Test
    public void productResponseDeserializeTest() throws IOException {
        String productJson = TestUtils.readFile(JSON_FILE);
        assertThat(this.json.parse(productJson)).isEqualToComparingFieldByField(this.productResponse);
    }

    @BeforeMethod
    public void setup() throws ParseException {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        this.productResponse = new ProductResponse();
        this.productResponse.setId(456);
        this.productResponse.setName("Steel Bracelet Watch");
        this.productResponse.setDefaultCategoryId(23);
        this.productResponse.setActive(true);
        this.productResponse.setPrimaryPortraitSource("test_source");
        this.productResponse.setAdditionalImageSource(Collections.singletonList("testImage"));
        this.productResponse.setLive(true);
        this.productResponse.setAvailable(true);

        PriceResponse price = new PriceResponse();
        price.setOriginalPrice(1900.);
        price.setRetailPrice(1900.);
        price.setPriceType(0);
        price.setSaleValue(0.);
        price.setIntermediateSaleValue(0.);
        price.setDisplayCode("dcode");
        price.setBasePriceType(0);
        price.setOnSale(false);
        this.productResponse.setPrice(price);

        FinalPriceResponse finalPriceResponse = new FinalPriceResponse();
        finalPriceResponse.setFinalPrice(106.24);
        finalPriceResponse.setDisplayFinalPrice("Always Show");
        finalPriceResponse.setProductTypePromotion("NONE");
        FinalPricePromotionResponse promotionResponse = new FinalPricePromotionResponse();
        promotionResponse.setPromotionId(19883394);
        promotionResponse.setPromotionName("SITEWIDE FC: FOURTH 15%");
        promotionResponse.setGlobal(false);
        finalPriceResponse.setPromotions(Collections.singletonList(promotionResponse));

        this.productResponse.setFinalPrice(finalPriceResponse);

        ShippingResponse shippingResponse = new ShippingResponse();
        shippingResponse.setNotes(Collections.singletonList("Test comment"));
        shippingResponse.setGiftWrappable(true);
        shippingResponse.setGiftMessageable(true);

        StateResponse state = new StateResponse();
        state.setCode("AK");
        state.setName("Alaska");
        shippingResponse.setExcludedStates(Collections.singletonList(state));

        ShippingMethodResponse shippingMethodResponse = new ShippingMethodResponse();
        shippingMethodResponse.setCode("G");
        shippingMethodResponse.setName("Standard");
        shippingResponse.setShippingMethods(Collections.singletonList(shippingMethodResponse));

        ReturnConstraintResponse returnConstraintResponse = new ReturnConstraintResponse();
        returnConstraintResponse.setReturnCode("SS");
        shippingResponse.setReturnConstraints(Collections.singletonList(returnConstraintResponse));
        this.productResponse.setShipping(shippingResponse);

        ColorwayImageResponse colorwayImageResponse = new ColorwayImageResponse();
        colorwayImageResponse.setImageName("testImage");
        this.productResponse.setPrimaryImage(colorwayImageResponse);

        AttributeResponse attributeResponse = new AttributeResponse();
        attributeResponse.setName("STRAP");
        attributeResponse.setSortWeight(0);
        attributeResponse.setVisible(true);
        attributeResponse.setUnary(true);

        AttributeValueResponse attributeValueResponse = new AttributeValueResponse();
        attributeValueResponse.setValue("Stainless Steel");
        attributeResponse.setAttributeValues(Collections.singletonList(attributeValueResponse));

        this.productResponse.setAttributes(Collections.singletonList(attributeResponse));

        UpcResponse upcResponse = new UpcResponse();
        upcResponse.setId(111);
        upcResponse.setUpc(112l);
        upcResponse.setProductId(456);
        upcResponse.setPrice(price);
        upcResponse.setAddByApp("MCOM");
        upcResponse.setBaseFeeExempt(false);
        upcResponse.setSurchargeFee(0.);
        upcResponse.setActive(true);

        AvailabilityResponse availabilityResponse = new AvailabilityResponse();
        availabilityResponse.setUpcId(111);
        availabilityResponse.setAvailable(true);
        availabilityResponse.setShipDays(2);
        availabilityResponse.setPremiumShipDays(0);
        availabilityResponse.setExpressShipDays(0);
        availabilityResponse.setOrderMethod("POOL");
        availabilityResponse.setGiftWrappable(true);
        availabilityResponse.setGiftMessageable(true);
        availabilityResponse.setReturnConstraints("SS");
        availabilityResponse.setVendorId(111);
        availabilityResponse.setSource("UNKN");
        availabilityResponse.setShippingMethodsCode((short) 7);
        availabilityResponse.setShipMethodsSource("UNKN");
        availabilityResponse.setInStoreEligibility(true);
        availabilityResponse.setInventoryStatusCode("AVAILABLE");
        availabilityResponse.setUpcAvailabilityMessage("test_message");
        availabilityResponse.setRegistryEligible(false);

        availabilityResponse.setNgfProductDate(dateFormat.parse("2017-12-30 00:00:00"));
        availabilityResponse.setCheckoutGiftAttrUpdt(false);
        availabilityResponse.setCheckoutFulfilUpdt(false);
        availabilityResponse.setAvailabilityLastModified(dateFormat.parse("2018-03-25 00:48:04"));
        availabilityResponse.setGiftAttributesLastModified(dateFormat.parse("2018-03-25 00:48:04"));
        upcResponse.setAvailability(availabilityResponse);

        ColorwayResponse upcColorwayResponse = new ColorwayResponse();
        upcColorwayResponse.setColorwayId(27);
        upcColorwayResponse.setColorName("Two-Tone");
        upcColorwayResponse.setColorNormalName("Silver");
        upcColorwayResponse.setSwatchSeqNumber(0);
        upcColorwayResponse.setSwapoutSeqNumber(0);

        ColorwayImageResponse upcColorwayImageResponse = new ColorwayImageResponse();
        upcColorwayImageResponse.setImageName("9167018.fpx");
        upcColorwayImageResponse.setSeqNumber(1);
        upcColorwayResponse.setPrimaryImage(upcColorwayImageResponse);
        upcResponse.setColorway(upcColorwayResponse);

        AttributeResponse upcAttribute = new AttributeResponse();
        upcAttribute.setName("COLOR_NORMAL");
        upcAttribute.setSortWeight(0);
        upcAttribute.setVisible(true);
        upcAttribute.setUnary(false);
        AttributeValueResponse upcAttributeValueResponse = new AttributeValueResponse();
        upcAttributeValueResponse.setValue("Silver");
        upcAttributeValueResponse.setSequenceNumber(0);
        upcAttribute.setAttributeValues(Arrays.asList(upcAttributeValueResponse));
        upcResponse.setAttributes(Arrays.asList(upcAttribute));

        upcResponse.setFinalPrice(finalPriceResponse);

        productResponse.setUpcs(Arrays.asList(upcResponse));

    }

}
