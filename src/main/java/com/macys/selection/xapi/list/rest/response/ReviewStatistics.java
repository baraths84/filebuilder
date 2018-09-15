/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.google.common.base.MoreObjects;

/**
 * @author YH03933
 *
 */
@JsonRootName("reviewStatistics")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"averageRating", "reviewCount"})
@JsonInclude(Include.NON_NULL)
public class ReviewStatistics implements Serializable {

  /**
	 * 
	 */
	private static final long serialVersionUID = 9072977073149625712L;

@JsonProperty("averageRating")
  private Float averageRating;

  @JsonProperty("reviewCount")
  private Integer reviewCount;

  public Float getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(Float averageRating) {
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
    return MoreObjects.toStringHelper(this).add("averageRating", averageRating)
        .add("reviewCount", reviewCount).toString();
  }

  @Override
  public int hashCode() {
    HashCodeBuilder builder = new HashCodeBuilder();
    builder.append(this.averageRating)
    .append(this.reviewCount);
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
    ReviewStatistics other = (ReviewStatistics) obj;
    builder.append(this.getAverageRating(), other.getAverageRating());
    builder.append(this.getReviewCount(), other.getReviewCount());
  }  
  
}

