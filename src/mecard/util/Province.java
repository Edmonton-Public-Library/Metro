/**
 *
 * This class is part of the Metro, MeCard project. Copyright (C) 2013 Andrew
 * Nisbet, Edmonton public Library.
 * 
* This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
* This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
* You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 * 
*/
package mecard.util;

/**
 *
 * @author metro
 */
public class Province
{
    private boolean isValid = true;
    private String province;
    
    public Province(String p)
    {
        province = "";
        // Special case where the string is empty
        if (p == null || p.length() == 0)
        {
            this.province = ProvinceType.ALBERTA.toString();
        }
        if (p.equalsIgnoreCase("Alberta") || p.equalsIgnoreCase("ab"))
        {
            this.province = ProvinceType.ALBERTA.toString();
        }
        if (p.equalsIgnoreCase("British Columbia") || p.equalsIgnoreCase("bc"))
        {
            this.province = ProvinceType.BRITISH_COLUMBIA.toString();
        }
        if (p.equalsIgnoreCase("Manitoba") || p.equalsIgnoreCase("mb"))
        {
            this.province = ProvinceType.MANITOBA.toString();
        }
        if (p.equalsIgnoreCase("New Brunswick") || p.equalsIgnoreCase("nb"))
        {
            this.province = ProvinceType.NEW_BRUNSWICK.toString();
        }
        if (p.equalsIgnoreCase("Newfoundland") || p.equalsIgnoreCase("nl"))
        {
            this.province = ProvinceType.NEWFOUNDLAND_LABRADOR.toString();
        }
        if (p.equalsIgnoreCase("Northwest Territories") || p.equalsIgnoreCase("nwt"))
        {
            this.province = ProvinceType.NORTHWEST_TERRITORIES.toString();
        }
        if (p.equalsIgnoreCase("Nova Scotia") || p.equalsIgnoreCase("ns"))
        {
            this.province = ProvinceType.MANITOBA.toString();
        }
        if (p.equalsIgnoreCase("Nunavut") || p.equalsIgnoreCase("nu"))
        {
            this.province = ProvinceType.NUNAVUT.toString();
        }
        if (p.equalsIgnoreCase("Ontario") || p.equalsIgnoreCase("on"))
        {
            this.province = ProvinceType.ONTARIO.toString();
        }
        if (p.equalsIgnoreCase("Prince Edward Island") || p.equalsIgnoreCase("pei"))
        {
            this.province = ProvinceType.PRINCE_EDWARD_ISLAND.toString();
        }
        if (p.equalsIgnoreCase("Quebec") || p.equalsIgnoreCase("qc"))
        {
            this.province = ProvinceType.QUEBEC.toString();
        }
        if (p.equalsIgnoreCase("Saskatchewan") || p.equalsIgnoreCase("sk"))
        {
            this.province = ProvinceType.SASKATCHEWAN.toString();
        }
        if (p.equalsIgnoreCase("Yukon") || p.equalsIgnoreCase("yk"))
        {
            this.province = ProvinceType.YUKON.toString();
        }
    }
    
    public boolean isValid()
    {
        return this.isValid;
    }
    
    @Override
    public String toString()
    {
        return this.province;
    }
}
