package be.anouchka.exercise.adapter.outbound.jdbc.postgres;

import be.anouchka.exercise.port.outbound.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static be.anouchka.exercise.core.Anomalies.successResult;
import static be.anouchka.exercise.core.Anomalies.unavailableResult;
import static be.anouchka.exercise.util.MapOperations.mapWith;

@Repository
@Slf4j
public class PostgresFileRepository implements FileRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String INSERT_FILE = "INSERT INTO textfile(words)" + "VALUES(:words)";

    private static final String TRUNCATE = "TRUNCATE TABLE textfile";

    private static final String SELECT_ALL = "SELECT * FROM textfile";

    private static final String SELECT_ONE = "SELECT words FROM textfile where id = :id";

    @Override
    public Map<String, Object> create(final String text) {
        log.info("Insert text met sql: {} \n args: {}", INSERT_FILE, text);
        try {
            jdbcTemplate.update(INSERT_FILE, mapWith("words", text));
            return successResult();
        } catch (Exception ex) {
            log.error("Could not insert file due to exception", ex);
            return unavailableResult();
        }
    }

    @Override
    public void truncate() {
        log.info("Truncate table textfile");

        jdbcTemplate.getJdbcOperations().execute(TRUNCATE);
    }

    @Override
    public List<Map<String, Object>> getAll() {

        try {
            return jdbcTemplate.getJdbcOperations().queryForList(SELECT_ALL);
        } catch (Exception ex) {
            log.error("Could not collect saved textfiles due to exception", ex);
            return Collections.emptyList();
        }

    }

    @Override
    public String getById(Integer id){
        try {
            return jdbcTemplate.queryForObject(SELECT_ONE,mapWith("id", id), String.class);
        } catch (Exception ex) {
            log.error("Could not collect saved textfiles due to exception", ex);
            return null;
        }
    }
}
