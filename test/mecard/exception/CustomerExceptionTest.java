/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2025  Edmonton Public Library
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
package mecard.exception;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import static org.junit.Assert.assertEquals;
import org.junit.Test;


/**
 *
 * @author anisbet
 */
public class CustomerExceptionTest 
{
    
    @Test
    public void testBasicConstructorMethod() 
    {
        String expectedResult = """
                                Oops!
                                please contact this library for more information about your registration.""";
        try
        {
            throw new CustomerException();
        }
        catch (CustomerException e)
        {
            assertEquals(e.getMessage(), expectedResult);
            System.out.println(">>>" + e.getMessage());
        }
    }
    
    @Test
    public void testCustomerCreationStringMethod() 
    {
        String expectedResult = """
                                Oops!,
                                please contact this library for more information about your registration.""";
        Properties props = PropertyReader.getProperties(ConfigFileTypes.MESSAGES);
        String message = props.getProperty(MessagesTypes.DUPLICATE_USER.toString());
//        String failPolicyMessage = props.getProperty(MessagesTypes.FAIL_METRO_POLICY.toString());
        try
        {
            throw new CustomerCreationException(message);
        }
        catch (CustomerException e)
        {
//            assertEquals(e.getMessage(), expectedResult);
            System.out.println(">>>" + e.getMessage());
        }
    }
    
}
