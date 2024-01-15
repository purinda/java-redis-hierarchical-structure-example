import redis.clients.jedis.Jedis;

public class Client {

    private Jedis jedis;

    public Client(String host, int port) {
        this.jedis = new Jedis(host, port);
    }

    public void setHierarchyReference(int underageHierId, int evClassId, int evTypeId, String status) {
        String key = "UAGE:HierarchyReferences:" + underageHierId;
        jedis.hset(key, "ev_class_id", String.valueOf(evClassId));
        jedis.hset(key, "ev_type_id", String.valueOf(evTypeId));
        jedis.hset(key, "status", status);
    }

    public Map<String, String> getHierarchyReference(int underageHierId) {
        String key = "UAGE:HierarchyReferences:" + underageHierId;
        return jedis.hgetAll(key);
    }

    public void deleteHierarchyReference(int underageHierId) {
        String key = "UAGE:HierarchyReferences:" + underageHierId;
        jedis.del(key);
    }

    public void close() {
        jedis.close();
    }
}
