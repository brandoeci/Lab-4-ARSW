package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Repository
@Profile("postgres")
public class PostgresBlueprintPersistence implements BlueprintPersistence {

    private final JdbcTemplate jdbc;

    public PostgresBlueprintPersistence(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @Override
    @Transactional
    public void saveBlueprint(Blueprint bp) throws BlueprintPersistenceException {
        try {
            Long id = jdbc.queryForObject(
                    "INSERT INTO blueprints(author, name) VALUES (?, ?) RETURNING id",
                    Long.class, bp.getAuthor(), bp.getName());
            int seq = 0;
            for (Point p : bp.getPoints()) {
                jdbc.update("INSERT INTO points(blueprint_id, x, y, seq) VALUES (?,?,?,?)",
                        id, p.x(), p.y(), seq++);
            }
        } catch (DuplicateKeyException e) {
            throw new BlueprintPersistenceException(
                    "Blueprint already exists: " + bp.getAuthor() + ":" + bp.getName());
        } catch (DataAccessException e) {
            throw new BlueprintPersistenceException("Error saving blueprint: " + e.getMessage());
        }
    }

    @Override
    public Blueprint getBlueprint(String author, String name) throws BlueprintNotFoundException {
        Long id = findBlueprintId(author, name);
        return new Blueprint(author, name, pointsOf(id));
    }

    @Override
    public Set<Blueprint> getBlueprintsByAuthor(String author) throws BlueprintNotFoundException {
        List<Map<String, Object>> rows =
                jdbc.queryForList("SELECT id, name FROM blueprints WHERE author = ?", author);
        if (rows.isEmpty()) throw new BlueprintNotFoundException("No blueprints for author: " + author);

        Set<Blueprint> result = new HashSet<>();
        for (Map<String, Object> row : rows) {
            Long id = ((Number) row.get("id")).longValue();
            String name = (String) row.get("name");
            result.add(new Blueprint(author, name, pointsOf(id)));
        }
        return result;
    }

    @Override
    public Set<Blueprint> getAllBlueprints() {
        List<Map<String, Object>> rows = jdbc.queryForList("SELECT id, author, name FROM blueprints");
        Set<Blueprint> result = new HashSet<>();
        for (Map<String, Object> row : rows) {
            Long id = ((Number) row.get("id")).longValue();
            String author = (String) row.get("author");
            String name = (String) row.get("name");
            result.add(new Blueprint(author, name, pointsOf(id)));
        }
        return result;
    }

    @Override
    @Transactional
    public void addPoint(String author, String name, int x, int y) throws BlueprintNotFoundException {
        Long id = findBlueprintId(author, name);
        Integer maxSeq = jdbc.queryForObject(
                "SELECT COALESCE(MAX(seq), -1) FROM points WHERE blueprint_id = ?", Integer.class, id);
        jdbc.update("INSERT INTO points(blueprint_id, x, y, seq) VALUES (?,?,?,?)",
                id, x, y, maxSeq + 1);
    }

    private Long findBlueprintId(String author, String name) throws BlueprintNotFoundException {
        try {
            return jdbc.queryForObject(
                    "SELECT id FROM blueprints WHERE author = ? AND name = ?", Long.class, author, name);
        } catch (EmptyResultDataAccessException e) {
            throw new BlueprintNotFoundException("Blueprint not found: %s/%s".formatted(author, name));
        }
    }

    private List<Point> pointsOf(Long blueprintId) {
        return jdbc.query("SELECT x, y FROM points WHERE blueprint_id = ? ORDER BY seq",
                (rs, i) -> new Point(rs.getInt("x"), rs.getInt("y")), blueprintId);
    }
}