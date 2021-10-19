package be.anouchka.exercise.app.cleanup;

import be.anouchka.exercise.adapter.outbound.jdbc.postgres.PostgresFileRepository;
import be.anouchka.exercise.port.inbound.CleanUpHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CleanUpHandlerImpl implements CleanUpHandler {

    @Autowired
    private PostgresFileRepository fileRepository;


    @Override
    public void cleanUp() {

        fileRepository.truncate();

    }

}
