package su.dru.ignite.domain;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * @author Andrey Vorobyov
 */
public class Cell {
    public static final String CACHE_NAME = "cell";

    @QuerySqlField(index = true)
    private long cellId;

    public Cell() {
    }

    public Cell(Long cellId) {
        this.cellId = cellId;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "cellId=" + cellId +
                '}';
    }
}
