package com.macys.selection.xapi.list.rest.v1.resource;

import com.macys.selection.xapi.list.rest.response.KillSwitches;

import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import com.macys.selection.xapi.list.util.ListUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Component
@Path("/wishlist/v1/lists/dksswitches")
@Api(value = "List API's")
public class DKSPropertyResource {

    @Autowired
    private KillSwitchPropertiesBean killswitchProperties;

    /**
     * To get DKS Killswitches
     *
     * @return Json string (Killswitches)
     */
    @ApiOperation(value = "Get DKS Killswitches", notes = "Getting DKS Killswitches for List")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "")})

    @GET
    @Produces("application/json")
    public Response getDKSInfo() {

        KillSwitches response = ListUtil.updateProperties(killswitchProperties);
        return Response.ok(response).build();

    }

}
