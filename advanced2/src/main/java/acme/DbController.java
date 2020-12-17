package acme;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DbController {
    private final Database database;
    private final Sorter sorter = new Sorter();
    public DbController(Database database) {
        this.database = database;
    }

    @GetMapping(value = "/db", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getKeys() {
        ArrayList<String> strings = new ArrayList<>(database.getKeys());
        sorter.sortStrings(strings);
        return strings;
    }
    @GetMapping(value = "/db/{key}", produces = MediaType.TEXT_PLAIN_VALUE)
    public String getEntry(@PathVariable String key) {
        return database.get(key);
    }

    @PutMapping(value = "/db/{key}", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> addEntry(@PathVariable String key, String value) {
        database.add(key, value);
        return ResponseEntity.created(URI.create("/db/" + key)).build();
    }

    @DeleteMapping(value = "/db/{key}")
    public ResponseEntity<Void> deleteEntry(@PathVariable String key) {
        database.delete(key);
        return ResponseEntity.noContent().build();
    }
}
