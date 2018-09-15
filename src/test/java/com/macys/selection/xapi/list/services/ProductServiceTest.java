package com.macys.selection.xapi.list.services;

import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.client.FccRestClient;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.client.response.fcc.ProductSetResponse;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;

import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProductServiceTest {
    private static final Integer PRODUCT_ID = 1234;

    @Mock
    private ProductService productService;
    @Mock
    private FccRestClient fccRestClient;

    private ProductSetResponse productSet;

    @Before
    public void init() {

        MockitoAnnotations.initMocks(this);

        when(productService.getProductsByProdIds(any())).thenCallRealMethod();
        when(productService.getFccRestClient()).thenReturn(fccRestClient);

        productSet = new ProductSetResponse();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(PRODUCT_ID);

        productSet.setProduct(Arrays.asList(productResponse));

    }

    @Test
    public void testGetProductsByIds() {
        List<Integer> productIds = Collections.singletonList(PRODUCT_ID);


        try {
            when(fccRestClient.getProductsByProdIds(productIds)).thenReturn(this.productSet);
        } catch (RestClientException e) {
            Assert.fail();
        }
        List<ProductResponse> actualProducts = productService.getProductsByProdIds(productIds);
        Assert.assertEquals(productSet.getProduct(), actualProducts);
    }

    @Test(expected = ListServiceException.class)
    public void shouldWrapExceptionsFromGetUpcsByIds() {
        try {
            when(fccRestClient.getProductsByProdIds(any()))
                    .thenThrow(new RestClientException("exc message", new Exception()));
        } catch (RestClientException e) {
            Assert.fail();
        }
        try {
            productService.getProductsByProdIds(Collections.singletonList(PRODUCT_ID));
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testNullIdsOnGetUpcsByIds() {
        List<ProductResponse> actualProducts = productService.getProductsByProdIds(null);
        try {
            verify(fccRestClient, never()).getProductsByProdIds(any());
        } catch (RestClientException e) {
            Assert.fail();
        }
        Assert.assertNotNull(actualProducts);
        Assert.assertEquals(0, actualProducts.size());
    }

}
