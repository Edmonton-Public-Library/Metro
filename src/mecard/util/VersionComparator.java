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
package mecard.util;

/**
 * Checks various versions of version numbers, providing logical Boolean
 * operators for comparison.
 * @author Andrew Nisbet <andrew(at)dev-ils.com>
 */
public class VersionComparator 
{
    /**
     * Compares if version1 is less than or equal to version2
     * @param version1 First version string (e.g., "7", "7.6", "3.10.1009")
     * @param version2 Second version string to compare against
     * @return true if version1 is less than or equal to version2, false otherwise
     */
    public static boolean lessThanEqualTo(String version1, String version2) 
    {
        return compareVersions(version1, version2) <= 0;
    }
    
    /**
     * Compares if version1 is less than version2
     * @param version1 First version string (e.g., "7", "7.6", "3.10.1009")
     * @param version2 Second version string to compare against
     * @return true if version1 is less than version2, false otherwise
     */
    public static boolean lessThan(String version1, String version2) 
    {
        return compareVersions(version1, version2) < 0;
    }
    
    /**
     * Compares if version1 is greater than version2
     * @param version1 First version string (e.g., "7", "7.6", "3.10.1009")
     * @param version2 Second version string to compare against
     * @return true if version1 is greater than version2, false otherwise
     */
    public static boolean greaterThan(String version1, String version2) 
    {
        return compareVersions(version1, version2) > 0;
    }
    
    /**
     * Compares if version1 is equal to version2
     * @param version1 First version string (e.g., "7", "7.6", "3.10.1009")
     * @param version2 Second version string to compare against
     * @return true if version1 is equal to version2, false otherwise
     */
    public static boolean equalTo(String version1, String version2) 
    {
        return compareVersions(version1, version2) == 0;
    }
    
    /**
     * Compares if version1 is greater than or equal to version2
     * @param version1 First version string (e.g., "7", "7.6", "3.10.1009")
     * @param version2 Second version string to compare against
     * @return true if version1 is greater than or equal to version2, false otherwise
     */
    public static boolean greaterThanEqualTo(String version1, String version2) 
    {
        return compareVersions(version1, version2) >= 0;
    }
    
    /**
     * Helper method that compares two version strings.
     * @param version1 First version string
     * @param version2 Second version string
     * @return -1 if version1 < version2, 0 if version1 == version2, 1 if version1 > version2
     */
    private static int compareVersions(String version1, String version2) 
    {
        // Remove 'v' or 'V' prefix if present
        version1 = removeVersionPrefix(version1);
        version2 = removeVersionPrefix(version2);
        // Split the version strings by dots
        String[] parts1 = version1.split("\\.");
        String[] parts2 = version2.split("\\.");
        
        // Compare each version component
        int length = Math.max(parts1.length, parts2.length);
        
        for (int i = 0; i < length; i++) 
        {
            // If parts1 is shorter, treat missing parts as 0
            int part1 = (i < parts1.length) ? Integer.parseInt(parts1[i]) : 0;
            // If parts2 is shorter, treat missing parts as 0
            int part2 = (i < parts2.length) ? Integer.parseInt(parts2[i]) : 0;
            
            if (part1 < part2) 
            {
                return -1;
            }
            if (part1 > part2) 
            {
                return 1;
            }
        }
        
        // All components are equal
        return 0;
    }
    
    /**
     * Removes 'v' or 'V' prefix from a version string if present.
     * 
     * @param version The version string that might have a prefix
     * @return The version string without 'v' or 'V' prefix
     */
    private static String removeVersionPrefix(String version) {
        if (version != null && version.length() > 0) {
            if (version.charAt(0) == 'v' || version.charAt(0) == 'V') {
                return version.substring(1);
            }
        }
        return version;
    }
    
}
