package com.macys.selection.xapi.list.rest.response;

import static org.assertj.core.api.Assertions.assertThat;
import java.io.IOException;
import java.text.ParseException;
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
public class UserTest extends AbstractTestNGSpringContextTests {

  @Autowired private JacksonTester <User> json; 

  private User user;
  
  @BeforeMethod
  public void setup() {
    user = new User();
    user.setGuestUser(Boolean.TRUE);
    user.setGuid("3c4b69fc-5cb9-42b8-bbed-1611f81ec252");
    user.setId(2158763568L);
    
    Profile profile = new Profile();
    profile.setFirstName("guest");
    user.setProfile(profile);
  }
  
  @Test
  public void userSerializeTest() throws ParseException, IOException {
    assertThat(this.json.write(user)).isEqualToJson("user.json");
  }  
  
  @Test
  public void userDeserializeTest() throws ParseException, IOException {
    String userJson = TestUtils.readFile("com/macys/selection/xapi/list/rest/response/user.json");
    assertThat(this.json.parse(userJson)).isEqualTo(user);
  }

  @Test
  public void userEquaslsTest() {
    assertThat(user.equals(null)).isFalse();
    assertThat(user.equals(user)).isTrue();    
  }
  
  @Test
  public void userHashCodeTest() throws IOException {
    String userJson = TestUtils.readFile("com/macys/selection/xapi/list/rest/response/user.json");
    User user = this.json.parseObject(userJson);  
    assertThat(user.hashCode()).isNotNull();    
  }

  @Test
  public void userToStringTest() throws IOException {
    String userJson = TestUtils.readFile("com/macys/selection/xapi/list/rest/response/user.json");
    User user = this.json.parseObject(userJson);  
    assertThat(user.toString()).isNotNull();    
  }  


}
