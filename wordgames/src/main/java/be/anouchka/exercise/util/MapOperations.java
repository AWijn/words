package be.anouchka.exercise.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

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

}
