package com.macys.selection.xapi.list.client.response.fcc;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ReviewStatistics")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewStatisticsResponse {

    private Double averageRating;
    private Integer reviewCount;

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(Integer reviewCount) {
        this.reviewCount = reviewCount;
    }

    @Override
    public String toString() {
        return "ReviewStatisticsBO{" +
                "averageRating=" + averageRating +
                ", reviewCount=" + reviewCount +
                '}';
    }
}
