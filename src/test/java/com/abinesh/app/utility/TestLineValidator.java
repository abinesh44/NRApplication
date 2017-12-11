package com.abinesh.app.utility;

import static com.abinesh.app.utility.LineValidator.isValidNumber;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by abine on 12/9/2017.
 */
public class TestLineValidator {
    @Test
    public void testIsValidNumber(){
        assertFalse(isValidNumber("0x3456789",9));
        assertTrue(isValidNumber("123456789",9));
        assertFalse(isValidNumber("1",2));

    }
}
