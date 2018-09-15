package com.macys.selection.xapi.list.services;

import static org.testng.Assert.assertTrue;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserPromotions;
import com.macys.selection.xapi.list.data.converters.JsonToPromotionConverter;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Product;
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
public class PromotionServiceTestGetProdIdForMultipleItems extends AbstractTestNGSpringContextTests {

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
    public void testGetMultipleProductIds() {
        WishList wishList = new WishList();

        Item item = new Item();
        Upc upc = new Upc();
        Product product = new Product();
        product.setId(100);
        upc.setProduct(product);
        item.setUpc(upc);

        Item item2 = new Item();
        Upc upc2 = new Upc();
        Product product2 = new Product();
        product2.setId(200);
        upc2.setProduct(product2);
        item2.setUpc(upc2);

        List<Item> items = new ArrayList<>();
        items.add(item);
        items.add(item2);
        wishList.setItems(items);

        List<Integer> ids = promotionService.getProductIds(wishList);

        assertTrue(ids.size() == 2);
        assertTrue(ids.contains(100));
        assertTrue(ids.contains(200));
    }
}
