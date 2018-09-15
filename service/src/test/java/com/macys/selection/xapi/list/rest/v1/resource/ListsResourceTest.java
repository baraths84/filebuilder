package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.services.ListsService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

public class ListsResourceTest {

    private static final String USER_GUID = "testUserGuid";

    @InjectMocks
    private ListsResource listsResource;

    @Mock
    private ListsService listsService;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
        listsResource = new ListsResource(listsService);
    }

    @Test
    public void testListsByTypes() {
        Response response = listsResource.getListsByTypes(USER_GUID);
        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(listsService).getListsByTypes(any(), any(), any());
    }

    @Test
    public void testAllListsByUserGuid() {
        Response response = listsResource.getAllListsByUserGuid(USER_GUID);
        Assert.assertNotNull(response);
        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        verify(listsService).getAllListsByUserGuid(USER_GUID);
    }
}
