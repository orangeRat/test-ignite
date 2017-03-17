package su.dru.ignite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.dru.ignite.dao.CellDao;
import su.dru.ignite.domain.Cell;

/**
 * @author Andrey Vorobyov
 */
@Service
public class CellService {
    @Autowired
    private CellDao cellDao;

    public Cell byId(long cellId) {
        return cellDao.cellById(cellId);
    }

    public void add(Cell cell) {
        cellDao.add(cell);
    }
}
