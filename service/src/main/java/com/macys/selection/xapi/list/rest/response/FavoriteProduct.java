package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoriteProduct implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1415873467198640602L;
	
    @JsonProperty("pid")
	private int pid;

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}
	
    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.pid);
        return builder.toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !this.getClass().equals(obj.getClass())) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        EqualsBuilder builder = new EqualsBuilder();
        appendEquals(builder, obj);
        return builder.isEquals();
    }

    protected void appendEquals(EqualsBuilder builder, Object obj) {
        FavoriteProduct other = (FavoriteProduct) obj;
        builder.append(this.getPid(), other.getPid());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("pid", pid)
                .add("pid", pid).toString();
    }

}
