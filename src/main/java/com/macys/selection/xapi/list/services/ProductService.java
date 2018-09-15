package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.FccRestClient;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.exception.RestException;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    private FccRestClient fccRestClient;

    @Autowired
    public ProductService(FccRestClient fccRestClient) {
        this.fccRestClient = fccRestClient;
    }


    public List<ProductResponse> getProductsByProdIds(Collection<Integer> prodIds) throws RestException {
        if (CollectionUtils.isEmpty(prodIds)) {
            LOGGER.warn("Upc Ids should not be empty");
            return Collections.emptyList();
        }
        try {
            return getFccRestClient().getProductsByProdIds(prodIds).getProduct();
        } catch (Exception e) {
            LOGGER.error("Error occured while fetching Product by ProductIds :: " + e.getCause());
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
        }

    }

    protected FccRestClient getFccRestClient() {
        return fccRestClient;
    }
}
