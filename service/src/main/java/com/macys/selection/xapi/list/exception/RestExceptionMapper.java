package com.macys.selection.xapi.list.exception;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.macys.platform.rest.core.fault.AbstractPlatformExceptionMapper;
import com.macys.platform.rest.core.fault.ErrorBinding;
import com.macys.platform.rest.core.fault.ErrorsBinding;

@Provider
@Component
public class RestExceptionMapper extends AbstractPlatformExceptionMapper<RestException> {
	private static final Integer LIST_XAPI_MODULE_ID = 50;

	@Autowired
	private ExceptionProperties exceptionMessages;

	@Override
	protected ErrorsBinding map(RestException re) {

		ErrorsBinding errors;

		ErrorBinding.Builder builder = ErrorBinding.builder(LIST_XAPI_MODULE_ID, ErrorCode.INTERNAL_SERVER_ERROR.getValue(),
				exceptionMessages.getTemporaryFailureMessage())
				.withDeveloperMessage(re.getMessage());

		errors = new ErrorsBinding(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, builder.create());
		return errors;
	} 

}
