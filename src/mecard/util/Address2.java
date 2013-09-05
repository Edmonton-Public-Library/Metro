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
import mecard.Protocol;
import mecard.util.City;
import mecard.util.AlbertaCity;

/**
 * Replacement class for Address.
 * @author andrew
 */
public class Address2
{
    protected Street   street;
    protected City     city;
    protected Phone    phone;
    protected PCode    postalCode;
    protected Province province;
    
    public Address2(String supposedAddress)
    {
        this.city       = new City();
        this.phone      = new Phone();
        this.postalCode = new PCode();
        this.province   = new Province();
        this.street     = new Street();
        
        if (supposedAddress == null || supposedAddress.isEmpty())
        {
            return;
        }
        // Usually fields are separated by ',', but it is inconsistent.
        String[] split = supposedAddress.split(",");
        for (int i = split.length -1; i >= 0; i--)
        {
            String field = split[i].trim();
            if (this.phone.test(field))
            {
                continue;
            }
            if (this.postalCode.test(field))
            {
                continue; // we found it.
            }
            if (this.province.test(field))
            {
                continue; // we found it.
            }
            if (this.city.test(field))
            {
                continue; // we found it.
            }
            if (this.street.test(field))
            {
                continue; // we found it.
            }
            else
            {
                System.out.println("'" + field
                        + "' didn't match expected address fields.");
            }
        }
    }
    
    public String getStreet()
    {
        return this.street.toString();
    }
    
    public String getCity()
    {
        return this.city.toString();
    }
    
    public String getProvince()
    {
        return this.province.toString();
    }
    
    public String getPostalCode()
    {
        return this.postalCode.toString();
    }
    
    public String getPhone()
    {
        return this.phone.toString();
    }
    
    @Override
    public String toString()
    {
        StringBuilder out = new StringBuilder(this.getStreet());
        out.append(", ");
        out.append(this.getCity());
        out.append(", ");
        out.append(this.getProvince());
        out.append(", ");
        out.append(this.getPostalCode());
        out.append(", ");
        out.append(this.getPhone());
        return out.toString();
    }
    
    protected abstract class AddressRecord
    {
        protected StringBuilder value;
        public AddressRecord()
        {
            this.value = new StringBuilder(Protocol.DEFAULT_FIELD_VALUE);
        }
        
        public abstract boolean test(String s);
        
        public String toString()
        {
            return this.value.toString();
        }
    }
    
    protected class Phone extends AddressRecord
    {
        private Pattern phonePattern;
        public Phone()
        {
            super();
            this.phonePattern = Pattern.compile("\\d{3}-?\\d{3}-?\\d{4}");
        }
        
        @Override
        public boolean test(String s)
        {
            // clean any '()' characters from the string if it is a phone number
            String testString = s;
            if (testString.contains("("))
            {
                testString = testString.replace('(', ' ');
                testString = testString.replace(')', '-');
                testString = testString.trim();
            }
            Matcher matcher = phonePattern.matcher(testString);
            if (matcher.find())
            {
                this.value = new StringBuilder(matcher.group());
                return true;
            }
            return false;
        }
    }
    
    protected class City extends AddressRecord
    {
        private mecard.util.City city;
        public City()
        {
            this.city = AlbertaCity.getInstanceOf();
        }
        
        @Override
        public boolean test(String place)
        {
            // Here we are going to do a lookup for city in the city table.
            if (this.city.isPlaceName(place))
            {
                this.value = new StringBuilder(Text.toDisplayCase(place));
                return true;
            }
            return false;
        }
    }
    
    protected class Street extends AddressRecord
    {
        private final Pattern streetPattern;
        public Street()
        {
            super();
            this.streetPattern = Pattern.compile(
                "^\\d{1,}\\s.*",
                Pattern.CASE_INSENSITIVE);
        }
        
        @Override
        public boolean test(String s)
        {
            // This is the most unreliable. All we can say about it is that it 
            // should start with at least one number. Check this last. It should
            // also be the first item on the address string.
            Matcher matcher = this.streetPattern.matcher(s);
            if (matcher.find())
            {
                this.value = new StringBuilder(Text.toDisplayCase(matcher.group()));
                return true;
            }
            return false;
        }
    }
    
    /**
     * Parses and tests postal codes. Canadian codes only.
     */
    protected class PCode extends AddressRecord
    {
        private final Pattern postalCodePattern;
        public PCode()
        {
            super();
            this.postalCodePattern = Pattern.compile( // Canadian postal code with or without spaces.
                "[ABCEGHJKLMNPRSTVXY]{1}\\d{1}[A-Z]{1} *\\d{1}[A-Z]{1}\\d{1}",
                Pattern.CASE_INSENSITIVE);
        }
        
        @Override
        public boolean test(String s)
        {
            Matcher matcher = postalCodePattern.matcher(s);
            if (matcher.find())
            {
                this.value = new StringBuilder(matcher.group().toUpperCase());
                return true;
            }
            return false;
        }
    }
    
    protected class Province extends AddressRecord
    {
        @Override
        public boolean test(String s)
        {
            mecard.util.Province province = new mecard.util.Province(s);
            if (province.isValid())
            {
                this.value = new StringBuilder(province.toString());
                return true;
            }
            return false;
        }
    }
}
