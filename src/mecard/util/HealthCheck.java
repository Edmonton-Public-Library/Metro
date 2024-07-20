
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
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Andrew Nisbet <andrew at dev-ils.com>
 */
public class HealthCheck
{

    public static long getServerUptime() 
    {
        String os = System.getProperty("os.name").toLowerCase();
        try 
        {
            if (os.contains("win")) {
                Process uptimeProc = Runtime.getRuntime().exec("net stats srv");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("Statistics since")) {
                        String[] parts = line.split("\\s+");
                        String[] timeParts = parts[parts.length - 1].split(":");
                        return TimeUnit.HOURS.toSeconds(Integer.parseInt(timeParts[0])) +
                               TimeUnit.MINUTES.toSeconds(Integer.parseInt(timeParts[1])) +
                               Integer.parseInt(timeParts[2]);
                    }
                }
            } 
            else if (os.contains("mac") || os.contains("nix") || os.contains("nux") || os.contains("aix")) 
            {
                Process uptimeProc = Runtime.getRuntime().exec("uptime");
                BufferedReader in = new BufferedReader(new InputStreamReader(uptimeProc.getInputStream()));
                String line = in.readLine();
                if (line != null) {
                    // Pattern to match different uptime formats
                    Pattern pattern = Pattern.compile("up\\s+(\\d+)\\s+days?|up\\s+(\\d+):?(\\d+)|(\\d+)\\s+min");
                    Matcher matcher = pattern.matcher(line);

                    long days = 0, hours = 0, minutes = 0;

                    while (matcher.find()) {
                        if (matcher.group(1) != null) {
                            // Days
                            days = Long.parseLong(matcher.group(1));
                        } else if (matcher.group(2) != null) {
                            // Hours and optionally minutes
                            hours = Long.parseLong(matcher.group(2));
                            if (matcher.group(3) != null) {
                                minutes = Long.parseLong(matcher.group(3));
                            }
                        } else if (matcher.group(4) != null) {
                            // Only minutes
                            minutes = Long.parseLong(matcher.group(4));
                        }
                    }
                    // hours
                    return ((days * 24 * 60 * 60) + (hours * 60 * 60) + (minutes * 60)) / (60 * 60);
                }
            }
        } 
        catch (IOException | NumberFormatException e) 
        {  }
        return -1; // Return -1 if unable to determine uptime
    }

    public static long getLogFileSize(String filePath) 
    {
        File file = new File(filePath);
        return file.length() / 1000;
    }

    public static int countLogTransactions(String filePath) throws IOException 
    {
        int customerCount = 0;
        Pattern pattern = Pattern.compile("^\\w{3} \\w{3} \\d{2} \\d{2}:\\d{2}:\\d{2} \\w{3} \\d{4} transaction completed\\.$");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) 
        {
            String line;
            while ((line = reader.readLine()) != null) 
            {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) 
                {
                    customerCount++;
                }
            }
        }

        return customerCount;
    }

    public static LocalDateTime getLastActivityDateTime(String filePath) throws IOException 
    {
        Path path = Paths.get(filePath);
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
        Instant instant = attr.lastModifiedTime().toInstant();
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

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

    public static void main(String[] args) 
    {
        String logFilePath = "logs/metro.out";
        String errFilePath = "logs/metro.err";
        String diskPath = "/"; // Root directory for disk space check

        try 
        {
            System.out.println("MeCard version number: " + getLastVersionNumber(logFilePath));
            System.out.println("Server uptime: " + getServerUptime() + " hours");
            System.out.println("Out file size: " + getLogFileSize(logFilePath) + " KB");
            System.out.println("Err file size: " + getLogFileSize(errFilePath) + " KB");
            System.out.println("Number of transactions: " + countLogTransactions(logFilePath));
            
            LocalDateTime lastActivity = getLastActivityDateTime(logFilePath);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            System.out.println("Last activity: " + lastActivity.format(formatter));
            System.out.println("Host disk space: " + getHostDiskSpace(diskPath));
            
        } catch (IOException e) {
            System.out.println("***error (IO exception) while checking server health!");
        }
    }
}