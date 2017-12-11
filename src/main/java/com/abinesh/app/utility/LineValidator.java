package com.abinesh.app.utility;

import static org.apache.commons.lang.math.NumberUtils.isDigits;

/**
 * Created by abine on 12/9/2017.
 */
public class LineValidator {

    private LineValidator(){}

    /**
     * Checks if its a valid number having the correct number of digits.
     *
     * @param line
     * @param numberOfDigits
     * @return
     */
    public static boolean isValidNumber(String line, int numberOfDigits){
        return (isDigits(line) && line.length()== numberOfDigits);
    }

    public static boolean isStopCommand(String line, String stopCommand){
        return line != null && stopCommand.equals(line);
    }
}
