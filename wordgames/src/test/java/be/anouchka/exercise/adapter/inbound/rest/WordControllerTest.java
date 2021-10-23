package be.anouchka.exercise.adapter.inbound.rest;

import be.anouchka.exercise.WordgamesApplication;
import be.anouchka.exercise.adapter.outbound.postgres.PostgresTestFileRepository;
import be.anouchka.exercise.app.word.WordHandlerImpl;
import be.anouchka.exercise.port.inbound.WordHandler;
import be.anouchka.exercise.util.MapOperations;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import static be.anouchka.exercise.util.JsonUtil.fromJsonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = WordgamesApplication.class, webEnvironment = RANDOM_PORT)
public class WordControllerTest {

    private static final String wordapplicationpath = "/words/file";
    private static final String cleanuppath = "/cleanup";
    private static final String byid = "/words//getWordsById/";
    private static final String savedtexts = "/words/savedTexts";

    @LocalServerPort
    private int port;

    @Autowired
    protected OkHttpClient httpClient;

    @Autowired
    protected PostgresTestFileRepository postgresFileRepository;

    @Autowired
    protected WordHandler wordHandler;

    @BeforeEach
    public void setUp() {
        postgresFileRepository.truncate();

    }

    @Test
    public void testNoCombinationsCode() throws IOException {

        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/oneword.txt"));

        assertThat(post(input).code()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @Test
    public void testEndpointProvidedFileCode() {
        assertThat(post("laptop"+ "\n" + "l" + "\n" + "banaan" + "\n" + "b" + "\n" + "naan" + "\n" + "fraaie" + "\n" + "fra" + "\n"
            + "ai" + "\n" + "actief" + "\n" + "actie" + "\n" + "ap" + "\n" + "babbel" + "\n" + "xy" + "\n" + "blarb" + "\n" + "e" + "\n"
            + "top" + "\n" + "babb" + "\n" + "krekel" + "\n" + "f" + "\n" + "k" + "\n" + "kel").code()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void testEndpointProvidedFileBody() throws IOException {
        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/inputtest.txt"));

        String expectedOutput = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/output.txt"));

        ResponseBody responseBody = post(input).body();

        assertThat(responseBody.string()).isEqualTo(expectedOutput);

        List<Map<String, Object>> texts = postgresFileRepository.getAll();
        assertThat(texts.size()).isEqualTo(1);
        String words = MapOperations.get(texts.get(0),"words");
        assertThat(words).isEqualTo(input);
    }

    @Test
    public void testEmptyFileCode() throws IOException {
        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/emptyfile.txt"));
        assertThat(post(input).code()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(postgresFileRepository.getAll().size()).isEqualTo(0);
    }

    @Test
    public void testTruncate() throws IOException {

        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/inputtest.txt"));
        post(input);

        assertThat(postgresFileRepository.getAll().size()).isEqualTo(1);

        postgresFileRepository.truncate();

        assertThat(postgresFileRepository.getAll().size()).isEqualTo(0);

    }

    @Test
    public void testCallTruncate() throws IOException {

        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/inputtest.txt"));
        post(input);

        assertThat(postgresFileRepository.getAll().size()).isEqualTo(1);

        Response response = truncate();


        assertThat(response.code()).isEqualTo(HttpStatus.OK.value());
        assertThat(postgresFileRepository.getAll().size()).isEqualTo(0);

    }

    @Test
    public void testGetById() throws IOException {

        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/inputtest.txt"));
        ResponseBody responseBody = post(input).body();

        List<Map<String, Object>> texts = postgresFileRepository.getAll();
        Integer id = MapOperations.get(texts.get(0),"id");

        Response responseBodyForId = getById(id);

        assertThat(responseBodyForId.code()).isEqualTo(HttpStatus.OK.value());
        assertThat(responseBodyForId.body().string()).isEqualTo(responseBody.string());

    }

    @Test
    public void testGetByUnknownIdEmpty() {

        Response responseBodyForId = getById(99);

        assertThat(responseBodyForId.code()).isEqualTo(HttpStatus.GONE.value());

    }


    @Test
    public void testGetAll() throws IOException {

        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/inputtest.txt"));

        post(input);
        post(input);


        Response savedTexts = getSavedTexts();

        assertThat(fromJsonList(savedTexts.body().string()).size()).isEqualTo(2);


    }


    protected String getUrl(){
        return String.format("http://localhost:%d", port);
    }

    @SneakyThrows
    protected Response post(String text)
    {
        Request request = new Request
            .Builder()
            .post(RequestBody.create(MediaType.parse("text/plain"),text))
            .url(String.format("%s%s", getUrl(), wordapplicationpath))
            .build();

        Call call = httpClient.newCall(request);
        return call.execute();
    }

    @SneakyThrows
    protected Response truncate()
    {

        RequestBody reqbody = RequestBody.create(null, new byte[0]);

        Request request = new Request
            .Builder()
            .url(String.format("%s%s", getUrl(), cleanuppath))
            .method("POST", reqbody)
            .build();

        Call call = httpClient.newCall(request);
        return call.execute();
    }

    @SneakyThrows
    protected Response getById(Integer id)
    {

        Request request = new Request
            .Builder()
            .get()
            .url(String.format("%s%s%s", getUrl(), byid, id))
            .build();

        Call call = httpClient.newCall(request);
        return call.execute();
    }

    @SneakyThrows
    protected Response getSavedTexts()
    {

        Request request = new Request
            .Builder()
            .get()
            .url(String.format("%s%s", getUrl(), savedtexts))
            .build();

        Call call = httpClient.newCall(request);
        return call.execute();
    }

}
