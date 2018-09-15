package com.macys.selection.xapi.list.services;

import com.macys.selection.xapi.list.client.FccRestClient;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
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
public class UpcService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UpcService.class);
    private final FccRestClient upcRestClient;

    @Autowired
    public UpcService(FccRestClient upcRestClient) {
        this.upcRestClient = upcRestClient;
    }

    public List<UpcResponse> getUpcsByUpcIds(Collection<Integer> upcIds) {
        if (CollectionUtils.isEmpty(upcIds)) {
            LOGGER.warn("Upc Ids should not be empty");
            return Collections.emptyList();
        }
        try {
            return upcRestClient.getUpcsByIds(upcIds);
        } catch (Exception e) {
            LOGGER.error("exception reading fcc json data", e);
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
        }
    }

    public List<UpcResponse> getUpcsByUpcNumbers(Collection<Long> upcNumbers) {
        if (CollectionUtils.isEmpty(upcNumbers)) {
            LOGGER.warn("Upc Numbers should not be empty");
            return Collections.emptyList();
        }
        try {
            return upcRestClient.getUpcsByUpcNumbers(upcNumbers);
        } catch (Exception e) {
            LOGGER.error("exception reading fcc json data", e);
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
        }
    }
}
