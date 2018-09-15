package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("activityLogPage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityLogPage {

    private List<ActivityLog> activityLog;

    private Integer totalNumberOfLogs;

    public List<ActivityLog> getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(List<ActivityLog> activityLog) {
        this.activityLog = activityLog;
    }

    public Integer getTotalNumberOfLogs() {
        return totalNumberOfLogs;
    }

    public void setTotalNumberOfLogs(Integer totalNumberOfLogs) {
        this.totalNumberOfLogs = totalNumberOfLogs;
    }
}
