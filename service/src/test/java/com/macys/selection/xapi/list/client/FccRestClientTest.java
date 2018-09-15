package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxClient;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxInvocationBuilder;
import com.macys.platform.rest.framework.client.api.rx.hystrix.RxWebTarget;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.platform.rest.framework.fault.v2.entity.PlatformErrorBinding;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.client.response.fcc.ProductSetResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcSetResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FccRestClientTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(FccRestClientTest.class);
    private static final String HOST_NAME = "http://localhost:8080";
    private static final String BASE_PATH = "/api/catalog/v2";

    private static String GET_CLIENT_EXP = "exception while getting clinet:";
    private static final Integer UPC_ID_1 = 12345678;
    private static final Integer UPC_ID_2 = 87654321;

    private static final Long UPC_NUMBER_1 = 123456789L;
    private static final Long UPC_NUMBER_2 = 987654321L;

    private static final Integer PRODUCT_ID_1 = 2345;
    private static final Integer PRODUCT_ID_2 = 6541;

    private static final String FIELD_1 = "f1";
    private static final String FIELD_2 = "f2";
    private static final String FIELDS = "f1,f2";

    private FccRestClient fccRestClient;

    @Mock
    private RestClientFactory.JaxRSClientPool upcClientPool;

    @Mock
    private RestClientFactory.JaxRSClientPool productClientPool;

    @Mock
    private RxClient rxClient;

    @Mock
    private RxWebTarget rxWebTarget;

    @Mock
    private RxInvocationBuilder invocationBuilder;

    @Mock
    private KillSwitchPropertiesBean killSwitchPropertiesBean;


    @Before
    public void init() {
        Map<String, List<String>> upcFieldsQuery = new LinkedHashMap<>();
        upcFieldsQuery.put(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, Arrays.asList(FIELD_1, FIELD_2));
        List<String> productFieldsQuery = Arrays.asList(FIELD_1, FIELD_2);
        fccRestClient = Mockito.spy(new FccRestClient(upcClientPool, upcFieldsQuery, productClientPool, productFieldsQuery, killSwitchPropertiesBean));

        try {
            when(upcClientPool.getRxClient(anyString())).thenReturn(rxClient);
            when(productClientPool.getRxClient(anyString())).thenReturn(rxClient);
        } catch (RestClientException e) {
            LOGGER.error(GET_CLIENT_EXP, e);
        }

        when(upcClientPool.getHostName()).thenReturn(HOST_NAME);
        when(upcClientPool.getBasePath()).thenReturn(BASE_PATH);

        when(productClientPool.getHostName()).thenReturn(HOST_NAME);
        when(productClientPool.getBasePath()).thenReturn(BASE_PATH);

        when(rxWebTarget.resolveTemplate(anyString(), anyString())).thenReturn(rxWebTarget);

        when(rxClient.target(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.path(anyString())).thenReturn(rxWebTarget);
        when(rxWebTarget.queryParam(anyString(), anyString())).thenReturn(rxWebTarget);
        when(killSwitchPropertiesBean.isFinalPriceDisplayEnabled()).thenReturn(true);

    }

    @Test
    public void shouldGetMultipleUpcsByIds() {
        List<Integer> upcIds = Arrays.asList(UPC_ID_1, UPC_ID_2);
        UpcSetResponse upcSet = prepareUpcTestData(UPC_ID_1, UPC_ID_2);

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(UpcResponse.class)).thenReturn(upcSet.getUpcs().get(0));
        when(response.readEntity(UpcSetResponse.class)).thenReturn(upcSet);

        List<UpcResponse> upcs = null;
        try {
            upcs = fccRestClient.getUpcsByIds(upcIds);
        } catch (RestClientException e) {
            Assert.fail();
        }
        Mockito.verify(response, Mockito.times(0)).readEntity(UpcResponse.class);
        Mockito.verify(response, Mockito.times(1)).readEntity(UpcSetResponse.class);
        Mockito.verify(rxWebTarget, Mockito.times(1)).resolveTemplate(WishlistConstants.UPCS_IDS, UPC_ID_1 + "," + UPC_ID_2);
        Mockito.verify(rxWebTarget, Mockito.times(1)).queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, FIELDS);
        Assert.assertNotNull(upcs);
        Assert.assertEquals(2, upcs.size());
        Assert.assertTrue(upcs.stream().anyMatch(u -> u.getId().equals(UPC_ID_1)));
        Assert.assertTrue(upcs.stream().anyMatch(u -> u.getId().equals(UPC_ID_2)));
    }

    @Test
    public void shouldGetSingleUpcById() {
        List<Integer> upcIds = Collections.singletonList(UPC_ID_1);
        UpcSetResponse upcSet = prepareUpcTestData(UPC_ID_1);

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(UpcResponse.class)).thenReturn(upcSet.getUpcs().get(0));
        when(response.readEntity(UpcSetResponse.class)).thenReturn(upcSet);

        List<UpcResponse> upcs = null;
        try {
            upcs = fccRestClient.getUpcsByIds(upcIds);
        } catch (RestClientException e) {
            Assert.fail();
        }
        Mockito.verify(response, Mockito.times(1)).readEntity(UpcResponse.class);
        Mockito.verify(response, Mockito.times(0)).readEntity(UpcSetResponse.class);
        Mockito.verify(rxWebTarget, Mockito.times(1)).resolveTemplate(WishlistConstants.UPCS_IDS, UPC_ID_1.toString());
        Mockito.verify(rxWebTarget, Mockito.times(1)).queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, FIELDS);
        Assert.assertNotNull(upcs);
        Assert.assertEquals(1, upcs.size());
        Assert.assertEquals(UPC_ID_1, upcs.get(0).getId());
    }

    @Test(expected = ListServiceException.class)
    public void shouldFailGetUpcsByIdsOnBadResponseStatus() {
        List<Integer> upcIds = Arrays.asList(UPC_ID_1, UPC_ID_2);

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());

        try {
            fccRestClient.getUpcsByIds(upcIds);
        } catch (RestClientException e) {
            Assert.fail();
        }
    }

    @Test(expected = ListServiceException.class)
    public void shouldFailWhenUpcsNotFoundByIds() {
        List<Integer> upcIds = Arrays.asList(UPC_ID_1, UPC_ID_2);
        UpcSetResponse upcSet = new UpcSetResponse();
        upcSet.setUpcs(Collections.emptyList());

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        try {
            fccRestClient.getUpcsByIds(upcIds);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        } catch (RestClientException e) {
            Assert.fail();
        }
    }

    @Test
    public void shouldGetMultipleUpcsByNumbers() {
        List<Long> upcNumbers = Arrays.asList(UPC_NUMBER_1, UPC_NUMBER_2);
        UpcSetResponse upcSet = prepareUpcTestData(UPC_NUMBER_1, UPC_NUMBER_2);

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(UpcResponse.class)).thenReturn(upcSet.getUpcs().get(0));
        when(response.readEntity(UpcSetResponse.class)).thenReturn(upcSet);

        List<UpcResponse> upcs = null;
        try {
            upcs = fccRestClient.getUpcsByUpcNumbers(upcNumbers);
        } catch (RestClientException e) {
            Assert.fail();
        }
        Mockito.verify(response, Mockito.times(0)).readEntity(UpcResponse.class);
        Mockito.verify(response, Mockito.times(1)).readEntity(UpcSetResponse.class);
        Mockito.verify(rxWebTarget, Mockito.times(1)).queryParam(WishlistConstants.UPCS_NUMBERS, UPC_NUMBER_1 + "," + UPC_NUMBER_2);
        Mockito.verify(rxWebTarget, Mockito.times(1)).queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, FIELDS);
        Assert.assertNotNull(upcs);
        Assert.assertEquals(2, upcs.size());
        Assert.assertTrue(upcs.stream().anyMatch(u -> u.getUpc().equals(UPC_NUMBER_1)));
        Assert.assertTrue(upcs.stream().anyMatch(u -> u.getUpc().equals(UPC_NUMBER_2)));
    }

    @Test
    public void shouldGetSingleUpcByNumber() {
        List<Long> upcNumbers = Collections.singletonList(UPC_NUMBER_1);
        UpcSetResponse upcSet = prepareUpcTestData(UPC_NUMBER_1);

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(UpcResponse.class)).thenReturn(upcSet.getUpcs().get(0));
        when(response.readEntity(UpcSetResponse.class)).thenReturn(upcSet);

        List<UpcResponse> upcs = null;
        try {
            upcs = fccRestClient.getUpcsByUpcNumbers(upcNumbers);
        } catch (RestClientException e) {
            Assert.fail();
        }
        Mockito.verify(response, Mockito.times(1)).readEntity(UpcResponse.class);
        Mockito.verify(response, Mockito.times(0)).readEntity(UpcSetResponse.class);
        Mockito.verify(rxWebTarget, Mockito.times(1)).queryParam(WishlistConstants.UPCS_NUMBERS, UPC_NUMBER_1.toString());
        Mockito.verify(rxWebTarget, Mockito.times(1)).queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, FIELDS);
        Assert.assertNotNull(upcs);
        Assert.assertEquals(1, upcs.size());
        Assert.assertEquals(UPC_NUMBER_1, upcs.get(0).getUpc());
    }

    @Test(expected = ListServiceException.class)
    public void shouldFailGetUpcsByNumbersOnBadResponseStatus() {
        List<Long> upcNumbers = Collections.singletonList(UPC_NUMBER_1);

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());

        try {
            fccRestClient.getUpcsByUpcNumbers(upcNumbers);
        } catch (RestClientException e) {
            Assert.fail();
        }
    }

    @Test(expected = ListServiceException.class)
    public void shouldFailWhenUpcsNotFoundByNumbers() {
        List<Long> upcNumbers = Arrays.asList(UPC_NUMBER_1, UPC_NUMBER_2);
        UpcSetResponse upcSet = new UpcSetResponse();
        upcSet.setUpcs(Collections.emptyList());

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        try {
            fccRestClient.getUpcsByUpcNumbers(upcNumbers);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        } catch (RestClientException e) {
            Assert.fail();
        }
    }

    @Test
    public void testGetMultipleProductsByIds() {
        List<Integer> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);

        ProductSetResponse productSet = new ProductSetResponse();
        ProductResponse product1 = new ProductResponse();
        product1.setId(PRODUCT_ID_1);
        ProductResponse product2 = new ProductResponse();
        product2.setId(PRODUCT_ID_2);
        productSet.setProduct(Arrays.asList(product1, product2));

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(ProductSetResponse.class)).thenReturn(productSet);

        ProductSetResponse productSetResponses = null;
        try {
            productSetResponses = fccRestClient.getProductsByProdIds(productIds);
        } catch (RestClientException e) {
            Assert.fail();
        }

        verify(response, never()).readEntity(ProductResponse.class);
        verify(response).readEntity(ProductSetResponse.class);
        verify(rxWebTarget).resolveTemplate(WishlistConstants.PRODUCT_IDS, PRODUCT_ID_1 + "," + PRODUCT_ID_2);
        verify(rxWebTarget).queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, FIELD_1 + "," + FIELD_2);
        verify(rxWebTarget).queryParam(WishlistConstants.INCLUDE_FINAL_PRICE, true);

        verify(fccRestClient).buildProductSetFromResponse(anyInt(), any(Response.class));
        verify(fccRestClient, never()).buildProductSet(any(ProductResponse.class));

        Assert.assertNotNull(productSetResponses);
        Assert.assertNotNull(productSetResponses.getProduct());
        Assert.assertEquals(2, productSetResponses.getProduct().size());
        Assert.assertNotNull(getProductByIdFromSet(productSetResponses, PRODUCT_ID_1));
        Assert.assertNotNull(getProductByIdFromSet(productSetResponses, PRODUCT_ID_2));

    }

    @Test
    public void testGetSingleProductById() {
        List<Integer> productIds = Collections.singletonList(PRODUCT_ID_1);

        ProductSetResponse productSet = new ProductSetResponse();
        ProductResponse product1 = new ProductResponse();
        product1.setId(PRODUCT_ID_1);
        productSet.setProduct(Collections.singletonList(product1));

        Response response = Mockito.mock(Response.class);
        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());
        when(response.readEntity(ProductResponse.class)).thenReturn(product1);

        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);

        ProductSetResponse productSetResponses = null;
        try {
            productSetResponses = fccRestClient.getProductsByProdIds(productIds);
        } catch (RestClientException e) {
            Assert.fail();
        }
        verify(response).readEntity(ProductResponse.class);
        verify(response, never()).readEntity(ProductSetResponse.class);

        verify(rxWebTarget).resolveTemplate(WishlistConstants.PRODUCT_IDS, PRODUCT_ID_1.toString());
        verify(rxWebTarget).queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, FIELD_1 + "," + FIELD_2);
        verify(rxWebTarget).queryParam(WishlistConstants.INCLUDE_FINAL_PRICE, true);

        verify(fccRestClient).buildProductSetFromResponse(anyInt(), any(Response.class));
        verify(fccRestClient).buildProductSet(any(ProductResponse.class));

        Assert.assertNotNull(productSetResponses);
        Assert.assertNotNull(productSetResponses.getProduct());
        Assert.assertEquals(1, productSetResponses.getProduct().size());
        Assert.assertNotNull(getProductByIdFromSet(productSetResponses, PRODUCT_ID_1));

    }

    @Test(expected = ListServiceException.class)
    public void testFailGetProductsByIdsOnBadResponseStatus() {
        List<Integer> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);
        when(response.getStatus()).thenReturn(Response.Status.BAD_REQUEST.getStatusCode());

        when(response.readEntity(PlatformErrorBinding.class)).thenReturn(new PlatformErrorBinding());

        try {
            fccRestClient.getProductsByProdIds(productIds);
        } catch (RestClientException e) {
            Assert.fail();
        }
    }

    @Test(expected = ListServiceException.class)
    public void shouldFailWhenProductsNotFoundByIds() {
        List<Integer> productIds = Arrays.asList(PRODUCT_ID_1, PRODUCT_ID_2);
        ProductSetResponse productSet = new ProductSetResponse();
        productSet.setProduct(Collections.emptyList());

        Response response = Mockito.mock(Response.class);
        when(rxWebTarget.request(MediaType.APPLICATION_JSON_TYPE)).thenReturn(invocationBuilder);
        when(invocationBuilder.get(Response.class)).thenReturn(response);

        when(response.getStatus()).thenReturn(Response.Status.OK.getStatusCode());

        try {
            fccRestClient.getProductsByProdIds(productIds);
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        } catch (RestClientException e) {
            Assert.fail();
        }
    }

    private ProductResponse getProductByIdFromSet(ProductSetResponse response, Integer id) {
        return response.getProduct().stream().filter(p -> p.getId().equals(id)).findFirst().get();
    }

    private UpcSetResponse prepareUpcTestData(Integer... upcIds) {
        UpcSetResponse upcSet = new UpcSetResponse();
        List<UpcResponse> upcs = new ArrayList<>(upcIds.length);
        for (Integer id : upcIds) {
            UpcResponse upc = new UpcResponse();
            upc.setId(id);
            upcs.add(upc);
        }
        upcSet.setUpcs(upcs);
        return upcSet;
    }

    private UpcSetResponse prepareUpcTestData(Long... upcNumbers) {
        UpcSetResponse upcSet = new UpcSetResponse();
        List<UpcResponse> upcs = new ArrayList<>(upcNumbers.length);
        for (Long number : upcNumbers) {
            UpcResponse upc = new UpcResponse();
            upc.setUpc(number);
            upcs.add(upc);
        }
        upcSet.setUpcs(upcs);
        return upcSet;
    }
}
