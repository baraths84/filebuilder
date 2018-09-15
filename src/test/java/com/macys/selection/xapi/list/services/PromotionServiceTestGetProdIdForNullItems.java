package com.macys.selection.xapi.list.services;

import static org.testng.Assert.assertTrue;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.WishList;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes = {JsonResponseParserPromotions.class, JsonToPromotionConverter.class})
public class PromotionServiceTestGetProdIdForNullItems extends AbstractTestNGSpringContextTests {

    @Mock
    private JsonResponseParserPromotions promotions;

    @Mock
    private JsonToPromotionConverter promotionsConverter;

    @Mock
    private PromotionsRestClient promotionsRestClient;

    @InjectMocks
    private PromotionService promotionService;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testWithNullUpc() {
        WishList wishList = new WishList();

        Item item = new Item();

        List<Item> items = new ArrayList<>();
        items.add(item);
        wishList.setItems(items);

        List<Integer> ids = promotionService.getProductIds(wishList);
        assertTrue(ids.size() == 0);
    }

    @Test
    public void testWithNullProduct() {
        WishList wishList = new WishList();

        Item item = new Item();
        Upc upc = new Upc();
        item.setUpc(upc);

        List<Item> items = new ArrayList<>();
        items.add(item);
        wishList.setItems(items);

        List<Integer> ids = promotionService.getProductIds(wishList);
        assertTrue(ids.size() == 0);
    }

}
