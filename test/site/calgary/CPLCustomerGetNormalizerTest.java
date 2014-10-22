
package site.calgary;

import api.SIPCustomerMessage;
import api.SIPMessage;
import mecard.config.CustomerFieldTypes;
import mecard.util.Address3;
import mecard.util.DateComparer;
import mecard.util.Phone;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */


public class CPLCustomerGetNormalizerTest
{
    private final String goodStanding;
    private final String juvenile;
    private final String suspended;
    private final String nonResident;
    private final String expired;
    private final String lost;
    public CPLCustomerGetNormalizerTest()
    {
//        Charge Privileges Denied :          Y
//        Renewal Privileges Denied :         Y
//        Recall Privileges Denied :          Y
//        Hold Privileges Denied:             Y
//        Card Reported Lost :                Y
//        Too Many Items Charged :             
//        Too many Items Overdue :             
//        Too Many Renewals :                  
//        Too Many Claims Of Items Returned :  
//        Too Many Items Lost :                
//        Excessive Outstanding Fines :        
//        Excessive Outstanding Fees :         
//        Recall Overdue :                     
//        Too Many Items Billed :
        // these are returned from CAR (ChinookArch).
        this.goodStanding = "64              00020140328    130720000000000000000000000000AO|AA55544466677788|AEkelso, test adult|AQCENT|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD123 sad street nw CALGARY, AB t3g 1m1|BE21@test.ca|BF403-123-4567|BHUSD|PA20141008    235900|PD|PCADULT|PEAR|DB$0.00|DM$500.00|AFOK|AY0AZB6B0";
        this.suspended    = "64YYYY          00020140328    131112000000000000000000000000AO|AA29065010271955|AETest, Piglet 2|AQCENT|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD616 MacLeod Trail SE Calgary AB T2G 2M2|BEcarolyn.kelso@calgarypubliclibrary.com|BF403-260-2747|BHUSD|PA20150327    235900|PD|PCADULT|PECR|PGMALE|PHALL|DB$0.00|DM$500.00|AFUser BARRED|AY4AZA40D";
        this.juvenile     = "64              00020140328    131024000000000000000000000000AO|AA29065013913017|AEBrat, Tiny Test|AQSIG|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD123 Aisforapple Drive Calgary AB X0X 0X0|BEtest@test.ca|BF403-123-4567|BHUSD|PA20150206    235900|PD|PCCHILD|PECR|PGFEMALE|PHFILTER|PIYES|DB$0.00|DM$500.00|AFOK|AY3AZAD73";
        this.nonResident  = "64              00020140328    130932000000000000000000000000AO|AA29065010071066|AETest, Roo|AQCENT|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD616 MacLeod Trail SE Calgary AB T2G 2M2|BECarolyn.kelso@calgarypubliclibrary.com|BF403-260-2747|BHUSD|PA20150327    235900|PD|PCADULT|PEANR|PGMALE|PHALL|PIYES|DB$0.00|DM$500.00|AFOK|AY2AZA71D";
        this.expired      = "64YYYY          00020140328    130831000000000000000000000000AO|AA29065013913025|AEBrat, Small Test|AQSIG|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD123 Aisforapple Drive Calgary AB X0X 0X0|BEtest@shaw.ca|BF403-123-4567|BHUSD|PA20130327    235900|PD19990226|PCADULT|PECR|PGMALE|PHFILTER|PIYES|DB$0.00|DM$500.00|AFPrivilege has expired|AY1AZA398";
        this.lost         = "64YYYY          00020140328    150819000000000000000000000000AO|AA29065023801673|AETest Metro, Adult Lostcard|AQCROW|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD123 Reside Rd NW CALGARY, AB T3G 3G3|BF403-123-4567|BHUSD|PA20150108    235900|PD19580103|PCADULT|PEAR|PGFEMALE|PHALL|DB$32.99|DM$500.00|AFUser BARRED|AY1AZAF1D";
//        recv:64              00020140304    070512000000000000000000000000AO|AA21817002446849|AEGAMACHE, ARMAND|AQLPL|BZ9999|CA9999|CB9999|BLY|CQY|BV 0.00|BDBOX 43 LETHBRIDGE, ALBERTA T1J 3Y3 403-555-1234|BEpwauters@hotmail.com|BF403-555-1234|BHUSD|PA20150218    235900|PD|PCLPLADULT|PELETHCITY|PFADULT|PGMALE|DB$0.00|DM$500.00|AFOK|AY0AZACA0
    }

    /**
     * Test of getCustomerProfile method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetCustomerProfile()
    {
        System.out.println("===getCustomerProfile===");
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(goodStanding);
        System.out.println("GOOD:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
        
        sipMessage = new SIPCustomerMessage(suspended);
        System.out.println("suspended:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
       
        
        sipMessage = new SIPCustomerMessage(juvenile);
        System.out.println("juvenile:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
        
        sipMessage = new SIPCustomerMessage(nonResident);
        System.out.println("nonResident:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
        
        sipMessage = new SIPCustomerMessage(expired);
        System.out.println("expired:>>"+sipMessage + "<<");
        this.printContents(sipMessage);
        
    }
    
    private void printContents(SIPMessage sipMessage)
    {
        System.out.println(CustomerFieldTypes.ID + ":" + sipMessage.getField("AA"));
        System.out.println(CustomerFieldTypes.PREFEREDNAME + ":" + sipMessage.getField("AE"));
        System.out.println(CustomerFieldTypes.RESERVED + ":" + sipMessage.getField("AF"));
        System.out.println(CustomerFieldTypes.EMAIL + ":" +  sipMessage.getField("BE"));
        // Phone object
        Phone phone = new Phone(sipMessage.getField("BF"));
        System.out.println(CustomerFieldTypes.PHONE + ":" + phone.getUnformattedPhone());
        // Privilege date dates: horizon uses PE and puts profile in PA.
        // The test is slightly less expensive 
        String cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PA"));
        if (DateComparer.isDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // PE can also be privilege expires, so if PA fails try this field.
        else
        {
            cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PE"));
            System.out.println(CustomerFieldTypes.PRIVILEGE_EXPIRES + ":" + cleanDate);
        }
        // DOB same thing
        cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PB"));
        if (DateComparer.isDate(cleanDate))
        {
            System.out.println(CustomerFieldTypes.DOB + ":" + DateComparer.cleanDateTime(sipMessage.getField("PB"))); // Strathcona.
        }
        else
        {
            cleanDate = DateComparer.cleanDateTime(sipMessage.getField("PD"));
            System.out.println(CustomerFieldTypes.DOB + ":" + cleanDate);
        }
        System.out.println(CustomerFieldTypes.SEX + ":" + sipMessage.getField("PF"));
        // Complete address
        System.out.println("===ADDRESS===\nShould be in field BD>>"
                + sipMessage.getField("BD") + "<<");
        Address3 address = new Address3(sipMessage.getField("BD"));
        System.out.println(CustomerFieldTypes.STREET + ":" + address.getStreet());
        System.out.println(CustomerFieldTypes.CITY + ":" + address.getCity());
        System.out.println(CustomerFieldTypes.PROVINCE + ":" + address.getProvince());
        System.out.println(CustomerFieldTypes.POSTALCODE + ":" + address.getPostalCode());
        // Next careful, EPL gloms the phone on the end of the address, but if a lib returns
        // the phone in the correct field parsing this will erase the phone we already
        // collected.
        System.out.println(CustomerFieldTypes.PHONE + ":" + address.getPhone());
        System.out.println("===ADDRESS===\n\n");
    }

    /**
     * Test of getMessage method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetMessage()
    {
        System.out.println("==getMessage==");
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(this.goodStanding);
        assertFalse(sipMessage.isReported(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
        sipMessage = new SIPCustomerMessage(this.lost);
        assertTrue(sipMessage.isReported(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
        assertTrue(sipMessage.isTrue(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
        assertFalse(sipMessage.isFalse(SIPCustomerMessage.PATRON_STATUS_FLAGS.CARD_REPORTED_LOST));
    }

    /**
     * Test of getStanding method, of class SIPCustomerMessage.
     */
    @Test
    public void testGetStanding()
    {
        System.out.println("==getStanding==");
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(this.goodStanding);
        assertFalse(sipMessage.cardReportedLost());
    }

    /**
     * Test of cardReportedLost method, of class SIPCustomerMessage.
     */
    @Test
    public void testCardReportedLost()
    {
        System.out.println("==cardReportedLost==");
//        ####### LOST card user
//        sent:63                               AO|AA29065023801673|AD1234|AY1AZF3A8
//        recv:64YYYY          00020140328    150819000000000000000000000000AO|AA29065023801673|AETest Metro, Adult Lostcard|AQCROW|BZ0050|CA0000|CB0099|BLY|CQY|BV0.00|BD123 Reside Rd NW CALGARY, AB T3G 3G3|BF403-123-4567|BHUSD|PA20150108    235900|PD19580103|PCADULT|PEAR|PGFEMALE|PHALL|DB$32.99|DM$500.00|AFUser BARRED|AY1AZAF1D
//
//        Response code:64
//        Patron Information Response
//          (O) Patron Status : YYYY          
//                Charge Privileges Denied :          Y
//                Renewal Privileges Denied :         Y
//                Recall Privileges Denied :          Y
//                Hold Privileges Denied:             Y
//                Card Reported Lost :                 
//                Too Many Items Charged :             
//                Too many Items Overdue :             
//                Too Many Renewals :                  
//                Too Many Claims Of Items Returned :  
//                Too Many Items Lost :                
//                Excessive Outstanding Fines :        
//                Excessive Outstanding Fees :         
//                Recall Overdue :                     
//                Too Many Items Billed :              
//          (F) Language:                      000
//          (F) Transaction Date:              20140328    150819
//          (F) Hold Items Count:              0000
//          (F) Overdue Items Count:           0000
//          (F) Charged Items Count:           0000
//          (F) Fine Items Count:              0000
//          (F) Recall Items Count:            0000
//          (F) Unavailable Hold Items Count:  0000
//          (R) Institution Id:
//          (R) Patron Identifier:29065023801673
//          (R) Personal Name:Test Metro, Adult Lostcard
//          (R) Patron Library:CROW
//          (O) Hold Items Limit:0050:
//          (O) Overdue Items Limit:0000:
//          (O) Charged Items Limit:0099:
//          (O) Valid Patron:Y:
//          (O) Valid Patron Password:Y:
//          (O) Fee Amount:0.00
//          (O) Home Address:123 Reside Rd NW CALGARY, AB T3G 3G3
//          (O) Home Phone Number:403-123-4567
//          (O) Currency Type:USD:
//          (O) Expiration Date:20150108    235900
//          (O) Birth date:19580103
//          (O) Profile:ADULT
//          (O) User Category 1:AR
//          (O) User Category 3:FEMALE
//          (O) User Category 4:ALL
//          (O) DB INVALID DATA CODE FOR THIS MESSAGE:$32.99
//          (O) DM INVALID DATA CODE FOR THIS MESSAGE:$500.00
//          (O) Screen Message:User BARRED
//          (R) Sequence Number : 1 :  matches what was sent
//          (R) Checksum : AF1D : Checksum OK
        SIPCustomerMessage sipMessage = new SIPCustomerMessage(this.goodStanding);
        assertFalse(sipMessage.cardReportedLost());
        sipMessage = new SIPCustomerMessage(this.lost);
        assertTrue(sipMessage.cardReportedLost());
    }
    
}
