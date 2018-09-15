package com.macys.selection.xapi.list.exception;


import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

/**
 * Created by Narasim Bayanaboina on 1/17/18.
 */
@JsonRootName("error")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "requestId",
        "clientId",
        "errorCode",
        "message"
})
public class WishListError implements Serializable {

    @JsonProperty("requestId")
    private String requestId;
    @JsonProperty("clientId")
    private String clientId;
    @JsonProperty("errorCode")
    private String errorCode;
    @JsonProperty("message")
    private String message;

    @JsonProperty("requestId")
    public String getRequestId() {
        return requestId;
    }

    @JsonProperty("requestId")
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    @JsonProperty("clientId")
    public String getClientId() {
        return clientId;
    }

    @JsonProperty("clientId")
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @JsonProperty("errorCode")
    public String getErrorCode() {
        return errorCode;
    }

    @JsonProperty("errorCode")
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

}
