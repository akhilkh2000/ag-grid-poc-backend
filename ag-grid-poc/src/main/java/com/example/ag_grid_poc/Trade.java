package com.example.ag_grid_poc;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.*;

public class Trade {
    public String id;
    public String symbol;
    public double price;
    public long timestamp;
    public Map<String, Object> nestedDetails;
    public List<Map<String, Object>> level1;
    public List<List<Map<String, Object>>> level2;

    public static Trade generateRandom() {
        Trade t = new Trade();
        t.id = UUID.randomUUID().toString();
        t.symbol = "SYM" + new Random().nextInt(1000);
        t.price = 100 + new Random().nextDouble() * 100;
        t.timestamp = System.currentTimeMillis();

        t.nestedDetails = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            t.nestedDetails.put("field" + i, UUID.randomUUID().toString());
        }

        t.level1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Map<String, Object> lvl2 = new HashMap<>();
            for (int j = 0; j < 10; j++) {
                lvl2.put("inner" + j, UUID.randomUUID().toString());
            }
            t.level1.add(lvl2);
        }

        t.level2 = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            List<Map<String, Object>> lvl2List = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                Map<String, Object> map = new HashMap<>();
                for (int k = 0; k < 5; k++) {
                    map.put("deep" + k, UUID.randomUUID().toString());
                }
                lvl2List.add(map);
            }
            t.level2.add(lvl2List);
        }

        return t;
    }
}