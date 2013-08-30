package api;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

 


import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrew Nisbet <anisbet@epl.ca>
 */
public class SSHAPICommandTest
{
    
    public SSHAPICommandTest()
    {
    }

    /**
     * Test of modifyForRemoteInvocation method, of class SSHInvocationModel.
     */
    @Test
    public void testModifyForRemoteInvocation()
    {
        System.out.println("===modifyForRemoteInvocation===");
        List<String> commandList = new ArrayList<String>();
//        commandList.add("cat");
//        commandList.add("-");
//        commandList.add(">");
//        commandList.add("/s/sirsi/metro/logs/Customers/catTest01.txt");
//        commandList.add("|");
        commandList.add("seluser");
        commandList.add("-iB");
//        commandList.add("B");
        commandList.add("-oBU");
//        commandList.add("BU");
//        List<String> testData = new ArrayList<String>();
//        testData.add("line 1");
//        testData.add("line 2");
//        testData.add("line 3");
//        testData.add("line 4");
        System.out.println("CMD:"+commandList);
//        APICommand command = new APICommand.Builder().cat(testData).commandLine(commandList).build();
        APICommand command = new APICommand.Builder("sirsi@edpl-t.library.ualberta.ca").echo("21221012345678").commandLine(commandList).build();
        CommandStatus status = command.execute();
        System.out.println("SSH_OUT:"+status.getStdout());
        System.out.println("SSH_ERR:"+status.getStderr());
    }
}