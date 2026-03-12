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
    
}

