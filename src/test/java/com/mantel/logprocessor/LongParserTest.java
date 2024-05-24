package com.mantel.logprocessor;

import com.mantel.logprocessor.app.LogParser;
import exceptions.LogFileReadException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class LongParserTest {
    @InjectMocks
    private LogParser logParser;

    public static final Path TEST_LOG_PATH = Path.of("src/test/resources/test-log.log");
    public static final Path INVALID_LOG_PATH = Path.of("src/test/resources/non-existent-file.log");
    private static final String  MOCK_LOG_LINE = "192.168.1.1 - - [25/Apr/2024:10:29:00 +0000] \"GET /index.html HTTP/1.1\"";
    private static final String EXPECTED_ERROR_MESSAGE = "Error reading log file";
    private static final Integer EXPECTED_LIST_SIZE = 2;
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile("\\b((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");
    private static final Pattern URL_PATTERN = Pattern.compile("(GET|POST|PUT|DELETE)\\s+(http://|https://)?[^\\s]+\\s+HTTP/1\\.1");

    @Test
    void testParseLogUnhappyPath() {
        LogFileReadException thrown = assertThrows(LogFileReadException.class, () -> logParser.parseLog(INVALID_LOG_PATH));
        assertThat(thrown.getMessage()).isEqualTo(EXPECTED_ERROR_MESSAGE);
    }

    @Test
    void testProcessLogIpAddress() {
        Map<String, Integer> logIpAddress = new HashMap<>();
        logParser.processMap(MOCK_LOG_LINE, logIpAddress, IP_ADDRESS_PATTERN );

        assertThat(logIpAddress).containsEntry("192.168.1.1",1);
    }

    @Test
    void testProcessLogUrls() {
        Map<String, Integer> logUrls = new HashMap<>();
        logParser.processMap(MOCK_LOG_LINE, logUrls, URL_PATTERN);

        assertThat(logUrls).containsEntry("GET /index.html HTTP/1.1",1);
    }

    @Test
    void testParseLog() {
        Map<String, Integer> expectedIpAddressMap = buildIpAddressMap();
        Map<String, Integer> expectedUrlMap = buildUrlMap();

        List<Map<String, Integer>> logResults = logParser.parseLog(TEST_LOG_PATH);

        assertThat(logResults).hasSize(EXPECTED_LIST_SIZE);
        assertThat(logResults.get(0)).isEqualTo(expectedIpAddressMap);
        assertThat(logResults.get(1)).isEqualTo(expectedUrlMap);
    }

    private Map<String, Integer> buildIpAddressMap(){
        Map<String, Integer> logIpAddress = new HashMap<>();
        logIpAddress.put("192.168.1.1", 7);
        logIpAddress.put("198.51.100.0", 5);
        logIpAddress.put("203.0.113.0", 4);
        return logIpAddress;
    }

    private Map<String, Integer> buildUrlMap(){
        Map<String, Integer> logUrls = new HashMap<>();
        logUrls.put("DELETE /remove_data HTTP/1.1", 3);
        logUrls.put("GET /index.html HTTP/1.1", 7);
        logUrls.put("POST /form_submit HTTP/1.1", 1);
        logUrls.put("PUT /update_info HTTP/1.1", 5);
        return logUrls;
    }
}
