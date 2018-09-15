package com.macys.selection.xapi.list.exception;

import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.text.ParseException;

import static org.assertj.core.api.Assertions.assertThat;

;

/**
 * Created by Narasim Bayanaboina on 1/18/18.
 */
@SpringBootTest
@JsonTest
public class WishListErrorTest extends AbstractTestNGSpringContextTests {


    private WishListError wishlistError;

    public static WishListError expectedWishListError() throws ParseException {
        WishListError list = new WishListError();
        list.setClientId("list-xAPI");
        list.setErrorCode("10101");
        list.setMessage("Invalid User ID");
        list.setRequestId("c9b0c251-dd74-484e-944c-f4c0e00017a6");

        return list;

    }

    @BeforeMethod
    public void setup() throws ParseException {
        wishlistError = expectedWishListError();
    }

    @Test
    public void clientIdEquaslsTest() {

        assertThat(wishlistError.getClientId().equals("list-xAPI")).isTrue();

    }

    @Test
    public void errorCodeEquaslsTest() {

        assertThat(wishlistError.getErrorCode().equals("10101")).isTrue();
    }

    @Test
    public void messageEquaslsTest() {

        assertThat(wishlistError.getMessage().equals("Invalid User ID")).isTrue();
    }

    @Test
    public void requestIdEquaslsTest() {

        assertThat(wishlistError.getRequestId().equals("c9b0c251-dd74-484e-944c-f4c0e00017a6")).isTrue();
    }

}

