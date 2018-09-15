package com.macys.selection.xapi.list.exception;

import com.macys.platform.rest.core.fault.ErrorBinding;
import com.macys.platform.rest.core.fault.ErrorsBinding;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest
        (classes = {
                ExceptionProperties.class})
public class ListWebApplicationExceptionMapperTest extends AbstractTestNGSpringContextTests {

    private static final String ERROR_MESSAGE_DEVELOPER = "error while validating input request.";
    private static final String ERROR_MESSAGE_VALIDATION  = "We're experiencing a technical glitch. " +
            "Please try again later or call us at 1-800-BUY-MACY (289-6229) for immediate assistance.";

    @InjectMocks
    private ListWebApplicationExceptionMapper listWebApplicationExceptionMapper;

    @Mock
    private ExceptionProperties exceptionMessages;

    @BeforeMethod
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testThrowWebApplicationExceptionShouldResponseWithBadRequest400Error() {
        ListWebApplicationException exception = new ListWebApplicationException(ERROR_MESSAGE_DEVELOPER, Response.Status.BAD_REQUEST.getStatusCode());

        when(exceptionMessages.getServiceFailureMessage()).thenReturn(ERROR_MESSAGE_VALIDATION);

        ErrorsBinding errors = listWebApplicationExceptionMapper.map(exception);
        errors.getHttpStatus();
        assertNotNull(errors);

        List<? extends ErrorBinding> error = errors.getError();
        ErrorBinding errorBinding = error.get(0);
        assertNotNull(errorBinding);

        String actualErrorMsg = errorBinding.getMessage();
        String actualDeveloperErrorMsg = errorBinding.getDeveloperMessage();

        assertEquals(ERROR_MESSAGE_VALIDATION, actualErrorMsg);
        assertEquals(ERROR_MESSAGE_DEVELOPER, actualDeveloperErrorMsg);
    }


}
