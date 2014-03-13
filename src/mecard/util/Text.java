/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 *
 */
package mecard.util;

import java.util.List;

/**
 * General text handling static methods are found here.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class Text 
{

    
    private Text(){}
    /**
     * Converts strings to proper case. Used for normalizing names and streets
     * to a proper case convention. One of the libraries uses UPPERCASE for
     * customer first name last name and address fields, which is not helpful
     * for other libraries. Each should expect their data in a constant format.
     * @param s string to be Proper Cased.
     * @return the string in proper case.
     */
    public static String toDisplayCase(String s)
    {
        // Credit to: http://stackoverflow.com/questions/1086123/titlecase-conversion
        final String ACTIONABLE_DELIMITERS = " -"; // these cause the character following
                                                   // to be capitalized
        StringBuilder sb = new StringBuilder();
        boolean capNext = true;

        for (char c : s.toCharArray()) 
        {
            c = (capNext)
                    ? Character.toUpperCase(c)
                    : Character.toLowerCase(c);
            sb.append(c);
            capNext = (ACTIONABLE_DELIMITERS.indexOf((int) c) >= 0); // explicit cast not needed
        }
        return sb.toString();
    }
    
    /**
     * Creates a random 4 digit PIN suitable for Horizon requirements.
     * @return 4 digit pin of random numbers as a String.
     */
    public static String getNew4DigitPin()
    {
        int min =  1000;
        int max = 10000;
        int value = min + (int)(Math.random() * ((max - min) + 1));
        String returnValue = String.valueOf(value);
        return returnValue.substring(returnValue.length() -4);
    }
    
    /** 
     * Determines if a string is composed exclusively of digits.
     * @param s input string, could be a PIN for example.
     * @param maxLength the maximum width you will allow s to be.
     * @return true if the arg string is all digits and false otherwise.
     */
    public static boolean isMaximumDigits(String s, int maxLength)
    {
        return s.matches("\\d{" + maxLength + "}");
    }
    
    /**
     * This method takes the sentence argument and chops off words starting 
     * at the left, checking for a match in the list of strings. In this way
     * it finds the longest match from the original sentence. Example:
     * find "BOX 43 LETHBRIDGE" in a list of ["County of Lethbridge", "Lethbridge"]
     * returns "Lethbridge".
     * @param possibleMatches
     * @param sentence 
     * @return The match as it was found in possibleMatches or an empty string if
     * no match was found.
     */
    public static String longestMatch(List<String> possibleMatches, String sentence)
    {
        if (sentence.length() == 0)
        {
            return "";
        }
        // chop off words from the front of the string and recursively 
        // test against the collection of names.
        for (String s: possibleMatches)
        {
            if (s.equalsIgnoreCase(sentence))
            {
                return s;
            }
        }
        // chop off words from the front of the string and recursively 
        // test against the collection of names.
        return Text.longestMatch(possibleMatches, chopLeft(sentence));
    }
    
    /**
     * Chops off the first word of a sentence and returns the remainder.
     * @param sentence
     * @return the first word of the sentence or an empty string if there wasn't one.
     */
    public static String chopLeft(String sentence)
    {
        int pos = sentence.indexOf(" ");
        if (pos < 0)
        {
            return "";
        }
        return sentence.substring(pos).trim();
    }
    
    /**
     * Works like chopLeft but returns only the word on the end, not the
     * remaining sentence.
     * @param sentence
     * @return last word on the sentence if there is one and empty String otherwise.
     */
    public static String lastWord(String sentence)
    {
        int pos = sentence.lastIndexOf(" ");
        if (pos < 0)
        {
            return "";
        }
        return sentence.substring(pos).trim();
    }
    
    /**
     * Removes the choppedText string from the end of the argument sentence.
     * @param sentence
     * @param choppedText text to remove
     * @return returns remainder of String after chopping off choppedText,
     * or the original sentence if the chopped text wasn't found or if the sentence
     * argument was an empty String.
     */
    public static String chopOff(String sentence, String choppedText)
    {
        if (choppedText.length() == 0)
        {
            return sentence;
        }
        int pos = sentence.toLowerCase().indexOf(choppedText.toLowerCase());
        if (pos >= 0)
        {
            return sentence.substring(0, pos).trim();
        }
        return sentence;
    }
    
    /**
     * This method cleans any string in clean off the end of the string along 
     * with any surrounding white space and returns the remainder.
     * @param clean string to clean off.
     * @param sentence
     * @return the sentence cleaned of clean or sentence untouched if clean
     * wasn't found.
     */
    public static String cleanTrailing(String clean, String sentence)
    {
        if (sentence.trim().endsWith(clean))
        {
            int pos = sentence.lastIndexOf(clean);
            if (pos >= 0)
            {
                return sentence.substring(0, pos).trim();
            }
        }
        return sentence;
    }
}
