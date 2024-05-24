package utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class MapUtilsTest {

    @InjectMocks
    private MapUtils mapUtils;
    private static final Integer EXPECTED_TOP_THREE_SIZE = 3;

    @Test
    void testGetTopThreeForIpMap() {
        Map<String, Integer> mockIpAddressMap = buildIpAddressMap();

        List<String> topThree = mapUtils.getTopThree(mockIpAddressMap);

        assertThat(topThree).hasSize(EXPECTED_TOP_THREE_SIZE);
        assertThat(topThree.get(0)).isEqualTo("192.168.1.1, with 7 entries: ");
        assertThat(topThree.get(1)).isEqualTo("198.51.100.0, with 5 entries: ");
        assertThat(topThree.get(2)).isEqualTo("203.0.113.0, with 4 entries: ");
    }

    @Test
    void testGetTopThreeForUrlMap() {
        Map<String, Integer> mockUrlMap = buildUrlMap();

        List<String> topThree = mapUtils.getTopThree(mockUrlMap);

        assertThat(topThree).hasSize(EXPECTED_TOP_THREE_SIZE);
        assertThat(topThree.get(0)).isEqualTo("GET /index.html HTTP/1.1, with 7 entries: ");
        assertThat(topThree.get(1)).isEqualTo("PUT /update_info HTTP/1.1, with 5 entries: ");
        assertThat(topThree.get(2)).isEqualTo("DELETE /remove_data HTTP/1.1, with 3 entries: ");
    }

    private Map<String, Integer> buildIpAddressMap() {
        Map<String, Integer> logIpAddress = new HashMap<>();
        logIpAddress.put("203.0.113.0", 4);
        logIpAddress.put("192.168.1.1", 7);
        logIpAddress.put("198.51.100.0", 5);
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
