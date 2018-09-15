package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.*;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.common.base.MoreObjects;

@JsonRootName("analytics")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({ "data" })
@JsonInclude(Include.NON_NULL)
public class Analytics implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7843631630497203817L;
    /**
     *
     */

    @JsonIgnore
    private DigitalAnalytics digitalAnalytics;

    @JsonIgnore
    private ResponseDigitalAnalytics responseDigitalAnalytics;

    @JsonGetter("data")
    public Object getDigitalAnalytics() throws NullPointerException {
        if (  digitalAnalytics == null && responseDigitalAnalytics == null) {
            throw new NullPointerException();
        }
        return digitalAnalytics != null? digitalAnalytics: responseDigitalAnalytics;
    }

    public void setDigitalAnalytics(DigitalAnalytics digitalAnalytics) {
        this.digitalAnalytics = digitalAnalytics;
    }

    public void setResponseDigitalAnalytics(ResponseDigitalAnalytics responseDigitalAnalytics) {
        this.responseDigitalAnalytics = responseDigitalAnalytics;
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.getDigitalAnalytics().hashCode());
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !this.getClass().equals(obj.getClass())) {
            return false;
        }

        if(obj == this) {
            return true;
        }

        EqualsBuilder builder = new EqualsBuilder();
        appendEquals(builder, obj);
        return builder.isEquals();
    }

    protected void appendEquals(EqualsBuilder builder, Object obj) {
        Analytics other = (Analytics) obj;
        builder.append(this.getDigitalAnalytics(), other.getDigitalAnalytics());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("data", this.getDigitalAnalytics()).toString();
    }

}
