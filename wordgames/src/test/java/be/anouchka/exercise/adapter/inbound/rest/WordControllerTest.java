package be.anouchka.exercise.adapter.inbound.rest;

import be.anouchka.exercise.WordgamesApplication;
import be.anouchka.exercise.app.word.WordHandlerImpl;
import lombok.SneakyThrows;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;


import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(classes = WordgamesApplication.class, webEnvironment = RANDOM_PORT)
public class WordControllerTest {

    private static final String wordapplicationpath = "/words/file";

    @LocalServerPort
    private int port;

    @Autowired
    protected OkHttpClient httpClient;


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
    }

    @Test
    public void testEmptyFileCode() throws IOException {
        String input = IOUtils.toString(WordHandlerImpl.class.getResourceAsStream("/emptyfile.txt"));
        assertThat(post(input).code()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void testNoCombinationsCode() {

        assertThat(post("blabla").code()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY.value());
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

}
