/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mecard.util;

import mecard.ResponseTypes;


public class ProcessWatcherHandler
{
    private ResponseTypes status;
    private StringBuffer stdout;
    private StringBuffer stderr;
    
    ProcessWatcherHandler()
    { 
        stdout = new StringBuffer();
        stderr = new StringBuffer();
        status = ResponseTypes.INIT;
    }
    
    void setStarted()
    {   
        this.status = ResponseTypes.BUSY;
    }

    void setStdout(String line)
    {
        this.stdout.append(line);
        this.stdout.append("|");
    }

    void setStderr(String line)
    {
        this.stderr.append(line);
        this.stderr.append("|");
    }

    void setEnded(int value)
    {
        this.stdout.append(value);
        this.stdout.append("|");
        this.stderr.append(value);
        this.stderr.append("|");
        status = ResponseTypes.OK;
    }

    void setError(Throwable th)
    {
        this.stderr.append(th.getMessage());
        this.status = ResponseTypes.ERROR;
    }

    public ResponseTypes getStatus() 
    {
        return status;
    }

    public String getStdout() 
    {
        return stdout.toString();
    }

    public String getStderr() 
    {
        return stderr.toString();
    }
}
