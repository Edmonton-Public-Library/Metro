/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2013  Andrew Nisbet
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

import mecard.Protocol;
import mecard.ProtocolPayload;
import mecard.QueryTypes;

/**
 *
 * @author metro
 */
public class Request extends ProtocolPayload 
{

    protected QueryTypes code;
    protected String authorityToken;

    public Request(String request) 
    {
        // split to command into it's parts
        this.splitCommand(request);
        if (this.authorityToken == null)
        {
            this.authorityToken = ""; // some commands don't require a token
        }
    }
    
    /**
     * Split the commandArguments on the Protocol's delimiter breaking the
     * commandArguments into chunks. The first element on the list is the
     * commandArguments itself which can be ignored since it was already dealt
     * with when this object was created. The second is the MD5 hash of the
     * query salted with the senders shared secret. The rest of the elements (if
     * any) are arguments to the commandArguments.
     *
     * @param cmd
     * @return
     */
    private void splitCommand(String cmd) 
    {
        String[] cmdLine = cmd.split("\\"+Protocol.DELIMITER);
        // every command must have at least one command.
        this.code = getQueryType(cmdLine[0]);
        // some commands like NULL and GET_STATUS don't come with a authorityToken
        if (cmdLine.length > 1)
        {
            this.authorityToken = cmdLine[1];
            // the rest (if any) are arguments.
            for (int i = 2; i < cmdLine.length; i++) 
            {
                this.addResponse(cmdLine[i]);
            }
        }
    }

    /**
     * 
     * @return the request code.
     */
    public QueryTypes getCommandType() 
    {
        return code;
    }

    /** 
     *
     * @return the request arguments of the request, not including any request
     * code or authority token.
     * @see ProtocolPayload#toString() 
     */
    public String getArgs() 
    {
        return super.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.code);
        sb.append(Protocol.DELIMITER);
        sb.append(this.authorityToken);
        sb.append(Protocol.DELIMITER);
        sb.append(super.toString());
        return sb.toString();
    }

    public String getTransactionId() 
    {
        return this.authorityToken;
    }

    public String get(int ordinal) 
    {
        try
        {
            return payload.get(ordinal);
        }
        catch (IndexOutOfBoundsException e)
        {
            System.out.println(e.getMessage());
            return "";
        }
    }

    private QueryTypes getQueryType(String string) 
    {
        QueryTypes type = QueryTypes.NULL;
        for (QueryTypes q : QueryTypes.values()) {
            if (q.toString().compareTo(string) == 0) {
                type = q;
            }
        }
        return type;
    }
        }
