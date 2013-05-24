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

import mecard.ResponseTypes;

/**
 *
 * @author metro
 */
public class CreateCustomerResponder extends Responder
{
    public CreateCustomerResponder(String cmd)
    {
        super(cmd);
        this.state = ResponseTypes.BUSY;
        this.command = splitCommand(cmd);
    }

    @Override
    public String getResponse()
    {
        this.state = ResponseTypes.SUCCESS;
        return pack(response);
    }
}
