package com.purinda.repositories;

import java.util.ArrayList;
import java.util.List;

import com.purinda.definitions.Rule.RuleType;
import com.purinda.models.SuspensionRule;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

public class SuspensionRuleRepository {
    private static final String PREFIX = "JRHS:SuspensionRules:";
    private Jedis jedis;

    public SuspensionRuleRepository(Jedis jedis) {
        this.jedis = jedis;
    }

    public void setSuspensionRule(int ruleId, RuleType ruleData) {
        String key = PREFIX + ruleId;
        jedis.set(key, ruleData.toString());
    }

    public String getSuspensionRule(int ruleId) {
        String key = PREFIX + ruleId;
        return jedis.get(key);
    }

    public List<SuspensionRule> getAllSuspensionRules() {
        List<SuspensionRule> list = new ArrayList<>();
        String cursor = "0";
        ScanParams scanParams = new ScanParams().match(PREFIX + "*").count(100);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            for (String key : keys) {
                String ruleData = jedis.get(key);
                int ruleId = Integer.parseInt(key.split(":")[2]);
                RuleType ruleType = RuleType.valueOf(ruleData);
                list.add(new SuspensionRule(ruleId, ruleType));
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
