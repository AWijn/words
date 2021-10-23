package be.anouchka.exercise.adapter.inbound.rest;

import be.anouchka.exercise.port.inbound.WordHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.MediaType.*;

@Slf4j
@RestController
@RequestMapping("/words")
public class WordController {

    @Autowired
    private WordHandler wordHandler;

    @PostMapping(path = "/file", consumes = TEXT_PLAIN_VALUE)
    public ResponseEntity<?> postFile(
        @RequestBody
            String text) {

        log.info("Received file call");
        log.info(text);

        Set<String> generatedWords = wordHandler.fileOntvangen(text);

        return generatedWords.isEmpty() ?
            ResponseEntity.unprocessableEntity().build() :
            ResponseEntity.ok(generatedWords);

    }

    @GetMapping(path = "/savedTexts", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getSavedTexts() {
        log.info("Get all saved texts");

        List<Map<String, Object>> texts = wordHandler.getAllTexts();

        if (texts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.GONE).build();
        }
        return ResponseEntity.ok(texts);
    }

    @GetMapping(path = "/getWordsById/{id}")
    public ResponseEntity<?> getWordsById(
        @PathVariable("id")
            Integer id) {
        log.info("Get all words by id");

        Set<String> words = wordHandler.getWordsById(id);

        return words.isEmpty() ?
            ResponseEntity.status(HttpStatus.GONE).build() :
            ResponseEntity.ok(words);
    }

}
