package com.macys.selection.xapi.list.services;

import com.google.common.collect.Iterables;
import com.macys.selection.xapi.list.client.response.fcc.AttributeResponse;
import com.macys.selection.xapi.list.client.response.fcc.AttributeValueResponse;
import com.macys.selection.xapi.list.client.response.fcc.AvailabilityResponse;
import com.macys.selection.xapi.list.client.response.fcc.ColorwayImageResponse;
import com.macys.selection.xapi.list.client.response.fcc.ColorwayResponse;
import com.macys.selection.xapi.list.client.response.fcc.FinalPriceResponse;
import com.macys.selection.xapi.list.client.response.fcc.PriceResponse;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.client.response.fcc.ReviewStatisticsResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.comparators.UpcResponsePriceComparator;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.rest.response.Availability;
import com.macys.selection.xapi.list.rest.response.CollaborativeList;
import com.macys.selection.xapi.list.rest.response.FinalPrice;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Product;
import com.macys.selection.xapi.list.rest.response.ReviewStatistics;
import com.macys.selection.xapi.list.rest.response.Upc;
import com.macys.selection.xapi.list.rest.response.UpcResultSet;
import com.macys.selection.xapi.list.rest.response.WishList;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import ma.glasnost.orika.MapperFacade;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ListCatalogService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ListCatalogService.class);

    private static final String ATTRIBUTE_NAMES_COLOR = "COLOR";
    private static final String ATTRIBUTE_NAMES_SIZE = "SIZE";
    private static final String ATTRIBUTE_NAMES_TYPE = "TYPE";
    private static final String ATTRIBUTE_SUPPRESS_REVIEWS = "SUPPRESS_REVIEWS";
    private static final String ATTRIBUTE_PHONE_ONLY = "PHONE_ONLY";
    private static final String ATTRIBUTE_CLICK_TO_CALL = "CLICK_TO_CALL";
    private static final String IMAGE_EXTENSION_FPX = ".fpx";
    private static final String IMAGE_EXTENSION_TIF = ".tif";
    private static final String IMAGE_FPX_PLACEHOLDER = "_fpx";
    private static final String IMAGE_OPTIMIZED_PATH = "/optimized/";
    private static final int HUNDRED_PERCENT = 100;

    private final UpcService upcService;
    private final ProductService productService;
    private final KillSwitchPropertiesBean killswitchPropertiesBean;
    private final MapperFacade mapperFacade;

    @Autowired
    public ListCatalogService(UpcService upcService,
                              ProductService productService,
                              KillSwitchPropertiesBean killswitchPropertiesBean,
                              MapperFacade mapperFacade) {
        this.upcService = upcService;
        this.productService = productService;
        this.killswitchPropertiesBean = killswitchPropertiesBean;
        this.mapperFacade = mapperFacade;
    }


    public Integer findUpcIdFromProduct(Integer productId) {
        if (productId == null) {
            return null;
        }
        Item tempItem = new Item();
        Product product = new Product();
        product.setId(productId);
        tempItem.setProduct(product);
        List<Item> items = Collections.singletonList(tempItem);
        populateProductDetailsBaseOnProductId(items, false);
        return items.get(0).getUpc() != null ? items.get(0).getUpc().getId() : null;
    }

    public void populateListsItemDetails(List<WishList> wishLists) {
        for (WishList wishList : wishLists) {
            populateWishListItemDetails(wishList, false);
        }
    }

    public void populateWishListItemDetails(WishList wishList, boolean productForUpcLevelItemRequired) {
        if (wishList != null && CollectionUtils.isNotEmpty(wishList.getItems())) {
            populateWishListItemDetails(wishList.getItems(), productForUpcLevelItemRequired);
            wishList.setImageUrlsList(buildImageUrlsList(wishList));
        }
    }

    public void populateWishListItemDetails(CollaborativeList list) {
        if (list != null && CollectionUtils.isNotEmpty(list.getItems())) {
            populateWishListItemDetails(list.getItems(), false);
            list.setImageUrlsList(buildImageUrlsList(list));
        }
    }

    public void populateWishListItemDetails(List<CollaborativeList> lists) {
        List<Item> items = new ArrayList<>();
        lists.forEach(l -> {
            if (l.getItems() != null) {
                items.addAll(l.getItems());
            }
        });
        populateWishListItemDetails(items, false);
        lists.forEach(l -> l.setImageUrlsList(buildImageUrlsList(l)));
    }

    public void populateWishListItemDetails(List<Item> items, boolean productForUpcLevelItemRequired) {
        List<UpcResultSet> upcResultSets = new ArrayList<>();

        Set<Integer> upcIds = getUpcIds(items);
        if (CollectionUtils.isNotEmpty(upcIds)) {
            upcResultSets.add(new UpcResultSet(upcService.getUpcsByUpcIds(upcIds)));
        }

        Set<Long> upcNumbers = getUpcNumbers(items);
        if (CollectionUtils.isNotEmpty(upcNumbers)) {
            upcResultSets.add(new UpcResultSet(upcService.getUpcsByUpcNumbers(upcNumbers)));
        }

        items.forEach(i -> {
            if (i.getRetailPriceDropAfterAddedToList() == null) {
                i.setRetailPriceDropAfterAddedToList(NumberUtils.DOUBLE_ZERO);
            }
            if (i.getRetailPriceDropPercentage() == null) {
                i.setRetailPriceDropPercentage(NumberUtils.INTEGER_ZERO);
            }
            if (i.getRetailPriceWhenAdded() == null) {
                i.setRetailPriceWhenAdded(NumberUtils.DOUBLE_ZERO);
            }
            if (i.getQtyStillNeeded() == null) {
                i.setQtyStillNeeded(NumberUtils.INTEGER_ZERO);
            }
        });

        if (CollectionUtils.isNotEmpty(upcResultSets) && !upcResultSets.contains(null)) {
            populateWishListItemFromUPCResult(upcResultSets, items);
        }

        populateProductDetailsBaseOnProductId(items, productForUpcLevelItemRequired);
    }

    public void populateWishListItemDetailsForNewItems(WishList wishList) {
        if (wishList == null || CollectionUtils.isEmpty(wishList.getItems())) {
            return;
        }
        populateWishListItemDetails(wishList, true);
        wishList.getItems().forEach(item -> {
            if (item.getProduct() != null && BooleanUtils.isNotFalse(item.isUpcLevelItem())
                    && (item.getProduct().isMultipleUpc() != null && item.getProduct().isMultipleUpc())) {
                item.setProduct(null);
            }
            // if upc was not found in catalog
            if (item.getUpc() != null && (!hasUpcNumber(item.getUpc()) || !hasUpcId(item.getUpc()))) {
                item.setUpc(null);
            }

            if (item.getRetailPriceWhenAdded() <= 0.0 && item.getUpc() != null && item.getUpc().getPrice() != null) {
                item.setRetailPriceWhenAdded(item.getUpc().getPrice().getRetailPrice());
            }
        });
    }

    private boolean hasUpcId(Upc upc) {
        return upc.getId() != null && upc.getId() > 0;
    }

    private boolean hasUpcNumber(Upc upc) {
        return upc.getUpcNumber() != null && upc.getUpcNumber() > 0;
    }

    private Set<Integer> getUpcIds(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptySet();
        }
        return items
                .stream()
                .map(Item::getUpc)
                .filter(Objects::nonNull)
                .map(Upc::getId)
                .filter(i -> Objects.nonNull(i) && i != 0)
                .collect(Collectors.toSet());
    }

    private Set<Long> getUpcNumbers(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptySet();
        }
        return items
                .stream()
                .map(Item::getUpc)
                .filter(Objects::nonNull)
                .map(Upc::getUpcNumber)
                .filter(i -> Objects.nonNull(i) && i != 0)
                .collect(Collectors.toSet());
    }

    private void populateWishListItemFromUPCResult(List<UpcResultSet> upcResultSets, List<Item> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            for (Item item : items) {
                // Setting UPC Level Item Flag, based on whether call came from Add or GET
                //true:
                // This can happen either for Add by UPC where product id along with it can't come,
                // Or during get call where that upc id in wish list item table is a upc level item and don't have any product id
                //false:
                // This can happen only for GET call where that UPC id also have product id in wish list item table.
                item.setUpcLevelItem(item.getProduct() == null || item.getProduct().getId() == null || item.getProduct().getId() <= 0);
                if (item.getUpc() != null) {
                    populateWishListItemFromUPCResult(upcResultSets, item, item.getUpc().getId(), item.getUpc().getUpcNumber());
                }
            }
        }
    }

    private void populateWishListItemFromUPCResult(List<UpcResultSet> upcResultSets, Item item, Integer upcId, Long upcNumber) {
        UpcResponse upcResponse = findUpcInResultSet(upcResultSets, upcId, upcNumber);
        if (upcResponse == null) {
            return;
        }
        Upc upc = new Upc();
        populateUpcDetails(upc, upcResponse);

        Product product = Optional.ofNullable(item.getProduct()).orElse(new Product());
        product.setId(upcResponse.getProductId());
        item.setProduct(product);

        Product upcProduct = new Product();
        upcProduct.setId(upcResponse.getProductId());
        upc.setProduct(upcProduct);

        if (BooleanUtils.isTrue(item.isUpcLevelItem())) {
            calculatePriceDrop(item, upcResponse);
        }
        item.setUpc(upc);
    }

    private void populateUpcDetails(Upc upc, UpcResponse upcResponse) {
        upc.setId(upcResponse.getId());
        upc.setUpcNumber(upcResponse.getUpc());
        PriceResponse priceResponse = upcResponse.getPrice();
        populateUpcPriceDetails(upc, priceResponse);
        populateUpcAvailabilityInfo(upc, priceResponse, upcResponse.getAvailability());
        populateUpcAttributes(upc, upcResponse.getAttributes());
        populateUpcPrimaryImage(upc, upcResponse.getColorway());
    }

    private UpcResponse findUpcInResultSet(List<UpcResultSet> upcResultSets, Integer upcId, Long upcNumber) {
        UpcResponse upc = null;
        for (UpcResultSet upcResultSet : upcResultSets) {
            if (upcId != null && upcId != 0 && upcResultSet.containsId(upcId)) {
                return upcResultSet.get(upcId);
            }
            if (upcNumber != null && upcResultSet.containsUpcNumber(upcNumber)) {
                return upcResultSet.getByUpcNumber(upcNumber);
            }
        }
        return upc;
    }

    private void populateUpcPriceDetails(Upc upc, PriceResponse priceResponse) {
        Price price = Optional.ofNullable(upc.getPrice()).orElse(new Price());
        PriceResponse response = Optional.ofNullable(priceResponse).orElse(new PriceResponse());
        mapperFacade.map(response, price);
        upc.setPrice(price);
    }

    private void populateUpcAvailabilityInfo(Upc upc, PriceResponse priceResponse, AvailabilityResponse availabilityResponse) {
        Availability availability = new Availability();
        if (priceResponse != null && availabilityResponse != null) {
            availability.setAvailable(availabilityResponse.isAvailable());
            availability.setUpcAvailabilityMessage(availabilityResponse.getUpcAvailabilityMessage());
            availability.setInStoreEligible(availabilityResponse.getInStoreEligibility());
            availability.setOrderMethod(availabilityResponse.getOrderMethod());
        } else {
            availability.setAvailable(false);
            availability.setInStoreEligible(false);
        }
        upc.setAvailability(availability);
    }

    private void populateUpcAttributes(Upc upc, List<AttributeResponse> attributes) {
        if (CollectionUtils.isEmpty(attributes)) {
            return;
        }
        attributes.forEach(a -> {
            if (CollectionUtils.isNotEmpty(a.getAttributeValues())) {
                Optional<AttributeValueResponse> firstValue = a.getAttributeValues().stream().findFirst();
                if (firstValue.isPresent() && StringUtils.isNotEmpty(firstValue.get().getValue())) {
                    switch (a.getName()) {
                        case ATTRIBUTE_NAMES_COLOR:
                            upc.setColor(firstValue.get().getValue());
                            break;
                        case ATTRIBUTE_NAMES_SIZE:
                            upc.setSize(firstValue.get().getValue());
                            break;
                        case ATTRIBUTE_NAMES_TYPE:
                            upc.setType(firstValue.get().getValue());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void populateUpcPrimaryImage(Upc upc, ColorwayResponse colorwayResponse) {
        if (colorwayResponse != null && colorwayResponse.getPrimaryImage() != null
                && (StringUtils.isNotBlank(colorwayResponse.getPrimaryImage().getImageName()))) {
            upc.setUpcPrimaryImageName(colorwayResponse.getPrimaryImage().getImageName());
        }
    }

    private void calculatePriceDropForProductLevelItem(Item item, ProductResponse productResponse) {
        if (productResponse.getUpcs() == null) {
            return;
        }
        List<UpcResponse> upcs = productResponse.getUpcs()
                .stream()
                .filter(u -> u != null && u.getAvailability() != null && u.getAvailability().isAvailable())
                .sorted(new UpcResponsePriceComparator())
                .collect(Collectors.toList());
        if (!upcs.isEmpty()) {
            UpcResponse highestPriceUPC = Iterables.getLast(upcs);
            calculatePriceDrop(item, highestPriceUPC);
        }
    }

    /**
     * This method is used to calculate percentage of drop in price since item has been added to list
     */
    private void calculatePriceDrop(Item item, UpcResponse upcResponse) {
        int priceDropPercentage;
        double priceDrop;
        DecimalFormat f = new DecimalFormat("##.00");
        PriceResponse price = upcResponse.getPrice();
        if (price != null && BooleanUtils.isTrue(price.isOnSale()) &&
                isSalesPriceLessThenRetailPriceWhenAdded(price.getRetailPrice(), item.getRetailPriceWhenAdded())) {
            priceDrop = item.getRetailPriceWhenAdded() - price.getRetailPrice();
            priceDropPercentage = (int) (((priceDrop) / item.getRetailPriceWhenAdded()) * HUNDRED_PERCENT);
            item.setRetailPriceDropPercentage(priceDropPercentage);
            item.setRetailPriceDropAfterAddedToList(Double.parseDouble(f.format(priceDrop)));
        }
    }

    private boolean isSalesPriceLessThenRetailPriceWhenAdded(Double retailPrice, Double retailPriceWhenAdded) {
        return retailPrice != null && retailPrice > 0 && retailPrice < retailPriceWhenAdded;
    }

    private void populateProductDetailsBaseOnProductId(List<Item> items, boolean productForUpcLevelItemRequired) {
        LOGGER.debug("START: populateProductDetailsBaseOnProductId {}", items);

        try {
            Set<Integer> productIds = getItemProductIds(items);
            if (productIds.isEmpty()) {
                LOGGER.debug("There are no products defined in wish list");
            } else {
                Map<Integer, ProductResponse> products = productService.getProductsByProdIds(productIds)
                        .stream()
                        .collect(Collectors.toMap(ProductResponse::getId, Function.identity(), (p1, p2) -> p1));
                for (Item item : items) {
                    if (item.getProduct() != null) {
                        Integer productId = item.getProduct().getId();
                        item.setProduct(null); //should be null if it AddByUPC or AddByProductId with single UPC (UPC Level Item)
                        populateProductDetailsBaseOnProductId(products, item, productId, productForUpcLevelItemRequired);
                    }
                }
            }
        } catch (ListServiceException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("error while populate product details base on productId", e);
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
        }

    }

    private Set<Integer> getProductIds(List<WishList> wishLists) {
        Set<Integer> productIdList = new HashSet<>();

        if (CollectionUtils.isNotEmpty(wishLists)) {
            for (WishList wishList : wishLists) {
                productIdList.addAll(getItemProductIds(wishList.getItems()));
            }
        }
        return productIdList;
    }

    private Set<Integer> getItemProductIds(List<Item> items) {
        if (CollectionUtils.isEmpty(items)) {
            return Collections.emptySet();
        }
        return items
                .stream()
                .filter(Objects::nonNull)
                .map(Item::getProduct)
                .filter(Objects::nonNull)
                .map(Product::getId)
                .filter(i -> Objects.nonNull(i) && i > 0)
                .collect(Collectors.toSet());
    }

    private void populateProductDetailsBaseOnProductId(Map<Integer, ProductResponse> products, Item item, Integer productId, boolean productForUpcLevelItemRequired) {
        if (productId == null) {
            return;
        }

        ProductResponse productResponse = products.get(productId);
        if (productResponse != null) {
            assertNotMasterProduct(productResponse);
            boolean hasUpc = item.getUpc() != null && item.getUpc().getId() != null && item.getUpc().getId() > 0;
            if (BooleanUtils.isNotTrue(item.isUpcLevelItem()) || productForUpcLevelItemRequired) {
                populateProductWithinItem(item, productResponse, hasUpc);
            }
            if (hasUpc) {
                populateProductWithinUpc(item, productResponse);
            } else {
                populateUpcFromProductResult(item, productResponse);
            }
            if (BooleanUtils.isFalse(item.isUpcLevelItem())) {
                calculatePriceDropForProductLevelItem(item, productResponse);
            }
            buildImageUrlsForDisplay(item.getUpc(), item.getProduct());
        } else {
            // This will happen only for Add by UPC Call
            item.setUpcLevelItem(true);
        }
    }

    private void assertNotMasterProduct(ProductResponse productResponse) {
        boolean isMasterProduct = CollectionUtils.isNotEmpty(productResponse.getMemberProductIds());
        if (isMasterProduct) {
            throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.INVALID_MASTER_PRODUCT);
        }
    }

    private void populateProductWithinItem(Item item, ProductResponse productResponse, boolean hasUpc) {
        item.setProduct(new Product());
        populateProductDetails(item.getProduct(), productResponse);
        populateProductPriceDetails(item.getProduct(), productResponse.getPrice());
        if (!hasUpc) {
            populateProductFinalPriceAttributes(item.getProduct(), productResponse.getFinalPrice());
        }
        item.getProduct().setMultipleUpc(CollectionUtils.isNotEmpty(productResponse.getUpcs()) && productResponse.getUpcs().size() > 1);
    }

    private void populateProductWithinUpc(Item item, ProductResponse productResponse) {
        Product upcProduct = Optional.ofNullable(item.getUpc().getProduct()).orElse(new Product());
        populateProductDetails(upcProduct, productResponse);
        populateUpcPriceLabelAttributesBasedOnProductPriceResponse(item.getUpc(), productResponse.getPrice(), item.getRetailPriceWhenAdded());
        populateUpcFinalPriceAttributesBasedOnProductUpcsResponse(item.getUpc(), productResponse.getUpcs());
        item.getUpc().setProduct(upcProduct);
    }

    private void populateUpcFromProductResult(Item item, ProductResponse productResponse) {
        // This can only happen for Add by product call, where upc id cannot come along
        // Or GET Call where we have Product Level data in wish list item table
        Upc upc = new Upc();
        if (CollectionUtils.isNotEmpty(productResponse.getUpcs())) {
            // It will be only for Add, GET call should never come here
            if (productResponse.getUpcs().size() > 1) {
                Optional<UpcResponse> lowestPriceUPC = productResponse.getUpcs()
                        .stream()
                        .filter(u -> u != null && u.getAvailability() != null && u.getAvailability().isAvailable())
                        .min(new UpcResponsePriceComparator());
                lowestPriceUPC.ifPresent(u -> {
                    item.setRetailPriceWhenAdded(u.getPrice().getRetailPrice());
                    populateUpcDetails(upc, u);
                    upc.setProduct(new Product());
                    populateProductDetails(upc.getProduct(), productResponse);
                    populateUpcPriceLabelAttributesBasedOnProductPriceResponse(upc, productResponse.getPrice(), item.getRetailPriceWhenAdded());
                    item.setUpc(upc);
                    // As it is a product level item now
                    item.setUpcLevelItem(false);
                });
            } else {
                // If single UPC consider it as UPC level Item, and will remove the product Info from table later
                // Copying UPC details from assigned UPC
                populateUpcDetails(upc, productResponse.getUpcs().get(0));
                upc.setProduct(new Product());
                populateProductDetails(upc.getProduct(), productResponse);
                populateUpcPriceLabelAttributesBasedOnProductPriceResponse(upc, productResponse.getPrice(), item.getRetailPriceWhenAdded());
                item.setUpc(upc);
                item.setUpcLevelItem(true);
            }
        } else {
            // This should never happen that product has no UPC's
            item.setUpc(new Upc());
        }
    }

    private void populateProductDetails(Product product, ProductResponse productResponse) {
        product.setId(productResponse.getId());
        product.setName(productResponse.getName());
        product.setActive(productResponse.isActive());
        product.setLive(productResponse.isLive());
        product.setAvailable(productResponse.getAvailable());
        populateProductAttributes(product, productResponse.getAttributes());
        populateReviewStats(product, productResponse.getReviewStatistics());
        populateProductImage(product, productResponse.getPrimaryImage());
    }

    private void populateProductImage(Product product, ColorwayImageResponse colorwayImageResponse) {
        if (colorwayImageResponse != null && StringUtils.isNotBlank(colorwayImageResponse.getImageName())) {
            product.setPrimaryImage(colorwayImageResponse.getImageName());
        }
    }

    /**
     * This method generates the image url to be displayed for ui.To avoid
     * product thumbnail call
     */
    private void buildImageUrlsForDisplay(Upc upc, Product product) {
        Optional<Upc> nullableUpc = Optional.ofNullable(upc);
        Optional<Product> nullableProduct = Optional.ofNullable(product);
        String primaryImage = null;
        if (nullableUpc.isPresent() && StringUtils.isNotBlank(nullableUpc.get().getUpcPrimaryImageName())) {
            primaryImage = nullableUpc.get().getUpcPrimaryImageName();
        } else if (nullableProduct.isPresent() && StringUtils.isNotBlank(nullableProduct.get().getPrimaryImage())) {
            primaryImage = nullableProduct.get().getPrimaryImage();
        }
        String imageUrl = constructImageUrl(primaryImage);
        nullableUpc.ifPresent(u -> {
            if (u.getProduct() != null) {
                u.getProduct().setImageURL(imageUrl);
            }
        });
        nullableProduct.ifPresent(p -> p.setImageURL(imageUrl));
    }

    private String constructImageUrl(String imgName) {
        String buildImageUrl = null;
        if (imgName != null) {
            int index = imgName.indexOf(IMAGE_EXTENSION_FPX);
            if (index != -1) {
                String buildImageName = imgName.replace(IMAGE_EXTENSION_FPX, IMAGE_FPX_PLACEHOLDER + IMAGE_EXTENSION_TIF);
                buildImageUrl = imgName.charAt(index - 1) + IMAGE_OPTIMIZED_PATH + buildImageName;
            }
        }
        return buildImageUrl;
    }

    private List<String> buildImageUrlsList(WishList wishList) {
        List<String> imageUrlsList = new ArrayList<>();
        if ((wishList != null && CollectionUtils.isNotEmpty(wishList.getItems()))) {
            imageUrlsList = getItemImages(wishList.getItems());
        }
        return imageUrlsList;
    }

    private List<String> buildImageUrlsList(CollaborativeList list) {
        List<String> imageUrlsList = new ArrayList<>();
        if ((list != null && CollectionUtils.isNotEmpty(list.getItems()))) {
            imageUrlsList = getItemImages(list.getItems());
        }
        return imageUrlsList;
    }

    private List<String> getItemImages(List<Item> items) {
        List<String> imageUrlsList = new ArrayList<>();
        if ((CollectionUtils.isNotEmpty(items))) {
            imageUrlsList = items
                    .stream()
                    .map(i -> {
                        String imageUrl = null;
                        if (i.getUpc() != null && i.getUpc().getProduct() != null && i.getUpc().getProduct().getImageURL() != null) {
                            imageUrl = i.getUpc().getProduct().getImageURL();
                        } else if (i.getProduct() != null && i.getProduct().getImageURL() != null) {
                            imageUrl = i.getProduct().getImageURL();
                        }
                        return imageUrl;
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        }
        return imageUrlsList;
    }

    private void populateProductAttributes(Product product, List<AttributeResponse> attributes) {
        if (CollectionUtils.isEmpty(attributes)) {
            return;
        }
        attributes.forEach(a -> {
            if (CollectionUtils.isNotEmpty(a.getAttributeValues())) {
                AttributeValueResponse firstValue = a.getAttributeValues().stream().findFirst().orElse(null);
                if (firstValue != null && StringUtils.isNotEmpty(firstValue.getValue())) {
                    switch (a.getName()) {
                        case ATTRIBUTE_SUPPRESS_REVIEWS:
                            product.setSuppressReviews(true);
                            break;
                        case ATTRIBUTE_PHONE_ONLY:
                            product.setPhoneOnly(firstValue.getValue());
                            break;
                        case ATTRIBUTE_CLICK_TO_CALL:
                            product.setClickToCall(firstValue.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }

    private void populateReviewStats(Product product, ReviewStatisticsResponse reviewStatisticsResponse) {
        if (reviewStatisticsResponse != null) {
            ReviewStatistics reviewStatistics = new ReviewStatistics();
            if (reviewStatisticsResponse.getAverageRating() != null) {
                Float avgReviewRating = reviewStatisticsResponse.getAverageRating().floatValue();
                reviewStatistics.setAverageRating(avgReviewRating);
            }
            reviewStatistics.setReviewCount(reviewStatisticsResponse.getReviewCount());
            product.setReviewStatistics(reviewStatistics);
        }
    }

    private void populateProductPriceDetails(Product product, PriceResponse priceResponse) {
        Price price = new Price();
        if (priceResponse != null) {
            mapperFacade.map(priceResponse, price);
            price.setBasePriceType(null);
            price.setSalesValue(null);
        }
        product.setPrice(price);
    }

    private void populateUpcFinalPriceAttributesBasedOnProductUpcsResponse(Upc upc, List<UpcResponse> productUpcs) {
        if (CollectionUtils.isNotEmpty(productUpcs) && killswitchPropertiesBean.isFinalPriceDisplayEnabled()) {
            Optional<UpcResponse> upcResponse = productUpcs.stream().filter(u -> u.getFinalPrice() != null && u.getId().equals(upc.getId())).findFirst();
            upcResponse.ifPresent(u -> upc.setFinalPrice(mapperFacade.map(u.getFinalPrice(), FinalPrice.class)));
        }
    }

    private void populateProductFinalPriceAttributes(Product product, FinalPriceResponse finalPriceResponse) {
        if (finalPriceResponse != null && killswitchPropertiesBean.isFinalPriceDisplayEnabled()) {
            product.setFinalPrice(mapperFacade.map(finalPriceResponse, FinalPrice.class));
        }
    }

    private void populateUpcPriceLabelAttributesBasedOnProductPriceResponse(Upc upc, PriceResponse priceResponse, Double retailPriceWhenAdded) {
        if (priceResponse != null) {
            Price upcPrice = Optional.ofNullable(upc.getPrice()).orElse(new Price());
            if (StringUtils.isEmpty(upcPrice.getOriginalPriceLabel()) && upcPrice.getOriginalPrice() != null) {
                double originalPrice = 0.0;
                if (upcPrice.getOriginalPrice() > 0.0) {
                    originalPrice = upcPrice.getOriginalPrice();
                }
                if (upcPrice.getOriginalPrice() > 0.0 && originalPrice == priceResponse.getOriginalPrice()) {
                    upcPrice.setOriginalPriceLabel(priceResponse.getOriginalPriceLabel());
                }
            }
            if (StringUtils.isEmpty(upcPrice.getRetailPriceLabel()) && isRetailPriceSameSinceAdded(retailPriceWhenAdded, priceResponse.getRetailPrice())) {
                upcPrice.setRetailPriceLabel(priceResponse.getRetailPriceLabel());
            }
            upc.setPrice(upcPrice);
        }
    }

    private boolean isRetailPriceSameSinceAdded(Double retailPriceWhenAdded, Double retailPrice) {
        return retailPriceWhenAdded != null && retailPriceWhenAdded > 0.0 && retailPriceWhenAdded.equals(retailPrice);
    }

    // B-81460 populate list item image urls list
    public void populateListItemImageUrlsList(List<WishList> wishLists) {
        Set<Integer> upcIds = getAllUpcIds(wishLists);
        List<UpcResultSet> upcResultSets = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(upcIds) && CollectionUtils.isNotEmpty(wishLists)) {
            upcResultSets.add(new UpcResultSet(upcService.getUpcsByUpcIds(upcIds)));

            if (!upcResultSets.isEmpty() && !upcResultSets.contains(null)) {
                for (WishList wishList : wishLists) {
                    if (wishList != null && CollectionUtils.isNotEmpty(wishList.getItems())) {
                        populateWishListItemFromUPCResult(upcResultSets, wishList.getItems());
                    }
                }
            }
            populateListItemImageUrlsFromProduct(wishLists);
            wishLists.forEach(w -> w.setImageUrlsList(buildImageUrlsList(w)));
        }
    }

    public void copyItemCatalogDetails(WishList sourceList, WishList destinationList) {
        if (CollectionUtils.isNotEmpty(sourceList.getItems())) {
            destinationList.getItems().forEach(destination ->
                    sourceList.getItems().forEach(source -> {
                        if (source.isUpcLevelItem() ?
                                source.getUpc() != null && isSameUPC(destination, source.getUpc().getId()) :
                                source.getProduct() != null && isSameProduct(destination, source.getProduct().getId())) {
                            copyItemCatalogDetails(source, destination);
                        }
                    })
            );
        }
    }

    protected void copyItemCatalogDetails(Item source, Item destination) {
        if (source != null && destination != null) {
            if (source.isUpcLevelItem() != null && !source.isUpcLevelItem()) {
                destination.setProduct(source.getProduct());
            } else {
                destination.setProduct(null);
            }
            destination.setUpc(source.getUpc());
            destination.setUpcLevelItem(source.isUpcLevelItem());
        }
    }

    public boolean isSameUPC(Item item, Integer upcId) {
        return upcId != null && item.getUpc() != null &&
                upcId.equals(item.getUpc().getId());
    }

    public boolean isSameProduct(Item item, Integer productId) {
        return productId != null && item.getProduct() != null &&
                productId.equals(item.getProduct().getId());
    }

    private Set<Integer> getAllUpcIds(List<WishList> wishLists) {
        Set<Integer> upcIds = new HashSet<>();
        if (CollectionUtils.isNotEmpty(wishLists)) {
            wishLists.stream().filter(Objects::nonNull).forEach(w -> upcIds.addAll(getUpcIds(w.getItems())));
        }
        return upcIds;
    }

    private void populateListItemImageUrlsFromProduct(List<WishList> wishLists) {
        Set<Integer> productIds = getProductIds(wishLists);
        if (productIds.isEmpty()) {
            LOGGER.debug("There are no products defined in wish list");
        } else {
            try {
                Map<Integer, ProductResponse> products = productService.getProductsByProdIds(productIds)
                        .stream()
                        .collect(Collectors.toMap(ProductResponse::getId, Function.identity(), (p1, p2) -> p1));
                for (WishList wishList : wishLists) {
                    if (CollectionUtils.isNotEmpty(wishList.getItems())) {
                        for (Item item : wishList.getItems()) {
                            if (item.getProduct() == null || item.getProduct().getId() == null) {
                                continue;
                            }
                            ProductResponse productResponse = products.get(item.getProduct().getId());
                            if (productResponse != null) {
                                item.setUpc(Optional.ofNullable(item.getUpc()).orElse(new Upc()));
                                item.getUpc().setProduct(Optional.ofNullable(item.getUpc().getProduct()).orElse(new Product()));

                                populateProductImage(item.getUpc().getProduct(), productResponse.getPrimaryImage());
                                populateProductImage(item.getProduct(), productResponse.getPrimaryImage());
                                buildImageUrlsForDisplay(item.getUpc(), item.getProduct());
                            }
                        }
                    }
                }
            } catch (ListServiceException e) {
                throw e;
            } catch (Exception e) {
                LOGGER.error("error while populate list item image urls from product", e);
                throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
            }
        }
    }
}
