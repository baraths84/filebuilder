package com.macys.selection.xapi.list.client.response.fcc;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonRootName("upcs")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UpcSetResponse {
    @JsonProperty("upc")
    private List<UpcResponse> upcs;

    public List<UpcResponse> getUpcs() {
        return upcs;
    }

    public void setUpcs(List<UpcResponse> upcs) {
        this.upcs = upcs;
    }
}
