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
public class FavoriteListTest extends AbstractTestNGSpringContextTests {

    private static final String FAV_JSON_FILE = "com/macys/selection/xapi/list/rest/response/favList.json";
    private static final String TEST_LIST_GUID = "164e9807-a337-4d6c-afa3-aa4ce08f6113";
    private static final int TEST_PROD_ID = 22805;


    @Autowired
    private JacksonTester<FavoriteList> json;

    private FavoriteList fav;


    @BeforeMethod
    public void setup() {
        fav = new FavoriteList();
        fav.setListGuid(TEST_LIST_GUID);
        List<FavoriteProduct> products = new ArrayList<>();
        
        FavoriteProduct product = new FavoriteProduct();
        product.setPid(TEST_PROD_ID);
        products.add(product);

        fav.setProducts(products);


    }

    @Test
    public void favoriteListSerializeTest() throws ParseException, IOException {
        assertThat(this.json.write(fav)).isEqualToJson("favList.json");
    }

    @Test
    public void favoriteListDeserializeTest() throws ParseException, IOException {
        String favJson = TestUtils.readFile(FAV_JSON_FILE);
        assertThat(this.json.parse(favJson)).isEqualTo(fav);
        assertThat(this.json.parse(favJson)).isEqualToComparingFieldByField(fav);
    }

    @Test
    public void favoriteListEquaslsTest() {
        assertThat(fav.equals(null)).isFalse();
        assertThat(fav.equals(fav)).isTrue();
    }

    @Test
    public void favoriteListHashCodeTest() throws IOException {
        String favJson = TestUtils.readFile(FAV_JSON_FILE);
        FavoriteList fav = this.json.parseObject(favJson);
        assertThat(fav.hashCode()).isNotNull();
    }

    @Test
    public void favoriteListToStringTest() throws IOException {
        String favJson = TestUtils.readFile(FAV_JSON_FILE);
        FavoriteList fav = this.json.parseObject(favJson);
        assertThat(fav.toString()).isNotNull();
    }

}
