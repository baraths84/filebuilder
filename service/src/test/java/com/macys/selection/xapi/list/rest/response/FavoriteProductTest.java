package com.macys.selection.xapi.list.rest.response;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.macys.selection.xapi.list.data.converters.TestUtils;

@SpringBootTest
@JsonTest
public class FavoriteProductTest extends AbstractTestNGSpringContextTests {
	
	  @Autowired private JacksonTester <FavoriteProduct> json; 
	  
	  private FavoriteProduct product;
	  
	  private static final int pid = 22805;
	  
	  private static String FILE = "com/macys/selection/xapi/list/rest/response/favoriteProduct.json";
	  
	  @BeforeMethod
	  public void setup() {
		  product = new FavoriteProduct();
		  product.setPid(pid);
	  }

	  @Test
	  public void productSerializeTest() throws IOException {
	    assertThat(this.json.write(product)).isEqualToJson("favoriteProduct.json");
	  }
	  
	  @Test
	  public void productDeserializeTest() throws IOException {
	    String productJson = TestUtils.readFile(FILE);
	    assertThat(this.json.parse(productJson)).isEqualTo(product);
	  }
	  
	  @Test
	  public void productEquaslsTest() {
	    assertThat(product.equals(null)).isFalse();
	    assertThat(product.equals(product)).isTrue();    
	  }
	  
	  @Test
	  public void productHashCodeTest() throws IOException {
	    String productJson = TestUtils.readFile(FILE);
	    FavoriteProduct productTemp = this.json.parseObject(productJson);  
	    assertThat(productTemp.hashCode()).isNotNull();    
	  }

	  @Test
	  public void productToStringTest() throws IOException {
	    String productJson = TestUtils.readFile(FILE);
	    FavoriteProduct productTemp = this.json.parseObject(productJson);  
	    assertThat(productTemp.toString()).isNotNull();    
	  }  
}
