package pl.parser.nbp.web;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import java.io.FileNotFoundException;
import java.io.InputStream;
import static org.junit.Assert.*;

/**
 * Created by Wojtek on 2017-04-24.
 */
public class HttpClientTest {
    private static final String NBP_URI = "http://www.nbp.pl/kursy/xml/";

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowExceptionWhenFileNotExist() throws Exception {
        //given
        HttpClient httpClient = new HttpClient(NBP_URI);

        //when
        httpClient.getFile("nonExistingFile");

        //then throw FileNotFoundException
    }

    @Test(expected = FileNotFoundException.class)
    public void shouldThrowExceptionWhenFileNotExist2() throws Exception {
        //given
        HttpClient httpClient = new HttpClient(NBP_URI);

        //when
        httpClient.getFile("nonExistingFile");

        //then throw FileNotFoundException
    }

    @Test()
    public void shouldGetDirWithFileNames() throws Exception {
        //given
        HttpClient httpClient = new HttpClient(NBP_URI);
        InputStream expectedResponseContent = loadFile("dir2016.txt");

        //when
        InputStream responseContent = httpClient.getFile("dir2016.txt");

        //then
        assertTrue(IOUtils.contentEquals(expectedResponseContent, responseContent));
        assertEquals(IOUtils.toString(expectedResponseContent, "UTF-8"), IOUtils.toString(responseContent, "UTF-8"));
    }

    @Test()
    public void shouldGetXMLWithWithCurrencyRates() throws Exception {
        //given
        HttpClient httpClient = new HttpClient(NBP_URI);
        InputStream expectedResponseContent = loadFile("c021z130130.xml");

        //when
        InputStream responseContent = httpClient.getFile("c021z130130.xml");

        //then
        String normalizedExpectedResponse = removeWhiteSpaces(IOUtils.toString(expectedResponseContent, "ISO-8859-2"));
        String normalizedActualResponse = removeWhiteSpaces(IOUtils.toString(responseContent, "ISO-8859-2"));
        assertEquals(normalizedExpectedResponse, normalizedActualResponse);
    }

    private InputStream loadFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    private String removeWhiteSpaces(String input) {
        return input.replaceAll("\\s+", "");
    }
}