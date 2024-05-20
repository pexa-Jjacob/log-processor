package com.mantel.logprocessor;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class LogParser {

    Path logPath = Path.of("src/main/resources/programming-task-example-data.log");
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
    private static final Pattern URL_PATTERN = Pattern.compile("(GET|POST|PUT|DELETE)\\s+(http://|https://)?[^\\s]+\\s+HTTP/1\\.1");

    Map<String, Integer> logIpAddress = new HashMap<>();
    Map<String, Integer> logUrls = new HashMap<>();

    public static void main(String[] args) {
        LogParser logParser = new LogParser();
        logParser.parseLog();
    }

    void parseLog() {
        log.info("Parsing log file: {}", logPath);
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                processLogIpAddress(line);
                processLogUrls(line);
            }
            printResults();
        } catch (IOException e) {
            log.error("Error reading log file");
            throw new RuntimeException(e);
        }
    }

    void processLogIpAddress(String line) {
        Matcher matcher = IP_ADDRESS_PATTERN.matcher(line);
        while (matcher.find()) {
            String ip = matcher.group();
            updateIpCount(ip);
        }
    }
    void updateIpCount(String url) {
        logIpAddress.put(url, logIpAddress.getOrDefault(url, 0) + 1);
    }

    void processLogUrls(String line) {
        Matcher matcher = URL_PATTERN.matcher(line);
        while (matcher.find()) {
            String url = matcher.group();
            updateUrlCount(url);
        }
    }

   private void updateUrlCount(String url) {
        logUrls.put(url, logUrls.getOrDefault(url, 0) + 1);
    }

    public List<String> getTopThree(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(entry -> entry.getKey() + ", with " + entry.getValue() + " entries: ")
                .toList();
    }

    void printResults() {
        String result = "For logfile: " + logPath.getFileName() + ", The top 3 IP Addresses are: \n" + String.join("\n", getTopThree(logIpAddress)) +
                "\n  There are " + logIpAddress.values().size() + " Unique IP Addresses" +
                "\n and the top 3 URLs are: \n" + String.join("\n", getTopThree(logUrls));
        log.info(result);
    }
}
