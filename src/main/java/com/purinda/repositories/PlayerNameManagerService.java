package com.purinda.repositories;

import java.util.ArrayList;
import java.util.List;

import com.purinda.models.PlayerName;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.ScanParams;
import redis.clients.jedis.resps.ScanResult;

public class PlayerNameManagerService {
    private static final String PREFIX = "JRHS:PlayerNames:";
    private Jedis jedis;

    public PlayerNameManagerService(Jedis jedis) {
        this.jedis = jedis;
    }

    public void setPlayerName(int playerId, String playerName) {
        String key = PREFIX + playerId;
        jedis.set(key, playerName);
    }

    public String getPlayerName(int playerId) {
        String key = PREFIX + playerId;
        return jedis.get(key);
    }

    public List<PlayerName> getAllPlayerNames() {
        List<PlayerName> list = new ArrayList<>();
        String cursor = "0";
        ScanParams scanParams = new ScanParams().match(PREFIX + "PlayerNames:*").count(100);
        do {
            ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
            List<String> keys = scanResult.getResult();
            for (String key : keys) {
                String playerName = jedis.hget(key, "player_name");
                PlayerName pn = new PlayerName(
                        Integer.parseInt(key.split(":")[2]),
                        playerName);
                list.add(pn);
            }
            cursor = scanResult.getCursor();
        } while (!cursor.equals("0"));
        return list;
    }

    public void deletePlayerName(int playerId) {
        String key = PREFIX + playerId;
        jedis.del(key);
    }

}
