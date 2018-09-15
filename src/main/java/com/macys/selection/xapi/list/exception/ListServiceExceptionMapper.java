package com.macys.selection.xapi.list.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.google.common.collect.Lists;
import com.macys.platform.rest.core.fault.AbstractPlatformExceptionMapper;
import com.macys.platform.rest.core.fault.ErrorBinding;
import com.macys.platform.rest.core.fault.ErrorsBinding;
import com.macys.selection.xapi.list.common.WishlistConstants;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserCustomerLists;
import com.macys.selection.xapi.list.data.converters.JsonResponseParserServiceErrors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.Provider;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 *  Service exception mapper
 **/

@Provider
@Component
public class ListServiceExceptionMapper extends AbstractPlatformExceptionMapper<ListServiceException> {
	private static final Logger LOGGER = LoggerFactory.getLogger(ListServiceExceptionMapper.class);
	private static final Integer LIST_XAPI_MODULE_ID = 50;
	private static final String  ERROR_CODE = "errorCode";
	private static final String  ERROR_MESSAGE = "message";
	
	@Autowired
	private JsonResponseParserCustomerLists customerLists;
	
	@Autowired
	private JsonResponseParserServiceErrors serviceErrors;
	
	@Autowired
	private ExceptionProperties exceptionMessages;

    @Value("${application.name}")
    private String applicationName;

    @Value("#{'${application.name}' == 'BCOM'}")
    private boolean isBCOM;

	@Override
    protected ErrorsBinding map(ListServiceException listServiceException) {
		
		parseServiceErrors(listServiceException);
		int statusCode = listServiceException.getStatusCode();
        ErrorsBinding errors;

        if(statusCode == WishlistConstants.BAD_REQUEST){
            List<ErrorBinding> validationErrors = buildValidationErrors(listServiceException);
            errors = new ErrorsBinding(HttpServletResponse.SC_BAD_REQUEST, validationErrors);
        }
        else{
            ErrorBinding.Builder builder = ErrorBinding.builder(LIST_XAPI_MODULE_ID, ErrorCode.INTERNAL_SERVER_ERROR.getValue(),
            		exceptionMessages.getTemporaryFailureMessage())
                    .withDeveloperMessage(listServiceException.getMessage());

            addCommonErrorAttributes(builder);
            errors = new ErrorsBinding(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, builder.create());
        }
        return errors;
    }

    private List<ErrorBinding> buildValidationErrors(ListServiceException listServiceException) {
	    if(isBCOM){
            exceptionMessages.setServiceFailureMessage(exceptionMessages.getServiceFailureMessageForBcom());
            exceptionMessages.setMessage50001(exceptionMessages.getMessage50001Bcom());

        }
        String errorMessage = "";
        Optional<String> developerMessage = Optional.empty();
        Optional<ListServiceErrorCodesEnum> serviceErrorCodesEnum = Arrays.stream(ListServiceErrorCodesEnum.values()).filter(l -> l.getInternalCode() == listServiceException.getServiceErrorCode()).findFirst();
        if (serviceErrorCodesEnum.isPresent()) {
            switch (serviceErrorCodesEnum.get()) {
                case MAX_LIST_PER_USER_REACHED:
                    errorMessage = exceptionMessages.getMessage10147();
                    break;
                case MAX_ITEMS_PER_LIST_REACHED:
                    errorMessage = exceptionMessages.getMessage10148();
                    break;
                case INVALID_SORT_BY_FIELD:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10108());
                    break;
                case INVALID_SORT_BY_ORDER:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10109());
                    break;
                case DUPLICATE_WISHLIST_NAME:
                    errorMessage = exceptionMessages.getMessage50001();
                    break;
                case DUPLICATE_LIST_NAME:
                    errorMessage = exceptionMessages.getMessage10156();
                    break;
                case INVALID_USER_ID:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10101());
                    break;
                case INVALID_INPUT:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10111());
                    break;
                case NO_USER_INFO:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10103());
                    break;
                case NO_SECURE_TOKEN:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10119());
                    break;
                case CATALOG_LOOKUP_ERROR:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10110());
                    break;
                case INVALID_MASTER_PRODUCT:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10139());
                    break;
                case INVALID_COLLABORATOR_ID:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
                    developerMessage = Optional.ofNullable(exceptionMessages.getMessage10151());
                    break;
                default:
                    errorMessage = exceptionMessages.getServiceFailureMessage();
            }
        } else {
            errorMessage = exceptionMessages.getServiceFailureMessage();
        }
		
        List<ErrorBinding> errorBindings;
        ErrorBinding.Builder builder = ErrorBinding.builder(LIST_XAPI_MODULE_ID, ErrorCode.BAD_REQUEST.getValue(), errorMessage)
                .withDeveloperMessage(developerMessage.orElse(listServiceException.getMessage()));
        addCommonErrorAttributes(builder);
        ErrorBinding errorBinding = builder.create();
        errorBindings = Lists.newArrayList(errorBinding);
        return errorBindings;
    }


  
  private void addCommonErrorAttributes(ErrorBinding.Builder builder) {
    builder.withMoreInfo("");
  }
  
  public ListServiceException parseServiceErrors(ListServiceException listServiceException) {
	  if(listServiceException.getServiceError() != null){
		  JsonNode node = customerLists.parse(listServiceException.getServiceError());
		  String serviceErrorMessage = null;
		  int serviceErrorCode = 0;
		  JsonNode errorsNode = serviceErrors.readValue(node);
		  JsonNode errorNode = errorsNode !=null ? errorsNode.get("error") : null;
		  ArrayNode errorInfo = errorNode != null ? (ArrayNode) errorNode : null;
		  
		  if(errorInfo != null){
			  
			  for(JsonNode errorInfoNode: errorInfo) {
				  serviceErrorCode = ( errorInfoNode.get(ERROR_CODE) != null ) ? errorInfoNode.get(ERROR_CODE).asInt() : null;
				  serviceErrorMessage = ( errorInfoNode.get(ERROR_MESSAGE) != null ) ? errorInfoNode.get(ERROR_MESSAGE).asText() : null;
				  LOGGER.error("service responded with error code  {} and  error message {} ", serviceErrorCode, serviceErrorMessage);
				  listServiceException.setServiceErrorCode(serviceErrorCode);
				  listServiceException.setMessage(serviceErrorMessage);
			  }
		  }
	  }
	  
	return listServiceException;
	  
  }

}


