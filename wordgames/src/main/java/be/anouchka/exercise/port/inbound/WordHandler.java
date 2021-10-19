package be.anouchka.exercise.port.inbound;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface WordHandler {

    Set<String> getWords(String text);

    Set<String> fileOntvangen(String file);

    List<Map<String, Object>> getAllTexts();

   Set<String> getWordsById(Integer id);

}
