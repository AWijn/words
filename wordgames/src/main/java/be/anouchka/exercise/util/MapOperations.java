package be.anouchka.exercise.util;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import static java.util.Arrays.asList;

public class MapOperations {

    private MapOperations(){}


    public static Map<String, Object> mapWith(Object... keyVals)
    {
        Map<String, Object> map = new HashMap<>();
        ListIterator<Object> iterator = asList(keyVals).listIterator();
        while(iterator.hasNext())
        {
            String key = (String) iterator.next();
            Object value = iterator.next();
            map.put(key, value);
        }
        return map;
    }

    public static <T> T get(Map<String, Object> map, String key)
    {
        return (T) map.get(key);
    }

}
