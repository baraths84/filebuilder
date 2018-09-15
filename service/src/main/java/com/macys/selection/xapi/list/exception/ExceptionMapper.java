package com.macys.selection.xapi.list.exception;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.macys.platform.rest.core.fault.AbstractPlatformExceptionMapper;
import com.macys.platform.rest.core.fault.ErrorBinding;
import com.macys.platform.rest.core.fault.ErrorsBinding;

@Component
@Provider
public class ExceptionMapper extends AbstractPlatformExceptionMapper<Exception> {
	private static final Integer LIST_XAPI_MODULE_ID = 50;

	@Autowired
	private ExceptionProperties exceptionMessages;

	@Override
	protected ErrorsBinding map(Exception exception) {

		ErrorsBinding errors;
		ErrorBinding.Builder builder;
		if(exception instanceof NullPointerException){
			builder = ErrorBinding.builder(LIST_XAPI_MODULE_ID, ErrorCode.INTERNAL_SERVER_ERROR.getValue(),
					exceptionMessages.getTemporaryFailureMessage())
					.withDeveloperMessage(exception.getMessage());
			errors = new ErrorsBinding(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, builder.create());
		}else{
			builder = ErrorBinding.builder(LIST_XAPI_MODULE_ID, ErrorCode.INTERNAL_SERVER_ERROR.getValue(),
					exceptionMessages.getTemporaryFailureMessage())
					.withDeveloperMessage(exception.getMessage());
			errors = new ErrorsBinding(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, builder.create());
		}


		return errors;
	}

}
