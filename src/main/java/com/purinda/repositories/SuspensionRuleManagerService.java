package com.purinda.repositories;

import java.util.ArrayList;
import java.util.List;

import com.purinda.models.SuspensionRule;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

public class SuspensionRuleManagerService {
    private static final String PREFIX = "JRHS:SuspensionRules:";
    private Jedis jedis;

    public SuspensionRuleManagerService(Jedis jedis) {
        this.jedis = jedis;
    }

    public void setSuspensionRule(int ruleId, String ruleData) {
        String key = PREFIX + ruleId;
        jedis.set(key, ruleData);
    }

    public String getSuspensionRule(int ruleId) {
        String key = PREFIX + ruleId;
        return jedis.get(key);
    }


    public List<SuspensionRule> getAllSuspensionRules() {
        List<SuspensionRule> list = new ArrayList<>();
        String cursor = "0";
        ScanParams scanParams = new ScanParams().match(PREFIX + "SuspensionRules:*").count(100);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            for (String key : keys) {
                String rule = jedis.hget(key, "rule");
                SuspensionRule sr = new SuspensionRule(
                        Integer.parseInt(key.split(":")[2]),
                        rule);
                list.add(sr);
            }
            cursor = scanResult.getCursor();
        } while (!cursor.equals("0"));
        return list;
    }

    public void deleteSuspensionRule(int ruleId) {
        String key = PREFIX + ruleId;
        jedis.del(key);
    }
}