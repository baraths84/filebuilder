package com.macys.selection.xapi.list.client.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonRootName;

import java.util.List;

@JsonRootName("listFeedback")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListFeedbackDTO {

    private String listGuid;

    private List<ItemFeedbackDTO> itemFeedback;

    public String getListGuid() {
        return listGuid;
    }

    public void setListGuid(String listGuid) {
        this.listGuid = listGuid;
    }

    public List<ItemFeedbackDTO> getItemFeedback() {
        return itemFeedback;
    }

    public void setItemFeedback(List<ItemFeedbackDTO> itemFeedback) {
        this.itemFeedback = itemFeedback;
    }
}
