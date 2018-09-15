package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.common.ListTypeEnum;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.request.PaginationQueryParam;
import com.macys.selection.xapi.list.rest.request.UserQueryParam;
import com.macys.selection.xapi.list.rest.response.ListsPresentation;
import com.macys.selection.xapi.list.rest.response.ListsResponse;
import com.macys.selection.xapi.list.services.ListsService;
import com.macys.selection.xapi.list.util.ListRequestParamUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Component
@Path("/lists/v1/lists")
@Api(value = "List API's")
public class ListsResource {

    private final ListsService listsService;

    @Autowired
    public ListsResource(ListsService listsService) {
        this.listsService = listsService;
    }

    @GET
    @ApiOperation(value = "Retrieves a customer lists grouped by listTypes", response = ListsPresentation.class)
    public Response getListsByTypes(@QueryParam("userGuid") String userGuid) {
        UserQueryParam userQueryParam = new UserQueryParam();
        userQueryParam.setUserGuid(userGuid);

        ListQueryParam listQueryParam = new ListQueryParam();
        listQueryParam.setDefaultList(true);
        listQueryParam.setListType(ListTypeEnum.WISH_LIST.getValue());
        listQueryParam.setExpand(ListRequestParamUtil.EXPAND_ITEMS);

        PaginationQueryParam paginationQueryParam = new PaginationQueryParam();
        paginationQueryParam.setLimit(1);

        ListsPresentation response = listsService.getListsByTypes(userQueryParam, listQueryParam, paginationQueryParam);
        return Response.ok(response).build();
    }

    @GET
    @Path("/all")
    @ApiOperation(value = "Retrieves all customer lists", response = ListsResponse.class)
    public Response getAllListsByUserGuid(@QueryParam("userGuid") String userGuid) {
        ListsResponse response = listsService.getAllListsByUserGuid(userGuid);
        return Response.ok(response).build();
    }
}
