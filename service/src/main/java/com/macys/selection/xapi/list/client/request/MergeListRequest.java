package com.macys.selection.xapi.list.client.request;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonRootName;


@JsonRootName(value="mergeList")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(Include.NON_NULL)
public class MergeListRequest {
    private Long srcUserId;
    private Boolean srcGuestUser;
    private Long targetUserId;
    private Boolean targetGuestUser;
    private String targetUserGuid;
    private String targetUserFirstName;

    public Long getSrcUserId() {
        return srcUserId;
    }

    public void setSrcUserId(Long srcUserId) {
        this.srcUserId = srcUserId;
    }

    public Boolean getSrcGuestUser() {
        return srcGuestUser;
    }

    public void setSrcGuestUser(Boolean srcGuestUser) {
        this.srcGuestUser = srcGuestUser;
    }

    public Long getTargetUserId() {
        return targetUserId;
    }

    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }

    public Boolean getTargetGuestUser() {
        return targetGuestUser;
    }

    public void setTargetGuestUser(Boolean targetGuestUser) {
        this.targetGuestUser = targetGuestUser;
    }

    public String getTargetUserGuid() {
        return targetUserGuid;
    }

    public void setTargetUserGuid(String targetUserGuid) {
        this.targetUserGuid = targetUserGuid;
    }

    public String getTargetUserFirstName() {
        return targetUserFirstName;
    }

    public void setTargetUserFirstName(String targetUserFirstName) {
        this.targetUserFirstName = targetUserFirstName;
    }
}
