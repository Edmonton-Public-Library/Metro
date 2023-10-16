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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import mecard.Protocol;

/**
 * Address2 replaces Address and provides a robust method of parsing address strings
 * typically produced by SIP2. The algorithm typically works on string in the 
 * form of <b>1234 56 Ave. St. Albert, AB T6Y 8M7 780-343-9908</b>, but is designed
 * to be flexible enough to able to identify variations on each field and even if
 * some of the fields are missing or damaged.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public final class Address3
{
    protected Street   street;
    protected City     city;
    protected Phone    phone;
    protected PCode    postalCode;
    protected Province province;
    
    public Address3(String supposedAddress)
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
        supposedAddress = supposedAddress.replaceAll(",", "");
        StringBuilder sbAddress = new StringBuilder(supposedAddress);
        if (phone.test(sbAddress))
        {
            String remaining = supposedAddress.substring(0,supposedAddress.indexOf(phone.value));
            sbAddress = new StringBuilder(remaining);
        }
        postalCode.test(sbAddress);
        province.test(sbAddress);
        setStreetCity(sbAddress.toString().trim());
    }
    
    /**
     * Minimally sets street, but tests for place names and potentially province
     * in the so-far-unseen case of street city province test data.
     * @param streetCity 
     * @return true if the city was found at the end of the string and false otherwise.
     */
    private boolean setStreetCity(String streetCity)
    {
        // 31 Chansellorsville Street apt. 3 Medicine Hat, AB T1A 3N7
        // do best match city on addrValues[0]
        // province on addrValues[1]
        // The last value should be part of the city name and we could get multiple enties
        // if the last word is 'County'.
        String possiblePlaceName = Text.lastWord(streetCity);
        // TODO move get instance of to City so we can scale this class to other provinces and states.
        mecard.util.City myCity = mecard.util.AlbertaCity.getInstanceOf();
        List<String> possiblePlaceMatches = myCity.getPlaceNames(possiblePlaceName);
        String mostLikelyMatch = Text.longestMatch(possiblePlaceMatches, streetCity);
        boolean gotCity = this.city.test(mostLikelyMatch);
        if (gotCity)
        {
            // the rest of the string will be address.
            this.street.test(Text.chopOff(streetCity, mostLikelyMatch));
        }
        else
        {
            this.street.test(streetCity); // captures everything safe, but not ideal.
        }
        return gotCity;
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
    public abstract class AddressRecord
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
        // Not used.
        public boolean isSet() { return this.value.compareTo(Protocol.DEFAULT_FIELD_VALUE) != 0; }
        @Override
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
        private final Pattern phonePattern;
        public Phone()
        {
            super();
            // end of line matching important to avoid 209-1123 street matching.
            // I added the optional 'T' because a library prefixes textable phone numbers 
            // with 'T', yeah I know right?
            this.phonePattern = Pattern.compile("T?(\\+1)?\\(?\\d{3}\\)?[-| ]?\\d{3}[-| ]?\\d{4}((\\s)?\\D{1,3}?(\\.)?(\\s\\d{2,4}))?$");
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
        public String toString()
        {
            if (this.value.matches("\\d{10}$"))
            {
                StringBuilder sb = new StringBuilder(this.value);
                sb.insert(this.value.length() -4, '-');
                sb.insert(this.value.length() -7, '-');
                this.value = sb.toString();
            }
            // This to get rid of stored 'T', which is annoying as I've said.
            // you have to do it here or it upsets the indexing of other elements
            // like postal code and province.
            if (this.value.startsWith("T"))
            {
                this.value = this.value.replaceFirst("T", "");
            }
            return this.value;
        }

        @Override
        public boolean test(StringBuilder s)
        {
            String testString = s.toString();
            Matcher matcher = phonePattern.matcher(testString);
            if (matcher.find())
            {
                this.value = matcher.group();
                s.delete(matcher.start(), matcher.end());
                return true;
            }
            return false;
        }
    }
    /**
     * Utility class for testing place name strings.
     */
    public class City extends AddressRecord
    {
        private final mecard.util.City city;
                
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
    }
    
    /**
     * Utility class for testing street strings.
     */
    protected class Street extends AddressRecord
    {
        public Street()
        {
            super();
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
            if (s.length() > 0)
            {
                this.value = Text.toDisplayCase(s);
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
                this.value = this.value.replaceAll("\\s+", "");
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
//                s = new StringBuilder(s.toString().trim());
//                if (s.charAt(s.length() -1) == ' ')
//                    s.deleteCharAt(s.length() -1);
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
            mecard.util.Province province = new mecard.util.Province(s.toString().trim());
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
            String possibleProvince = Text.lastWord(s.toString().trim());
            if (this.test(possibleProvince))
            {
                int pos = s.lastIndexOf(possibleProvince);
                if (pos >= 0)
                {
                    s.delete(pos, pos + possibleProvince.length());
                }
                return true;
            }
            return false;
        }
    }
}
