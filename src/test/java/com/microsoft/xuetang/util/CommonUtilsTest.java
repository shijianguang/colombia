package com.microsoft.xuetang.util;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CommonUtilsTest {

    @Test
    public void testTrimBefore() throws Exception {
        String test = "test";
        assertEquals(CommonUtils.trimBefore(test, new char[] {}), test);

        test = "test";
        assertEquals(CommonUtils.trimBefore(test, new char[] {'e'}), test);

        test = "\r\ntest";
        assertEquals(CommonUtils.trimBefore(test, new char[] {'\n', '\r'}), "test");

        test = "test";
        assertEquals(CommonUtils.trimBefore(test, new char[] {'t', 'e', 's', 't'}), StringUtils.EMPTY);
    }
}