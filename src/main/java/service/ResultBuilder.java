package service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import utils.MapUtils;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
public class ResultBuilder {
    private final MapUtils mapUtils;

    public static final String SEPARATOR = "--------------------------------------------------";

    /**
     * Builds a formatted result string from the processed log data.
     * The result string includes the top three IP addresses and URLs, and the total number of unique IP addresses.
     *
     * @param logResults A list of maps containing the processed log data. The first map contains IP addresses and their occurrence count, and the second map contains URLs and their occurrence count.
     * @param logPath The path of the log file that was parsed.
     * @return A formatted result string.
     */
    public String buildResultString(List<Map<String, Integer>> logResults, Path logPath) {
        Map<String, Integer> logIpAddress = logResults.get(0);
        Map<String, Integer> logUrls = logResults.get(1);

        return String.format(
                "%s%nParsing Logfile: %s%n%s%nTop 3 IP Addresses:%n%s%n%s%nUnique IP Addresses: %d%n%s%nTop 3 URLs:%n%s%n%s%n",
                SEPARATOR,
                logPath.getFileName(),
                SEPARATOR,
                formatTopThree(logIpAddress),
                SEPARATOR,
                logIpAddress.values().size(),
                SEPARATOR,
                formatTopThree(logUrls),
                SEPARATOR
        );
    }

    private String formatTopThree(Map<String, Integer> map) {
        return mapUtils.getTopThree(map).stream()
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
    }
}
