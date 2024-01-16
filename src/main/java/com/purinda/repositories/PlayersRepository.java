package com.purinda.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.purinda.models.PlayerName;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

public class PlayersRepository {
    private static final String PREFIX = "JRHS:PlayerNames:";
    private Jedis jedis;

    public PlayersRepository(Jedis jedis) {
        this.jedis = jedis;
    }

    public void addPlayerName(int hierarchyReferenceID, String playerName) {
        String key = PREFIX + hierarchyReferenceID;
        jedis.sadd(key, playerName);
    }

    public List<PlayerName> getPlayerNames(int hierarchyReferenceID) {
        String key = PREFIX + hierarchyReferenceID;
        Set<String> names = jedis.smembers(key);
        
        List<PlayerName> playerNames = new ArrayList<>();
        for (String name : names) {
            playerNames.add(new PlayerName(hierarchyReferenceID, name));
        }
    
        return playerNames;
    }

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

    public void deletePlayerName(int hierarchyReferenceID) {
        String key = PREFIX + hierarchyReferenceID;
        jedis.del(key);
    }
}
