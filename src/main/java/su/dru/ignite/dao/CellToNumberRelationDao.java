package su.dru.ignite.dao;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.query.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import su.dru.ignite.domain.CellToNumberRelation;

import javax.cache.Cache;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrey Vorobyov
 */
@Repository
public class CellToNumberRelationDao {
    @Autowired
    @Qualifier(CellToNumberRelation.CACHE_NAME)
    private final IgniteCache<Long, CellToNumberRelation> cache;
    @Autowired
    private final Ignite ignite;

    public CellToNumberRelationDao(IgniteCache<Long, CellToNumberRelation> cache, Ignite ignite) {
        this.cache = cache;
        this.ignite = ignite;
    }

    public void add(CellToNumberRelation cellToNumberRelation) {
        cache.put(cellToNumberRelation.getCtn(), cellToNumberRelation);
    }

    public void addAll(Collection<CellToNumberRelation> relations) {
        try (IgniteDataStreamer<Long, CellToNumberRelation> streamer = ignite.dataStreamer(CellToNumberRelation.CACHE_NAME)) {
            relations.forEach(relation -> streamer.addData(relation.getCtn(), relation));
        }
    }

    public List<CellToNumberRelation> numbers(long cellId) {
        SqlQuery<Long, CellToNumberRelation> query = new SqlQuery<>(CellToNumberRelation.class, "cellId = ?");
        return cache.query(query.setArgs(cellId))
                .getAll()
                .stream()
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());
    }
}
