package be.anouchka.exercise.app.word;

import be.anouchka.exercise.adapter.outbound.jdbc.postgres.PostgresFileRepository;
import be.anouchka.exercise.core.Anomalies;
import be.anouchka.exercise.port.inbound.WordHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class WordHandlerImpl implements WordHandler {

    @Autowired
    private PostgresFileRepository fileRepository;

    @Override
    public Set<String> getWords(final String text) {

        List<String> listOfWords = text.lines().collect(Collectors.toList());
        Set<String> words = new HashSet<>();

        collectWords(words, listOfWords);

        return words;
    }


    @Override
    public Set<String> fileOntvangen(final String text) {

        log.info(text);

        Map<String, Object> result = fileRepository.create(text);

        return Anomalies.isSuccessResult(result) ? getWords(text) : Collections.emptySet();



    }

    @Override
    public List<Map<String, Object>> getAllTexts(){
        return fileRepository.getAll();
    }

    @Override
    public Set<String> getWordsById(Integer id){


        String words = fileRepository.getById(id);

        return StringUtils.isBlank(words) ? Collections.emptySet() : getWords(words);

    }

    public void collectWords(Set<String> words, List<String> data) {

        Map<String, Object> charSets = new HashMap();
        Set<String> onechar = new HashSet<>();
        Set<String> twochar = new HashSet<>();
        Set<String> threechar = new HashSet<>();
        Set<String> fourchar = new HashSet<>();
        Set<String> fivechar = new HashSet<>();
        Set<String> sixchar = new HashSet<>();

        data.forEach(w -> {
            switch (w.length()) {
            case 1:
                onechar.add(w);
                break;
            case 2:
                twochar.add(w);
                break;
            case 3:
                threechar.add(w);
                break;
            case 4:
                fourchar.add(w);
                break;
            case 5:
                fivechar.add(w);
                break;
            case 6:
                sixchar.add(w);
                break;
            }
        });

        charSets.put("onechar", onechar);
        charSets.put("twochar", twochar);
        charSets.put("threechar", threechar);
        charSets.put("fourchar", fourchar);
        charSets.put("fivechar", fivechar);
        charSets.put("sixchar", sixchar);

        addFiveCharWords(words, charSets);
        addFourCharWords(words, charSets);
        addThreeCharWords(words, charSets);
        addTwoCharWords(words, charSets);
        addOneCharWords(words, charSets);

    }

    public void addFiveCharWords(Set<String> words, Map<String, Object> charSets) {

        Set<String> sixchar = (Set) charSets.get("sixchar");
        Set<String> fivechar = (Set) charSets.get("fivechar");
        Set<String> onechar = (Set) charSets.get("onechar");

        sixchar.forEach(full -> {
            List<String> fiveCharWordsThatAreInSixCharList = fivechar.stream()
                .filter(five -> full.substring(0, 5).contentEquals(five))
                .collect(Collectors.toList());
            fiveCharWordsThatAreInSixCharList.forEach(five -> onechar.forEach(one -> {
                if (full.contentEquals(five + one)) {
                    words.add(getWordBuildup(full, five, one));
                }
            }));
        });
    }

    public void addFourCharWords(Set<String> words, Map<String, Object> charSets) {

        Set<String> sixchar = (Set) charSets.get("sixchar");
        Set<String> fourchar = (Set) charSets.get("fourchar");
        Set<String> twochar = (Set) charSets.get("twochar");
        Set<String> onechar = (Set) charSets.get("onechar");

        sixchar.forEach(full -> {

            if (fourchar.contains(full.substring(0, 4)) && onechar.contains(String.valueOf(full.charAt(4))) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.substring(0, 4), full.charAt(4),full.charAt(5)));
            }

            if (fourchar.contains(full.substring(0, 4)) && twochar.contains(full.substring(4, 6))) {
                words.add(getWordBuildup(full, full.substring(0, 4), full.substring(4, 6)));
            }

        });
    }

    public void addThreeCharWords(Set<String> words, Map<String, Object> charSets) {

        Set<String> sixchar = (Set) charSets.get("sixchar");
        Set<String> threechar = (Set) charSets.get("threechar");
        Set<String> twochar = (Set) charSets.get("twochar");
        Set<String> onechar = (Set) charSets.get("onechar");

        sixchar.forEach(full -> {

            if (threechar.contains(full.substring(0, 3)) && threechar.contains(full.substring(3, 6))) {
                words.add(getWordBuildup(full, full.substring(0, 3), full.substring(3, 6)));
            }

            if (threechar.contains(full.substring(0, 3)) && onechar.contains(String.valueOf(full.charAt(3))) &&
                onechar.contains(String.valueOf(full.charAt(4))) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.substring(0, 3), full.charAt(3), full.charAt(4), full.charAt(5)));
            }

            if (threechar.contains(full.substring(0, 3)) && twochar.contains(full.substring(3, 5)) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.substring(0, 3), full.substring(3, 5), full.charAt(5)));
            }

            if (threechar.contains(full.substring(0, 3)) && onechar.contains(String.valueOf(full.charAt(3))) && twochar.contains(full.substring(4, 6))) {
                words.add(getWordBuildup(full, full.substring(0, 3), full.charAt(3), full.substring(4, 6)));
            }

        });
    }

    public void addTwoCharWords(Set<String> words, Map<String, Object> charSets) {

        Set<String> sixchar = (Set) charSets.get("sixchar");
        Set<String> fourchar = (Set) charSets.get("fourchar");
        Set<String> threechar = (Set) charSets.get("threechar");
        Set<String> twochar = (Set) charSets.get("twochar");
        Set<String> onechar = (Set) charSets.get("onechar");

        sixchar.forEach(full -> {

            if (twochar.contains(full.substring(0, 2)) && fourchar.contains(full.substring(2, 6))) {
                words.add(getWordBuildup(full, full.substring(0, 2), full.substring(2, 6)));
            }

            if (twochar.contains(full.substring(0, 2)) && onechar.contains(String.valueOf(full.charAt(2))) && threechar.contains(full.substring(3, 6))) {
                words.add(getWordBuildup(full, full.substring(0, 2), full.charAt(2), full.substring(3, 6)));
            }

            if (twochar.contains(full.substring(0, 2)) && threechar.contains(full.substring(2, 5)) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.substring(0, 2), full.substring(2, 5), full.charAt(5)));
            }

            if (twochar.contains(full.substring(0, 2)) && onechar.contains(String.valueOf(full.charAt(2))) &&
                onechar.contains(String.valueOf(full.charAt(3))) && twochar.contains(full.substring(4, 6))) {
                words.add(getWordBuildup(full, full.substring(0, 2), full.charAt(2), full.charAt(3), full.substring(4, 6)));
            }

            if (twochar.contains(full.substring(0, 2)) && onechar.contains(String.valueOf(full.charAt(2))) &&
                twochar.contains(full.substring(3, 5)) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.substring(0, 2), full.charAt(2), full.substring(3, 5), full.charAt(5)));
            }

            if (twochar.contains(full.substring(0, 2)) && twochar.contains(full.substring(2, 4)) &&
                onechar.contains(String.valueOf(full.charAt(4))) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.substring(0, 2), full.substring(2, 4), full.charAt(4), full.charAt(5)));
            }

            if (twochar.contains(full.substring(0, 2)) && twochar.contains(full.substring(2, 4)) && twochar.contains(full.substring(4, 6))) {
                words.add(getWordBuildup(full, full.substring(0, 2), full.substring(2, 4), full.substring(4, 6)));
            }

        });
    }

    public void addOneCharWords(Set<String> words, Map<String, Object> charSets) {

        Set<String> sixchar = (Set) charSets.get("sixchar");
        Set<String> fivechar = (Set) charSets.get("fivechar");
        Set<String> fourchar = (Set) charSets.get("fourchar");
        Set<String> threechar = (Set) charSets.get("threechar");
        Set<String> twochar = (Set) charSets.get("twochar");
        Set<String> onechar = (Set) charSets.get("onechar");

        sixchar.forEach(full -> {

            if (onechar.contains(String.valueOf(full.charAt(0))) && onechar.contains(String.valueOf(full.charAt(1))) &&
                onechar.contains(String.valueOf(full.charAt(2))) && onechar.contains(String.valueOf(full.charAt(3))) &&
                onechar.contains(String.valueOf(full.charAt(4))) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.charAt(1), full.charAt(2), full.charAt(3), full.charAt(4), full.charAt(5)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && onechar.contains(String.valueOf(full.charAt(1))) &&
                onechar.contains(String.valueOf(full.charAt(2))) && twochar.contains(full.substring(3, 5)) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.charAt(1), full.charAt(2), full.substring(3, 5), full.charAt(5)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && onechar.contains(String.valueOf(full.charAt(1)))
                && twochar.contains(full.substring(2, 4)) && onechar.contains(String.valueOf(full.charAt(4))) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.charAt(1), full.substring(2, 4), full.charAt(4), full.charAt(5)));
            }
            if (onechar.contains(String.valueOf(full.charAt(0))) && twochar.contains(full.substring(1, 3)) &&
                onechar.contains(String.valueOf(full.charAt(3))) && onechar.contains(String.valueOf(full.charAt(4))) &&
                onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.substring(1, 3), full.charAt(3), full.charAt(4), full.charAt(5)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && twochar.contains(full.substring(1, 3)) &&
                twochar.contains(full.substring(3, 5)) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.substring(1, 3), full.substring(3, 5), full.charAt(5)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && twochar.contains(full.substring(1, 3)) &&
                onechar.contains(String.valueOf(full.charAt(3))) && twochar.contains(full.substring(4, 6))) {
                words.add(getWordBuildup(full, full.charAt(0), full.substring(1, 3), full.charAt(3), full.substring(4, 6)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && onechar.contains(String.valueOf(full.charAt(1))) &&
                twochar.contains(full.substring(2, 4)) && twochar.contains(full.substring(4, 6))) {
                words.add(getWordBuildup(full, full.charAt(0), full.charAt(1), full.substring(2, 4), full.substring(4, 6)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && threechar.contains(full.substring(1, 4)) &&
                onechar.contains(String.valueOf(full.charAt(4))) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.substring(1, 4), full.charAt(4), full.charAt(5)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && onechar.contains(String.valueOf(full.charAt(1))) &&
                threechar.contains(full.substring(2, 5)) && onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.charAt(1), full.substring(2, 5), full.charAt(5)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && onechar.contains(String.valueOf(full.charAt(1))) &&
                onechar.contains(String.valueOf(full.charAt(2))) && threechar.contains(full.substring(3, 6))) {
                words.add(getWordBuildup(full, full.charAt(0), full.charAt(1), full.charAt(2), full.substring(3, 6)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && twochar.contains(full.substring(1, 3)) && threechar.contains(full.substring(3, 6))) {
                words.add(getWordBuildup(full, full.charAt(0), full.substring(1, 3), full.substring(3, 6)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && threechar.contains(full.substring(1, 4)) && twochar.contains(full.substring(4, 6))) {
                words.add(getWordBuildup(full, full.charAt(0), full.substring(1, 4), full.substring(4, 6)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && fourchar.contains(full.substring(1, 5)) &&
                onechar.contains(String.valueOf(full.charAt(5)))) {
                words.add(getWordBuildup(full, full.charAt(0), full.substring(1, 5), full.charAt(5)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && onechar.contains(String.valueOf(full.charAt(1))) && fourchar.contains(full.substring(2, 6))) {
                words.add(getWordBuildup(full, full.charAt(0), full.charAt(1),full.substring(2, 6)));
            }

            if (onechar.contains(String.valueOf(full.charAt(0))) && fivechar.contains(full.substring(1, 6))) {
                words.add(getWordBuildup(full, String.valueOf(full.charAt(0)), full.substring(1, 6)));
            }

        });
    }

    public String getWordBuildup(String full, Object... args){


        StringBuilder stringBuilder = new StringBuilder();

        for (Object arg: args) {
            stringBuilder.append(arg);
            stringBuilder.append(" + ");
        }

        stringBuilder.setLength(stringBuilder.length() - 3);
        stringBuilder.append(" = ");
        stringBuilder.append(full);

        return stringBuilder.toString();


    }

}
