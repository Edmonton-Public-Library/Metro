/**
 *
 * vpc-commons library
 *
 * Description: <start><end>
 *
 * Copyright (C) 2006-2008 Taha BEN SALAH
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 */
package api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * <pre>
 *      Process process = Runtime.getRuntime().exec(new String[]{"/bin/java","-version"}, null, new File("."));
 *      CommandWatcher w = new CommandWatcher(process, new CommandStatus() {
 *          public void setStarted(Process process) {
 *              System.out.println("Prcess setStarted");
 *          }
 *
 *          public void setStdout(Process process, String line) {
 *              System.out.println(line);
 *          }
 *
 *          public void setStderr(Process process, String line) {
 *              System.err.println(line);
 *          }
 *
 *          public void setEnded(Process process, int value) {
 *              System.out.println("Process Shutdown. Exit Value :" + value);
 *          }
 *
 *          public void setError(Process process, Throwable th) {
 *              System.err.println(th);
 *          }
 *      });
 *      w.start();
 * </pre>
 *
 * @author Taha Ben Salah (taha.bensalah@gmail.com)
 */
public class CommandWatcher
{

    private Process process;
    private Thread end;
    private Thread out;
    private Thread err;
    private int result;
    private boolean stopped = false;
    private CommandStatus handler;

    public CommandWatcher(Process theProcess, CommandStatus theHandler)
    {
        this.process = theProcess;
        this.handler = theHandler;
        this.end = new EndThread();
        this.out = new StdOutThread();
        this.err = new StdErrThread();
    }

    public void start()
    {
        handler.setStarted();
        out.start();
        err.start();
        end.start();
        try
        {
            // now join the threads to this one so that they all end together.
            this.end.join();
            this.out.join();
            this.err.join();
        }
        catch (InterruptedException ex)
        {
            Logger.getLogger(CommandWatcher.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int waitFor()
    {
        while (!stopped)
        {
            Thread.yield();
        }
        return result;
    }

    private class StdErrThread extends Thread
    {

        @Override
        public void run()
        {
            String read;
            BufferedReader in = null;
            try
            {
                in = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                while (!stopped)
                {
                    read = in.readLine();
                    if (read == null)
                    {
                        break;
                    }
                    handler.setStderr(read);
                }
            }
            catch (Throwable e)
            {
                handler.setError(e);
            }
            finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    }
                    catch (IOException e)
                    {
                        handler.setError(e);
                    }
                }
            }
        }
    }

    private class StdOutThread extends Thread
    {

        @Override
        public void run()
        {
            String read;
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (!stopped)
            {
                try
                {
                    read = in.readLine();
                    if (read == null)
                    {
                        break;
                    }
                    handler.setStdout(read);
                }
                catch (Throwable e)
                {
                    handler.setError(e);
                    break;
                }
            }
        }
    }

    private class EndThread extends Thread
    {

        @Override
        public void run()
        {
            try
            {
                result = process.waitFor();
                handler.setEnded(result);
            }
            catch (Throwable e)
            {
                handler.setError(e);
            }
            finally
            {
                stopped = true;
            }
        }
    }
}
