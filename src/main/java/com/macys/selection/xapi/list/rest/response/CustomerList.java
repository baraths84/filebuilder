
package com.macys.selection.xapi.list.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.MoreObjects;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * Customer Wishlist Response
 **/
@JsonPropertyOrder({ "user", "list", "meta","KillSwitches" })
@Validated
public class CustomerList implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 812917164137352130L;

    @JsonProperty("user")
    private User user;

    @JsonProperty("list")
    private List<WishList> wishlist;

    @JsonProperty("meta")
    private AnalyticsMeta meta;




    @JsonProperty("KillSwitches")
    private KillSwitches killswitches;


    public User getUser() {
        return user;
    }

    public void setUser(@Size(min = 8, max = 10) User user) {
        this.user = user;
    }

    public List<WishList> getWishlist() {
        return wishlist;
    }

    public void setWishlist(List<WishList> wishlist) {
        this.wishlist = wishlist;
    }

    public AnalyticsMeta getMeta() {
        return meta;
    }

    public void setMeta(AnalyticsMeta meta) {
        this.meta = meta;
    }

    public KillSwitches getKillswitches() {
        return killswitches;
    }

    public void setKillswitches(KillSwitches killswitches) {
        this.killswitches = killswitches;
    }



    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("user", user).add("lists", wishlist).add("meta", meta).add("killswitches",killswitches).toString();
    }

}
