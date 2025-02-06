/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013 - 2025 Edmonton Public Library
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


import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class PostalCodeTest {
    
    public PostalCodeTest() 
    {  }

    /**
     * Test of setPostalCode method, of class PostalCode.
     */
    @Test
    public void testSetPostalCode()
    {
        System.out.println("==setPostalCode==");
        String postalCode = "T6G-0G4";
        assertTrue(PostalCode.isValid(postalCode));
        
        postalCode = "T6G0G4";
        assertTrue(PostalCode.isValid(postalCode));
        
        postalCode = "T6GOG4"; // contains an 'O'
        assertFalse(PostalCode.isValid(postalCode));
        
        postalCode = "TTG0G4";
        assertFalse(PostalCode.isValid(postalCode));
        
        postalCode = "      ";
        assertFalse(PostalCode.isValid(postalCode));
        
        postalCode = "xxxxxxxxxxxxx";
        assertFalse(PostalCode.isValid(postalCode));
        
        postalCode = "";
        assertFalse(PostalCode.isValid(postalCode));
        
        postalCode = "";
        assertFalse(PostalCode.isValid(postalCode));
        
        postalCode = "TTG0G4";
        assertFalse(PostalCode.isValid(postalCode));
    }

    /**
     * Test of cleanPostalCode method, of class PostalCode.
     */
    @Test
    public void testFormatPostalCode()
    {
        System.out.println("==formatPostalCode==");
        String postalCode = "H0H0H0";
        String expResult = "H0H 0H0";
        String result = PostalCode.formatPostalCode(postalCode);
        assertEquals(expResult, result);
        
        postalCode = "H0H0H0bima";
        expResult = "H0H 0H0";
        result = PostalCode.formatPostalCode(postalCode);
        assertEquals(expResult, result);

        postalCode = "T6G0G4";
        expResult = "T6G 0G4";
        result = PostalCode.formatPostalCode(postalCode);
        assertEquals(expResult, result);
        
        postalCode = "T6G";
        expResult = "X";
        result = PostalCode.formatPostalCode(postalCode);
        assertEquals(expResult, result);
        
        postalCode = "T6G        ";
        expResult = "X";
        result = PostalCode.formatPostalCode(postalCode);
        assertEquals(expResult, result);
    }

    /**
     * Test of cleanPostalCode method, of class PostalCode.
     */
    @Test
    public void testCleanPostalCode() 
    {
        System.out.println("==cleanPostalCode==");
        String postalCode = "TTG  0G4";
        String newPCode = PostalCode.formatPostalCode(postalCode);
        System.out.println("PCODE:"+newPCode);
        assertTrue(newPCode.compareTo("TTG 0G4") == 0);
    }

    /**
     * Test of toString method, of class PostalCode.
     */
    @Test
    public void testToString() 
    {
        System.out.println("==toString==");
        PostalCode pCode = new PostalCode("T 6   g - 0    g  4    ");
        System.out.println("pCode:"+pCode);
        assertTrue(pCode.toString().compareTo("T6G 0G4") == 0);
    }

    /**
     * Test of isValid method, of class PostalCode.
     */
    @Test
    public void testIsValid() 
    {
        System.out.println("==isValid==");
        PostalCode pCode = new PostalCode("T6g  0g4");
        System.out.println("pCode:"+pCode);
        assertTrue(PostalCode.isValid(pCode.toString()));
    }
    
}
