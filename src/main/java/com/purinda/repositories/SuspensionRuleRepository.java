package com.purinda.repositories;

import java.util.ArrayList;
import java.util.List;

import com.purinda.definitions.Rule.RuleType;
import com.purinda.models.SuspensionRule;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

/**
 * Repository class for managing suspension rules in a Redis data store.
 */
public class SuspensionRuleRepository {
    private static final String PREFIX = "JRHS:SuspensionRules:";
    private Jedis jedis;

    /**
     * Constructs a new SuspensionRuleRepository instance with a given Jedis connection.
     *
     * @param jedis the Jedis connection to be used for Redis operations
     */
    public SuspensionRuleRepository(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Stores a suspension rule in Redis.
     *
     * @param ruleId The ID of the suspension rule
     * @param ruleData The data of the suspension rule
     */
    public void setSuspensionRule(int ruleId, RuleType ruleData) {
        String key = PREFIX + ruleId;
        jedis.set(key, ruleData.toString());
    }

    /** 
     * Retrieves a suspension rule from Redis.
     * 
     * @param ruleId The ID of the suspension rule
     * @return The data of the suspension rule
     */
    public String getSuspensionRule(int ruleId) {
        String key = PREFIX + ruleId;
        return jedis.get(key);
    }

    /**
     * Retrieves all suspension rules from Redis.
     *
     * @return A list of SuspensionRule objects
     */
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

    /**
     * Deletes a suspension rule from Redis.
     *
     * @param ruleId The ID of the suspension rule
     */
    public void deleteSuspensionRule(int ruleId) {
        String key = PREFIX + ruleId;
        jedis.del(key);
    }
}
