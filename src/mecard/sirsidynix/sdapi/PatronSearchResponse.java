/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2026 Edmonton Public Library
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either httpVersion 2 of the License, or
 * (at your option) any later httpVersion.
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

package mecard.sirsidynix.sdapi;

import api.CustomerMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;
import mecard.config.SDapiUserFields;

public abstract class PatronSearchResponse 
        extends SDapiResponse
        implements CustomerMessage
{

//    @Override
//    public abstract boolean cardReportedLost();
//
//    // Failed response
//    //{
//    //   "searchQuery": "ID:212210123456789",
//    //   "startRow": 1,
//    //   "lastRow": 10,
//    //   "rowsPerPage": 10,
//    //   "totalResults": 0,
//    //   "result": []
//    //}
//    @Override
//    public abstract String errorMessage();
//
//    @Override
//    public abstract String getCustomerProfile();
//
//    @Override
//    public abstract String getDateField(String fieldName);
//
//    @Override
//    public abstract String getField(String fieldName);
//
//    @Override
//    public abstract String getStanding();

    public abstract int getTotalResults();

//    @Override
//    public abstract boolean isEmpty(String fieldName);
//
//    @Override
//    public abstract boolean isInGoodStanding();
//
//    @Override
//    public abstract boolean succeeded();


    @Override
    public String errorMessage() 
    {
        if (! this.succeeded())
        {
            return "Account not found.";
        }
        return "";
    }

    @Override
    public String getCustomerProfile()
    {
        return this.getField(SDapiUserFields.PROFILE.toString());
    }
    
    @Override
    public boolean cardReportedLost() 
    {
//        return this.getCustomerProfile().equals("LOST") || this.getCustomerProfile().equals("LOSTCARD");
        String customerCardProfile = this.getCustomerProfile();
        List<String> lostTypes = new ArrayList<>();
        Properties props       = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
        // Find the lostcard sentinal types.
        // read optional fields from environment. Should be ',' separated.
        // <entry key="lost-card-sentinel">LOST, LOSTCARD</entry>
        PropertyReader.loadDelimitedEntry(props, LibraryPropertyTypes.LOST_CARD_SENTINEL, lostTypes);

        // Non-residents
        for (String str: lostTypes)
        {
            if (customerCardProfile.equalsIgnoreCase(str)) // Test fails lost card.
            {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean isInGoodStanding() 
    {
        return "OK".equals(this.getStanding());
    }
    
    @Override
    public boolean isEmpty(String fieldName) 
    {
        return this.getField(fieldName).isBlank();
    }

    @Override
    public String getStanding() 
    {
        return this.getField(SDapiUserFields.STANDING.toString());
    }
    
    @Override
    public boolean succeeded() 
    {
        // User Key is the only value returned in common between a successful 'barcode',
        // 'search' or 'slim' response.
        return ! this.getField(SDapiUserFields.USER_KEY.toString()).isBlank();
    }
    
}

