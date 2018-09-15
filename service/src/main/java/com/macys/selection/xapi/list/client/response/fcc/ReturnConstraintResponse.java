package com.macys.selection.xapi.list.client.response.fcc;

import com.google.common.base.MoreObjects;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class ReturnConstraintResponse {

    private String returnCode;
    private String returnInstruction;

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnInstruction() {
        return returnInstruction;
    }

    public void setReturnInstruction(String returnInstruction) {
        this.returnInstruction = returnInstruction;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("returnCode", returnCode)
                .add("returnInstruction", returnInstruction)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ReturnConstraintResponse that = (ReturnConstraintResponse) o;

        return new EqualsBuilder()
                .append(returnCode, that.returnCode)
                .append(returnInstruction, that.returnInstruction)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(returnCode)
                .append(returnInstruction)
                .toHashCode();
    }
}
