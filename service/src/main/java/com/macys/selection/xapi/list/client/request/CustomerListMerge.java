package com.macys.selection.xapi.list.client.request;

import com.fasterxml.jackson.annotation.JsonRootName;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@JsonRootName("wishListRequestDTO")
@XmlRootElement(name = "wishListRequestDTO")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(propOrder = { "guestUserId", "userId"})
public class CustomerListMerge {

    private Long guestUserId;
    private Long userId;

    public Long getGuestUserId() {
        return guestUserId;
    }

    public void setGuestUserId(Long guestUserId) {
        this.guestUserId = guestUserId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
