package su.dru.ignite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import su.dru.ignite.dao.CellDao;
import su.dru.ignite.dao.CellToNumberRelationDao;
import su.dru.ignite.domain.Cell;
import su.dru.ignite.domain.CellToNumberRelation;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * @author Andrey Vorobyov
 */
@Service
public class CellToNumberService {
    @Autowired
    private CellDao cellDao;
    @Autowired
    private CellToNumberRelationDao cellToNumberRelationDao;

    public void add(CellToNumberRelation relation) {
        cellDao.add(new Cell(relation.getCellId()));
        cellToNumberRelationDao.add(relation);
    }

    public void addAll(Collection<CellToNumberRelation> relations) {
        List<Cell> cells = relations.stream()
                .map(relation -> new Cell(relation.getCellId()))
                .collect(toList());
        cellDao.addAll(cells);
        cellToNumberRelationDao.addAll(relations);
    }

    public List<CellToNumberRelation> numbers(long cellId) {
        return cellToNumberRelationDao.numbers(cellId);
    }
}
