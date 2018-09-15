package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName(value="activityLogPage")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ActivityLogPageDTO {
    private List<ActivityLogDTO> activityLog;

    private Integer totalNumberOfLogs;

    public List<ActivityLogDTO> getActivityLog() {
        return activityLog;
    }

    public void setActivityLog(List<ActivityLogDTO> activityLog) {
        this.activityLog = activityLog;
    }

    public Integer getTotalNumberOfLogs() {
        return totalNumberOfLogs;
    }

    public void setTotalNumberOfLogs(Integer totalNumberOfLogs) {
        this.totalNumberOfLogs = totalNumberOfLogs;
    }
}
