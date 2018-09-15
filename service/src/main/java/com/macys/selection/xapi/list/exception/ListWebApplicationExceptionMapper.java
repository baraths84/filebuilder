package com.macys.selection.xapi.list.exception;

import com.macys.platform.rest.core.fault.AbstractPlatformExceptionMapper;
import com.macys.platform.rest.core.fault.ErrorBinding;
import com.macys.platform.rest.core.fault.ErrorsBinding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class ListWebApplicationExceptionMapper extends AbstractPlatformExceptionMapper<ListWebApplicationException> {
    private static final Integer LIST_XAPI_MODULE_ID = 50;

    @Autowired
    private ExceptionProperties exceptionMessages;

    @Override
    protected ErrorsBinding map(ListWebApplicationException e) {
        ErrorsBinding errors;

        ErrorBinding.Builder builder = ErrorBinding.builder(LIST_XAPI_MODULE_ID, ErrorCode.BAD_REQUEST.getValue(), exceptionMessages.getServiceFailureMessage())
                .withDeveloperMessage(e.getMessage());
        ErrorBinding errorBinding = builder.create();

        errors = new ErrorsBinding(HttpServletResponse.SC_BAD_REQUEST, errorBinding);
        return errors;
    }
}
