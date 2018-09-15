package com.macys.selection.xapi.list.services;

import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.selection.xapi.list.client.FccRestClient;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.testng.Assert;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class UpcServiceTest {
    private static final Integer UPC_ID = 1;
    private static final Long UPC_NUMBER = 1L;

    private UpcService upcService;
    @Mock
    private FccRestClient upcRestClient;
    private List<UpcResponse> upcs;

    @Before
    public void init() {
        upcService = new UpcService(upcRestClient);
        UpcResponse upc = new UpcResponse();
        upc.setId(UPC_ID);
        upc.setUpc(UPC_NUMBER);
        upcs = Collections.singletonList(upc);
    }

    @Test
    public void shouldGetUpcsByIds() {
        List<Integer> upcIds = Collections.singletonList(UPC_ID);
        try {
            Mockito.when(upcRestClient.getUpcsByIds(upcIds)).thenReturn(upcs);
        } catch (RestClientException e) {
            Assert.fail();
        }
        List<UpcResponse> actualUpcs = upcService.getUpcsByUpcIds(upcIds);
        Assert.assertEquals(upcs, actualUpcs);
    }

    @Test(expected = ListServiceException.class)
    public void shouldWrapExceptionsFromGetUpcsByIds() {
        try {
            Mockito.when(upcRestClient.getUpcsByIds(Mockito.any())).thenThrow(new RestClientException("exc message", new Exception()));
        } catch (RestClientException e) {
            Assert.fail();
        }
        try {
            upcService.getUpcsByUpcIds(Collections.singletonList(UPC_ID));
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testNullIdsOnGetUpcsByIds() {
        List<UpcResponse> upcs = upcService.getUpcsByUpcIds(null);
        try {
            Mockito.verify(upcRestClient, Mockito.times(0)).getUpcsByIds(Mockito.any());
        } catch (RestClientException e) {
            Assert.fail();
        }
        Assert.assertNotNull(upcs);
        Assert.assertEquals(0, upcs.size());
    }

    @Test
    public void shouldGetUpcsByUpcNumbers() {
        List<Long> upcNumbers = Collections.singletonList(UPC_NUMBER);
        try {
            Mockito.when(upcRestClient.getUpcsByUpcNumbers(upcNumbers)).thenReturn(upcs);
        } catch (RestClientException e) {
            Assert.fail();
        }
        List<UpcResponse> actualUpcs = upcService.getUpcsByUpcNumbers(upcNumbers);
        Assert.assertEquals(upcs, actualUpcs);
    }

    @Test(expected = ListServiceException.class)
    public void shouldWrapExceptionsFromGetUpcsByNumbers() {
        try {
            Mockito.when(upcRestClient.getUpcsByUpcNumbers(Mockito.any())).thenThrow(new RestClientException("exc message", new Exception()));
        } catch (RestClientException e) {
            Assert.fail();
        }
        try {
            upcService.getUpcsByUpcNumbers(Collections.singletonList(UPC_NUMBER));
        } catch (ListServiceException e) {
            Assert.assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), e.getStatusCode());
            Assert.assertEquals(ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR.getInternalCode(), e.getServiceErrorCode());
            throw e;
        }
    }

    @Test
    public void testNullNumbersOnGetUpcsByNumbers() {
        List<UpcResponse> upcs = upcService.getUpcsByUpcNumbers(null);
        try {
            Mockito.verify(upcRestClient, Mockito.times(0)).getUpcsByUpcNumbers(Mockito.any());
        } catch (RestClientException e) {
            Assert.fail();
        }
        Assert.assertNotNull(upcs);
        Assert.assertEquals(0, upcs.size());
    }

}
