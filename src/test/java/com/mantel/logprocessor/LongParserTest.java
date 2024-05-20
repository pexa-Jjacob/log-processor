package com.mantel.logprocessor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class LongParserTest {
    @Spy
    private LogParser logParser;

    private static final String  mockLogLine = "192.168.1.1 - - [25/Apr/2024:10:29:00 +0000] \"GET /index.html HTTP/1.1\"";
    private static final Integer EXPECTED_TOP_THREE_SIZE = 3;

    @BeforeEach
    public void setUp() {
        logParser.logPath = Paths.get("src/test/resources/test-log.log");
    }

    @Test
    void testParseLog() {
        logParser.parseLog();
        verify(logParser, times(1)).parseLog();
    }

    @Test
    void testParseLogUnhappyPath() {
        logParser.logPath = Paths.get("src/test/resources/non-existent-file.log");

        verifyNoMoreInteractions(logParser);
    }

    @Test
    void testProcessLogIpAddress() {
        logParser.processLogIpAddress(mockLogLine);
        Map<String, Integer> logIpAddress = logParser.logIpAddress;
        assertThat(logIpAddress.get("192.168.1.1")).isEqualTo(1);
    }

    @Test
    void testProcessLogUrls() {
         logParser.processLogUrls(mockLogLine);
        Map<String, Integer> logUrls = logParser.logUrls;
        assertThat(logUrls.get("GET /index.html HTTP/1.1")).isEqualTo(1);
    }

    @Test
    public void testGetTopThree() {
       Map<String, Integer> mockIpAddress = buildIpAddressMap();

        List<String> topThree = logParser.getTopThree(mockIpAddress);

        assertThat(topThree).hasSize(EXPECTED_TOP_THREE_SIZE);
        assertThat( topThree.get(0)).isEqualTo("192.168.1.1, with 5 entries: ");
        assertThat( topThree.get(1)).isEqualTo("192.168.1.2, with 3 entries: ");
        assertThat( topThree.get(2)).isEqualTo("192.168.1.3, with 2 entries: ");
    }

    @Test
    void testParseLogEndToEnd() {
        logParser.logPath = Paths.get("src/test/resources/test-log.log");


        logParser.parseLog();

        Map<String, Integer> logIpAddress = logParser.logIpAddress;
        Map<String, Integer> logUrls = logParser.logUrls;
        List<String> topThree = logParser.getTopThree(logIpAddress);


        assertEquals(3, topThree.size());
        assertEquals("192.168.1.1, with 7 entries: ", topThree.get(0));
        assertEquals("198.51.100.0, with 5 entries: ", topThree.get(1));
        assertEquals("203.0.113.0, with 4 entries: ", topThree.get(2));

    }

    private Map<String, Integer> buildIpAddressMap(){
        Map<String, Integer> logIpAddress = new HashMap<String, Integer>();
        logIpAddress.put("192.168.1.1", 5);
        logIpAddress.put("192.168.1.2", 3);
        logIpAddress.put("192.168.1.3", 2);
        logIpAddress.put("192.168.1.4", 1);
        return logIpAddress;
    }


}
