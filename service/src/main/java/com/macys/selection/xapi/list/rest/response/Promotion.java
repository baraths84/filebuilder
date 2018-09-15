/*******************************************************************************
 * Copyright (c) 2017 macys.com. All rights reserved.
 *******************************************************************************/
package com.macys.selection.xapi.list.rest.response;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;

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
@JsonRootName("promotion")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonPropertyOrder({"promotionId", "badgeTextAttributeValue"})
@JsonInclude(Include.NON_NULL)
public class Promotion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1392424895515095131L;

	@JsonProperty("promotionId")
	private Long promotionId;

	@JsonProperty("badgeTextAttributeValue")
	private String badgeTextAttributeValue;

	public Long getPromotionId() {
		return promotionId;
	}

	public void setPromotionId(Long promotionId) {
		this.promotionId = promotionId;
	}

	public String getBadgeTextAttributeValue() {
		return badgeTextAttributeValue;
	}

	public void setBadgeTextAttributeValue(String badgeTextAttributeValue) {
		this.badgeTextAttributeValue = badgeTextAttributeValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((promotionId == null) ? 0 : promotionId.hashCode());
		return result;
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
		Promotion other = (Promotion) obj;
		builder.append(this.getPromotionId(), other.getPromotionId());
		builder.append(this.getBadgeTextAttributeValue(), other.getBadgeTextAttributeValue());
	}  

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("promotionId", promotionId)
				.add("badgeTextAttributeValue", badgeTextAttributeValue).toString();
	}

}
