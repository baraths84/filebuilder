package com.macys.selection.xapi.list.util;

import com.macys.selection.xapi.list.data.converters.JsonToObjectConverter;
import com.macys.selection.xapi.list.exception.ListServiceErrorCodesEnum;
import com.macys.selection.xapi.list.exception.WishListError;
import com.macys.selection.xapi.list.exception.WishListErrors;
import org.apache.commons.collections.CollectionUtils;

public class ErrorResponseUtil {

    private static JsonToObjectConverter<WishListErrors> errorsConverter = new JsonToObjectConverter<>(WishListErrors.class);

    public static boolean evaluateErrorResponse(String responseStr) {
        boolean isError = false;
        WishListErrors wishListsResponseErrors = errorsConverter.parseJsonToObject(responseStr);
        if (wishListsResponseErrors != null && CollectionUtils.isNotEmpty(wishListsResponseErrors.getError())) {
            WishListError error = wishListsResponseErrors.getError().get(0);
            String errorCode = error.getErrorCode();
            if (String.valueOf(ListServiceErrorCodesEnum.INVALID_USER_ID.getInternalCode()).equalsIgnoreCase(errorCode)) {
                isError = true;
            }
        }
        return isError;
    }

}
