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
            if (os.contains("win")) {
                long value = 0;
                Process uptimeProc = Runtime.getRuntime().exec("net stats srv");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                while ((line = in.readLine()) != null) {
                    outLine.append(line).append("\n");
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

    /**
     * Returns the most recent version number of the MeCard server.
     * @param filePath path and name of the metro stdout log file.
     * @return String of the version number or 'No version found' if the MeCard
     * server is so early it doesn't report its version number.
     * @throws IOException 
     */
    public static String getLastVersionNumber(String filePath) throws IOException 
    {
        String lastVersion = null;
        Pattern pattern = Pattern.compile("Metro \\(MeCard\\) server version (\\d+\\.\\d+\\.\\d+(?:[_a-zA-Z]+)?)");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) 
                {
                    lastVersion = matcher.group(1);
                }
            }
        }

        return lastVersion != null ? lastVersion : "No version found";
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
     * @param filePath 
     * @return System information in a String.
     */
    public static String getServerHealth(String filePath)
    {
        String logFilePath = filePath + "/metro.out";
        String errFilePath = filePath + "/metro.err";
        String diskPath = "/"; // Root directory for disk space check
        StringBuilder outStr = new StringBuilder();
        try 
        {
            outStr.append(System.getProperty("os.name"))
                    .append(" version: ")
                    .append(System.getProperty("os.version"))
                    .append(" arch:")
                    .append(System.clearProperty("os.arch"))
                    .append("\n");
            outStr.append("Java: ").append(System.getProperty("java.version")).append("\n");
            outStr.append("MeCard version: ").append(getLastVersionNumber(logFilePath)).append("\n");
            outStr.append("User: ").append(System.getProperty("user.name")).append("\n");
            outStr.append("User dir: ").append(System.getProperty("user.dir")).append("\n");
            outStr.append("Up time: ").append(getServerUptime()).append(" hours").append("\n");
            LocalDateTime lastActivity = getLastActivityDateTime(logFilePath);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            outStr.append("Last active: ").append(lastActivity.format(formatter)).append("\n");
            outStr.append("Host disk: ").append(getHostDiskSpace(diskPath)).append("\n");
            outStr.append("Log size: ").append(getLogFileSize(logFilePath)).append(" KB").append("\n");
            outStr.append("Err size: ").append(getLogFileSize(errFilePath)).append(" KB").append("\n");
            outStr.append("Transactions: ").append(countLogTransactions(logFilePath)).append("\n");
        } catch (IOException e) {
            outStr.append("***error (IO exception) while checking server health!")
                    .append(" Is the log file missing?").append("\n");
        }
        return outStr.toString();
    }
    
    /**
     * For testing from command line.
     * @param args 
     */
    public static void main(String[] args) 
    {
        String logPath = "logs";
        System.out.println(getServerHealth(logPath));
    }
}