package service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import utils.MapUtils;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResultBuilderTest {
    @InjectMocks
    ResultBuilder resultBuilder;
    @Mock
    MapUtils mapUtils;

    public static final Path TEST_LOG_PATH = Path.of("src/test/resources/test-log.log");

    private static final String expectedResultString = "--------------------------------------------------\n" +
            "Parsing Logfile: test-log.log\n" +
            "--------------------------------------------------\n" +
            "Top 3 IP Addresses:\n" +
            "192.168.1.1, with 7 entries: \n" +
            "198.51.100.0, with 5 entries: \n" +
            "203.0.113.0, with 4 entries: \n" +
            "--------------------------------------------------\n" +
            "Unique IP Addresses: 3\n" +
            "--------------------------------------------------\n" +
            "Top 3 URLs:\n" +
            "GET /index.html HTTP/1.1, with 7 entries: \n" +
            "PUT /update_info HTTP/1.1, with 5 entries: \n" +
            "DELETE /remove_data HTTP/1.1, with 3 entries: \n" +
            "--------------------------------------------------\n" +
            "";

    private static final List<String> ipMapTopThreeResultList = List.of("192.168.1.1, with 7 entries: ", "198.51.100.0, with 5 entries: ", "203.0.113.0, with 4 entries: ");
    private static final List<String> urlMapTopThreeResultList = List.of("GET /index.html HTTP/1.1, with 7 entries: ", "PUT /update_info HTTP/1.1, with 5 entries: ", "DELETE /remove_data HTTP/1.1, with 3 entries: ");

    @Test
    void testResultBuilder() {
        Map<String, Integer> mockUrlMap = buildUrlMap();
        Map<String, Integer> mockIpAddressMap = buildIpAddressMap();
        List<Map<String, Integer>> processedLogResulMaptList = List.of(mockIpAddressMap, mockUrlMap);

        when(mapUtils.getTopThree(mockUrlMap)).thenReturn(urlMapTopThreeResultList);
        when(mapUtils.getTopThree(mockIpAddressMap)).thenReturn(ipMapTopThreeResultList);

        String resultString = resultBuilder.buildResultString(processedLogResulMaptList, TEST_LOG_PATH);

        assertThat(resultString).isEqualTo(expectedResultString);
    }

    private Map<String, Integer> buildIpAddressMap() {
        Map<String, Integer> logIpAddress = new HashMap<>();
        logIpAddress.put("192.168.1.1", 7);
        logIpAddress.put("198.51.100.0", 5);
        logIpAddress.put("203.0.113.0", 4);
        return logIpAddress;
    }

    private Map<String, Integer> buildUrlMap() {
        Map<String, Integer> logUrls = new HashMap<>();
        logUrls.put("DELETE /remove_data HTTP/1.1", 3);
        logUrls.put("GET /index.html HTTP/1.1", 7);
        logUrls.put("POST /form_submit HTTP/1.1", 1);
        logUrls.put("PUT /update_info HTTP/1.1", 5);
        return logUrls;
    }
}
