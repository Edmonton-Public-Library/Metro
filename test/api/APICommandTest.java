/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Edmonton Public Library
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
package api;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests the command features of APICommand.
 * @author anisbet
 */
public class APICommandTest
{
    
    public APICommandTest()
    {
    }

    /**
     * Test of execute method, of class APICommand.
     */
    @Test
    public void testExecute()
    {
        System.out.println("==execute==");
        List<String> commandsList = new ArrayList<>();
        commandsList.add("C:\\Program Files (x86)\\WinSCP\\WinSCP.exe");
//        commandsList.add("/w");
        APICommand instance = new APICommand.Builder().commandLine(commandsList).build();
        CommandStatus result = instance.execute();
        System.out.println(result.status);
        System.out.println(result.stdout);
        System.out.println(result.stderr);
    }

    /**
     * Test of toString method, of class APICommand.
     */
    @Test
    public void testToString()
    {
        System.out.println("==toString==");
        List<String> commandsList = new ArrayList<>();
        commandsList.add("ls");
        commandsList.add("-lat");
        APICommand instance = new APICommand.Builder().commandLine(commandsList).build();
        System.out.println(instance);
    }
    
}
