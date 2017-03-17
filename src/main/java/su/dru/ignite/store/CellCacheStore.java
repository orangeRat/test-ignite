package su.dru.ignite.store;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import su.dru.ignite.domain.Cell;

import javax.cache.Cache;
import javax.cache.integration.CacheLoaderException;
import javax.cache.integration.CacheWriterException;
import java.sql.*;

/**
 * @author Andrey Vorobyov
 */
public class CellCacheStore extends CacheStoreAdapter<Long, Cell> {
    @Override public Cell load(Long key) {
        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("select * from Cell where cellId=?")) {
                st.setLong(1, key);

                ResultSet rs = st.executeQuery();

                return rs.next() ? new Cell(rs.getLong(1)) : null;
            }
        } catch (SQLException e) {
            throw new CacheLoaderException("Failed to load: " + key, e);
        }
    }

    @Override
    public void write(Cache.Entry<? extends Long, ? extends Cell> entry) throws CacheWriterException {
        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement(
                    "merge into Cell (cellId) key (cellId) VALUES (?)")) {
                    Cell val = entry.getValue();

                    st.setLong(1, val.getCellId());

                    st.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to write Cell [cellId=" + entry.getKey() + "]", e);
        }
    }

    @Override
    public void delete(Object key) {
        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("delete from CellS where id=?")) {
                st.setLong(1, (Long)key);

                st.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new CacheWriterException("Failed to delete: " + key, e);
        }
    }

    @Override
    public void loadCache(IgniteBiInClosure<Long, Cell> clo, Object... args) {
        if (args == null || args.length == 0 || args[0] == null)
            throw new CacheLoaderException("Expected entry count parameter is not provided.");

        final int entryCnt = (Integer)args[0];

        try (Connection conn = connection()) {
            try (PreparedStatement st = conn.prepareStatement("select * from CellS")) {
                try (ResultSet rs = st.executeQuery()) {
                    int cnt = 0;

                    while (cnt < entryCnt && rs.next()) {
                        Cell Cell = new Cell(rs.getLong(1));

                        clo.apply(Cell.getCellId(), Cell);

                        cnt++;
                    }
                }
            }
        }
        catch (SQLException e) {
            throw new CacheLoaderException("Failed to load values from cache store.", e);
        }
    }

    private Connection connection() throws SQLException  {
        Connection conn = DriverManager.getConnection("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1");

        conn.setAutoCommit(true);

        return conn;
    }
}
