package com.macys.selection.xapi.list.mapping;

import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.response.EmailItemDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;
import com.macys.selection.xapi.list.client.response.WishListsDTO;
import com.macys.selection.xapi.list.client.response.fcc.FinalPricePromotionResponse;
import com.macys.selection.xapi.list.client.response.fcc.FinalPriceResponse;
import com.macys.selection.xapi.list.client.response.fcc.PriceResponse;
import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.FinalPrice;
import com.macys.selection.xapi.list.rest.response.FinalPricePromotionDO;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.WishList;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.GregorianCalendar;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MapperConfig.class)
public class MappingTest {
    private static final String WISH_LISTS_BY_USER_JSON_FILE = "com/macys/selection/xapi/list/client/response/msp_wishlist_response_by_user.json";
    private JsonToObjectConverter<WishListsDTO> wishlistsConverter = new JsonToObjectConverter<>(WishListsDTO.class);

    @Autowired
    private MapperFacade mapperFacade;

    @Test
    public void testMappingWishlistsResponseToCustomerList() throws IOException {
        String wishlistsResponseJson = TestUtils.readFile(WISH_LISTS_BY_USER_JSON_FILE);
        WishListsDTO wishListsResponse = wishlistsConverter.parseJsonToObject(wishlistsResponseJson);

        CustomerList wishList = mapperFacade.map(wishListsResponse, CustomerList.class);

        Assert.assertEquals(wishListsResponse.getLists().size(), wishList.getWishlist().size());

        Assert.assertEquals(wishListsResponse.getLists().get(0).getListId(),
                wishList.getWishlist().get(0).getId());

        Assert.assertEquals(wishListsResponse.getLists().get(0).getItems().get(0).getItemId().intValue(),
                wishList.getWishlist().get(0).getItems().get(0).getId().intValue());

        Assert.assertEquals(wishListsResponse.getLists().get(0).getItems().get(0).getUpcId(),
                wishList.getWishlist().get(0).getItems().get(0).getUpc().getId());

        Assert.assertEquals(wishListsResponse.getLists().get(0).getItems().get(0).getProductId(),
                wishList.getWishlist().get(0).getItems().get(0).getProduct().getId());

    }

    @Test
    public void testPriceResponseMapping() {
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
        Price actualPrice = mapperFacade.map(priceResponse, Price.class);
        Assert.assertEquals(priceResponse.getPriceType(), actualPrice.getPriceType());
        Assert.assertEquals(priceResponse.getPriceTypeText(), actualPrice.getPriceTypeText());
        Assert.assertEquals(priceResponse.isOnSale(), actualPrice.getOnSale());
        Assert.assertEquals(priceResponse.getBasePriceType(), actualPrice.getBasePriceType());
        Assert.assertEquals(priceResponse.getSaleValue(), actualPrice.getSalesValue());
        Assert.assertEquals(priceResponse.getRetailPrice(), actualPrice.getRetailPrice());
        Assert.assertEquals(priceResponse.getOriginalPrice(), actualPrice.getOriginalPrice());
        Assert.assertEquals(priceResponse.getIntermediatePrice(), actualPrice.getIntermediateSalesValue());
        Assert.assertEquals(priceResponse.getOriginalPriceLabel(), actualPrice.getOriginalPriceLabel());
        Assert.assertEquals(priceResponse.getRetailPriceLabel(), actualPrice.getRetailPriceLabel());
        Assert.assertEquals(priceResponse.getPricingPolicy(), actualPrice.getPricingPolicy());
        Assert.assertEquals(priceResponse.getPricingPolicyText(), actualPrice.getPricingPolicyText());
        Assert.assertEquals(priceResponse.isUpcOnSale(), actualPrice.getUpcOnSale());
    }

    @Test
    public void testPriceResponseCustomMapping() {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setPriceType(null);
        priceResponse.setPriceTypeText("PriceTypeText");
        priceResponse.setOnSale(null);
        priceResponse.setBasePriceType(null);
        priceResponse.setSaleValue(null);
        priceResponse.setRetailPrice(null);
        priceResponse.setOriginalPrice(null);
        priceResponse.setIntermediatePrice(null);
        priceResponse.setOriginalPriceLabel("OriginalPriceLabel");
        priceResponse.setRetailPriceLabel("RetailPriceLabel");
        priceResponse.setPricingPolicy("PricingPolicy");
        priceResponse.setPricingPolicyText("PricingPolicyText");
        priceResponse.setUpcOnSale(null);
        Price actualPrice = mapperFacade.map(priceResponse, Price.class);
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, actualPrice.getPriceType());
        Assert.assertEquals(priceResponse.getPriceTypeText(), actualPrice.getPriceTypeText());
        Assert.assertEquals(false, actualPrice.getOnSale());
        Assert.assertEquals(NumberUtils.INTEGER_ZERO, actualPrice.getBasePriceType());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, actualPrice.getSalesValue());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, actualPrice.getRetailPrice());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, actualPrice.getOriginalPrice());
        Assert.assertEquals(NumberUtils.DOUBLE_ZERO, actualPrice.getIntermediateSalesValue());
        Assert.assertEquals(priceResponse.getOriginalPriceLabel(), actualPrice.getOriginalPriceLabel());
        Assert.assertEquals(priceResponse.getRetailPriceLabel(), actualPrice.getRetailPriceLabel());
        Assert.assertEquals(priceResponse.getPricingPolicy(), actualPrice.getPricingPolicy());
        Assert.assertEquals(priceResponse.getPricingPolicyText(), actualPrice.getPricingPolicyText());
        Assert.assertEquals(false, actualPrice.getUpcOnSale());
    }

    @Test
    public void testFinalPriceMapping() {
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
        FinalPrice actualFinalPrice = mapperFacade.map(finalPriceResponse, FinalPrice.class);
        Assert.assertEquals(finalPriceResponse.getDisplayFinalPrice(), actualFinalPrice.getDisplayFinalPrice());
        Assert.assertEquals(finalPriceResponse.getFinalPrice(), actualFinalPrice.getFinalPrice(), 0);
        Assert.assertEquals(finalPriceResponse.getFinalPriceHigh(), actualFinalPrice.getFinalPriceHigh(), 0);
        Assert.assertEquals(finalPriceResponse.getProductTypePromotion(), actualFinalPrice.getProductTypePromotion());
        Assert.assertTrue(CollectionUtils.isNotEmpty(actualFinalPrice.getPromotions()));
        Assert.assertEquals(1, actualFinalPrice.getPromotions().size());
        FinalPricePromotionDO actualPromotion = actualFinalPrice.getPromotions().get(0);
        Assert.assertEquals(promotion.isGlobal(), actualPromotion.isGlobal());
        Assert.assertEquals(promotion.getPromotionId(), actualPromotion.getPromotionId());
        Assert.assertEquals(promotion.getPromotionName(), actualPromotion.getPromotionName());
    }

    @Test
    public void testMappingCustomerListToWishlistsDTO() {
        CustomerList customerList = new CustomerList();

        WishList wishList = new WishList();
        wishList.setId(12l);
        wishList.setListGuid("testGuid");
        wishList.setName("listName");
        wishList.setListType("W");
        wishList.setDefaultList(true);
        wishList.setOnSaleNotify(true);
        wishList.setSearchable(true);
        wishList.setNumberOfItems(1);
        wishList.setShowPurchaseInfo(true);
        wishList.setCreatedDate(new GregorianCalendar(2018,2,12).getTime());
        wishList.setLastModified(new GregorianCalendar(2018,2,12).getTime());
        customerList.setWishlist(Arrays.asList(wishList));

        Item item = new Item();
        item.setId(34);
        item.setItemGuid("itemGuid");
        item.setQtyRequested(2);
        item.setRetailPriceWhenAdded(1230.);
        item.setUpc(new Upc());
        item.getUpc().setId(234);
        item.setProduct(new Product());
        item.getProduct().setId(56);
        item.setPriority("H");
        item.setAddedDate(new GregorianCalendar(2018,2,12).getTime());
        item.setLastModified(new GregorianCalendar(2018,2,12).getTime());
        wishList.setItems(Arrays.asList(item));

        WishListsDTO wishLists = mapperFacade.map(customerList, WishListsDTO.class);

        Assert.assertEquals(customerList.getWishlist().size(), wishLists.getLists().size());

        WishListDTO wishListDTO = wishLists.getLists().get(0);
        Assert.assertEquals(wishList.getId(), wishListDTO.getListId());
        Assert.assertEquals(wishList.getListGuid(), wishListDTO.getListGuid());
        Assert.assertEquals(wishList.getName(), wishListDTO.getName());
        Assert.assertEquals(wishList.getListType(), wishListDTO.getListType());
        Assert.assertEquals(wishList.getDefaultList(), wishListDTO.getDefaultList());
        Assert.assertEquals(wishList.getOnSaleNotify(), wishListDTO.getOnSaleNotify());
        Assert.assertEquals(wishList.getSearchable(), wishListDTO.getSearchable());
        Assert.assertEquals(wishList.getNumberOfItems(), wishListDTO.getNumberOfItems());
        Assert.assertEquals(wishList.getShowPurchaseInfo(), wishListDTO.getShowPurchaseInfo());
        Assert.assertEquals(wishList.getCreatedDate(), wishListDTO.getCreatedDate());
        Assert.assertEquals(wishList.getLastModified(), wishListDTO.getLastModified());

        ItemDTO itemDTO = wishListDTO.getItems().get(0);
        Assert.assertEquals(item.getId().intValue(), itemDTO.getItemId().intValue());
        Assert.assertEquals(item.getItemGuid(), itemDTO.getItemGuid());
        Assert.assertEquals(item.getQtyRequested(), itemDTO.getQtyRequested());
        Assert.assertEquals(item.getRetailPriceWhenAdded(), itemDTO.getRetailPriceWhenAdded());
        Assert.assertEquals(item.getUpc().getId(), itemDTO.getUpcId());
        Assert.assertEquals(item.getProduct().getId(), itemDTO.getProductId());
        Assert.assertEquals(item.getPriority(), itemDTO.getPriority());
        Assert.assertEquals(item.getAddedDate(), itemDTO.getAddedDate());
        Assert.assertEquals(item.getLastModified(), itemDTO.getLastModified());
    }

    @Test
    public void testEmailShareToEmailShareDTO() {
        EmailShare emailShare = new EmailShare();
        emailShare.setFrom("testFrom");
        emailShare.setTo("testTo");
        emailShare.setMessage("testMessage");
        emailShare.setLink("testLink");
        emailShare.setFirstName("testFirstName");
        emailShare.setLastName("testLastName");

        EmailShareDTO emailShareDTO = mapperFacade.map(emailShare, EmailShareDTO.class);

        Assert.assertNotNull(emailShareDTO);
        Assert.assertEquals("testFrom", emailShareDTO.getFrom());
        Assert.assertEquals("testTo", emailShareDTO.getTo());
        Assert.assertEquals("testMessage", emailShareDTO.getMessage());
        Assert.assertEquals("testLink", emailShareDTO.getLink());
        Assert.assertEquals("testFirstName", emailShareDTO.getFirstName());
        Assert.assertEquals("testLastName", emailShareDTO.getLastName());
    }

    @Test
    public void testItemToEmailItemDTO() {
        Item item = new Item();
        item.setItemGuid("testGuid");
        item.setUpc(new Upc());
        item.getUpc().setUpcNumber(123456L);
        item.setProduct(new Product());
        item.getProduct().setId(123);
        item.getProduct().setName("testProductName");
        item.getProduct().setImageURL("testImageURL");

        EmailItemDTO emailShareDTO = mapperFacade.map(item, EmailItemDTO.class);

        Assert.assertNotNull(emailShareDTO);
        Assert.assertEquals("testGuid", emailShareDTO.getItemGuid());
        Assert.assertEquals(123456L, emailShareDTO.getUpcNumber().longValue());
        Assert.assertEquals(123, emailShareDTO.getProductId().intValue());
        Assert.assertEquals("testProductName", emailShareDTO.getProductName());
        Assert.assertEquals("testImageURL", emailShareDTO.getImageUrl());
    }

    @Test
    public void testItemToEmailItemDTOWithoutProductAndUpc() {
        Item item = new Item();
        item.setItemGuid("testGuid");

        EmailItemDTO emailShareDTO = mapperFacade.map(item, EmailItemDTO.class);

        Assert.assertNotNull(emailShareDTO);
        Assert.assertEquals("testGuid", emailShareDTO.getItemGuid());
        Assert.assertNull(emailShareDTO.getUpcNumber());
        Assert.assertNull(emailShareDTO.getProductId());
        Assert.assertNull(emailShareDTO.getProductName());
        Assert.assertNull(emailShareDTO.getImageUrl());
    }
}
