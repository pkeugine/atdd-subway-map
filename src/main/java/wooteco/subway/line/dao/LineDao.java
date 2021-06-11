package wooteco.subway.line.dao;

import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.line.domain.Line;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;
    private final RowMapper<Line> rowMapper = (rs, rn) ->
            new Line(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("color")
            );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName("LINE")
                .usingGeneratedKeyColumns("id");
    }

    public Line insert(Line line) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(line);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Line(id, line.getName(), line.getColor());
    }

    public List<Line> findAll() {
        String sql = "SELECT * FROM LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<Line> findById(Long lineId) {
        String sql = "SELECT * FROM LINE WHERE id = ?";
        List<Line> result = jdbcTemplate.query(sql, rowMapper, lineId);
        return Optional.ofNullable(DataAccessUtils.singleResult(result));
    }

    public void update(Line line) {
        String sql = "UPDATE LINE SET name = ?, color = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                line.getName(),
                line.getColor(),
                line.getId());
    }

    public void delete(Long id) {
        String sql = "DELETE FROM LINE WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public boolean existsName(String name) {
        String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE name = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, name);
    }

    public boolean existsName2(String name) {
        String sql = "SELECT COUNT(*) FROM LINE WHERE name = ?";
        int count = jdbcTemplate.queryForObject(sql, Integer.class, name);
        return count > 0;
    }

    public boolean existsColor(String color) {
        String sql = "SELECT EXISTS (SELECT * FROM LINE WHERE color = ?)";
        return jdbcTemplate.queryForObject(sql, Boolean.class, color);
    }
}
