package be.anouchka.exercise.adapter.inbound.rest;

import be.anouchka.exercise.port.inbound.CleanUpHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/cleanup")
public class CleanUpController {

    @Autowired
    private CleanUpHandler cleanUpHandler;

    @PostMapping
    public ResponseEntity<Void> cleanup() {
        log.info("received cleanup call");

        cleanUpHandler.cleanUp();

        return ResponseEntity.ok().build();
    }
}
