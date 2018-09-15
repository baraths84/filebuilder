package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;


import java.io.Serializable;

/**
 * Class holding all the killswitch properties
 *
 * @author m898623
 */
@JsonRootName("KillSwitches")
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KillSwitches implements Serializable {


    private boolean responsiveWishlistEnabled;

    private boolean mspListEnabled;

    private boolean responsiveWishlistFindEnabled;

    private boolean responsiveWishlistPromotionsEnabled;

    private boolean finalPriceDisplayEnabled;

    private boolean responsiveCleanupExperimentEnabled;

    private boolean separateFindUsersEnabled;

    private boolean headerAsAServiceEnabled;

    private boolean prosPageNavigationEnabled;

    private boolean prosZonesAddToListEnabled;

    private boolean quickViewProsZoneEnabled;

    public void setFinalPriceDisplayEnabled(boolean finalPriceDisplayEnabled) {
        this.finalPriceDisplayEnabled = finalPriceDisplayEnabled;
    }

    public void setResponsiveWishlistEnabled(boolean responsiveWishlistEnabled) {
        this.responsiveWishlistEnabled = responsiveWishlistEnabled;
    }

    public void setMspListEnabled(boolean mspListEnabled) {
        this.mspListEnabled = mspListEnabled;
    }

    public void setResponsiveWishlistPromotionsEnabled(boolean responsiveWishlistPromotionsEnabled) {
        this.responsiveWishlistPromotionsEnabled = responsiveWishlistPromotionsEnabled;
    }

    public void setResponsiveWishlistFindEnabled(boolean responsiveWishlistFindEnabled) {
        this.responsiveWishlistFindEnabled = responsiveWishlistFindEnabled;
    }

    public void setResponsiveCleanupExperimentEnabled(boolean responsiveCleanupExperimentEnabled) {
        this.responsiveCleanupExperimentEnabled = responsiveCleanupExperimentEnabled;
    }

    public void setSeparateFindUsersEnabled(boolean separateFindUsersEnabled) {
        this.separateFindUsersEnabled = separateFindUsersEnabled;
    }

    public void setHeaderAsAServiceEnabled(boolean headerAsAServiceEnabled) {
        this.headerAsAServiceEnabled = headerAsAServiceEnabled;
    }

    public void setProsPageNavigationEnabled(boolean prosPageNavigationEnabled) {
        this.prosPageNavigationEnabled = prosPageNavigationEnabled;
    }

    public void setProsZonesAddToListEnabled(boolean prosZonesAddToListEnabled) {
        this.prosZonesAddToListEnabled = prosZonesAddToListEnabled;
    }

    public void setQuickViewProsZoneEnabled(boolean quickViewProsZoneEnabled) {
        this.quickViewProsZoneEnabled = quickViewProsZoneEnabled;
    }

    @JsonProperty("responsiveWishlistEnabled")
    public boolean isResponsiveWishlistEnabled() {
        return responsiveWishlistEnabled;
    }

    @JsonProperty("mspListEnabled")
    public boolean isMspListEnabled() {
        return mspListEnabled;
    }

    @JsonProperty("responsiveWishlistFindEnabled")
    public boolean isresponsiveWishlistFindEnabled() {
        return responsiveWishlistFindEnabled;
    }


    @JsonProperty("responsiveWishlistPromotionsEnabled")
    public boolean isresponsiveWishlistPromotionsEnabled() {
        return responsiveWishlistPromotionsEnabled;
    }

    @JsonProperty("finalPriceDisplayEnabled")
    public boolean isFinalPriceDisplayEnabled() {
        return finalPriceDisplayEnabled;
    }

    @JsonProperty("responsiveCleanupExperimentEnabled")
    public boolean isResponsiveCleanupExperimentEnabled() {
        return responsiveCleanupExperimentEnabled;
    }


    @JsonProperty("separateFindUsersEnabled")
    public boolean isSeparateFindUsersEnabled() {
        return separateFindUsersEnabled;
    }

    @JsonProperty("headerAsAServiceEnabled")
    public boolean isHeaderAsAServiceEnabled() {
        return headerAsAServiceEnabled;
    }

    @JsonProperty("prosPageNavigationEnabled")
    public boolean isProsPageNavigationEnabled() {
        return prosPageNavigationEnabled;
    }

    @JsonProperty("prosZonesAddToListEnabled")
    public boolean isProsZonesAddToListEnabled() {
        return prosZonesAddToListEnabled;
    }

    @JsonProperty("quickViewProsZoneEnabled")
    public boolean isQuickViewProsZoneEnabled() {
        return quickViewProsZoneEnabled;
    }


    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(this.responsiveWishlistEnabled).append(this.responsiveWishlistPromotionsEnabled)
                .append(this.responsiveWishlistFindEnabled).append(this.finalPriceDisplayEnabled)
                .append(this.responsiveCleanupExperimentEnabled).append(this.separateFindUsersEnabled)
                .append(this.mspListEnabled).append(this.headerAsAServiceEnabled)
                .append(this.prosPageNavigationEnabled).append(this.prosZonesAddToListEnabled)
                .append(this.quickViewProsZoneEnabled);
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
        KillSwitches other = (KillSwitches) obj;
        builder.append(this.isResponsiveWishlistEnabled(), other.isResponsiveWishlistEnabled());
        builder.append(this.isresponsiveWishlistFindEnabled(), other.isresponsiveWishlistFindEnabled());
        builder.append(this.isresponsiveWishlistPromotionsEnabled(), other.isresponsiveWishlistPromotionsEnabled());
        builder.append(this.isFinalPriceDisplayEnabled(), other.isFinalPriceDisplayEnabled());
        builder.append(this.isResponsiveCleanupExperimentEnabled(), other.isResponsiveCleanupExperimentEnabled());
        builder.append(this.isSeparateFindUsersEnabled(), other.isSeparateFindUsersEnabled());
        builder.append(this.isMspListEnabled(), other.isMspListEnabled());
        builder.append(this.isHeaderAsAServiceEnabled(), other.isHeaderAsAServiceEnabled());
        builder.append(this.isProsPageNavigationEnabled(), other.isProsPageNavigationEnabled());
        builder.append(this.isProsZonesAddToListEnabled(), other.isProsZonesAddToListEnabled());
        builder.append(this.isQuickViewProsZoneEnabled(), other.isQuickViewProsZoneEnabled());
    }


    @Override
    public String toString() {
        return "KillSwitches{" +
                "responsiveWishlistEnabled=" + responsiveWishlistEnabled +
                ", mspListEnabled=" + mspListEnabled +
                ", responsiveWishlistFindEnabled=" + responsiveWishlistFindEnabled +
                ", responsiveWishlistPromotionsEnabled=" + responsiveWishlistPromotionsEnabled +
                ", finalPriceDisplayEnabled=" + finalPriceDisplayEnabled +
                ", responsiveCleanupExperimentEnabled=" + responsiveCleanupExperimentEnabled +
                ", separateFindUsersEnabled=" + separateFindUsersEnabled +
                ", headerAsAServiceEnabled=" + headerAsAServiceEnabled +
                ", prosPageNavigationEnabled=" + prosPageNavigationEnabled +
                ", prosZonesAddToListEnabled=" + prosZonesAddToListEnabled +
                ", quickViewProsZoneEnabled=" + quickViewProsZoneEnabled +
                '}';
    }
}
