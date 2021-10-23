package be.anouchka.exercise.adapter.outbound.postgres;

import be.anouchka.exercise.adapter.outbound.jdbc.postgres.PostgresFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Primary
@Repository
@Slf4j
public class PostgresTestFileRepository extends PostgresFileRepository {

    //test adaptations can be added here if necessary
}
