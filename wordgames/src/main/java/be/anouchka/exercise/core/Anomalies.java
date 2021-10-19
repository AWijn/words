package be.anouchka.exercise.core;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Collections.singletonMap;

public class Anomalies {

    private static final String SUCCESS = "success";

    private static final String ANOMALY = "anomaly";

    private static final String VALUE = "value";

    private static final String UNAVAILABLE = "unavailable";


    public static Map<String, Object> successResult()
    {
        return singletonMap(SUCCESS, TRUE);
    }

    public static Map<String, Object> successResult(Object val)
    {
        Map<String, Object> result = new HashMap<>();
        result.put(SUCCESS, TRUE);
        result.put(VALUE, val);
        return result;
    }

    public static Map<String, Object> unavailableResult()
    {
        Map<String, Object> result = new HashMap<>();
        result.put(SUCCESS, FALSE);
        result.put(ANOMALY, UNAVAILABLE);
        return result;
    }

    public static boolean isSuccessResult(Map<String, Object> result) {
        return Boolean.TRUE == result.get(SUCCESS);
    }
}
