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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a helper class to parse out and make sense of a single string
 * address.
 *
 * @author metro
 * @deprecated Use Address2 instead.
 */
public class Address
{
    private String content;
    protected String street;
    protected String city;
    protected Province province;
    protected PostalCode postalCode;
    protected Phone phoneNumber;
    protected Pattern postalCodePattern;
    protected Pattern provincePattern;
    protected Pattern phonePattern;
    private final boolean isValid;

    public Address(String address, boolean isRequired)
    {
        street = new String();
        city = new String();
        content = new String();
        postalCodePattern = Pattern.compile( // Canadian postal code with or without spaces.
                "[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}",
                Pattern.CASE_INSENSITIVE);
        provincePattern = Pattern.compile( // Canadian postal code with or without spaces.
                "A[B|.{1}b](erta)?" // has to match AB or ALBERTA but not match ST. ALBERT.
                );
        phonePattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
        isValid = setContent(address.trim());
    }

    public String getStreet()
    {
        return street;
    }

    public String getCity()
    {
        return city;
    }

    public String getProvince()
    {
        return province.toString();
    }

    /**
     * 
     * @return postal code as raw code (not whitespace).
     */
    public String getPostalCode()
    {
        return postalCode.toString();
    }

    /**
     * 
     * @return phone number as raw digits.
     */
    public String getPhone()
    {
        return phoneNumber.getUnformattedPhone();
    }

    protected boolean setContent(String address)
    {
        // TODO needs to be able to process:
        // 10255 PRINCESS ELIZABETH AVENUE, Edmonton, Ab, T5G 0Y1
        // and:
        // 364 8915 147 Avenue NW Edmonton, AB T5E 5Y5 780-702-3114
        if (address == null || address.length() == 0)
        {
            return false;
        }
        content = address;
        // Typical string:
        //7 Sir Winston Churchill Square Edmonton, AB T5J 2V4 780-340-9998
        // Here's another one.
        //5 St. Anne St., St. Albert, AB, T8N 3Z9
        // so we can get the postal code that is at the end if it exists
        // and has a specific postalCodePattern.
        Matcher matcher = phonePattern.matcher(address);
        if (matcher.find())
        {
            phoneNumber = new Phone(matcher.group());
            // now chop off the phone number from the address if found.
            content = address.substring(0, matcher.start()).trim();
        }
        else
        {
            phoneNumber = new Phone("");
            System.out.println("no phone number found in address field");
        }
        
        matcher = postalCodePattern.matcher(address);
        // now chop off the postal code from the address if found.
        if (matcher.find())
        {
            postalCode = new PostalCode(matcher.group());
            content = address.substring(0, matcher.start()).trim();
        }
        else
        {
            postalCode = new PostalCode("");
            System.out.println("no postalcode found.");
        }
        // now we have:
        //5 St. Anne St., St. Albert, AB, 
        // get rid of potential last ','
        if (content.endsWith(","))
        {
            content = content.substring(0, content.length() - 1);
        }
        // This should be the province 
        matcher = provincePattern.matcher(content);
        if (matcher.find())
        {
            // take the rest of the string.
            String pString = content.substring(matcher.start());
            province = new Province(pString);
            content = address.substring(0, matcher.start()).trim();
        }
        else
        {
            province = new Province("");
            System.out.println("no province found.");
        }
        // now we have:
        //5 St. Anne St., St. Albert, 
        // get rid of potential last ','
        try
        {
            if (content.endsWith(","))
            {
                content = content.substring(0, content.length() - 1).trim();
            }
            // The town is next. Here we use a simple but not robust strategy
            // all other members have two word names. if the last word is some variation
            // of edmonton then grab it and if not take the last two words.
        
            int pos = content.lastIndexOf(" ");
            String cityString = content.substring(pos);
            // set content now since we will grab the next word as part of the city name.
            content = content.substring(0, pos);
            if (cityString.contains("dmonton") == false)
            {
                // find the start of the next word 'St.'.
                pos = content.lastIndexOf(" ");
                // put the two together
                cityString = content.substring(pos) + cityString;
                // chop off the city
                content = content.substring(0, pos);
                // get rid of any trailing ','s.
                if (content.endsWith(","))
                {
                    content = content.substring(0, content.length() - 1).trim();
                }
            }
            // chops off initial space from ' Edmonton'
            city = cityString.trim();
        } catch (RuntimeException ex)
        {
            System.out.println("error while parsing address: '"+ address + "'");
        }
        // the rest is street address.
        street = content.trim();
        if (street == null || street.length() == 0)
        {
            return false;
        }
        if (city == null || city.length() == 0)
        {
            return false;
        }
        if (postalCode.isValid() == false)
        {
            return false;
        }
        if (province.isValid() == false)
        {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getStreet());
        sb.append(", ");
        sb.append(getCity());
        sb.append(", ");
        sb.append(getProvince());
        sb.append(", ");
        sb.append(getPostalCode());
        sb.append(", ");
        sb.append(getPhone());
        return sb.toString();
    }
    
    public class PhoneNumber
    {
        private String phone;
        private boolean hasPhone;
        protected Pattern phonePattern;
        
        public PhoneNumber(String addressLine)
        {
            this.hasPhone = false;
            this.phonePattern = Pattern.compile("\\d{3}-\\d{3}-\\d{4}");
        }
        
        public boolean hasPhone()
        {
            return this.hasPhone;
        }
        
        public String getPhone()
        {
            return this.phone;
        }
    }
}
