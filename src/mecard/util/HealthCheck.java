/*
 * Metro allows customers from any affiliate library to join any other member library.
 *    Copyright (C) 2024  Edmonton Public Library
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
package mecard.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import mecard.config.PropertyReader;

/**
 * Utility class to report important environment diagnostic information.
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class HealthCheck
{
    /**
     * Get the server up time. Does its best to work with Unix, Mac, and Windows.
     * @return up time in hours.
     */
    public static String getServerUptime() 
    {
        String os = System.getProperty("os.name").toLowerCase();
        StringBuilder outLine = new StringBuilder();
        try 
        {
            String line;
            if (os.contains("win")) 
            {
                long value = 0;
                Process uptimeProc = Runtime.getRuntime().exec("net stats srv");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                while ((line = in.readLine()) != null) 
                {
                    outLine.append(line).append(", ");
                }
            } 
            else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) 
            {
                Process uptimeProc = Runtime.getRuntime().exec("uptime");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                outLine.append(in.readLine());
            }
        } 
        catch (IOException | NumberFormatException e) 
        {  }
        return outLine.length() != 0 ? outLine.toString() : "-1";
    }

    /**
     * Computes and returns the size of a given file, esp. the log or error
     * file.
     * @param logPath
     * @return long size in KBs.
     */
    public static long getLogFileSize(String logPath) 
    {
        File file = new File(logPath);
        return file.length() / 1000;
    }

    /**
     * Counts the number of transactions performed.
     * @param filePath - log file path and name.
     * @return integer number of transactions.
     * @throws IOException 
     */
    public static int countLogTransactions(String filePath) throws IOException 
    {
        int transactionCount = 0;
        Pattern pattern = Pattern.compile("^\\w{3} \\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2} \\w{3} \\d{4} transaction completed\\.$");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) 
                {
                    transactionCount++;
                }
            }
        }

        return transactionCount;
    }
    
    /**
     * Returns the last modification time of the log file.
     * @param filePath path and name of the log file.
     * @return The last modification time stamp.
     * @throws IOException 
     */
    public static LocalDateTime getLastActivityDateTime(String filePath) throws IOException 
    {
        Path path = Paths.get(filePath);
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        Instant instant = attr.lastModifiedTime().toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Returns the disk space available to the system (on this partition anyway).
     * @param path
     * @return String of the total, free, and usable disk space on the system.
     */
    public static String getHostDiskSpace(String path) 
    {
        File file = new File(path);
        long totalSpace = file.getTotalSpace();
        long freeSpace = file.getFreeSpace();
        long usableSpace = file.getUsableSpace();

        return String.format("Total: %d GB, Free: %d GB, Usable: %d GB",
                totalSpace / (1024 * 1024 * 1024),
                freeSpace / (1024 * 1024 * 1024),
                usableSpace / (1024 * 1024 * 1024));
    }
    
    protected static class LogFinder 
    {
        private String startDirectory;
        private String logFilePath;
        private String errFilePath;

        public LogFinder(String startDirectory) 
        {
            this.startDirectory = startDirectory;
        }

        public String findLogPath() 
        {
           return searchDirectory(new File(startDirectory));
        }

        private String searchDirectory(File directory) 
        {
            File[] files = directory.listFiles();
            if (files != null) 
            {
                for (File file : files) 
                {
                    if (file.isFile()) 
                    {
                        String fileName = file.getName();
                        if (fileName.equals("metro.out")) 
                        {
                            this.logFilePath = file.getAbsolutePath();
                            this.errFilePath = file.getParent() + File.separator + "metro.err";
                            return file.getParent();
                        }
                        else if (fileName.equals("stdout.txt"))
                        {
                            this.logFilePath = file.getAbsolutePath();
                            this.errFilePath = file.getParent() + File.separator + "stderr.txt";
                            return file.getParent();
                        }
                    } 
                    else if (file.isDirectory()) 
                    {
                        String result = searchDirectory(file);
                        if (result != null) 
                        {
                            return result;
                        }
                    }
                }
            }
            return null;
        }
        
        public String getLogFile()
        {
            return this.logFilePath;
        }
        
        public String getErrFile()
        {
            return this.errFilePath;
        }
    }
    
    
    /**
     * Prints out basic server health as:
     * <li>Version number.</li>
     * <li>Server up time.</li>
     * <li>Log file size.</li>
     * <li>Error file size.</li>
     * <li>Number of transactions.</li>
     * <li>Last activity of the MeCard server.</li>
     * <li>Host disk space.</li> 
     * @return System information in a String.
     */
    public static String getServerHealth()
    {
        StringBuilder outStr = new StringBuilder();
        try 
        {
            String os = System.getProperty("os.name").toLowerCase();
            outStr.append(System.getProperty("os.name"));
            outStr.append(" version: ").append(System.getProperty("os.version"));
            outStr.append(" arch:").append(System.clearProperty("os.arch")).append(", ");
            outStr.append("Java: ").append(System.getProperty("java.runtime.version")).append(", ");
            outStr.append("MeCard version: ").append(PropertyReader.getVersion()).append(", ");
            outStr.append("User: ").append(System.getProperty("user.name")).append(", ");
            outStr.append("Up time: ").append(getServerUptime()).append(" hours").append(", ");
            
            System.out.println(">>>" + System.getenv("LOG_OUT") + "<<<");
            String homeDir = System.getProperty("user.home");
            LogFinder finder = new LogFinder(homeDir);
            String logPath = finder.findLogPath();
            // Testing log file specifically.
            if (logPath != null) 
            {
                String logFilePath = finder.getLogFile();
                String errFilePath = finder.getErrFile();
                LocalDateTime lastActivity = getLastActivityDateTime(logFilePath);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                outStr.append("Last active: ").append(lastActivity.format(formatter)).append(", ");
                outStr.append("Log size: ").append(getLogFileSize(logFilePath)).append(" KB").append(", ");
                outStr.append("Transactions: ").append(countLogTransactions(logFilePath)).append(", ");
                outStr.append("Err size: ").append(getLogFileSize(errFilePath)).append(" KB").append(", ");
                outStr.append("Host disk: ").append(getHostDiskSpace(logPath));
            }
            else
            {
                outStr.append("log file not found. Some tests cannot be completed.");
            }
        } catch (IOException e) {
            outStr.append("***error (IO exception) while checking server health!")
                  .append(" Is the log file missing?");
        }
        return outStr.toString();
    }
    
    /**
     * For testing from command line.
     * @param args 
     */
    public static void main(String[] args) 
    {
        System.out.println(getServerHealth());
    }
}