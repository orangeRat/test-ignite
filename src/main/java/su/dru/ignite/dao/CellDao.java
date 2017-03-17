package su.dru.ignite.dao;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import su.dru.ignite.domain.Cell;

import java.util.List;

/**
 * @author Andrey Vorobyov
 */
@Repository
public class CellDao {
    @Autowired
    @Qualifier(Cell.CACHE_NAME)
    private final IgniteCache<Long, Cell> cache;

    @Autowired
    private final Ignite ignite;

    public CellDao(IgniteCache<Long, Cell> cache, Ignite ignite) {
        this.cache = cache;
        this.ignite = ignite;
    }

    public Cell cellById(long cellId) {
        return cache.get(cellId);
    }

    public void add(Cell cell) {
        cache.put(cell.getCellId(), cell);
    }

    public void addAll(List<Cell> cells) {
        try (IgniteDataStreamer<Long, Cell> streamer = ignite.dataStreamer(Cell.CACHE_NAME)) {
            cells.forEach(cell -> streamer.addData(cell.getCellId(), cell));
        }
    }
}
