package com.macys.selection.xapi.list.client;

import com.macys.platform.rest.framework.client.api.RestClientFactory;
import com.macys.platform.rest.framework.client.exception.RestClientException;
import com.macys.platform.rest.framework.fault.v2.entity.PlatformErrorBinding;
import com.macys.selection.xapi.list.client.response.fcc.ProductResponse;
import com.macys.selection.xapi.list.client.response.fcc.ProductSetResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcResponse;
import com.macys.selection.xapi.list.client.response.fcc.UpcSetResponse;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.ListServiceException;
import com.macys.selection.xapi.list.util.KillSwitchPropertiesBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FccRestClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(FccRestClient.class);

    private static final String PARAMS_DIVIDER = ",";
    private static final String ACTIVE_QUERY_PARAM = "active";

    private final RestClientFactory.JaxRSClientPool upcClientPool;
    private final RestClientFactory.JaxRSClientPool productClientPool;
    private final Map<String, List<String>> upcFieldsQuery;
    private final List<String> productFieldsQuery;
    private final KillSwitchPropertiesBean killswitchPropertiesBean;

    @Autowired
    public FccRestClient(@Qualifier(WishlistConstants.UPC_CLIENT_POOL) RestClientFactory.JaxRSClientPool upcClientPool,
                         @Value("#{upcFieldsQuery}") Map<String, List<String>> upcFieldsQuery,
                         @Qualifier(WishlistConstants.PRODUCT_CLIENT_POOL) RestClientFactory.JaxRSClientPool productClientPool,
                         @Value("#{productFieldsQuery}") List<String> productFieldsQuery,
                         KillSwitchPropertiesBean killswitchPropertiesBean) {
        this.upcClientPool = upcClientPool;
        this.upcFieldsQuery = upcFieldsQuery;
        this.productClientPool = productClientPool;
        this.productFieldsQuery = productFieldsQuery;
        this.killswitchPropertiesBean = killswitchPropertiesBean;
    }

    public List<UpcResponse> getUpcsByIds(Collection<Integer> upcIds) throws RestClientException {
        LOGGER.debug("START :: getUpcsByIds : upcIds - {}", upcIds);
        Optional<Response> response = Optional.empty();
        try {
            String upcFields = appendQueryFields(upcFieldsQuery);

            response = Optional.ofNullable(upcClientPool.getRxClient(WishlistConstants.UPC_CLIENT_POOL)
                    .target(upcClientPool.getHostName())
                    .path(upcClientPool.getBasePath())
                    .path(WishlistConstants.UPCS_BY_IDS_PATH)
                    .resolveTemplate(WishlistConstants.UPCS_IDS,
                            upcIds.stream().map(String::valueOf).collect(Collectors.joining(PARAMS_DIVIDER)))
                    .queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, upcFields)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class));

            response.ifPresent(r -> {
                if (r.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                    LOGGER.error("exception retrieving upcs for upcIds {} with responseStatus {} ", upcIds.toString(), r.getStatus());
                    throw new ListServiceException(r.getStatus(), r.readEntity(String.class));
                }
            });

            List<UpcResponse> upcs = response.map(u -> buildUpcSetFromResponse(upcIds.size(), u))
                    .map(UpcSetResponse::getUpcs)
                    .orElse(Collections.emptyList());
            if (CollectionUtils.isEmpty(upcs)) {
                LOGGER.error("Wish list FCC:: Could not look up upc info from fcc ");
                throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
            }
            LOGGER.debug("END :: getUpcsByIds");
            return upcs;

        } catch (RestClientException e) {
            LOGGER.error("Rest Error while getUpcsByIds from fcc:: upcs - {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("General Error while getUpcsByIds from fcc: {}", e.getCause());
            throw e;
        } finally {
            response.ifPresent(Response::close);
        }
    }

    public List<UpcResponse> getUpcsByUpcNumbers(Collection<Long> upcNumbers) throws RestClientException {
        LOGGER.debug("START :: getUpcsByUpcNumbers : upcNumbers - {}", upcNumbers);
        Optional<Response> response = Optional.empty();

        try {
            String upcFields = appendQueryFields(upcFieldsQuery);

            response = Optional.ofNullable(upcClientPool.getRxClient(WishlistConstants.UPC_CLIENT_POOL)
                    .target(upcClientPool.getHostName())
                    .path(upcClientPool.getBasePath())
                    .path(WishlistConstants.UPCS_PATH)
                    .queryParam(WishlistConstants.UPCS_NUMBERS, upcNumbers.stream().map(String::valueOf).collect(Collectors.joining(PARAMS_DIVIDER)))
                    .queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, upcFields)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class));

            response.ifPresent(r -> {
                if (r.getStatus() != WishlistConstants.STATUS_SUCCESS) {
                    LOGGER.error("exception retrieving upcs for upcNumbers {} with responseStatus {} ", upcNumbers.toString(), r.getStatus());
                    throw new ListServiceException(r.getStatus(), r.readEntity(String.class));
                }
            });

            List<UpcResponse> upcs = response.map(u -> buildUpcSetFromResponse(upcNumbers.size(), u))
                    .map(UpcSetResponse::getUpcs)
                    .orElse(Collections.emptyList());

            if (CollectionUtils.isEmpty(upcs)) {
                LOGGER.error("Wish list FCC:: Could not look up upc info from fcc ");
                throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
            }
            LOGGER.debug("END :: getUpcsByIds");
            return upcs;
        } catch (RestClientException e) {
            LOGGER.error("Rest Error while getUpcsByUpcNumbers from fcc:: upcs - {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            LOGGER.error("General Error while getUpcsByUpcNumbers from fcc: {}", e.getCause());
            throw e;
        } finally {
            response.ifPresent(Response::close);
        }
    }

    public ProductSetResponse getProductsByProdIds(Collection<Integer> productIds) throws RestClientException {

        if (CollectionUtils.isEmpty(productIds)) {
            LOGGER.warn("ProductIds list should not be empty");
            return null;
        }

        Response response = null;

        try {
            String productFields = productFieldsQuery.stream().collect(Collectors.joining(","));
            String productIdsParam = productIds.stream().filter(Objects::nonNull)
                    .map(Object::toString).collect(Collectors.joining(","));

            response = productClientPool.getRxClient(WishlistConstants.PRODUCT_CLIENT_POOL)
                    .target(productClientPool.getHostName())
                    .path(productClientPool.getBasePath())
                    .path(WishlistConstants.PRODUCTS_BY_IDS_PATH)
                    .resolveTemplate(WishlistConstants.PRODUCT_IDS, productIdsParam)
                    .queryParam(WishlistConstants.PATH_PARAMETER_FIELDS_NAME, productFields)
                    .queryParam(ACTIVE_QUERY_PARAM, true)
                    .queryParam(WishlistConstants.INCLUDE_FINAL_PRICE, killswitchPropertiesBean.isFinalPriceDisplayEnabled())
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .get(Response.class);

            if (response.getStatus() == WishlistConstants.STATUS_SUCCESS) {
                ProductSetResponse products = buildProductSetFromResponse(productIds.size(), response);
                if (products == null) {
                    LOGGER.error("Wish list FCC:: Could not look up product info from fcc ");
                    throw new ListServiceException(Response.Status.BAD_REQUEST.getStatusCode(), ListServiceErrorCodesEnum.CATALOG_LOOKUP_ERROR);
                }
                return products;
            } else {
                PlatformErrorBinding error = response.readEntity(PlatformErrorBinding.class);
                throw new ListServiceException(error.getDeveloperMessage());
            }

        } catch (RestClientException rcEx) {
            LOGGER.error("Rest client Error occured while fetching Product by ProductIds :: " + rcEx.getMessage());
            throw rcEx;
        } catch (Exception e) {
            LOGGER.error("General Error occured while fetching Product by ProductIds :: " + e);
            throw e;
        } finally {
            try {
                Optional.ofNullable(response).ifPresent(Response::close);
            } catch (Exception e) {
                LOGGER.debug("ProductRestClient.getProductsByProdIds() :: prodIds : " + productIds
                        + " : Exception while closing Response : ", e);
            }
        }
    }

    protected ProductSetResponse buildProductSetFromResponse(int numberOfProduct, Response response) {
        ProductSetResponse products = null;

        if (numberOfProduct > 1) {
            products = response.readEntity(ProductSetResponse.class);
        } else if (numberOfProduct == 1) {
            ProductResponse product = response.readEntity(ProductResponse.class);
            products = buildProductSet(product);
        }

        return products;
    }

    protected ProductSetResponse buildProductSet(ProductResponse product) {
        ProductSetResponse products = null;
        if (product != null && product.getId() != null) {
            products = new ProductSetResponse();
            products.setProduct(Arrays.asList(product));
        }
        return products;
    }
    private String appendQueryFields(Map<String, List<String>> queryFields) {
        if (MapUtils.isNotEmpty(queryFields)) {
            return queryFields.entrySet()
                    .stream()
                    .map(f -> String.join(PARAMS_DIVIDER, f.getValue()))
                    .collect(Collectors.joining(PARAMS_DIVIDER));
        } else {
            return StringUtils.EMPTY;
        }
    }

    private UpcSetResponse buildUpcSetFromResponse(int numberOfUPCs, Response upcResponse) {
        UpcSetResponse upcSet = null;
        if (numberOfUPCs > 1) {
            upcSet = upcResponse.readEntity(UpcSetResponse.class);
        } else if (numberOfUPCs == 1) {
            upcSet = new UpcSetResponse();
            upcSet.setUpcs(Collections.singletonList(upcResponse.readEntity(UpcResponse.class)));
        }
        return upcSet;
    }

}
