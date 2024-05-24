package com.mantel.logprocessor.app;

import exceptions.LogFileReadException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.MapUtils;
import service.ResultBuilder;

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
@RequiredArgsConstructor
public class LogParser {
    private final ResultBuilder resultBuilder;

    /**
     * Path to the log file to be parsed.
     */
    private static final Path logPath = Path.of("src/main/resources/programming-task-example-data.log");

    /**
     * Regular expression pattern for matching IP addresses in log lines.
     */
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");

    /**
     * Regular expression pattern for matching URLs in log lines.
     */
    private static final Pattern URL_PATTERN = Pattern.compile("(GET|POST|PUT|DELETE)\\s+(http://|https://)?[^\\s]+\\s+HTTP/1\\.1");

    public static void main(String[] args) {
        LogParser logParser = new LogParser(new ResultBuilder(new MapUtils()));
        log.info(logParser.resultBuilder.buildResultString(logParser.parseLog(logPath), logPath));
    }

    /**
     * Parses the log file and processes each line to extract IP addresses and URLs.
     * Returns a list of Maps where each map contains the extracted data and their occurrence count.
     *
     * @param logPath The path of the log file to be parsed.
     * @return A list of Maps containing the extracted data and their occurrence count.
     */
    public List<Map<String, Integer>> parseLog(Path logPath) {
        Map<String, Integer> logIpAddress = new HashMap<>();
        Map<String, Integer> logUrls = new HashMap<>();

        log.info("Parsing log file: {}", logPath);
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                processMap(line, logIpAddress, IP_ADDRESS_PATTERN);
                processMap(line, logUrls, URL_PATTERN);
            }
        } catch (IOException e) {
            throw new LogFileReadException("Error reading log file", e);
        }
        return List.of(logIpAddress, logUrls);
    }

    /**
     * Processes a log line to extract and count occurrences of IP addresses or URLs.
     * The extracted data is stored in the provided map.
     *
     * @param line The line from the log file to be processed.
     * @param map The map to store the extracted data and their occurrence count.
     * @param regexPattern The regular expression pattern to match the data to be extracted.
     */
    public void processMap(String line, Map<String, Integer> map, Pattern regexPattern) {
        Matcher matcher = regexPattern.matcher(line);
        while (matcher.find()) {
            String matchedString = matcher.group();
            map.put(matchedString, map.getOrDefault(matchedString, 0) + 1);
        }
    }
}
