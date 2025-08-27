/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
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

package mecard.calgary.cplapi;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class CPLapiCardNumberPinTest
{
    
    /**
     * Test of getPatronAuthentication method, of class CPLapiCardNumberPin.
     */
    @Test
    public void testGetPatronAuthentication() 
    {
        System.out.println("==getPatronAuthentication==");
        String cardNumber = "123456";
        String pin = " this is a password ";
        CPLapiCardNumberPin instance = new CPLapiCardNumberPin();
        String expResult = "{\"cardNumber\": \"123456\", \"pin\": \" this is a password \"}";
        String result = instance.getPatronAuthentication(cardNumber, pin);
        assertEquals(expResult, result);
    }

    /**
     * Test of getStaffAuthentication method, of class CPLapiCardNumberPin.
     */
    @Test
    public void testGetStaffAuthentication() 
    {
        System.out.println("==getStaffAuthentication==");
        String domain = "Some domain";
        String cardNumber = "123456";
        String pin = " this is a password ";
        CPLapiCardNumberPin instance = new CPLapiCardNumberPin();
        String expResult = "{\"cardNumber\": \"123456\", \"pin\": \" this is a password \"}";
        String result = instance.getStaffAuthentication(domain, cardNumber, pin);
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class CPLapiCardNumberPin.
     */
    @Test
    public void testToString() 
    {
        System.out.println("==toString==");
        String cardNumber = "123456";
        String pin = " this is a password ";
        CPLapiCardNumberPin instance = new CPLapiCardNumberPin(cardNumber, pin);
//        System.out.println(instance.toString());
        String expResult = "{\"cardNumber\": \"123456\", \"pin\": \" this is a password \"}";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
