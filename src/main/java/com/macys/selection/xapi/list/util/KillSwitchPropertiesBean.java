package com.macys.selection.xapi.list.util;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.macys.killswitch.annotation.KillSwitch;
import com.macys.killswitch.dto.Channel;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * Class holding all the killswitch properties
 *
 * @author m940030
 */
@Configuration
public class KillSwitchPropertiesBean {
	
	@KillSwitch(name = "finalPriceDisplayEnabled")
	public boolean isFinalPriceDisplayEnabled() {
		return true;
	}

    @KillSwitch(name = "responsiveWishlistPromotionsEnabled")
    public boolean isResponsiveWishlistPromotionsEnabled() {
        return false;
    }

    @KillSwitch(name = "responsiveWishlistFindEnabled")
    public boolean isResponsiveWishlistFindEnabled() {
        return false;
    }

    @KillSwitch(name = "responsiveWishlistEnabled")
    public boolean isResponsiveWishlistEnabled() {
        return false;
    }

    @KillSwitch(name = "mspListEnabled")
    public boolean isMspListEnabled() {
        return true;
    }

    @KillSwitch(name = "responsiveCleanupExperimentEnabled")
    public boolean isResponsiveCleanupExperimentEnabled() {
        return true;
    }

    @KillSwitch(name = "separateFindUsersEnabled")
    public boolean isSeparateFindUsersEnabled() {
        return true;
    }

    @KillSwitch(name = "headerAsAServiceEnabled")
    public boolean isHeaderAsAServiceEnabled() {
        return false;
    }

    @KillSwitch(name = "prosPageNavigationEnabled", channel = Channel.MCOM)
    public boolean isProsPageNavigationEnabled() {
        return false;
    }

    @KillSwitch(name = "prosZonesAddToListEnabled", channel = Channel.MCOM)
    public boolean isProsZonesAddToListEnabled() {
        return false;
    }

    @KillSwitch(name = "quickViewProsZoneEnabled", channel = Channel.MCOM)
    public boolean isQuickViewProsZoneEnabled() {
        return false;
    }
}
