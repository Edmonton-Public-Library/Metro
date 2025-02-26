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


import static org.junit.Assert.*;

import org.junit.Test;

/**
 *
 * @author anisbet
 */
public class VersionComparatorTest 
{
    
    /**
     * Test of lessThanEqualTo method, of class VersionComparator.
     */
    @Test
    public void testLessThanEqualTo() 
    {
        System.out.println("==lessThanEqualTo==");
        assertEquals(true, VersionComparator.lessThanEqualTo("7.6", "7.6"));
        assertEquals(true, VersionComparator.lessThanEqualTo("1.0", "7.6"));
        assertEquals(true, VersionComparator.lessThanEqualTo("0.00.0", "7.6"));
        assertEquals(true, VersionComparator.lessThanEqualTo("7.6.00", "7.6"));
        assertEquals(false, VersionComparator.lessThanEqualTo("7.6.01", "7.6"));
    }

    /**
     * Test of lessThan method, of class VersionComparator.
     */
    @Test
    public void testLessThan() 
    {
        System.out.println("==lessThan==");
        assertEquals(false, VersionComparator.lessThan("7.6", "7.6"));
        assertEquals(true, VersionComparator.lessThan("7.6", "7.6.00.0001"));
    }

    /**
     * Test of greaterThan method, of class VersionComparator.
     */
    @Test
    public void testGreaterThan() 
    {
        System.out.println("==greaterThan==");
        assertEquals(true, VersionComparator.greaterThan("7.6.1", "7.6"));
        assertEquals(false, VersionComparator.greaterThan("7.6.00", "7.6"));
    }

    /**
     * Test of equalTo method, of class VersionComparator.
     */
    @Test
    public void testEqualTo() 
    {
        System.out.println("==equalTo==");
        assertEquals(true, VersionComparator.equalTo("7.6", "7.6"));
        assertEquals(true, VersionComparator.equalTo("7.6.00", "7.6"));
    }

    /**
     * Test of greaterThanEqualTo method, of class VersionComparator.
     */
    @Test
    public void testGreaterThanEqualTo() 
    {
        System.out.println("==greaterThanEqualTo==");
        assertEquals(true, VersionComparator.greaterThanEqualTo("7.6", "7"));
        assertEquals(true, VersionComparator.greaterThanEqualTo("7.00.001", "7"));
        assertEquals(false, VersionComparator.greaterThanEqualTo("7.0.9502.0", "6.9"));
    }
    
}
