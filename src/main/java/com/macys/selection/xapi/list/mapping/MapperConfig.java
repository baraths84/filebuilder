package com.macys.selection.xapi.list.mapping;

import com.macys.platform.util.converter.PlatformMapperFactory;
import com.macys.platform.util.converter.impl.PlatformMapperFactoryImpl;
import com.macys.selection.xapi.list.client.request.EmailShare;
import com.macys.selection.xapi.list.client.response.ActivityLogPageDTO;
import com.macys.selection.xapi.list.client.response.CollaboratorDTO;
import com.macys.selection.xapi.list.client.response.EmailItemDTO;
import com.macys.selection.xapi.list.client.response.EmailShareDTO;
import com.macys.selection.xapi.list.client.response.ItemDTO;
import com.macys.selection.xapi.list.client.response.WishListDTO;
import com.macys.selection.xapi.list.client.response.WishListsDTO;
import com.macys.selection.xapi.list.client.response.fcc.FinalPriceResponse;
import com.macys.selection.xapi.list.client.response.fcc.PriceResponse;
import com.macys.selection.xapi.list.client.response.user.ProfileResponse;
import com.macys.selection.xapi.list.client.response.user.UserResponse;
import com.macys.selection.xapi.list.rest.response.ActivityLog;
import com.macys.selection.xapi.list.rest.response.CollaborativeItem;
import com.macys.selection.xapi.list.rest.response.CollaborativeListDetails;
import com.macys.selection.xapi.list.rest.response.Collaborator;
import com.macys.selection.xapi.list.rest.response.CustomerList;
import com.macys.selection.xapi.list.rest.response.FinalPrice;
import com.macys.selection.xapi.list.rest.response.Item;
import com.macys.selection.xapi.list.rest.response.Price;
import com.macys.selection.xapi.list.rest.response.Profile;
import com.macys.selection.xapi.list.rest.response.User;
import com.macys.selection.xapi.list.rest.response.UserProfile;
import com.macys.selection.xapi.list.rest.response.WishList;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MappingContext;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
public class MapperConfig {

    @Bean
    public MapperFacade mapperFacade() {
        return mapperFactory().getMapperFacade();
    }

    @Bean
    public PlatformMapperFactory mapperFactory() {
        PlatformMapperFactory platformMapperFactory = new PlatformMapperFactoryImpl.Builder().build();

        platformMapperFactory.classMap(WishListsDTO.class, CustomerList.class)
                .field("lists", "wishlist")
                .byDefault().register();

        platformMapperFactory.classMap(WishListDTO.class, WishList.class)
                .field("listId", "id")
                .byDefault().register();

        platformMapperFactory.classMap(ItemDTO.class, Item.class)
                .field("itemId", "id")
                .field("upcId", "upc.id")
                .field("productId", "product.id")
                .byDefault().register();

        platformMapperFactory.classMap(EmailShareDTO.class, EmailShare.class)
                .byDefault().register();

        platformMapperFactory.classMap(Item.class, EmailItemDTO.class)
                .fieldAToB("product.id", "productId")
                .field("product.name", "productName")
                .field("product.imageURL", "imageUrl")
                .fieldAToB("upc.upcNumber", "upcNumber")
                .byDefault().register();

        platformMapperFactory.classMap(UserResponse.class, User.class)
                .byDefault().register();

        platformMapperFactory.classMap(ProfileResponse.class, Profile.class)
                .byDefault().register();

        platformMapperFactory.classMap(UserResponse.class, UserProfile.class)
                .fieldAToB("profile.firstName", "firstName")
                .fieldAToB("profile.lastName", "lastName")
                .byDefault().register();

        platformMapperFactory.classMap(PriceResponse.class, Price.class)
                .field("saleValue", "salesValue")
                .field("intermediatePrice", "intermediateSalesValue")
                .customize(new CustomMapper<PriceResponse, Price>() {
                    @Override
                    public void mapAtoB(PriceResponse priceResponse, Price price, MappingContext context) {
                        super.mapAtoB(priceResponse, price, context);
                        price.setPriceType(Optional.ofNullable(priceResponse.getPriceType()).orElse(0));
                        price.setOnSale(BooleanUtils.isTrue(priceResponse.isOnSale()));
                        price.setBasePriceType(Optional.ofNullable(priceResponse.getBasePriceType()).orElse(0));
                        price.setSalesValue(Optional.ofNullable(priceResponse.getSaleValue()).orElse(NumberUtils.DOUBLE_ZERO));
                        price.setRetailPrice(Optional.ofNullable(priceResponse.getRetailPrice()).orElse(NumberUtils.DOUBLE_ZERO));
                        price.setOriginalPrice(Optional.ofNullable(priceResponse.getOriginalPrice()).orElse(NumberUtils.DOUBLE_ZERO));
                        price.setIntermediateSalesValue(Optional.ofNullable(priceResponse.getIntermediatePrice()).orElse(NumberUtils.DOUBLE_ZERO));
                        price.setUpcOnSale(BooleanUtils.isTrue(priceResponse.isUpcOnSale()));
                    }
                })
                .byDefault().register();

        platformMapperFactory.classMap(FinalPriceResponse.class, FinalPrice.class)
                .byDefault().register();

        platformMapperFactory.classMap(ActivityLogPageDTO.class, ActivityLog.class)
                .byDefault().register();

        platformMapperFactory.classMap(WishList.class, CollaborativeListDetails.class)
                .byDefault().register();

        platformMapperFactory.classMap(Item.class, CollaborativeItem.class )
                .byDefault().register();

        platformMapperFactory.classMap(CollaboratorDTO.class, Collaborator.class )
                .byDefault().register();

        return platformMapperFactory;
    }
}
