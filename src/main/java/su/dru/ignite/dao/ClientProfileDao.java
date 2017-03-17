package su.dru.ignite.dao;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.IgniteDataStreamer;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cache.query.SqlQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import su.dru.ignite.domain.CellToNumberRelation;
import su.dru.ignite.domain.ClientProfile;

import javax.cache.Cache;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Andrey Vorobyov
 */
@Repository
public class ClientProfileDao {
    @Autowired
    @Qualifier(ClientProfile.CACHE_NAME)
    private final IgniteCache<Long, ClientProfile> cache;

    @Autowired
    private final Ignite ignite;

    public ClientProfileDao(IgniteCache<Long, ClientProfile> cache, Ignite ignite) {
        this.cache = cache;
        this.ignite = ignite;
    }

    public ClientProfile byCtn(long ctn) {
        return cache.get(ctn);
    }

    public void add(ClientProfile clientProfile) {
        cache.put(clientProfile.getCtn(), clientProfile);
    }

    public List<ClientProfile> byCellId(long cellId) {
        String sql = "select cp.* from ClientProfile as cp, \"" + CellToNumberRelation.CACHE_NAME + "\".CellToNumberRelation as cpn " +
                " where cp.ctn = cpn.ctn and cpn.cellId = ?";
        QueryCursor<Cache.Entry<Long, ClientProfile>> cursor = cache.query(new SqlQuery<Long, ClientProfile>(ClientProfile.class, sql).setArgs(cellId));

        return cursor.getAll()
                .stream()
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());
    }

    public void addAll(List<ClientProfile> profiles) {
        try (IgniteDataStreamer<Long, ClientProfile> streamer = ignite.dataStreamer(ClientProfile.CACHE_NAME)) {
            profiles.forEach(profile -> streamer.addData(profile.getCtn(), profile));
        }
    }
}
