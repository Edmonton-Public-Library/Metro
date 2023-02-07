/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2023  Edmonton Public Library
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

package mecard.customer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class UserFileTest
{
    private final String testFileName;
    
    public UserFileTest()
    {
        this.testFileName = "userfiletest.txt";
    }

    /**
     * Test of addUserData method, of class UserFile.
     */
    @Test
    public void testAddUserData()
    {
        System.out.println("==create UserFile==");
        List<String> data = new ArrayList<>();
        data.add("*** DOCUMENT BOUNDARY ***");
//        data.add("FORM=LDUSER");
        data.add(".USER_ID.   |a21221012345678");
        data.add(".USER_FIRST_NAME.   |aBalzac");
        data.add(".USER_LAST_NAME.   |aBilly");
        data.add(".USER_PREFERRED_NAME.   |aBilly, Balzac");
        data.add(".USER_LIBRARY.   |aEPLMNA");
        data.add(".USER_PROFILE.   |aEPL-ADULT");
        data.add(".USER_PREF_LANG.   |aENGLISH");
        data.add(".USER_PIN.   |a64058");
        data.add(".USER_STATUS.   |aOK");
        data.add(".USER_ROUTING_FLAG.   |aY");
        data.add(".USER_CHG_HIST_RULE.   |aALLCHARGES");
        data.add(".USER_PRIV_GRANTED.   |a20130731");
        data.add(".USER_PRIV_EXPIRES.   |a20140602");
        data.add(".USER_BIRTH_DATE.   |a19750822");
        data.add(".USER_CATEGORY2.   |aF");
        data.add(".USER_ACCESS.   |aPUBLIC");
        data.add(".USER_ENVIRONMENT.   |aPUBLIC");
        data.add(".USER_ADDR1_BEGIN.");
        data.add(".STREET.   |a12345 123 St.");
        data.add(".CITY/STATE.   |aEdmonton, ALBERTA");
        data.add(".POSTALCODE.   |aH0H 0H0");
        data.add(".PHONE.   |a780-496-4058");
        data.add(".EMAIL.   |ailsteam@epl.ca");
        data.add(".USER_ADDR1_END.");
        // If this failes delete the file.
        if (new File(testFileName).exists() == false)
        {
            new File(testFileName).delete();
        }
        UserFile userFile = new UserFile(testFileName);
        userFile.addUserData(data);
        File f = new File(this.testFileName);
        assertTrue(f.exists());
        UserFile repeatUserFile = new UserFile(testFileName);
        repeatUserFile.addUserData(data);
        File backup = new File(this.testFileName + ".orig");
        assertFalse(backup.exists());
    }
}