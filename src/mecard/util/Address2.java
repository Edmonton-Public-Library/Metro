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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mecard.Protocol;

/**
 * Replacement class for Address.
 * @author Andrew Nisbet <anisbet@epl.ca>
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
        // Low hanging fruit lets see if there is a postal code.
        StringBuilder supposedAddressBuilder = new StringBuilder(supposedAddress);
        this.postalCode.test(supposedAddressBuilder);
        this.phone.test(supposedAddressBuilder);
        // Now we should split on commas, this will give us rough catagories
        // The first is street address, but may have a city on the end.
        String[] splitGroups = supposedAddressBuilder.toString().split(",");
        int numberOfGroups = splitGroups.length;
        if (numberOfGroups >= 3)
        {
            // then we have a well formed address with fields separated by commas,
            // the value should be the province.
            this.street.test(splitGroups[0].trim());
            this.city.test(splitGroups[1].trim());
            this.province.test(splitGroups[2].trim());
        }
        if (numberOfGroups == 2)
        {
            // The second may include a city and will include a province do it first
            String[] splitCityProvince = splitGroups[1].trim().split("\\s+");
            String[] splitStreetCity   = splitGroups[0].trim().split("\\s+");
            
            if (splitCityProvince.length > 1) // We have the city and then the province last
            {
                String provinceString = splitCityProvince[splitCityProvince.length -1];
                if (this.province.test(provinceString))
                {
                    splitCityProvince[splitCityProvince.length -1] = ""; // reset the last value so it doesn't end up on the address.
                }
                // put it back together again
                String cityString = splitCityProvince[splitCityProvince.length -2];
                this.city.testGently(cityString.trim());
            }
            else if (splitCityProvince.length == 1)
            {
                this.province.test(splitCityProvince[0]); // there is only one so it should be the province.
            }
            // Now what to do with the address, city?
            // Well if the city is the last field then it will test positive, but
            // we can tell just by checking if the first part found the city
            if (this.city.isSet()) // the rest is street
            {
                this.street.test(splitGroups[0]); 
            }
            else // Worst case we have to grab the last word and try and match --
                // two word place names like ft sask and st. albert are hit worst
            {
                // grab the last word in splitStreetCity
                String cityString = splitStreetCity[splitStreetCity.length -1];
                if (this.city.testGently(cityString)) // this could be set to a fuzzy search for the last word in a multiline place name.
                {
                    splitStreetCity[splitStreetCity.length -1] = ""; // reset the city field
                }
                // paste street back together.
                String addressString = "";
                for (String s: splitStreetCity)
                {
                    addressString += s + " ";
                }
                this.street.test(addressString.trim());
            }
        }
    }
    
    /**
     * 
     * @return street portion of the customer's address.
     */
    public String getStreet()
    {
        return this.street.toString();
    }
    
    /**
     * 
     * @return city portion of the customer's address.
     */
    public String getCity()
    {
        return this.city.toString();
    }
    
    /**
     * 
     * @return province portion of the customer's address.
     */
    public String getProvince()
    {
        return this.province.toString();
    }
    
    /**
     * 
     * @return postal code portion of the customer's address.
     */
    public String getPostalCode()
    {
        return this.postalCode.toString();
    }
    
    /**
     * 
     * @return phone portion of the customer's address.
     */
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

    /**
     * Utility class for testable address field strings.
     */
    protected abstract class AddressRecord
    {
        protected String value;
        public AddressRecord()
        {
            this.value = Protocol.DEFAULT_FIELD_VALUE;
        }
        
        /**
         * @param s the value of s
         * @return the boolean
         */
        public abstract boolean test(String s);
        public abstract boolean test(StringBuilder s);
        public boolean isSet() { return this.value.compareTo(Protocol.DEFAULT_FIELD_VALUE) != 0; }
        public String toString()
        {
            return this.value;
        }
    }
    
    /**
     * Utility class for testing phone number strings.
     */
    protected class Phone extends AddressRecord
    {
        private Pattern phonePattern;
        public Phone()
        {
            super();
            this.phonePattern = Pattern.compile("\\d{3}-?\\d{3}-?\\d{4}");
        }
        
        /**
         * Tests a string to determine if it could likely be a phone number.
         * @param s the suspected phone string.
         * @return the boolean
         */
        @Override
        public boolean test(String s)
        {
            String testString = s;
            Matcher matcher = phonePattern.matcher(testString);
            if (matcher.find())
            {
                this.value = matcher.group();
                return true;
            }
            return false;
        }

        @Override
        public boolean test(StringBuilder s)
        {
            String testString = s.toString();
            Matcher matcher = phonePattern.matcher(testString);
            if (matcher.find())
            {
                this.value = matcher.group();
                // if arg is a string builder we are supposed to modify it.
                s.delete(matcher.start(), matcher.end());
                return true;
            }
            return false;
        }
    }
    /**
     * Utility class for testing place name strings.
     */
    protected class City extends AddressRecord
    {
        private mecard.util.City city;
        public City()
        {
            this.city = AlbertaCity.getInstanceOf();
        }
        
        /**
         * Parses and populates the City object with a recognized place name.
         * @return true if a legal city name was found and false otherwise.
         */
        @Override
        public boolean test(String s)
        {
            // Here we are going to do a lookup for city in the city table.
            String place = Text.toDisplayCase(s.trim());
            if (this.city.isPlaceName(place))
            {
                this.value = place;
                return true;
            }
            return false;
        }

        /**
         * This method will always fail if you pass a complete address line since
         * it will look up the entire line. The recommended procedure is to pass
         * in a string that is a good candidate to be a city name, remembering that
         * many place names contain multiple words.
         * @param s potential city name to be tested.
         * @return true if the argument string matched a known place name and 
         * false otherwise.
         */
        @Override
        public boolean test(StringBuilder s)
        {
            return this.test(s.toString());
        }

        private boolean testGently(String s)
        {
            String place = Text.toDisplayCase(s.trim());
            if ((place = this.city.getPlaceNameLike(place)).isEmpty() == false)
            {
                this.value = place;
                return true;
            }
            return false;
        }
    }
    
    /**
     * Utility class for testing street strings.
     */
    protected class Street extends AddressRecord
    {
        private final Pattern streetPattern;
        public Street()
        {
            super();
            this.streetPattern = Pattern.compile(
                "^\\d{1,}\\s.*", // This will not catch PO boxes.
                Pattern.CASE_INSENSITIVE);
        }
        
        /**
         * This class has the weakest regex because a street address has no
         * real form.
         * @param s the possible address string.
         * @return true if the argument is a likely street address and false otherwise.
         */
        @Override
        public boolean test(String s)
        {
            // This is the most unreliable. All we can say about it is that it 
            // should start with at least one number. Check this last. It should
            // also be the first item on the address string.
            Matcher matcher = this.streetPattern.matcher(s);
            if (matcher.find())
            {
                this.value = Text.toDisplayCase(matcher.group());
                return true;
            }
            return false;
        }

        @Override
        public boolean test(StringBuilder s)
        {
            return this.test(s.toString());
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
        
        /**
         *
         *
         * @param s the value of s
         * @return the boolean
         */
        @Override
        public boolean test(String s)
        {
            Matcher matcher = postalCodePattern.matcher(s);
            if (matcher.find())
            {
                this.value = matcher.group().toUpperCase();
                return true;
            }
            return false;
        }

        @Override
        public boolean test(StringBuilder s)
        {
            Matcher matcher = postalCodePattern.matcher(s.toString());
            if (matcher.find())
            {
                this.value = matcher.group().toUpperCase();
                s.delete(matcher.start(), matcher.end());
                return true;
            }
            return false;
        }
    }
    
    /**
     * Utility class for testing strings for possible Canadian province names.
     */
    protected class Province extends AddressRecord
    {
        /**
         * @param s the possible province name
         * @return true if the arg is likely a province name and false otherwise.
         */
        @Override
        public boolean test(String s)
        {
            // TODO this needs a static tester method.
            mecard.util.Province province = new mecard.util.Province(s.toString());
            if (province.isValid())
            {
                this.value = province.toString();
                return true;
            }
            return false;
        }

        @Override
        public boolean test(StringBuilder s)
        {
            return this.test(s.toString());
        }
    }
}
