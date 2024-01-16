package com.purinda.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.purinda.models.PlayerName;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

/**
 * Repository class for managing player names associated with hierarchy references
 * in a Redis data store.
 */
public class PlayersRepository {
    private static final String PREFIX = "JRHS:PlayerNames:";
    private Jedis jedis;

    /**
     * Constructs a new PlayersRepository instance with a given Jedis connection.
     *
     * @param jedis the Jedis connection to be used for Redis operations
     */
    public PlayersRepository(Jedis jedis) {
        this.jedis = jedis;
    }

    /**
     * Adds a player name to the set of names associated with a specific hierarchy reference ID.
     *
     * @param hierarchyReferenceID The ID of the hierarchy reference
     * @param playerName The player's name to add
     */
    public void addPlayerName(int hierarchyReferenceID, String playerName) {
        String key = PREFIX + hierarchyReferenceID;
        jedis.sadd(key, playerName);
    }

    /**
     * Retrieves all player names associated with a specific hierarchy reference ID.
     * 
     * @param hierarchyReferenceID The ID of the hierarchy reference
     * @return A list of PlayerName objects
     */
    public List<PlayerName> getPlayerNames(int hierarchyReferenceID) {
        String key = PREFIX + hierarchyReferenceID;
        Set<String> names = jedis.smembers(key);
        
        List<PlayerName> playerNames = new ArrayList<>();
        for (String name : names) {
            playerNames.add(new PlayerName(hierarchyReferenceID, name));
        }
    
        return playerNames;
    }

    /**
     * Retrieves all player names associated with all hierarchy references.
     *
     * @return A list of PlayerName objects
     */
    public List<PlayerName> getAllPlayerNames() {
        List<PlayerName> list = new ArrayList<>();
        String cursor = "0";
        ScanParams scanParams = new ScanParams().match(PREFIX + "*").count(100);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            for (String key : keys) {
                Set<String> playerNames = jedis.smembers(key);
                int hierarchyReferenceID = Integer.parseInt(key.split(":")[2]);
                for (String playerName : playerNames) {
                    list.add(new PlayerName(hierarchyReferenceID, playerName));
                }
            }
            cursor = scanResult.getCursor();
        } while (!cursor.equals("0"));
        return list;
    }

    /**
     * Deletes a player name from the set of names associated with a specific hierarchy reference ID.
     *
     * @param hierarchyReferenceID The ID of the hierarchy reference
     */
    public void deletePlayerName(int hierarchyReferenceID) {
        String key = PREFIX + hierarchyReferenceID;
        jedis.del(key);
    }
}
