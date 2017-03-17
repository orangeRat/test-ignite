package su.dru.ignite.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.dru.ignite.domain.Cell;
import su.dru.ignite.domain.CellToNumberRelation;
import su.dru.ignite.service.CellService;
import su.dru.ignite.service.CellToNumberService;

import java.util.Collection;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Andrey Vorobyov
 */
@RestController
@RequestMapping("cell")
public class CellController {
    @Autowired
    private CellService cellService;
    @Autowired
    private CellToNumberService cellToNumberService;

    @RequestMapping(value = "{cellId}", method = GET)
    public Cell byId(@PathVariable long cellId) {
        Cell cell = cellService.byId(cellId);

        if (cell != null) {
            return cell;
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(value = "{cellId}/numbers", method = GET)
    public List<CellToNumberRelation> numbers(@PathVariable long cellId) {
        List<CellToNumberRelation> numbers = cellToNumberService.numbers(cellId);

        if (!numbers.isEmpty()) {
            return numbers;
        } else {
            throw new NotFoundException();
        }
    }

    @RequestMapping(path = "add-number", method = POST)
    public void addNumber(@RequestBody CellToNumberRelation relation) {
        cellToNumberService.add(relation);
    }

    @RequestMapping(path = "add-number-batch", method = POST)
    public void addManyNumbers(@RequestBody Collection<CellToNumberRelation> relations) {
        cellToNumberService.addAll(relations);
    }
}
