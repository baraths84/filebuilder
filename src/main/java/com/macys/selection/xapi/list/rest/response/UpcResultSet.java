package com.macys.selection.xapi.list.rest.response;

import com.macys.platform.util.api.AbstractOrderedMap;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpcResultSet extends AbstractOrderedMap<Integer, UpcResponse> {
	
	private final Map<Long, UpcResponse> lookupByUpcNumberIndex = new HashMap<>();

    public UpcResultSet(List<UpcResponse> upcs) {
        for(UpcResponse upc : upcs) {
            resultSet.put(upc.getId(), upc);
            lookupByUpcNumberIndex.put(upc.getUpc(), upc);
        }
    }

    public UpcResponse getByUpcNumber(Long upcNumber) {
        return lookupByUpcNumberIndex.get(upcNumber);
    }

    public boolean containsUpcNumber(Long upcNumber) {
        return lookupByUpcNumberIndex.containsKey(upcNumber);
    }
}
