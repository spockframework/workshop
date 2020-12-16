package acme;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Database {
    public static final Database INSTANCE = new Database();

    private final Map<String, String> data = new HashMap<>();

    private Database() {

    }

    public Set<String> getKeys() {
        return data.keySet();
    }

    public void add(String key, String value) {
        Worker.doWork();
        data.put(key, value);
    }

    public String get(String key) {
        Worker.doWork();
        return data.get(key);
    }

    public String delete(String key) {
        Worker.doWork();
        return data.remove(key);
    }

    public long size() {
        Worker.doWork();
        return data.keySet().stream().count();
    }

    public void nuke() {
        data.clear();
    }
}
