package com.purinda.repositories;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.purinda.models.HierarchyReference;

/**
 * This class provides repository functions for managing hierarchy references
 * in a Redis data store.
 */
public class HierarchyReferenceRepository {

    private static final String KEY_PATH = "JRHS:HierarchyReferences:";
    private Jedis jedis;

    /**
     * Constructs a new repository instance with a given Jedis connection.
     * 
     * @param jedis the Jedis connection to be used for Redis operations
     */
    public HierarchyReferenceRepository(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Stores a hierarchy reference in Redis.
     *
     * @param underageHierId The hierarchy reference ID
     * @param evClassId The event class ID
     * @param evTypeId The event type ID
     * @param status The status of the hierarchy reference
     */
    public void setHierarchyReference(int underageHierId, int evClassId, int evTypeId, String status) {
        String key = KEY_PATH + underageHierId;
        jedis.hset(key, "ev_class_id", String.valueOf(evClassId));
        jedis.hset(key, "ev_type_id", String.valueOf(evTypeId));
        jedis.hset(key, "status", status);
    }

    /**
     * Retrieves a hierarchy reference from Redis.
     *
     * @param underageHierId The hierarchy reference ID to retrieve
     * @return A map containing the fields of the hierarchy reference
     */
    public Map<String, String> getHierarchyReference(int underageHierId) {
        String key = KEY_PATH + underageHierId;
        return jedis.hgetAll(key);
    }

    /**
     * Retrieves all hierarchy references from Redis and return in form of list of HierarchyReference objects.
     * 
     * @return A list of HierarchyReference objects
     */
    public List<HierarchyReference> getAllHierarchyReferences() {
        List<HierarchyReference> list = new ArrayList<>();
        String cursor = "0";
        ScanParams scanParams = new ScanParams().match(KEY_PATH + "*").count(100);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            for (String key : keys) {
                Map<String, String> fields = jedis.hgetAll(key);
                HierarchyReference hr = new HierarchyReference(
                        Integer.parseInt(key.split(":")[2]),
                        Integer.parseInt(fields.get("ev_class_id")),
                        Integer.parseInt(fields.get("ev_type_id")),
                        fields.get("status"));
                list.add(hr);
            }
            cursor = scanResult.getCursor();
        } while (!cursor.equals("0"));
        return list;
    }

    /**
     * Deletes a hierarchy reference from Redis.
     *
     * @param underageHierId The ID of the hierarchy reference to delete
     */
    public void deleteHierarchyReference(int underageHierId) {
        String key = KEY_PATH + underageHierId;
        jedis.del(key);
    }
}