package com.example.podlibrary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Buzz on 04/01/2017.
 */

public class Configuration {
    private static Map<String, String> myMap;
    static
    {
        myMap = new HashMap<String, String>();
        myMap.put("url", "http");
    }
}