/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
package mecard.security;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import java.util.Properties;
import mecard.util.Text;

/**
 * Provides methods to check environment.properties and test passwords for site policy
 * restriction and compute a new hashed of the current password that does match
 * policy restrictions if necessary.
 * 
 * The parameters are controlled through the <strong>optional</strong> 
 * environment.properties file with the following entries
 *         ...
 * <pre>{@code
 *         This describes the maximum length of a password. In the case of
 *         WRBL they have an unusual restriction of '16' digits.
 *                <entry key="password-max-length">16</entry>
 *         This describes the minimum length of the password; typically this
 *         value would be '4'.
 *                <entry key="password-min-length">4</entry>
 *         This entry includes all the valid characters allowed by the site's
 *         password restriction policy. In this case only e-l-o-H and the digits
 *         from 1-8 are allowed.
 *                <entry key="allowed-password-characters">eloH12345678</entry>
 *            </properties>
 * }</pre>
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public final class CustomerPasswordRestrictions
{
    private final Properties properties = PropertyReader.getProperties(ConfigFileTypes.ENVIRONMENT);
    private final int passwordMaxLength;
    private final int passwordMinLength;
    private final String allowedCharacters;
    
    public CustomerPasswordRestrictions()
    {
        // if possible, get the password restrictions, but they are optional.
        String passwordMaxLengthProperty = this.properties.getProperty("password-max-length","");
        String passwordMinLengthProperty = this.properties.getProperty("password-min-length","");
        String passwordAllowedCharsProperty = this.properties.getProperty("allowed-password-characters","");
        
        
        if ( passwordMaxLengthProperty.isEmpty() )
        {
            // there are no restrictions so these values are not used.
            this.passwordMaxLength = 256;
        }
        else
        {
            this.passwordMaxLength = Integer.parseInt(passwordMaxLengthProperty);
            System.out.println("DEBUG: env.passwordMaxLength = " + this.passwordMaxLength);
        }
        
        
        if ( passwordMinLengthProperty.isEmpty() )
        {
            this.passwordMinLength = 4;
        }
        else
        {
            this.passwordMinLength = Integer.parseInt(passwordMinLengthProperty);
            System.out.println("DEBUG: env.passwordMinLength = " + this.passwordMinLength);
        }
        
        
        if ( passwordAllowedCharsProperty.isEmpty() )
        {
            this.allowedCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789\n" +
                "!@#$%^&*()-_=+[]{}|;:'\",.<>/?`";
        }
        else
        {
            this.allowedCharacters = passwordAllowedCharsProperty;
            System.out.println("DEBUG: env.allowedCharacters = " + this.allowedCharacters);
        }  
    }
    
    /**
     * Tests if the password fails any of the restrictions that may or may not
     * be defined in the environment.properties file.
     * @param password the customer's password string.
     * @return true if the password needs to be hashed because it contravenes 
     * password restrictions and false otherwise.
     */
    public boolean requiresHashedPassword(String password)
    {
        if ( password.length() < this.passwordMinLength ) return true;
        if ( password.length() > this.passwordMaxLength ) return true;
        // check if the password contains any characters not in the allowed char set
        // this is the most expensive so do it last.
        for ( int i = 0; i < password.length(); i++ ) 
        {
            char ch = password.charAt(i);
            if ( this.allowedCharacters.indexOf(ch) == -1 )
            {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Checks if the password meets or fails any password restrictions and if
     * it does computes a new password hash of max length digits.
     * @param password customer's string password.
     * @return the customers password if it doesn't conflict with configured
     * password restrictions at the site (specified in the environment.properties file),
     * and a hash of the customer's password as a string of digits if it does 
     * contravene site password restrictions.
     */
    public String checkPassword(String password)
    {
        if ( this.requiresHashedPassword( password ) )
        {
            return Text.getHashedPassword( password, this.passwordMaxLength );
        }
        return password;
    }
}
