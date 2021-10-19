package be.anouchka.exercise.port.outbound;

import java.util.List;
import java.util.Map;

public interface FileRepository {

    Map<String, Object> create (String text);

    void truncate();

    List<Map<String, Object>> getAll();

    String getById(Integer id);

}
