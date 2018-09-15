package com.macys.selection.xapi.list.exception;


import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.*;

/**
 * Created by Narasim Bayanaboina on 1/17/18.
 */
@JsonRootName("errors")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WishListErrors implements Serializable{

    @JsonProperty("error")
    private List<WishListError> error = null;

    @JsonProperty("error")
    public List<WishListError> getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(List<WishListError> error) {
        this.error = error;
    }

}
