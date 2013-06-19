
package mecard.util;

import java.util.Properties;
import mecard.config.ConfigFileTypes;
import mecard.config.LibraryPropertyTypes;
import mecard.config.PropertyReader;

/**
 * Provides phone conversion.
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class Phone 
{
    public final static CharSequence DEFAULT_PHONE_DELIMITER = "-";
    /**
     * Formats a phone number from 7804366077 to 780[delim]436[delim]6077.
     * @param p
     * @return formatted phone number (area code)-(exchange)-(phone).
     */
    public final static String formatPhone(String p)
    {
        String phNum = p.trim();
        if (p.contains(DEFAULT_PHONE_DELIMITER)) return p;
        int len = phNum.length();
        if (phNum.length() < 4) return p;
        // now get the phone number
        String phoneNumber = phNum.substring(len -4);
        System.out.println("PHONE_NUM:"+phoneNumber);
        if (phNum.length() < 7) return phoneNumber;
        String exchange = phNum.substring((len -7), (len -4));
        System.out.println("EXCHG:"+exchange);
        if (phNum.length() < 10) 
        {
            return exchange + DEFAULT_PHONE_DELIMITER +
               phoneNumber;
        }
        String areaCode = phNum.substring(0, (len -7));
        System.out.println("AREA:"+areaCode);
        return areaCode + DEFAULT_PHONE_DELIMITER +
               exchange + DEFAULT_PHONE_DELIMITER +
               phoneNumber;
    }
}
