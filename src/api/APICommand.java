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

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import mecard.MetroService;
import mecard.config.ConfigFileTypes;

/**
 * Builds commands ready for execution.
 *
 * @author metro
 */
public class APICommand implements Command
{
    public final static String WORKING_DIRECTORY_PROPERTY_NAME = "working-directory";
    private APICommandTypes cmdFormat;
    //    private final APICommand command;
    private final List<String> cmdArgs;
    private final List<String> stdinData;

    public static class Builder
    {
        // required
        private APICommandTypes cmdFormat;
        // optional
        private List<String> args;
        private List<String> stdinData;

        /**
         * Constructor that insists that the command gets at least a status
         * object and a command.
         */
        public Builder()
        {
            this.cmdFormat = APICommandTypes.CMD_LINE;
        }

        /**
         * Allows the inclusion of addition command-line arguments to a command.
         *
         * @param arguments
         * @return Builder. Usage: APICommand cmd = new APICommand.Builder(status,
         * APICommandTypes.WC).args(new Sting[]{"a","b"}).build();
         */
        public Builder args(List<String> arguments)
        {
            this.args = new ArrayList<String>();
            this.args.addAll(arguments);
            return this;
        }

        /**
         * Works like the 'cat' command in Unix, that is, will cats lines of
         * data to the stdin of this command.
         *
         * @param data List of Strings for input like what you would cat from a file.
         * @return Builder.
         */
        public Builder cat(List<String> data)
        {
            this.cmdFormat = APICommandTypes.CMD_PIPE;
            this.stdinData = new ArrayList<String>();
            this.stdinData.addAll(data);
            return this;
        }

        /**
         * Works like the 'echo' command in Unix, that is, will echo a single
         * string to the stdin of this command.
         *
         * @param stringIn String that you want to echo into this command.
         * @return Builder.
         */
        public Builder echo(String stringIn)
        {
            this.cmdFormat = APICommandTypes.CMD_PIPE;
            this.stdinData = new ArrayList<String>();
            this.stdinData.add(stringIn);
            return this;
        }

        /**
         * Builds the command and returns a reference to it.
         *
         * @return APICommand reference. Usage: APICommand cmd = new
         * APICommand.Builder(status,
         * APICommandTypes.WC).echo("21221012345678").build();
         */
        public APICommand build()
        {
            return new APICommand(this);
        }
    }

    /**
     * Constructor, private to forbid APICommand instantiation directly (without
     * builder).
     *
     * @param b - Builder
     */
    private APICommand(Builder b)
    {
        this.cmdFormat = b.cmdFormat;
        this.stdinData = b.stdinData;
        this.cmdArgs = b.args;
    }

    /**
     * Wrapper that executes the command.
     *
     */
    @Override
    public CommandStatus execute()
    {
        CommandStatus status;
        switch(this.cmdFormat)
        {
            case CMD_PIPE:
                status = this.executePipe();
                break;
            default:
                status = this.executeCmdLine();
                break;
        }
        return status;
    }

    /**
     * Executes a command in the format of 'cmd [args]. Use this for Windows,
     * and many Unix commands.
     *
     */
    private CommandStatus executeCmdLine()
    {
        CommandStatus processHandler = null;
        try
        {
            // http://stackoverflow.com/questions/10665104/executing-an-external-program-from-java-using-process-builder-or-apache-commons
            ProcessBuilder processBuilder = new ProcessBuilder(this.getCmd());
            // get properties and set them as necessary
            setEnvironment(processBuilder);
            Process process = processBuilder.start();
            processHandler = new CommandStatus();
            CommandWatcher watcher = new CommandWatcher(process, processHandler);
            watcher.start();
        }
        catch (IOException ex)
        {
            System.out.println("IOException no such command: " + toString() + "\n" + ex.getMessage());
//            Logger.getLogger(APICommand.class.getName()).log(
//                    Level.SEVERE, "no such command", ex);
        }
        return processHandler;
    }

    /**
     * Executes a pipe command. This facility is not available on Windoz.
     *
     */
    private CommandStatus executePipe()
    {
        CommandStatus processHandler = null;
        try
        {
            ProcessBuilder processBuilder = new ProcessBuilder(this.getCmd());
            setEnvironment(processBuilder);
            Process commandTwo = processBuilder.start();
            BufferedWriter commandTwoInput = new BufferedWriter(new OutputStreamWriter(commandTwo.getOutputStream()));
            // read each line from ls until there are no more
            for (String lineReadFromCommandOne : stdinData)
            {
                // and send them to stdin
                commandTwoInput.write(lineReadFromCommandOne);
                System.out.println("LN:'" + lineReadFromCommandOne + "'");
                commandTwoInput.newLine();
            }
            // send end-of-file signal to next process so it will terminate itself
            commandTwoInput.close();
            processHandler = new CommandStatus();
            CommandWatcher commandWatcher = new CommandWatcher(commandTwo, processHandler);
            commandWatcher.start();
        }
        catch (IOException ex)
        {
            System.out.println("IOException no such command: " + toString() + "\n" + ex.getMessage());
//            Logger.getLogger(APICommand.class.getName()).log(
//                    Level.SEVERE, "no such command", ex);
        }
        return processHandler;
    }

    private void setEnvironment(ProcessBuilder processBuilder)
    {
        Map<String, String> env = processBuilder.environment();
        MetroService.augmentProperties(env, ConfigFileTypes.VARS);
        if (env.get(APICommand.WORKING_DIRECTORY_PROPERTY_NAME) != null &&
            env.get(APICommand.WORKING_DIRECTORY_PROPERTY_NAME).isEmpty() == false)
            processBuilder.directory(new File(env.get(APICommand.WORKING_DIRECTORY_PROPERTY_NAME)));
    }

    /**
     * Returns the command as an array of discrete strings.
     *
     * @return array of Strings that make up the command. Note that commands
     * don't tolerate spaces so 'ls -la' should be passed as 'ls', '-la'.
     */
    private String[] getCmd()
    {
        String[] returnValues = new String[cmdArgs.size()];
        for (int i = 0; i < cmdArgs.size(); i++)
        {
            returnValues[i] = cmdArgs.get(i);
        }
        return returnValues;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        if (stdinData != null && stdinData.size() > 0)
        {
            sb.append("STDIN=>'");
            for (String s : stdinData)
            {
                sb.append(s);
            }
            sb.append("' | ");
        }
        else
        {
            sb.append("CMD:'");
        }

        for (String s : getCmd())
        {
            sb.append(s);
            sb.append(" ");
        }
        sb.append("'\n");
        return sb.toString();
    }
}
