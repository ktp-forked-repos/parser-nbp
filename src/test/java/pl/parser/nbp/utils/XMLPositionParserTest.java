package pl.parser.nbp.utils;

import org.junit.Test;
import org.xml.sax.SAXParseException;
import pl.parser.nbp.model.finance.CurrencyRate;
import java.io.InputStream;
import static org.junit.Assert.*;

/**
 * Created by Wojtek on 2017-04-23.
 */
public class XMLPositionParserTest {
    @Test
    public void shouldRetrieveProperlyBuyingAndSellingCurrencyRate() throws Exception {
        //given
        InputStream positionsXML = loadFile("positions.xml");
        XMLPositionParser xmlPositionParser = new XMLPositionParser(positionsXML, "USD");

        //when
        CurrencyRate currencyRate = xmlPositionParser.retrieveCurrencyRates();

        //then
        assertEquals(currencyRate.getBuyingRate(), 3, 0807.);
        assertEquals(currencyRate.getBuyingRate(), 3, 1429.);
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenMissingDataForCurrency() throws Exception {
        //given
        InputStream missingDataForEUR = loadFile("positions.xml");
        XMLPositionParser xmlPositionParser = new XMLPositionParser(missingDataForEUR, "EUR");

        //when
        xmlPositionParser.retrieveCurrencyRates();

        //then throw XMLParseException
    }

    @Test(expected = Exception.class)
    public void shouldThrowExceptionWhenMissingOneValue() throws Exception {
        //given
        InputStream missingValueBuyingRate = loadFile("positions.xml");
        XMLPositionParser xmlPositionParser = new XMLPositionParser(missingValueBuyingRate, "GBT");

        //when
        xmlPositionParser.retrieveCurrencyRates();

        //then throw XMLParseException
    }

    @Test(expected = SAXParseException.class)
    public void shouldThrowExceptionWhenIncorrectXML() throws Exception {
        //given
        InputStream incorrectXML = loadFile("incorrectXML.xml");
        XMLPositionParser xmlPositionParser = new XMLPositionParser(incorrectXML, "USD");

        //when
        xmlPositionParser.retrieveCurrencyRates();

        //then throw SAXParseException
    }

    private InputStream loadFile(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}