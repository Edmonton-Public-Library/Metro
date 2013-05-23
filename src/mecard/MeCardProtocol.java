/*
 * Copyright (C) 2013 metro
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package mecard;

public class MeCardProtocol 
{
    private ProtocolStates state;
    public MeCardProtocol()
    {
        state = ProtocolStates.WAITING;
    }
    
    public String processInput(String theInput) 
    {
        StringBuilder theOutput = new StringBuilder();
        
        switch(state)
        { // TODO Implement this!
            case WAITING:
                break;
            case QUERY_READY:
                break;
            case QUERY_CUSTOMER:
                break;
            case CREATE_CUSTOMER:
                break;
            case UPDATE_CUSTOMER:
                break;
            default:
                System.err.println("Unsupported operation requested.");
        }
        
        return theOutput.toString();
    }
}
