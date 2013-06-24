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
package api;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import mecard.Exception.MalformedCommandException;
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
        // Instantiate query type ot NULL.
        code = QueryTypes.NULL;
        // split to command into it's parts
        this.splitCommand(request);
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
            throws MalformedCommandException
    {
        if (cmd == null || cmd.isEmpty())
        {
            String msg = "Request is null or empty.";
            throw new MalformedCommandException(msg);
        }
        Gson gson = new Gson();
        try
        {
            List<String> cmdLine = gson.fromJson(cmd, List.class);
            this.code = this.getQueryType(cmdLine.get(0));
            this.authorityToken = cmdLine.get(1);
            for (int i = 2; i < cmdLine.size(); i++)
            {
                this.addResponse(cmdLine.get(i));
            }
        }
        catch (IndexOutOfBoundsException ex)
        {
            throw new MalformedCommandException("queries must include an API key.");
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
    public String toString()
    {
        List<String> retList = new ArrayList<String>();
        retList.add(code.toString());
        retList.add(authorityToken);
        retList.addAll(payload);
        Gson gson = new Gson();
        return gson.toJson(retList, List.class);
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
            throws UnsupportedOperationException
    {
        QueryTypes type;
        for (QueryTypes q : QueryTypes.values())
        {
            if (q.toString().compareTo(string) == 0)
            {
                return q;
            }
        }
        String msg = "The requested command '" + string 
                + "' is unsupported. Is there a mistake in your request?";
        throw new MalformedCommandException(msg);
    }
}
