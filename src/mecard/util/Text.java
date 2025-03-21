/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2021 - 2025  Edmonton Public Library
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mecard.Protocol;

/**
 * Text helper class that uses static methods to test strings for valid values.
 * @author Andrew Nisbet andrew.nisbet@epl.ca andrew@dev-ils.com.
 * @since 0.1
 */
public final class Text 
{
    private Text(){}
    
    /**
     * Cleans a name of '*' characters. Some ILSs include this in the name.
     * 
     * @param name
     * @return name cleaned of '*'.
     */
    public static String cleanName(String name)
    {
        if (name == null) return "";
        return name.replace("*", "");
    }
    
    /**
     * Returns the first word of a sentence if there is one and an empty string 
     * if there isn't.
     * @param sentence a string of text.
     * @return the first word from the parameter as specified by Java white space
     * regex definition.
     */
    public static String firstWord(String sentence)
    {
        if (sentence == null || sentence.isEmpty()) return "";
        String[] words = sentence.split("\\s{1,}");
        if (words.length > 0)
        {
            return words[0].trim();
        }
        return "";
    }

    /**
     * Does a check if the string parameter is a valid-looking email. Note it 
     * does not check the email account itself.
     * @param email
     * @return true if the string is a well formed email address string and false
     * otherwise.
     */
    public static boolean isValidEmail(String email)
    {
        // Special thanks to Mkyong founder of Mkyong.com
        // http://www.mkyong.com/regular-expressions/how-to-validate-email-address-with-regular-expression/
	String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

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
     * Given a blob of text that contains new line characters, find the line
     * that matches the 'matchThis' parameter and return the line.
     * @param content if null an empty string is returned. some output that 
     * contains several lines. If empty an empty string will be returned.
     * @param matchThis if null an empty string is returned. If the parameter 
     * is empty, the original content is returned in its unaltered entirety.
     * @return line content that matches or an empty string if not found.
     */
    public static String matchLine(String content, String matchThis)
    {
        if (content == null || matchThis == null) return "";
        if (content.isEmpty()) return "";
        if (matchThis.isEmpty()) return content;
        String[] lines = content.split("\\n");
        for (String line : lines)
        {
            if (line.contains(matchThis))
            {
                return line.trim();
            }
        }
        return "";
    }
    
    /** 
     * Determines if a string is composed exclusively of maxLength digits.
     * @param s input string, could be a PIN for example.
     * @param maxLength the maximum width you will allow s to be. This
     * value _must_ be greater than or equal to 1 or the method returns false.
     * @return true if s is maxLength digits long, and false otherwise.
     */
    public static boolean isUpToMaxDigits(String s, int maxLength)
    {
        if (s == null || maxLength < 1) return false;
        return s.matches("\\d{1," + maxLength + "}");
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
        if (sentence == null || sentence.isEmpty()) return "";
        String[] words = sentence.split("\\s{1,}");
        if (words.length > 1)
        {
            return words[words.length -1].trim();
        }
        return "";
    }
    
    /**
     * Returns the last 'nth' word in the sentence. 
     * @param sentence the sentence to parse out. 
     * @param which the word index you would like. '0' indexed. 
     * Example: String input = "399565 399566 399567 399568"
     * lastWord(input, 1) returns '399567'
     * lastWord(input, 0) returns '399568'
     * lastWord(input, -1) returns ''
     * lastWord(input, 100) returns ''
     * @return the last nth word in the sentence.
     */
    public static String lastWord(String sentence, int which)
    {
        if (sentence == null || sentence.isEmpty()) return "";
        String[] words = sentence.split("\\s{1,}");
        int whichWordFromTheBack = words.length -which;
        if (whichWordFromTheBack < 0 || whichWordFromTheBack >= words.length)
        {
            return "";
        }
        return words[words.length -which].trim();
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
     * This method cleanNames any string in cleanName off the end of the 
     * string along with any surrounding white space and returns the remainder.
     * @param clean string to cleanName off.
     * @param sentence
     * @return the sentence cleanNameed of cleanName or sentence untouched 
     * if cleanName wasn't found.
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
    
    /**
     * Returns the content of a string one up to the start of string two.
     * 
     * If string two does not appear in string one, string one is returned.
     * @param stringOne
     * @param stringTwo
     * @return The sub-string of stringOne upto the start of stringTwo or
     * stringOne if stringTwo is not found.
     */
    public static String getUpTo(String stringOne, String stringTwo)
    {
        int where = stringOne.indexOf(stringTwo);
        if (where > -1)
        {
            return stringOne.substring(0, where);
        }
        return stringOne;
    }
    
    /**
     * Determines if two strings are similar, once white space, non-word
     * characters are removed, and if case is ignored.
     * @param strOne input string one.
     * @param strTwo input string two.
     * @return true if the strings are similar, to within 10% of the string length
     * and ignoring case, punctuation, and white space.
     * white space are accounted for, and false otherwise.
     */
    public static boolean isLike(String strOne, String strTwo)
    {
        String badChars = "[\\W\\s]";
        // So string.contains("") returns true, so preload with a character
        // that won't be removed by replaceAll() later.
        String s1 = "y";
        String s2 = "z";
        if (strOne != null) s1 = strOne;
        if (strTwo != null) s2 = strTwo;
        s1 = s1.replaceAll(badChars, "");
        s2 = s2.replaceAll(badChars, "");
        if (s1.length() < s2.length())
        {
            return s2.toLowerCase().contains(s1.toLowerCase());
        }
        else
        {
            return s1.toLowerCase().contains(s2.toLowerCase());
        }
//        return Math.abs(s1.compareToIgnoreCase(s2)) == 0;
    }
    
    /**
     * Determines if the string argument is empty or is a default value as
     * defined in Protocol.DEFAULT_FIELD_VALUE. 
     * @param str to be tested.
     * @return If the string represents an unset value, that is it is empty
     * or the default value of "X", return true and false otherwise.
     */
    public static boolean isUnset(String str)
    {
        return str == null 
                || str.isBlank()
                || str.compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0;
    }
    
    /**
     * Determines if the string argument is not empty null or set to the system
     * default defined in Protocol.DEFAULT_FIELD_VALUE. 
     * @param str to be tested.
     * @return If the string represents a value, that is it is not null not empty
     * and not the default value of "X", return true and false otherwise.
     */
    public static boolean isSet(String str)
    {
        return ! Text.isUnset(str);
    }
    
    /**
     * Formats a North American phone number into xxx-xxx-xxxx format.
     * @param phoneNumber
     * @return formatted phone number
     */
    public static String formatPhoneNumber(String phoneNumber) 
    {
        phoneNumber = phoneNumber.replaceAll("[^0-9]", "");
        // Validate input
        if (phoneNumber == null || phoneNumber.length() != 10) 
        {
            throw new IllegalArgumentException("Phone number must be exactly 10 digits");
        }
        
        // Using StringBuilder for efficient string manipulation
        StringBuilder formatted = new StringBuilder(phoneNumber);
        formatted.insert(3, '-');
        formatted.insert(7, '-');
        
        return formatted.toString();
    }
}
