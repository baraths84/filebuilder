package com.macys.selection.xapi.list.client.request.converter;

import com.macys.selection.xapi.list.client.request.MergeListRequest;
import com.macys.selection.xapi.list.client.response.EmailItemDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.TestUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class ListRequestConverterTest {


    private DateFormat dateFormat;

    public ListRequestConverterTest() {
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    @Test
    public void convertTest() throws IOException, ParseException {
        WishListDTO wishList = getList();
        String testJsonString = ListRequestConverter.convert(wishList);
        String expectedJsonString = TestUtils.readFile("com/macys/selection/xapi/list/client/request/converter/request_wishlist.json");
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void convertNullableList() throws IOException {
        WishListDTO wishlist = null;
        String testJsonString = ListRequestConverter.convert(wishlist);
        String expectedJsonString = "{\"list\":{}}";
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void convertEmptyList() throws IOException {
        WishListDTO wishlist = new WishListDTO();
        String testJsonString = ListRequestConverter.convert(wishlist);
        String expectedJsonString = "{\"list\":{}}";
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void convertNullableItem() throws IOException {
        ItemDTO item = null;
        String testJsonString = ListRequestConverter.convert(item);
        String expectedJsonString = "{\"item\":{}}";
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void convertEmptyItem() throws IOException {
        ItemDTO item = new ItemDTO();
        String testJsonString = ListRequestConverter.convert(item);
        String expectedJsonString = "{\"item\":{}}";
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void convertItem() throws IOException, ParseException {
        ItemDTO item = getItem();
        String testJsonString = ListRequestConverter.convert(item);
        String expectedJsonString = TestUtils.readFile("com/macys/selection/xapi/list/client/request/converter/request_wishlist_item.json");
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void convertListItem() throws IOException, ParseException {
        List<ItemDTO> items = Arrays.asList(getItem(), getItem());
        String testJsonString = ListRequestConverter.convert(items);
        String expectedJsonString = TestUtils.readFile("com/macys/selection/xapi/list/client/request/converter/request_list_items.json");
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void convertEmptyListItem() throws IOException {
        List<ItemDTO> items = new ArrayList<>();
        String testJsonString = ListRequestConverter.convert(items);
        assertNotNull(testJsonString);
        assertEquals("[]", testJsonString);
    }

    @Test
    public void convertNullableListItem() throws IOException {
        List<ItemDTO> items = null;
        String testJsonString = ListRequestConverter.convert(items);
        assertNotNull(testJsonString);
        assertEquals("[]", testJsonString);
    }

    @Test
    public void testConvertEmailShareDTO() throws IOException {
        EmailShareDTO emailShareDTO = new EmailShareDTO();
        emailShareDTO.setFrom("testFrom");
        emailShareDTO.setTo("testTo");
        emailShareDTO.setMessage("testMessage");
        emailShareDTO.setLink("testLink");
        emailShareDTO.setFirstName("testFirstName");
        emailShareDTO.setLastName("testLastName");
        EmailItemDTO item = new EmailItemDTO();
        item.setItemGuid("testItemGuid");
        item.setUpcNumber(123L);
        item.setProductId(321L);
        item.setProductName("testProductName");
        item.setImageUrl("testImageUrl");
        emailShareDTO.setItems(Arrays.asList(item));

        String expectedJsonString = TestUtils.readFile("com/macys/selection/xapi/list/client/request/converter/request_list_email_share.json");
        String testJsonString = ListRequestConverter.convert(emailShareDTO);
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void testConvertEmailShareWithoutItems() throws IOException {
        EmailShareDTO emailShareDTO = new EmailShareDTO();
        emailShareDTO.setFrom("testFrom");
        emailShareDTO.setTo("testTo");
        emailShareDTO.setMessage("testMessage");
        emailShareDTO.setLink("testLink");
        emailShareDTO.setFirstName("testFirstName");
        emailShareDTO.setLastName("testLastName");

        String expectedJsonString = TestUtils.readFile("com/macys/selection/xapi/list/client/request/converter/request_list_email_share_without_items.json");
        String testJsonString = ListRequestConverter.convert(emailShareDTO);
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void testConvertNullableEmailShareDTO() throws IOException {
        EmailShareDTO emailShareDTO = null;

        String testJsonString = ListRequestConverter.convert(emailShareDTO);
        assertNotNull(testJsonString);
        assertEquals("{\"EmailShare\":{}}", testJsonString);
    }

    @Test
    public void testConvertMergeListRequest() throws IOException {
        MergeListRequest mergeListRequest = new MergeListRequest();
        mergeListRequest.setSrcUserId(123L);
        mergeListRequest.setSrcGuestUser(true);
        mergeListRequest.setTargetUserId(321L);
        mergeListRequest.setTargetGuestUser(false);
        mergeListRequest.setTargetUserGuid("testGuid");
        mergeListRequest.setTargetUserFirstName("testFirstName");

        String expectedJsonString = TestUtils.readFile("com/macys/selection/xapi/list/client/request/converter/request_merge_list.json");
        String testJsonString = ListRequestConverter.convert(mergeListRequest);
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void testConvertEmptyMergeListRequest() throws IOException {
        MergeListRequest mergeListRequest = new MergeListRequest();
        String expectedJsonString = "{\"mergeList\":{}}";
        String testJsonString = ListRequestConverter.convert(mergeListRequest);
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    @Test
    public void testConvertNullableMergeListRequest() throws IOException {
        MergeListRequest mergeListRequest = new MergeListRequest();
        String expectedJsonString = "{\"mergeList\":{}}";
        String testJsonString = ListRequestConverter.convert(mergeListRequest);
        assertNotNull(testJsonString);
        assertEquals(expectedJsonString, testJsonString);
    }

    private WishListDTO getList() throws ParseException {

        WishListDTO wishList = new WishListDTO();
        wishList.setListId(12L);
        wishList.setListGuid("testwishlistguid");
        wishList.setName("testing wishlist name");
        wishList.setListType(WishlistConstants.WISH_LIST_TYPE_VALUE);
        wishList.setDefaultList(Boolean.TRUE);
        wishList.setUserGuid("testUserId");
        wishList.setUserId(14L);
        wishList.setOnSaleNotify(Boolean.FALSE);
        wishList.setSearchable(Boolean.FALSE);
        wishList.setNumberOfItems(1);
        wishList.setShowPurchaseInfo(Boolean.FALSE);
        wishList.setCreatedDate(dateFormat.parse("2018-02-10 00:00:00"));
        wishList.setLastModified(dateFormat.parse("2018-03-14 00:00:00"));

        wishList.setItems(Arrays.asList(getItem()));

        return wishList;
    }

    private ItemDTO getItem() throws ParseException {
        ItemDTO item = new ItemDTO();
        item.setItemId(11L);
        item.setItemGuid("testItemGuid");
        item.setAddedDate(dateFormat.parse("2018-02-10 00:00:00"));
        item.setLastModified(dateFormat.parse("2018-03-14 00:00:00"));
        item.setRetailPriceWhenAdded(1001d);
        item.setQtyRequested(2);
        item.setPriority("H");
        item.setUpcId(125);
        item.setProductId(128);
        return item;
    }

}
