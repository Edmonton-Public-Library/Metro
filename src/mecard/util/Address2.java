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
import java.util.Arrays;
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
        if (this.phone.test(supposedAddressBuilder) == false)
        {
            // To get here SIP2 has pasted a partial phone number to the end of 
            // the address. Symphony customers are created with a default initial
            // value of '780-'. If left it becomes their phone number, though
            // clearly not valid. Let's test gently to get rid of this value.
            this.phone.testGently(supposedAddressBuilder);
        }
        
        // Different approach. If we have taken care of the phone and pcode
        // then chop off each of the remaining elements and try to match
        // The next would be province.
        // 
        // Some addresses like 'BOX 20 SITE 7 RR1, Red Deer, AB, T4N 5E1'
        // cause a problem that manifests as 'Box 20 Site 7 Rr1 Red, Red Deer, AB, T4N 5E1'
        // There are two ways to deal with this split on the commas...
        if (this.computedAddressCityAndProvinceSuccessfully(supposedAddressBuilder) == false)
        {
            String cleanAddress = supposedAddressBuilder.toString().replace(",", " ");
            List<String> addressWords = new ArrayList<>();
            addressWords.addAll(Arrays.asList(cleanAddress.split("\\s+")));
            if (addressWords.size() > 0) // at least one word
            {
                int lastWordPos = addressWords.size() -1;
                if (this.province.test(addressWords.get(lastWordPos)))
                {
                    addressWords.remove(lastWordPos);
                }
            }
            // next should be the city, or place name.
            if (addressWords.size() > 0) // at least one word
            {
                // Grab the next word off the end of the array of address words and try
                // and match it.
                int lastWordPos = addressWords.size() -1;
                String possiblePlaceName = addressWords.remove(lastWordPos);
                if (! this.city.testGently(possiblePlaceName))
                {
                    // There is still a chance that possiblePlaceName has too many selection
                    // if it was Valley, or County, so try the next word.
                    lastWordPos = addressWords.size() -1;
                    if (lastWordPos >= 0)
                    {
                        String nextWord = addressWords.remove(lastWordPos);
                        if (! this.city.testGently(nextWord))
                        {
                            // ok we're barking up the wrong tree, this doesn't look 
                            // like a valid place name so restore the address field.
                            addressWords.add(nextWord);
                            addressWords.add(possiblePlaceName);
                        }
                    }
                }
            }
            // paste street back together.
            String addressString = "";
            for (String s: addressWords)
            {
                addressString += s + " ";
            }
            this.street.test(addressString.trim());
        }
    }
    
    /**
     * There is a chance that the address is separated by ','s, and if so we have
     * a good chance of getting street, city and province easily.
     * @param supposedAddressBuilder
     * @return true if the street city and province could be parsed out of the 
     * input string and false otherwise.
     */
    private boolean computedAddressCityAndProvinceSuccessfully(StringBuilder supposedAddressBuilder)
    {
        // This strategy works on the principle that the the address string has some sort of ', ' delimiter.
        if (supposedAddressBuilder.indexOf(", ") < 0)
        {
            return false;
        }
        // ok if that worked then let's split the string.
        String[] addrValues = supposedAddressBuilder.toString().split(",\\s");
        if (addrValues.length != 3) // we got some strange splitting.
        {
            return false;
        }
        if (this.province.test(addrValues[2]) == false)
        {
            return false;
        }
        if (this.city.test(addrValues[1]) == false)
        {
            // Sometimes we get systems that place the apartment at the end between 
            // the street and city name, you know who you are, so here let's test 
            // if this failed because of an apartment number.
            String[] possibleApartmentCityName = parsePossibleApartmentNumber(addrValues[1]);
            if (possibleApartmentCityName.length < 2)
            {
                return false;
            }
            // value at [1] is the city place name(?)
            if (this.city.test(possibleApartmentCityName[1]) == false)
            {
                return false;
            }
            // value at [0] is the apartment number which will be
            addrValues[0] = possibleApartmentCityName[0] + "-" + addrValues[0];
        }
        return this.street.test(addrValues[0]);
    }
    
    private String[] parsePossibleApartmentNumber(String string)
    {
        String retString = string.trim();
        Pattern partialApartmentPattern = Pattern.compile("^[a|A]pt\\.? \\d{1,} ");
        Matcher matcher = partialApartmentPattern.matcher(retString);
        String[] retArray;
        if (matcher.find())
        {
            retArray = new String[2];
            retArray[0] = matcher.group().trim();
            retArray[1] = retString.substring(matcher.end());
        }
        else
        {
            retArray = new String[1];
            // no luck just return the entire string for reconsideration.
            retArray[0] = retString;
        } 
        return retArray;
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
        private final Pattern partialPhonePattern;
        public Phone()
        {
            super();
            // end of line matching important to avoid 209-1123 street matching.
//            this.phonePattern = Pattern.compile("\\d{3}[\\-| ]?\\d{3}-?\\d{4}$");
//            this.phonePattern = Pattern.compile("\\d{3}[-| ]\\d{3}[-| ]\\d{4}$");
            this.phonePattern = Pattern.compile("\\(?\\d{3}\\)?[-| ]\\d{3}[-| ]\\d{4}$");
//            this.partialPhonePattern = Pattern.compile("\\d{3}-$");
            // A broken partial could look like this:
            // 96-4058, 780-, (780-, and what about 780 555-1212
            this.partialPhonePattern = Pattern.compile("[(|\\d{1,}][-|\\s{1,}|\\d{1,}]*$");
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
                this.value = mecard.util.Phone.formatPhone(matcher.group());
                // if arg is a string builder we are supposed to modify it.
                s.delete(matcher.start(), matcher.end());
                return true;
            }
            return false;
        }

        /**
         * Gently tests if the substring at the end of the argument is a partial
         * phone number. If tests positive then the fragment will be removed.
         *
         * @param supposedAddressBuilder complete or partial address line.
         * @return true if a partial phone number was found at the end of the argument
         * and false otherwise. The phone number fragment, if found, is not retained.
         */
        private boolean testGently(StringBuilder supposedAddressBuilder)
        {
            String testString = supposedAddressBuilder.toString();
            Matcher matcher = partialPhonePattern.matcher(testString);
            if (matcher.find())
            {
                // in this case the phone number is not valid so we should delete it.
                supposedAddressBuilder.delete(matcher.start(), matcher.end());
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
        private List<String> placeName; // store multiple selections.
        
        public City()
        {
            this.city = AlbertaCity.getInstanceOf();
            this.placeName = new ArrayList<>();
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
            // so sometimes the name will end in a common term like: Valley, 
            // Lake or County. Here we will first check how big the selection
            // set is. If it is more than 1 then return false.
            // The first time this method is hit we populate the List of potential
            // names
            if (this.placeName.isEmpty())
            {
                this.placeName = this.city.getPlaceNames(place);
                if (this.placeName.isEmpty())
                {
                    return false;
                }
                if (this.placeName.size() > 1)
                {
                    // we have a couple of choices let's see if one of them matches
                    // exactly: find 'Lethbridge' from list of ['Lethbridge', 'County of Lethbridge']
                    for (int i = 0; i < this.placeName.size(); i++)
                    {
                        String possibleName = this.placeName.get(i);
                        if (possibleName.equalsIgnoreCase(place))
                        {
                            this.value = possibleName;
                            this.placeName.clear();
                            return true;
                        }
                    }
                    return false; // No exact matches.
                }
                else // there must be 1 name.
                {
                    this.value = this.placeName.get(0);
                    this.placeName.clear();
                    return true;
                }
            }
            else // The list is not empty so a previous call must have filled it.
            {
                for (int i = 0; i < this.placeName.size(); i++)
                {
                    String possibleName = this.placeName.get(i);
                    if (possibleName.contains(place))
                    {
                        this.value = possibleName;
                        this.placeName.clear();
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * Computes and returns the size of the city name. If the city has 
         * not been set it returns 0;
         * @return number of words in the place name.
         */
        private int getWordCount()
        {
            if (this.value.compareTo(Protocol.DEFAULT_FIELD_VALUE) == 0)
            {
                return 0;
            }
            return this.value.split("\\s+").length;
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
            return this.test(s.toString());
        }
    }
}
