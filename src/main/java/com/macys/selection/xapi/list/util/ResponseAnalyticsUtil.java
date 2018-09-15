package com.macys.selection.xapi.list.util;

import com.macys.selection.xapi.list.rest.response.*;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Optional;

import static com.macys.selection.xapi.list.common.WishlistConstants.*;

public class ResponseAnalyticsUtil {

    public static AnalyticsMeta buildResponseAnalyticsMeta(CustomerList customerList) {
        AnalyticsMeta analyticsMeta = new AnalyticsMeta();
        Analytics analytics = new Analytics();
        analytics.setResponseDigitalAnalytics(getDataJSON(customerList));
        analyticsMeta.setAnalytics(analytics);
        return analyticsMeta;
    }

    private static ResponseDigitalAnalytics getDataJSON(CustomerList customerList) {
        ResponseDigitalAnalytics responseDigitalAnalytics = new ResponseDigitalAnalytics();

        responseDigitalAnalytics.setEventName(WISHLIST_ADD);

        responseDigitalAnalytics.setProductId(Collections.singletonList(getProductId(customerList)));

        responseDigitalAnalytics.setProductName(Collections.singletonList(getProductName(customerList)));

        responseDigitalAnalytics.setProductOriginalPrice(Collections.singletonList(getProductOriginalPrice(customerList)));

        responseDigitalAnalytics.setProductPrice(Collections.singletonList(getProductPrice(customerList)));

        responseDigitalAnalytics.setProductSize(Collections.singletonList(getProductSize(customerList)));

        responseDigitalAnalytics.setProductColor(Collections.singletonList(getProductColor(customerList)));

        responseDigitalAnalytics.setProductUPC(Collections.singletonList(getProductUpcNumber(customerList)));

        return responseDigitalAnalytics;
    }

    private static Optional<Upc> getItemUpc(CustomerList customerList) {
        return Optional.ofNullable(customerList)
                       .map(CustomerList::getWishlist)
                       .flatMap(wishList -> Optional.of(wishList)
                                                    .orElseGet(Collections::emptyList)
                                                    .stream()
                                                    .findFirst()
                                                    .map(WishList::getItems)
                                                    .flatMap(items -> Optional.of(items)
                                                                              .orElseGet(Collections::emptyList)
                                                                              .stream()
                                                                              .filter(value -> Optional.ofNullable(value)
                                                                                      .map(Item::getUpc)
                                                                                      .map(Upc::getUpcNumber)
                                                                                      .isPresent()
                                                                              )
                                                                              .findFirst()
                                                                              .map(Item::getUpc)
                                                    )
                       );

    }

    private static String getProductId(CustomerList customerList) {
        return getItemUpc(customerList)
                .map(Upc::getProduct)
                .map(Product::getId)
                .map(Object::toString)
                .orElse(EMPTY_STRING);
    }

    private static String getProductName(CustomerList customerList) {
        return getItemUpc(customerList)
                .map(Upc::getProduct)
                .map(Product::getName)
                .orElse(EMPTY_STRING);

    }
    private static String getProductOriginalPrice(CustomerList customerList) {
        return getItemUpc(customerList)
                .map(Upc::getPrice)
                .map(Price::getOriginalPrice)
                .map(value -> new DecimalFormat(DECIMAL_FORMAT).format(value))
                .map(formattedValue -> formattedValue.replace(COMMA, EMPTY_STRING))
                .orElse(EMPTY_STRING);
    }

    private static String getProductPrice(CustomerList customerList) {
        return getItemUpc(customerList)
                .map(Upc::getPrice)
                .map(Price::getRetailPrice)
                .map(value -> new DecimalFormat(DECIMAL_FORMAT).format(value))
                .map(formattedValue -> formattedValue.replace(COMMA, EMPTY_STRING))
                .orElse(EMPTY_STRING);
    }

    private static String getProductSize(CustomerList customerList) {
        return getItemUpc(customerList)
                .map(Upc::getSize)
                .orElse(EMPTY_STRING);

    }

    private static String getProductColor(CustomerList customerList) {
        return getItemUpc(customerList)
                .map(Upc::getColor)
                .orElse(EMPTY_STRING);

    }

    private static String getProductUpcNumber(CustomerList customerList) {
        return getItemUpc(customerList)
                .map(Upc::getUpcNumber)
                .map(Object::toString)
                .orElse(EMPTY_STRING);

    }
}
