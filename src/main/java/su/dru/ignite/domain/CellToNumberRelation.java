package su.dru.ignite.domain;

import org.apache.ignite.cache.query.annotations.QuerySqlField;

/**
 * @author Andrey Vorobyov
 */
public class CellToNumberRelation {
    public static final String CACHE_NAME = "cellToNumber";

    @QuerySqlField(index = true)
    private long ctn;
    @QuerySqlField(index = true)
    private long cellId;

    public CellToNumberRelation() {
    }

    public CellToNumberRelation(long ctn, long cellId) {
        this.ctn = ctn;
        this.cellId = cellId;
    }

    public long getCtn() {
        return ctn;
    }

    public void setCtn(long ctn) {
        this.ctn = ctn;
    }

    public long getCellId() {
        return cellId;
    }

    public void setCellId(long cellId) {
        this.cellId = cellId;
    }
}
