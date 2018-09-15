package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.response.fcc.AttributeResponse;
import com.macys.selection.xapi.list.client.response.fcc.AttributeValueResponse;
import com.macys.selection.xapi.list.client.response.fcc.AvailabilityResponse;
import com.macys.selection.xapi.list.client.response.fcc.ColorwayImageResponse;
import com.macys.selection.xapi.list.client.response.fcc.ColorwayResponse;
import com.macys.selection.xapi.list.client.response.fcc.FinalPricePromotionResponse;
import com.macys.selection.xapi.list.client.response.fcc.FinalPriceResponse;
import com.macys.selection.xapi.list.client.response.fcc.PriceResponse;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.client.response.fcc.ReviewStatisticsResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.mapping.MapperConfig;
import com.macys.selection.xapi.list.rest.response.FinalPrice;
import com.macys.selection.xapi.list.rest.response.FinalPricePromotionDO;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.javers.common.collections.Sets;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ListCatalogServiceTest {
    private static final Integer UPC_ID_1 = 36146716;
    private static final Integer UPC_ID_2 = 36146717;
    private static final Integer UPC_ID_3 = 36146718;
    private static final Integer UPC_ID_4 = 36146719;
    private static final Integer PRODUCT_ID_1 = 2945905;
    private static final Integer PRODUCT_ID_2 = 2945906;
    private static final Long UPC_NUMBER_1 = 1234567L;
    private static final Long UPC_NUMBER_2 = 1234568L;
    private static final Long UPC_NUMBER_3 = 1234569L;
    private static final Long UPC_NUMBER_4 = 1234560L;
    private static final String PRODUCT_NAME = "Test product";

    private ListCatalogService listCatalogService;
    @Mock
    private UpcService upcService;
    @Mock
    private ProductService productService;
    @Mock
    private KillSwitchPropertiesBean killSwitchPropertiesBean;

    private MapperConfig mapperConfig = new MapperConfig();

    @Before
    public void init() {
        listCatalogService = new ListCatalogService(upcService, productService, killSwitchPropertiesBean, mapperConfig.mapperFacade());
    }

    @Test
    public void testPopulateWishListItemDetailsEmptyList() {
        listCatalogService.populateWishListItemDetails(Collections.emptyList(), false);
    }

    @Test
    public void shouldNotPopulateProductForUpcLevelItem() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, null);
        item.setRetailPriceWhenAdded(242.59);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertTrue(item.isUpcLevelItem());
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNull(item.getProduct());
        Assert.assertEquals(1.04, item.getRetailPriceDropAfterAddedToList(), 0);
    }

    @Test
    public void shouldPopulateProductForUpcLevelItemIfExplicitlyRequested() {
        boolean productForUpcLevelRequired = true;
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, null);
        item.setRetailPriceWhenAdded(242.59);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), productForUpcLevelRequired);
        Assert.assertTrue(item.isUpcLevelItem());
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getProduct());
        Assert.assertEquals(PRODUCT_ID_1, item.getProduct().getId());
        Assert.assertEquals(PRODUCT_NAME, item.getProduct().getName());
    }

    @Test
    public void shouldPopulateUpcByUpcNumberIfNotFoundByUpcId() {
        Item item = buildItem(null, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.emptyList());
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertFalse(item.isUpcLevelItem());
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getProduct());
    }

    @Test(expected = ListServiceException.class)
    public void shouldFailOnMasterProduct() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setMemberProductIds(Collections.singletonList(12345));

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        try {
            listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.INVALID_MASTER_PRODUCT.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void shouldNotPopulatePriceInUpcProduct() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);
        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setName(PRODUCT_NAME);
        productResponse.getPrice().setOriginalPrice(111.5);

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.emptyList());
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertFalse(item.isUpcLevelItem());
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getUpc().getPrice());
        Assert.assertNotNull(item.getUpc().getProduct());
        Assert.assertEquals(PRODUCT_NAME, item.getUpc().getProduct().getName());
        Assert.assertNull(item.getUpc().getProduct().getPrice());
        Assert.assertNotNull(item.getProduct());
        Assert.assertEquals(PRODUCT_NAME, item.getProduct().getName());
        Assert.assertNotNull(item.getProduct().getPrice());
        Assert.assertEquals(111.5, item.getProduct().getPrice().getOriginalPrice(), 0);
    }

    @Test
    public void shouldPopulateUpcAttributes() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);
        String expectedColor = "Empire Red";
        String expectedSize = "S";
        String expectedType = "test";

        upcResponse.setAttributes(Arrays.asList(buildAttributeResponse("COLOR", expectedColor),
                buildAttributeResponse("SIZE", expectedSize),
                buildAttributeResponse("TYPE", expectedType)));

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertEquals(expectedColor, item.getUpc().getColor());
        Assert.assertEquals(expectedSize, item.getUpc().getSize());
        Assert.assertEquals(expectedType, item.getUpc().getType());
    }

    @Test
    public void shouldPopulateProductAttributes() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);
        String expectedYesValue = "Y";
        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setAttributes(Arrays.asList(buildAttributeResponse("SUPPRESS_REVIEWS", "true"),
                buildAttributeResponse("PHONE_ONLY", expectedYesValue),
                buildAttributeResponse("CLICK_TO_CALL", expectedYesValue)));

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getProduct());
        Assert.assertTrue(item.getProduct().isSuppressReviews());
        Assert.assertEquals(expectedYesValue, item.getProduct().getPhoneOnly());
        Assert.assertEquals(expectedYesValue, item.getProduct().getClickToCall());
        Assert.assertTrue(item.getUpc().getProduct().isSuppressReviews());
        Assert.assertEquals(expectedYesValue, item.getUpc().getProduct().getPhoneOnly());
        Assert.assertEquals(expectedYesValue, item.getUpc().getProduct().getClickToCall());
    }

    private AttributeResponse buildAttributeResponse(String name, String value) {
        AttributeResponse attr = new AttributeResponse();
        attr.setName(name);
        AttributeValueResponse valueResponse = new AttributeValueResponse();
        valueResponse.setValue(value);
        attr.setAttributeValues(Arrays.asList(valueResponse));
        return attr;
    }

    @Test
    public void shouldPopulateUpcAvailabilityInfo() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);
        upcResponse.getAvailability().setUpcAvailabilityMessage("AvailabilityMessage");
        upcResponse.getAvailability().setInStoreEligibility(true);
        upcResponse.getAvailability().setOrderMethod("OrderMethod");

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertNotNull(item.getUpc().getAvailability());
        Assert.assertEquals(upcResponse.getAvailability().isAvailable(), item.getUpc().getAvailability().isAvailable());
        Assert.assertEquals(upcResponse.getAvailability().getInStoreEligibility(), item.getUpc().getAvailability().isInStoreEligible());
        Assert.assertEquals(upcResponse.getAvailability().getUpcAvailabilityMessage(), item.getUpc().getAvailability().getUpcAvailabilityMessage());
        Assert.assertEquals(upcResponse.getAvailability().getOrderMethod(), item.getUpc().getAvailability().getOrderMethod());
    }

    @Test
    public void shouldPopulateUpcAvailabilityInfoWithDefaultValues() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);
        upcResponse.setAvailability(null);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertNotNull(item.getUpc().getAvailability());
        Assert.assertFalse(item.getUpc().getAvailability().isAvailable());
        Assert.assertFalse(item.getUpc().getAvailability().isInStoreEligible());
    }

    @Test
    public void shouldPopulateUpcPrice() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertNotNull(item.getUpc().getPrice());
        Assert.assertEquals(upcResponse.getPrice().getPriceType(), item.getUpc().getPrice().getPriceType());
        Assert.assertEquals(upcResponse.getPrice().getPriceTypeText(), item.getUpc().getPrice().getPriceTypeText());
        Assert.assertEquals(upcResponse.getPrice().isOnSale(), item.getUpc().getPrice().getOnSale());
        Assert.assertEquals(upcResponse.getPrice().getBasePriceType(), item.getUpc().getPrice().getBasePriceType());
        Assert.assertEquals(upcResponse.getPrice().getSaleValue(), item.getUpc().getPrice().getSalesValue());
        Assert.assertEquals(upcResponse.getPrice().getRetailPrice(), item.getUpc().getPrice().getRetailPrice());
        Assert.assertEquals(upcResponse.getPrice().getOriginalPrice(), item.getUpc().getPrice().getOriginalPrice());
        Assert.assertEquals(upcResponse.getPrice().getIntermediatePrice(), item.getUpc().getPrice().getIntermediateSalesValue());
        Assert.assertEquals(upcResponse.getPrice().getOriginalPriceLabel(), item.getUpc().getPrice().getOriginalPriceLabel());
        Assert.assertEquals(upcResponse.getPrice().getRetailPriceLabel(), item.getUpc().getPrice().getRetailPriceLabel());
        Assert.assertEquals(upcResponse.getPrice().getPricingPolicy(), item.getUpc().getPrice().getPricingPolicy());
        Assert.assertEquals(upcResponse.getPrice().getPricingPolicyText(), item.getUpc().getPrice().getPricingPolicyText());
        Assert.assertEquals(upcResponse.getPrice().isUpcOnSale(), item.getUpc().getPrice().getUpcOnSale());
    }

    @Test
    public void shouldPopulateProductPrice() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, new PriceResponse(), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setPrice(buildPriceResponse());

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertNotNull(item.getUpc().getPrice());
        Assert.assertEquals(productResponse.getPrice().getPriceType(), item.getProduct().getPrice().getPriceType());
        Assert.assertEquals(productResponse.getPrice().getPriceTypeText(), item.getProduct().getPrice().getPriceTypeText());
        Assert.assertEquals(productResponse.getPrice().isOnSale(), item.getProduct().getPrice().getOnSale());
        Assert.assertNull(item.getProduct().getPrice().getBasePriceType());
        Assert.assertNull(item.getProduct().getPrice().getSalesValue());
        Assert.assertEquals(productResponse.getPrice().getRetailPrice(), item.getProduct().getPrice().getRetailPrice());
        Assert.assertEquals(productResponse.getPrice().getOriginalPrice(), item.getProduct().getPrice().getOriginalPrice());
        Assert.assertEquals(productResponse.getPrice().getIntermediatePrice(), item.getProduct().getPrice().getIntermediateSalesValue());
        Assert.assertEquals(productResponse.getPrice().getOriginalPriceLabel(), item.getProduct().getPrice().getOriginalPriceLabel());
        Assert.assertEquals(productResponse.getPrice().getRetailPriceLabel(), item.getProduct().getPrice().getRetailPriceLabel());
        Assert.assertEquals(productResponse.getPrice().getPricingPolicy(), item.getProduct().getPrice().getPricingPolicy());
        Assert.assertEquals(productResponse.getPrice().getPricingPolicyText(), item.getProduct().getPrice().getPricingPolicyText());
        Assert.assertEquals(productResponse.getPrice().isUpcOnSale(), item.getProduct().getPrice().getUpcOnSale());
    }

    private PriceResponse buildPriceResponse() {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setPriceType(10);
        priceResponse.setPriceTypeText("PriceTypeText");
        priceResponse.setOnSale(true);
        priceResponse.setBasePriceType(15);
        priceResponse.setSaleValue(15.1);
        priceResponse.setRetailPrice(15.2);
        priceResponse.setOriginalPrice(15.3);
        priceResponse.setIntermediatePrice(15.4);
        priceResponse.setOriginalPriceLabel("OriginalPriceLabel");
        priceResponse.setRetailPriceLabel("RetailPriceLabel");
        priceResponse.setPricingPolicy("PricingPolicy");
        priceResponse.setPricingPolicyText("PricingPolicyText");
        priceResponse.setUpcOnSale(false);
        return priceResponse;
    }

    @Test
    public void testPriceDropWhenProductResultSetWithSingleUnavailableUPC() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        item.setRetailPriceWhenAdded(199.9);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(144.0, null), false);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, item.getRetailPriceDropAfterAddedToList(), 0);
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, item.getRetailPriceDropPercentage());
        Assert.assertFalse(item.getUpc().getAvailability().isAvailable());
        Assert.assertFalse(item.getProduct().isMultipleUpc());
    }

    private ProductResponse buildSingleUPCProduct(Integer upcId, Long upcNumber, Integer productId) {
        ProductResponse productResponse = new ProductResponse();
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setRetailPrice(144.00);
        UpcResponse upcResponse = buildUpcResponse(upcId, upcNumber, buildPriceResponse(144.0, null), false);
        List<UpcResponse> upcs = Collections.singletonList(upcResponse);
        productResponse.setId(productId);
        productResponse.setPrice(priceResponse);
        productResponse.setName(PRODUCT_NAME);
        productResponse.setUpcs(upcs);
        return productResponse;
    }

    @Test
    public void testPriceDropWhenProductResultSetWithMultipleUPCAllUnavailableUPC() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        item.setRetailPriceWhenAdded(199.9);
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(144.0, null), true);

        ProductResponse productResponse = buildMultipleALLUnavailableUPCProduct();
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, item.getRetailPriceDropAfterAddedToList(), 0);
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, item.getRetailPriceDropPercentage());
        Assert.assertFalse(item.isUpcLevelItem());
        Assert.assertTrue(item.getProduct().isMultipleUpc());
    }

    private ProductResponse buildMultipleALLUnavailableUPCProduct() {
        ProductResponse productResponse = new ProductResponse();

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setRetailPrice(144.00);

        UpcResponse upcResponse1 = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, buildPriceResponse(144.0, null), false);
        UpcResponse upcResponse2 = buildUpcResponse(UPC_ID_2, UPC_NUMBER_2, buildPriceResponse(144.0, null), false);
        UpcResponse upcResponse3 = buildUpcResponse(UPC_ID_3, UPC_NUMBER_3, buildPriceResponse(144.0, null), false);
        UpcResponse upcResponse4 = buildUpcResponse(UPC_ID_4, UPC_NUMBER_4, buildPriceResponse(144.0, null), false);

        List<UpcResponse> upcs = Arrays.asList(upcResponse1, upcResponse2, upcResponse3, upcResponse4);

        productResponse.setId(PRODUCT_ID_1);
        productResponse.setPrice(priceResponse);
        productResponse.setUpcs(upcs);
        return productResponse;
    }

    @Test
    public void testPriceDropWhenProductResultSetWithMultipleUPCFewUnavailableUPC() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        item.setRetailPriceWhenAdded(199.9);

        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(144.0, null), true);

        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(55.9, item.getRetailPriceDropAfterAddedToList(), 0);
        Assert.assertEquals(Integer.valueOf(27), item.getRetailPriceDropPercentage());
        Assert.assertFalse(item.isUpcLevelItem());
        Assert.assertTrue(item.getProduct().isMultipleUpc());
    }

    private ProductResponse buildMultipleUPCFewUnavailableProduct() {
        ProductResponse productResponse = new ProductResponse();

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setRetailPrice(145.00);

        UpcResponse upcResponse1 = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, buildPriceResponse(104.0, null), false);
        UpcResponse upcResponse2 = buildUpcResponse(UPC_ID_2, UPC_NUMBER_2, buildPriceResponse(114.0, null), false);
        UpcResponse upcResponse3 = buildUpcResponse(UPC_ID_3, UPC_NUMBER_3, buildPriceResponse(144.0, true), true);
        UpcResponse upcResponse4 = buildUpcResponse(UPC_ID_4, UPC_NUMBER_4, buildPriceResponse(124.0, null), true);

        List<UpcResponse> upcs = Arrays.asList(upcResponse1, upcResponse2, upcResponse3, upcResponse4);

        productResponse.setId(PRODUCT_ID_1);
        productResponse.setPrice(priceResponse);
        productResponse.setUpcs(upcs);
        return productResponse;
    }

    @Test
    public void testPriceDropWhenProductResultSetWithMultipleUPCAllavailableUPC() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        item.setRetailPriceWhenAdded(199.9);

        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(144.0, null), true);

        ProductResponse productResponse = buildMultipleUPCAllavailableProduct();
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, item.getRetailPriceDropAfterAddedToList(), 0);
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, item.getRetailPriceDropPercentage());
        Assert.assertFalse(item.isUpcLevelItem());
        Assert.assertTrue(item.getProduct().isMultipleUpc());
    }

    private ProductResponse buildMultipleUPCAllavailableProduct() {
        ProductResponse productResponse = new ProductResponse();

        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setRetailPrice(144.00);

        UpcResponse upcResponse1 = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, buildPriceResponse(144.0, null), true);
        UpcResponse upcResponse2 = buildUpcResponse(UPC_ID_2, UPC_NUMBER_2, buildPriceResponse(144.0, null), true);
        UpcResponse upcResponse3 = buildUpcResponse(UPC_ID_3, UPC_NUMBER_3, buildPriceResponse(156.0, false), true);
        UpcResponse upcResponse4 = buildUpcResponse(UPC_ID_4, UPC_NUMBER_4, buildPriceResponse(144.0, true), true);

        List<UpcResponse> upcs = Arrays.asList(upcResponse1, upcResponse2, upcResponse3, upcResponse4);

        productResponse.setId(PRODUCT_ID_1);
        productResponse.setPrice(priceResponse);
        productResponse.setUpcs(upcs);
        return productResponse;
    }

    @Test
    public void testPriceDropWhenProductResultSetWithEmptyUPCs() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        item.setRetailPriceWhenAdded(199.9);

        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(144.0, null), false);

        ProductResponse productResponse = buildMultipleUPCAllavailableProduct();
        productResponse.setUpcs(null);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);

        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, item.getRetailPriceDropAfterAddedToList(), 0);
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, item.getRetailPriceDropPercentage());
        Assert.assertFalse(item.getUpc().getAvailability().isAvailable());
    }

    @Test(expected = ListServiceException.class)
    public void testPopulateProductDetailsBaseOnProductIdException() {
        Item item = buildItem(null, null, PRODUCT_ID_1);
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenThrow(new RuntimeException());
        try {
            listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testProductResultSetWithSingleAvailableUPCAndEmptyItemUpc() {
        Item item = buildItem(null, null, PRODUCT_ID_1);
        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getUpc().getProduct());
        Assert.assertEquals(PRODUCT_ID_1, item.getUpc().getProduct().getId());
        Assert.assertTrue(item.isUpcLevelItem());
    }

    @Test
    public void testProductResultSetWithMultipleAvailableUPCsAndEmptyItemUpc() {
        Item item = buildItem(null, null, PRODUCT_ID_1);
        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_4, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_4, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getUpc().getProduct());
        Assert.assertEquals(PRODUCT_ID_1, item.getUpc().getProduct().getId());
        Assert.assertFalse(item.isUpcLevelItem());
        Assert.assertEquals(124.0, item.getRetailPriceWhenAdded(), 0);
    }

    @Test
    public void shouldPopulateProductWithFinalPriceKillswitchOn() {
        Item item = buildItem(null, null, PRODUCT_ID_1);
        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        productResponse.setFinalPrice(buildFinalPriceResponse());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        Mockito.when(killSwitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getProduct());
        Assert.assertNotNull(item.getProduct().getFinalPrice());
        assertFinalPrice(productResponse.getFinalPrice(), item.getProduct().getFinalPrice());
    }

    @Test
    public void shouldNotPopulateProductWithFinalPriceKillswitchOff() {
        Item item = buildItem(null, null, PRODUCT_ID_1);
        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        productResponse.setFinalPrice(buildFinalPriceResponse());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        Mockito.when(killSwitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(false);
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getProduct());
        Assert.assertNull(item.getProduct().getFinalPrice());
    }

    @Test
    public void shouldPopulateUpcWithFinalPriceKillswitchOn() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        FinalPriceResponse productFinalPriceResponse = new FinalPriceResponse();
        productFinalPriceResponse.setDisplayFinalPrice("ProductFinalPriceResponse");
        productResponse.setFinalPrice(productFinalPriceResponse);

        UpcResponse upcResponse = productResponse.getUpcs().stream().filter(u -> u.getId().equals(UPC_ID_1)).findFirst().orElse(null);
        FinalPriceResponse upcFinalPriceResponse = buildFinalPriceResponse();
        upcResponse.setFinalPrice(upcFinalPriceResponse);

        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        Mockito.when(killSwitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getProduct());
        Assert.assertNull(item.getProduct().getFinalPrice());
        assertFinalPrice(upcFinalPriceResponse, item.getUpc().getFinalPrice());
    }

    @Test
    public void shouldNotPopulateUpcWithFinalPriceKillswitchOff() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        FinalPriceResponse productFinalPriceResponse = new FinalPriceResponse();
        productFinalPriceResponse.setDisplayFinalPrice("ProductFinalPriceResponse");
        productResponse.setFinalPrice(productFinalPriceResponse);

        UpcResponse upcResponse = productResponse.getUpcs().stream().filter(u -> u.getId().equals(UPC_ID_1)).findFirst().orElse(null);
        FinalPriceResponse upcFinalPriceResponse = buildFinalPriceResponse();
        upcResponse.setFinalPrice(upcFinalPriceResponse);

        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        Mockito.when(killSwitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(false);
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getProduct());
        Assert.assertNull(item.getProduct().getFinalPrice());
        Assert.assertNull(item.getUpc().getFinalPrice());
    }

    private FinalPriceResponse buildFinalPriceResponse() {
        FinalPriceResponse finalPriceResponse = new FinalPriceResponse();
        finalPriceResponse.setDisplayFinalPrice("DisplayFinalPrice");
        finalPriceResponse.setFinalPrice(10);
        finalPriceResponse.setFinalPriceHigh(11);
        finalPriceResponse.setProductTypePromotion("ProductTypePromotion");
        FinalPricePromotionResponse promotion = new FinalPricePromotionResponse();
        promotion.setGlobal(true);
        promotion.setPromotionId(1);
        promotion.setPromotionName("PromotionName");
        finalPriceResponse.setPromotions(Collections.singletonList(promotion));
        return finalPriceResponse;
    }

    private void assertFinalPrice(FinalPriceResponse finalPriceResponse, FinalPrice actualFinalPrice) {
        Assert.assertEquals(finalPriceResponse.getDisplayFinalPrice(), actualFinalPrice.getDisplayFinalPrice());
        Assert.assertEquals(finalPriceResponse.getFinalPrice(), actualFinalPrice.getFinalPrice(), 0);
        Assert.assertEquals(finalPriceResponse.getFinalPriceHigh(), actualFinalPrice.getFinalPriceHigh(), 0);
        Assert.assertEquals(finalPriceResponse.getProductTypePromotion(), actualFinalPrice.getProductTypePromotion());
        Assert.assertTrue(CollectionUtils.isNotEmpty(actualFinalPrice.getPromotions()));
        Assert.assertEquals(1, actualFinalPrice.getPromotions().size());
        FinalPricePromotionDO actualPromotion = actualFinalPrice.getPromotions().get(0);
        Assert.assertEquals(finalPriceResponse.getPromotions().get(0).isGlobal(), actualPromotion.isGlobal());
        Assert.assertEquals(finalPriceResponse.getPromotions().get(0).getPromotionId(), actualPromotion.getPromotionId());
        Assert.assertEquals(finalPriceResponse.getPromotions().get(0).getPromotionName(), actualPromotion.getPromotionName());
    }

    @Test
    public void shouldPopulateProductWithReviewStats() {
        Item item = buildItem(null, null, PRODUCT_ID_1);
        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        ReviewStatisticsResponse reviewStatsResponse = new ReviewStatisticsResponse();
        reviewStatsResponse.setAverageRating(7.77);
        reviewStatsResponse.setReviewCount(3);
        productResponse.setReviewStatistics(reviewStatsResponse);
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        Assert.assertNotNull(item.getProduct());
        Assert.assertNotNull(item.getProduct().getReviewStatistics());
        Assert.assertEquals(reviewStatsResponse.getAverageRating().floatValue(), item.getProduct().getReviewStatistics().getAverageRating().floatValue(), 0);
        Assert.assertEquals(reviewStatsResponse.getReviewCount(), item.getProduct().getReviewStatistics().getReviewCount());
    }

    @Test
    public void testPopulateListItemImageUrlsListNullAndEmpty() {
        listCatalogService.populateListItemImageUrlsList(Collections.emptyList());
        listCatalogService.populateListItemImageUrlsList(null);
    }

    @Test(expected = ListServiceException.class)
    public void testPopulateListItemImageUrlsListException() {
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(20.0, null), false);

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenThrow(new RuntimeException());

        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        try {
            listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);
        } catch (ListServiceException e) {
            org.testng.Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            org.testng.Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testPopulateWishListItemWithImageUrlsFromUpcResponse() {
        String upcResponsePrimaryImage = "7654.fpx";
        String productResponsePrimaryImage = "3210.fpx";
        String expectedImageUrl = "4/optimized/7654_fpx.tif";

        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(20.0, null), false);
        upcResponse.setColorway(buildColorway(upcResponsePrimaryImage));

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setPrimaryImage(buildPrimaryImage(productResponsePrimaryImage));

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);

        Assert.assertNotNull(item.getUpc().getUpcPrimaryImageName());
        Assert.assertEquals(productResponsePrimaryImage, item.getUpc().getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getUpc().getProduct().getImageURL());
        Assert.assertEquals(productResponsePrimaryImage, item.getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getProduct().getImageURL());
    }

    @Test
    public void testPopulateWishListItemWithImageUrlsFromProductResponse() {
        String productResponsePrimaryImage = "3210.fpx";
        String expectedImageUrl = "0/optimized/3210_fpx.tif";

        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(20.0, null), false);
        upcResponse.setColorway(null);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setPrimaryImage(buildPrimaryImage(productResponsePrimaryImage));

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        listCatalogService.populateWishListItemDetails(Collections.singletonList(item), false);

        Assert.assertNull(item.getUpc().getUpcPrimaryImageName());
        Assert.assertEquals(productResponsePrimaryImage, item.getUpc().getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getUpc().getProduct().getImageURL());
        Assert.assertEquals(productResponsePrimaryImage, item.getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getProduct().getImageURL());
    }

    @Test
    public void testPopulateListItemImageUrlsFromUpcResponse() {
        String upcResponsePrimaryImage = "22805.fpx";
        String productResponsePrimaryImage = "11111.fpx";
        String expectedImageUrl = "5/optimized/22805_fpx.tif";

        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(20.0, null), false);
        upcResponse.setColorway(buildColorway(upcResponsePrimaryImage));

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setPrimaryImage(buildPrimaryImage(productResponsePrimaryImage));

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        WishList list = new WishList();
        list.setItems(Collections.singletonList(item));
        List<WishList> wishLists = Collections.singletonList(list);

        listCatalogService.populateListItemImageUrlsList(wishLists);
        Assert.assertEquals(1, wishLists.size());
        Assert.assertNotNull(item.getUpc().getUpcPrimaryImageName());
        Assert.assertEquals(productResponsePrimaryImage, item.getUpc().getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getUpc().getProduct().getImageURL());
        Assert.assertEquals(productResponsePrimaryImage, item.getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getProduct().getImageURL());
    }

    @Test
    public void testPopulateListItemImageUrlsFromProductResponse() {
        String productResponsePrimaryImage = "479.fpx";
        String expectedImageUrl = "9/optimized/479_fpx.tif";

        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(20.0, null), false);
        upcResponse.setColorway(buildColorway(null));

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse.setPrimaryImage(buildPrimaryImage(productResponsePrimaryImage));

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        WishList list = new WishList();
        list.setItems(Collections.singletonList(item));
        List<WishList> wishLists = Collections.singletonList(list);

        listCatalogService.populateListItemImageUrlsList(wishLists);
        Assert.assertEquals(1, wishLists.size());
        Assert.assertNull(item.getUpc().getUpcPrimaryImageName());
        Assert.assertEquals(productResponsePrimaryImage, item.getUpc().getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getUpc().getProduct().getImageURL());
        Assert.assertEquals(productResponsePrimaryImage, item.getProduct().getPrimaryImage());
        Assert.assertEquals(expectedImageUrl, item.getProduct().getImageURL());
    }

    @Test
    public void shouldBuildImageUrlsListAfterPopulateListsItemDetails() {
        String upcResponsePrimaryImage = "0987.fpx";
        String productResponsePrimaryImage = "6543.fpx";
        String expectedImageUrl1 = "7/optimized/0987_fpx.tif";
        String expectedImageUrl2 = "3/optimized/6543_fpx.tif";
        List<WishList> wishLists = buildTwoItemsWishListWithImages(upcResponsePrimaryImage, productResponsePrimaryImage);

        listCatalogService.populateListsItemDetails(wishLists);
        Assert.assertEquals(1, wishLists.size());
        Assert.assertNotNull(wishLists.get(0).getImageUrlsList());
        Assert.assertEquals(2, wishLists.get(0).getImageUrlsList().size());
        Assert.assertTrue(wishLists.get(0).getImageUrlsList().contains(expectedImageUrl1));
        Assert.assertTrue(wishLists.get(0).getImageUrlsList().contains(expectedImageUrl2));
    }

    @Test
    public void shouldBuildImageUrlsListAfterPopulateListItemImageUrlsList() {
        String upcResponsePrimaryImage = "0987.fpx";
        String productResponsePrimaryImage = "6543.fpx";
        String expectedImageUrl1 = "7/optimized/0987_fpx.tif";
        String expectedImageUrl2 = "3/optimized/6543_fpx.tif";
        List<WishList> wishLists = buildTwoItemsWishListWithImages(upcResponsePrimaryImage, productResponsePrimaryImage);

        listCatalogService.populateListItemImageUrlsList(wishLists);
        Assert.assertEquals(1, wishLists.size());
        Assert.assertNotNull(wishLists.get(0).getImageUrlsList());
        Assert.assertEquals(2, wishLists.get(0).getImageUrlsList().size());
        Assert.assertTrue(wishLists.get(0).getImageUrlsList().contains(expectedImageUrl1));
        Assert.assertTrue(wishLists.get(0).getImageUrlsList().contains(expectedImageUrl2));
    }

    @Test
    public void testCopyItemCatalogDetailsForListWithoutItems() {
        WishList sourceList = new WishList();
        WishList targetList = new WishList();
        listCatalogService.copyItemCatalogDetails(sourceList, targetList);
    }

    @Test
    public void testCopyItemCatalogDetailsUpcLevelItem() {
        WishList sourceList = new WishList();
        Item sourceItem = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        sourceItem.setUpcLevelItem(true);
        sourceList.setItems(Arrays.asList(sourceItem));
        WishList targetList = new WishList();
        Item targetItem = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        targetList.setItems(Arrays.asList(targetItem));
        listCatalogService.copyItemCatalogDetails(sourceList, targetList);
        Assert.assertEquals(sourceItem.getUpc(), targetItem.getUpc());
        Assert.assertNull(targetItem.getProduct());
    }

    @Test
    public void testCopyItemCatalogDetailsProductLevelItem() {
        WishList sourceList = new WishList();
        Item sourceItem = buildItem(null, null, PRODUCT_ID_1);
        sourceItem.setUpcLevelItem(false);
        sourceList.setItems(Arrays.asList(sourceItem));
        WishList targetList = new WishList();
        Item targetItem = buildItem(null, null, PRODUCT_ID_1);
        targetList.setItems(Arrays.asList(targetItem));
        listCatalogService.copyItemCatalogDetails(sourceList, targetList);
        Assert.assertNull(targetItem.getUpc().getId());
        Assert.assertEquals(sourceItem.getProduct(), targetItem.getProduct());
    }

    private List<WishList> buildTwoItemsWishListWithImages(String upcResponsePrimaryImage, String productResponsePrimaryImage) {
        UpcResponse upcResponse1 = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(20.0, null), false);
        upcResponse1.setColorway(buildColorway(upcResponsePrimaryImage));
        UpcResponse upcResponse2 = buildUpcResponse(UPC_ID_2, UPC_NUMBER_2, PRODUCT_ID_2, buildPriceResponse(20.0, null), false);
        upcResponse2.setColorway(buildColorway(null));

        ProductResponse productResponse1 = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        productResponse1.setPrimaryImage(buildPrimaryImage(productResponsePrimaryImage));
        ProductResponse productResponse2 = buildSingleUPCProduct(UPC_ID_2, UPC_NUMBER_2, PRODUCT_ID_2);
        productResponse2.setPrimaryImage(buildPrimaryImage(productResponsePrimaryImage));

        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1, UPC_ID_2))).thenReturn(Arrays.asList(upcResponse1, upcResponse2));
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1, PRODUCT_ID_2))).thenReturn(Arrays.asList(productResponse1, productResponse2));

        Item item1 = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Item item2 = buildItem(UPC_ID_2, UPC_NUMBER_2, PRODUCT_ID_2);
        WishList list = new WishList();
        list.setItems(Arrays.asList(item1, item2));
        return Collections.singletonList(list);
    }

    private Item buildItem(Integer upcId, Long upcNumber, Integer productId) {
        Item item = new Item();
        Upc upc = new Upc();
        upc.setId(upcId);
        upc.setUpcNumber(upcNumber);
        item.setUpc(upc);
        Product product = new Product();
        product.setId(productId);
        item.setProduct(product);
        return item;
    }

    private UpcResponse buildUpcResponse(Integer upcId, Long upcNumber, Integer productId, PriceResponse priceResponse, boolean available) {
        UpcResponse upcResponse = buildUpcResponse(upcId, upcNumber, priceResponse, available);
        upcResponse.setProductId(productId);
        return upcResponse;
    }

    private UpcResponse buildUpcResponse(Integer id, Long upcNumber, PriceResponse priceResponse, boolean available) {
        AvailabilityResponse availabilityResponse = new AvailabilityResponse();
        availabilityResponse.setAvailable(available);

        UpcResponse upcResponse = new UpcResponse();
        upcResponse.setId(id);
        upcResponse.setUpc(upcNumber);
        upcResponse.setPrice(priceResponse);
        upcResponse.setAvailability(availabilityResponse);

        return upcResponse;
    }

    private PriceResponse buildPriceResponse(Double retailPrice, Boolean onSale) {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setRetailPrice(retailPrice);
        priceResponse.setOnSale(onSale);
        return priceResponse;
    }

    private ColorwayResponse buildColorway(String name) {
        ColorwayResponse colorwayResponse = new ColorwayResponse();
        colorwayResponse.setPrimaryImage(buildPrimaryImage(name));
        return colorwayResponse;
    }

    private ColorwayImageResponse buildPrimaryImage(String name) {
        ColorwayImageResponse primaryImage = new ColorwayImageResponse();
        primaryImage.setImageName(name);
        return primaryImage;
    }

    @Test
    public void testGetUpcIdFromProductSingleUpc() {
        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        Integer upcId = listCatalogService.findUpcIdFromProduct(PRODUCT_ID_1);
        Assert.assertEquals(UPC_ID_1, upcId);
    }

    @Test
    public void testGetUpcIdFromProductMultipleUpc() {
        ProductResponse productResponse = buildMultipleUPCFewUnavailableProduct();
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        Integer upcId = listCatalogService.findUpcIdFromProduct(PRODUCT_ID_1);
        Assert.assertEquals(UPC_ID_4, upcId);
    }

    @Test
    public void testGetUpcIdFromProductNoUpc() {
        ProductResponse productResponse = new ProductResponse();
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));
        Integer upcId = listCatalogService.findUpcIdFromProduct(PRODUCT_ID_1);
        Assert.assertNull(upcId);
    }

    @Test
    public void testGetUpcIdFromProductNullProductId() {
        Integer upcId = listCatalogService.findUpcIdFromProduct(null);
        Assert.assertNull(upcId);
    }

    @Test
    public void testPopulateWishListItemDetailsForNewItemsNullWishList() {
        listCatalogService.populateWishListItemDetailsForNewItems(null);
    }

    @Test
    public void testPopulateWishListItemDetailsForNewItemsNullItems() {
        listCatalogService.populateWishListItemDetailsForNewItems(new WishList());
    }

    @Test
    public void testPopulateWishListItemDetailsForNewItemsNotUpcLevelOneUpc() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        WishList wishList = new WishList();
        wishList.setItems(Collections.singletonList(item));
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetailsForNewItems(wishList);
        Assert.assertFalse(item.isUpcLevelItem());
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getProduct());
        Assert.assertEquals(PRODUCT_ID_1, item.getProduct().getId());
        Assert.assertEquals(PRODUCT_NAME, item.getProduct().getName());
    }

    @Test
    public void testPopulateWishListItemDetailsForNewItemsUpcLevelOneUpc() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, null);
        WishList wishList = new WishList();
        wishList.setItems(Collections.singletonList(item));
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetailsForNewItems(wishList);
        Assert.assertTrue(item.isUpcLevelItem());
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNotNull(item.getProduct());
        Assert.assertEquals(PRODUCT_ID_1, item.getProduct().getId());
        Assert.assertEquals(PRODUCT_NAME, item.getProduct().getName());
    }

    @Test
    public void testProductNullAfterPopulateWishListItemDetailsForNewItemsUpcLevelMultipleUpcs() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, null);
        WishList wishList = new WishList();
        wishList.setItems(Collections.singletonList(item));
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildMultipleUPCAllavailableProduct();
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetailsForNewItems(wishList);
        Assert.assertTrue(item.isUpcLevelItem());
        Assert.assertNotNull(item.getUpc());
        Assert.assertEquals(UPC_ID_1, item.getUpc().getId());
        Assert.assertEquals(UPC_NUMBER_1, item.getUpc().getUpcNumber());
        Assert.assertNull(item.getProduct());
    }

    @Test
    public void testUpcNullAfterPopulateWishListItemDetailsForNewItems() {
        Item item = buildItem(0, 0l, null);
        WishList wishList = new WishList();
        wishList.setItems(Collections.singletonList(item));

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.emptyList());
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetailsForNewItems(wishList);
        Assert.assertNull(item.getUpc());
    }

    @Test
    public void testPopulateRetailPriceWhenAddedForNewItems() {
        Item item = buildItem(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        WishList wishList = new WishList();
        wishList.setItems(Collections.singletonList(item));
        UpcResponse upcResponse = buildUpcResponse(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1, buildPriceResponse(241.55, true), true);

        ProductResponse productResponse = buildSingleUPCProduct(UPC_ID_1, UPC_NUMBER_1, PRODUCT_ID_1);
        Mockito.when(upcService.getUpcsByUpcIds(Sets.asSet(UPC_ID_1))).thenReturn(Collections.singletonList(upcResponse));
        Mockito.when(upcService.getUpcsByUpcNumbers(Sets.asSet(UPC_NUMBER_1))).thenReturn(Collections.emptyList());
        Mockito.when(productService.getProductsByProdIds(Sets.asSet(PRODUCT_ID_1))).thenReturn(Collections.singletonList(productResponse));

        listCatalogService.populateWishListItemDetailsForNewItems(wishList);
        Assert.assertNotNull(item.getRetailPriceWhenAdded());
        Assert.assertEquals(item.getUpc().getPrice().getRetailPrice(), item.getRetailPriceWhenAdded());
    }
}
