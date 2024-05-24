package utils;

import java.util.List;
import java.util.Map;



public class MapUtils {

    /**
     * Gets the top three most frequent entries in a map.
     * The entries are sorted in descending order of their values.
     * Each entry is formatted as "{key}, with {value} entries: ".
     *
     * @param map The map to get the top three entries from. The map's keys are the data items, and the values are their occurrence counts.
     * @return A list of the top three entries in the map. Each entry is a string formatted as "{key}, with {value} entries: ".
     */
    public List<String> getTopThree(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(3)
                .map(entry -> entry.getKey() + ", with " + entry.getValue() + " entries: ")
                .toList();
    }
}
