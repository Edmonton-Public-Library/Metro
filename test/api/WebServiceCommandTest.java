
package api;

import mecard.ResponseTypes;
import org.apache.http.entity.ContentType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anisbet
 */


public class WebServiceCommandTest
{
    
    public WebServiceCommandTest()
    {
    }

    /**
     * Test of execute method, of class WebServiceCommand.
     */
    @Test
    public void testExecute()
    {
        System.out.println("==execute==");
        WebServiceCommand command = new WebServiceCommand
                .Builder()
                .setURI("http://207.167.28.31/PAPIService/REST/public/v1/1033/100/1/patron/29335002291059/messages")
                .setHTTPVerb("GET")
//                .debug()
                .build();
        CommandStatus result = command.execute();
        System.out.println("TEST_OUTPUT: '" + result.getStdout() + "'");
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
        
        command = new WebServiceCommand
                .Builder()
                .setURI("http://207.167.28.31/UhOh.html")
                .setHTTPVerb("GET")
//                .debug()
                .build();
        result = command.execute();
        System.out.println("TEST_OUTPUT: '" + result.getStdout() + "'");
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.CONFIG_ERROR);
    }
    
    @Test
    public void testAuthenticate()
    {
        System.out.println("== Authenticate ==");
        String xmlContent = "<AuthenticationData>\n" +
            "<Domain>yrls</Domain>\n" +
            "<Username>yrldtest</Username>\n" +
            "<Password>m3Tr0seRv</Password>\n" +
            "</AuthenticationData>";
        WebServiceCommand command = new WebServiceCommand
                .Builder()
                .setURI("https://207.167.28.31/PAPIService/REST/protected/v1/1033/100/1/authenticator/staff")
                .setHTTPVerb("POST")
                .setContentType(xmlContent, ContentType.APPLICATION_XML)
                .debug()
                .build();
        CommandStatus result = command.execute();
        System.out.println("Authentication OUTPUT: '" + result.getStdout() + "'");
        assertNotNull(result);
        assertTrue(result.getStatus() == ResponseTypes.SUCCESS);
    }
    
}
