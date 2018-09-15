package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.CustomerServiceRestClient;
import com.macys.selection.xapi.list.client.PromotionsRestClient;
import com.macys.selection.xapi.list.client.RestResponse;
import com.macys.selection.xapi.list.data.converters.*;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DeleteFavoriteServiceTest {

    private static String DELETE_FAVORITE_ERROR_FILE = "com/macys/selection/xapi/wishlist/converters/deleteFavorite_Error.json";
    private static String REST_EXCEP = "RestException from the RestClient";

    @Mock
    private CustomerServiceRestClient restClient;

    @Mock
    private PromotionsRestClient promotionsRestClient;

    @Mock
    private JsonResponseParserPromotions promotions;

    @Mock
    private JsonToPromotionConverter promotionsConverter;

    @InjectMocks
    private CustomerService customerService;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDeleteFavorite() {
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.NO_CONTENT.getStatusCode());
        when(restClient.deleteFavorite(any(), any(), any())).thenReturn(restResponse);
        customerService.deleteFavorite(any(), any(), any());
    }

    @Test(expectedExceptions = ListServiceException.class)
    public void testDeleteFavoriteServiceError() throws IOException {
        String errorMessageString = TestUtils.readFile(DELETE_FAVORITE_ERROR_FILE);
        RestResponse restResponse = new RestResponse();
        restResponse.setStatusCode(Response.Status.BAD_REQUEST.getStatusCode());
        restResponse.setBody(errorMessageString);
        when(restClient.deleteFavorite(any(), any(), any())).thenReturn(restResponse);
        customerService.deleteFavorite(any(), any(), any());
    }

    @Test(expectedExceptions = RestException.class)
    public void testDeleteFavoriteException() throws IOException {
        when(restClient.deleteFavorite(any(), any(), any())).thenThrow(new RestException(REST_EXCEP));
        customerService.deleteFavorite(any(), any(), any());
    }
}
