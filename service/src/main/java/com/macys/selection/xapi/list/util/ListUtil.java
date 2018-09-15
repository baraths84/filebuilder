package com.macys.selection.xapi.list.util;

import java.net.URI;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.macys.platform.rest.core.RequestContext;
import com.macys.platform.rest.framework.jaxb.Link;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.rest.request.ListQueryParam;
import com.macys.selection.xapi.list.rest.response.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.macys.selection.xapi.list.rest.response.FinalPrice;
import org.springframework.stereotype.Component;

@Component
public class ListUtil {

    private static final String SLASH = "/";
    private static final String URI_HOST_DEFAULT = "http://api.macys.com/";
    private static final String LINK_USER_BY_GUID_TEMPLATE = "%scustomer/v1/users/userdetails/getuser?userguid=%s";
    private static final String LINK_USER_BY_ID_TEMPLATE = "%scustomer/v1/users/userdetails/getuser?userid=%d";
    private static final String LINK_SELF_BY_GUID_TEMPLATE = "%swishlist/v1/lists?userGuid=%s";
    private static final String LINK_SELF_BY_ID_TEMPLATE = "%swishlist/v1/lists?userId=%d";
    private static final String LINK_LIST_ITEMS_TEMPLATE = "%swishlist/v1/lists/%s?expand=items";


    @Autowired
	private KillSwitchPropertiesBean killswitchPropertiesBean;

	public KillSwitchPropertiesBean getKillswitchPropertiesBean() {
		return killswitchPropertiesBean;
	}

	public void setKillswitchPropertiesBean(KillSwitchPropertiesBean killswitchPropertiesBean) {
		this.killswitchPropertiesBean = killswitchPropertiesBean;
	}

	private ListUtil() {

    }



    public static List<Item> filterListByAvailability(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return new ArrayList<>();
        }
        return items.stream()
                .filter(item -> item.getUpc() != null && item.getUpc().getAvailability() != null &&
                        item.getUpc().getAvailability().isAvailable() != null && item.getUpc().getAvailability().isAvailable())
                .collect(Collectors.toList());
    }
    
    public void filterFinalPrice(List<Item> itemList, ListQueryParam listQueryParam) {
		for (Item item : itemList) {
			//filter final price based on show status. We will have either UPC level finalPrice or productLevel. Not both
			if (item.getUpc() != null && item.getUpc().getFinalPrice() != null) {
				filterFinalUpcByShowStatus(item, listQueryParam);
			} else if (item.getProduct() != null && item.getProduct().getFinalPrice() != null) {
				filterFinalProductByShowStatus(item, listQueryParam);
			}
		}
	}

	public void filterFinalProductByShowStatus (Item item, ListQueryParam listQueryParam) {
		Product product = item.getProduct();

		FinalPrice finalPrice = product.getFinalPrice();

		if (!killswitchPropertiesBean.isFinalPriceDisplayEnabled()) {
			product.setFinalPrice(null);
		}
        else if(WishlistConstants.FINAL_PRICE_NEVER_SHOW.equals(finalPrice.getDisplayFinalPrice())) {
            product.setFinalPrice(null);
        }
        // if final price is conditional show, the promotion type is map and the user is not signed in do not display final price
        else if(WishlistConstants.FINAL_PRICE_CONDITIONAL_SHOW.equals(finalPrice.getDisplayFinalPrice()) && finalPrice.getProductTypePromotion()!=null && listQueryParam.getCustomerState()!= null)   {
            if(!WishlistConstants.SIGNED_IN.equalsIgnoreCase(listQueryParam.getCustomerState()) && !WishlistConstants.RECOGNIZED.equalsIgnoreCase(listQueryParam.getCustomerState()) && WishlistConstants.MAP.equalsIgnoreCase(finalPrice.getProductTypePromotion()))  {
                product.setFinalPrice(null);
            }
        }
        else if (listQueryParam.getCustomerState()==null){
            product.setFinalPrice(null);
        }
        // Dont display finalPrice if priceDrop label present in response
		if (item.getRetailPriceDropAfterAddedToList() != null && !item.getRetailPriceDropAfterAddedToList().equals(0.0)) {
			product.setFinalPrice(null);
		}

	}

	public void filterFinalUpcByShowStatus (Item item, ListQueryParam listQueryParam) {

		Upc upc = item.getUpc();

		FinalPrice finalPrice = upc.getFinalPrice();

		if (!killswitchPropertiesBean.isFinalPriceDisplayEnabled()) {
			upc.setFinalPrice(null);
		}
        else if(WishlistConstants.FINAL_PRICE_NEVER_SHOW.equals(finalPrice.getDisplayFinalPrice())) {
            upc.setFinalPrice(null);
        }
        // if final price is conditional show, the promotion type is map and the user is not signed in do not display final price
        else if(WishlistConstants.FINAL_PRICE_CONDITIONAL_SHOW.equals(finalPrice.getDisplayFinalPrice()) && finalPrice.getProductTypePromotion()!=null && listQueryParam.getCustomerState()!= null)   {
            if(!WishlistConstants.SIGNED_IN.equalsIgnoreCase(listQueryParam.getCustomerState()) && !WishlistConstants.RECOGNIZED.equalsIgnoreCase(listQueryParam.getCustomerState()) && WishlistConstants.MAP.equalsIgnoreCase(finalPrice.getProductTypePromotion()))  {
                upc.setFinalPrice(null);
            }
        }
        else if (listQueryParam.getCustomerState()==null){
            upc.setFinalPrice(null);
        }

        // Dont display finalPrice if priceDrop label present in response
		if (item.getRetailPriceDropAfterAddedToList() != null && !item.getRetailPriceDropAfterAddedToList().equals(0.0)) {
			upc.setFinalPrice(null);
		}

	}

    
    public static AnalyticsMeta buildAnalyticsMeta(WishList wishlist) {
        AnalyticsMeta meta = new AnalyticsMeta();
        meta.setAnalytics(buildAnalytics(wishlist));
        return meta;
    }

    public static Analytics buildAnalytics(WishList wishlist) {
        Analytics analytics = new Analytics();
        analytics.setDigitalAnalytics(buildDigitalAnalytics(wishlist));
        return analytics;
    }

    public static DigitalAnalytics buildDigitalAnalytics(WishList wishlist) {
        DigitalAnalytics digitalAnalytics = new DigitalAnalytics();

        final List<String> productIdList = new ArrayList<>();
        final List<String> productNameList = new ArrayList<>();
        final List<String> productPriceList = new ArrayList<>();
        final List<String> productPricingStateList = new ArrayList<>();
        final List<String> productQuantityList = new ArrayList<>();
        final List<String> productUPCList = new ArrayList<>();

        final Predicate<Item> itemNotNullPredicate = entry -> entry != null;

        if (wishlist != null && wishlist.getItems() != null) {
            wishlist.getItems().stream().filter(itemNotNullPredicate).forEach((item) -> {
                if (item.getUpc() != null) {
                    if (item.getUpc().getProduct() != null && item.getUpc().getProduct().getId() != null) {
                        productIdList.add(String.valueOf(item.getUpc().getProduct().getId()));
                    }
                    if (item.getUpc().getProduct() != null && item.getUpc().getProduct().getName() != null) {
                        productNameList.add(item.getUpc().getProduct().getName());
                    }
                    if (item.getUpc().getPrice() != null && item.getUpc().getPrice().getRetailPrice() != null) {
                        productPriceList.add(String.valueOf(item.getUpc().getPrice().getRetailPrice()));
                    }
                    if (item.getUpc().getPrice() != null && item.getUpc().getPrice().getPriceTypeText() != null) {
                        productPricingStateList.add(item.getUpc().getPrice().getPriceTypeText());
                    }
                    if (item.getUpc().getUpcNumber() != null) {
                        productUPCList.add(String.valueOf(item.getUpc().getUpcNumber()));
                    }
                }
                if (item.getQtyRequested() != null) {
                    productQuantityList.add(String.valueOf(item.getQtyRequested()));
                }
            });

            if (CollectionUtils.isNotEmpty(productIdList)) {
                digitalAnalytics.setProductId(productIdList);
            }
            if (CollectionUtils.isNotEmpty(productNameList)) {
                digitalAnalytics.setProductName(productNameList);
            }
            if (CollectionUtils.isNotEmpty(productPriceList)) {
                digitalAnalytics.setProductPrice(productPriceList);
            }
            if (CollectionUtils.isNotEmpty(productPricingStateList)) {
                digitalAnalytics.setProductPricingState(productPricingStateList);
            }
            if (CollectionUtils.isNotEmpty(productQuantityList)) {
                digitalAnalytics.setProductQuantity(productQuantityList);
            }
            if (CollectionUtils.isNotEmpty(productUPCList)) {
                digitalAnalytics.setProductUPC(productUPCList);
            }
            if (wishlist.getListGuid() != null) {
                digitalAnalytics.setWishListId(wishlist.getListGuid());
            }
        }

        return digitalAnalytics;
    }

    public static KillSwitches updateProperties(KillSwitchPropertiesBean dks){
        KillSwitches ks = new KillSwitches();
        ks.setResponsiveWishlistEnabled(dks.isResponsiveWishlistEnabled());
        ks.setMspListEnabled(dks.isMspListEnabled());
        ks.setResponsiveWishlistFindEnabled(dks.isResponsiveWishlistFindEnabled());
        ks.setResponsiveWishlistPromotionsEnabled(dks.isResponsiveWishlistPromotionsEnabled());
        ks.setFinalPriceDisplayEnabled(dks.isFinalPriceDisplayEnabled());
        ks.setResponsiveCleanupExperimentEnabled(dks.isResponsiveCleanupExperimentEnabled());
        ks.setSeparateFindUsersEnabled(dks.isSeparateFindUsersEnabled());
        ks.setHeaderAsAServiceEnabled(dks.isHeaderAsAServiceEnabled());
        ks.setProsPageNavigationEnabled(dks.isProsPageNavigationEnabled());
        ks.setProsZonesAddToListEnabled(dks.isProsZonesAddToListEnabled());
        ks.setQuickViewProsZoneEnabled(dks.isQuickViewProsZoneEnabled());
        return ks;
    }

    public static String getUriHost(RequestContext restContext) {
        URI baseUri = restContext.getBaseUri();
        return baseUri != null ? baseUri.toString() : URI_HOST_DEFAULT;
    }

    public static void populateLinks(CustomerList customerList, Long userId, String userGuid, String uriHost, String customerHost) {
        if (CollectionUtils.isNotEmpty(customerList.getWishlist())) {
            customerList.getWishlist().forEach(l -> l.setLinks(buildLink(userId, userGuid, l.getListGuid(), uriHost, customerHost)));
        }
    }

    private static List<Link> buildLink(Long userId, String userGuid, String listGuid, String uriHost, String customerHost) {
        uriHost = addSlashIfNeeded(uriHost);
        customerHost = addSlashIfNeeded(customerHost);

        Link userLink = buildUserLink(userId, userGuid, customerHost);
        Link selfLink = buildSelfLink(userId, userGuid, uriHost);
        Link itemsLink = buildItemsLink(listGuid, uriHost);
        return Arrays.asList(userLink, selfLink, itemsLink);
    }

    private static String addSlashIfNeeded(String uri) {
        if (uri != null && !uri.endsWith(SLASH)) {
            uri += SLASH;
        }
        return uri;
    }

    private static Link buildUserLink(Long userId, String userGuid, String uriHost) {
        Link userLink = new Link();
        userLink.setRel(LinkTypeEnum.USER.getValue());
        if (uriHost != null) {
            if (StringUtils.isNotEmpty(userGuid)) {
                userLink.setRef(String.format(LINK_USER_BY_GUID_TEMPLATE, uriHost, userGuid));
            } else {
                userLink.setRef(String.format(LINK_USER_BY_ID_TEMPLATE, uriHost, userId));
            }
        }
        return userLink;
    }

    private static Link buildSelfLink(Long userId, String userGuid, String uriHost) {
        String ref;
        Link selfLink = new Link();
        selfLink.setRel(LinkTypeEnum.SELF.getValue());
        if (uriHost != null) {
            if (StringUtils.isNotEmpty(userGuid)) {
                ref = String.format(LINK_SELF_BY_GUID_TEMPLATE, uriHost, userGuid);
            } else {
                ref = String.format(LINK_SELF_BY_ID_TEMPLATE, uriHost, userId);
            }
            selfLink.setRef(ref);
        }
        return selfLink;

    }

    private static Link buildItemsLink(String listGuid, String uriHost) {
        Link itemsLink = new Link();
        itemsLink.setRel(LinkTypeEnum.ITEMS.getValue());
        if (uriHost != null) {
            if (StringUtils.isNotEmpty(listGuid)) {
                itemsLink.setRef(String.format(LINK_LIST_ITEMS_TEMPLATE, uriHost, listGuid));
            }
        }
        return itemsLink;
    }


}
