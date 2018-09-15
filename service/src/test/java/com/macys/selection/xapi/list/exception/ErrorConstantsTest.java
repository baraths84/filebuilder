package com.macys.selection.xapi.list.exception;

import static org.testng.Assert.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

@SpringBootTest(classes = {ErrorConstants.class})
public class ErrorConstantsTest extends AbstractTestNGSpringContextTests  {

    private static final String EXPECTED_BAD_JASON_INPUT_ITMES_NOT_AVAILABLE = "Bad Json from UI, at least one item is required!";
    private static final String EXPECTED_BAD_JSON_INPUT_BODY_USER_IS_NULL = "User cannot be null when trying to delete the list, specify the user info in the payload.";

    @Test
    public void testBadJsonNotAvailable() {
        assertEquals(EXPECTED_BAD_JASON_INPUT_ITMES_NOT_AVAILABLE, ErrorConstants.BAD_JASON_INPUT_ITMES_NOT_AVAILABLE);
    }

    @Test
    public void testBadJsonInputBody() {
        assertEquals(EXPECTED_BAD_JSON_INPUT_BODY_USER_IS_NULL, ErrorConstants.BAD_JSON_INPUT_BODY_USER_IS_NULL);
    }

}
