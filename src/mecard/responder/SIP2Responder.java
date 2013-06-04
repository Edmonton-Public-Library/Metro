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
package mecard.responder;

import java.util.Properties;
import mecard.ResponseTypes;
import mecard.config.ConfigFileTypes;
import mecard.config.PropertyReader;
import mecard.config.SipPropertyTypes;

/**
 * The SIP2 strategy is meant to retrieve initial information about customers.
 * Some computation is done to allow each library an opportunity to compute
 * answers to questions like is this customer a reciprocal customer?
 * @author andrew
 */
public class SIP2Responder extends ResponderStrategy
{
    private final String host;
    private final String port;
    
    public SIP2Responder(String command, boolean debugMode)
    {
        super(command, debugMode);
        this.state = ResponseTypes.BUSY;
        
        Properties sipProps = PropertyReader.getProperties(ConfigFileTypes.SIP2);
        host = sipProps.getProperty(SipPropertyTypes.HOST.toString());
        port = sipProps.getProperty(SipPropertyTypes.PORT.toString(), "6001"); // port optional in config.
    }

    @Override
    public String getResponse()
    {
        // test for the operations that this responder is capable of performing
        // SIP can't create customers, BImport can't query customers.
        StringBuffer responseBuffer = new StringBuffer();
        switch (this.cmdType)
        {
            case GET_CUSTOMER:
//                this.state = submitCustomer(responseBuffer);
//                this.response.add(responseBuffer.toString());
                this.state = ResponseTypes.OK;
                this.response.add("Hello World");
                break;
            case GET_STATUS:
                this.state = ResponseTypes.OK;
                this.response.add("Hello World");
                break; // is SIP2 the best way to get the ILS status, it is one way.
            default:
                this.state = ResponseTypes.ERROR;
                this.response.add(BImportResponder.class.getName()
                        + " cannot perform operation: " + this.cmdType.toString());
        }
        return pack(response);
    }
    
}
