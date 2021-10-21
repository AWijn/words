package be.anouchka.exercise.adapter.outbound.postgres;

import be.anouchka.exercise.port.outbound.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static be.anouchka.exercise.core.Anomalies.successResult;

@Primary
@Repository
@Slf4j
public class PostgresMockFileRepository implements FileRepository {

    public PostgresMockFileRepository() {
    }

    @Override
    public Map<String, Object> create(final String text) {

            return successResult();
    }

    @Override
    public void truncate() {
    }

    @Override
    public List<Map<String, Object>> getAll() {

            return Collections.emptyList();

    }

    @Override
    public String getById(Integer id){

        return null;
    }
}
