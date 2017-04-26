package pl.parser.nbp.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import pl.parser.nbp.model.finance.CurrencyRate;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

/**
 * Created by Wojtek on 2017-04-22.
 */

public class XMLPositionParser {
    private InputStream xmlPositions;
    private String currencyCode;

    public XMLPositionParser(InputStream xmlPositions, String currencyCode) {
        this.xmlPositions = xmlPositions;
        this.currencyCode = currencyCode;
    }

    public CurrencyRate retrieveCurrencyRates() throws Exception {
        NodeList positions = getPositionNodes(xmlPositions);
        for (int count = 0; count < positions.getLength(); count++) {
            NodeList currentPosition = positions.item(count).getChildNodes();
            CurrencyRate currencyRate = parseCurrenyRate(currentPosition);
            if (currencyRate != null) {
                return currencyRate;
            }
        }
        throw new Exception("Cannot find currency rates for: " + currencyCode);
    }

    private CurrencyRate parseCurrenyRate(NodeList currentPosition) {
        for (int i = 0; i < currentPosition.getLength(); i++) {
            Node tempNode = currentPosition.item(i);
            if (checkCurrencyCode(currencyCode, tempNode)) {
                Node nodeBuyingRate = currentPosition.item(i + 2);
                Node nodeSellingRate = currentPosition.item(i + 4);
                return getCurrencyRate(nodeBuyingRate, nodeSellingRate);
            }
        }
        return null;
    }

    private boolean checkCurrencyCode(String currency, Node tempNode) {
        return tempNode.getNodeName().equals("kod_waluty") && tempNode.getTextContent().equals(currency);
    }

    private double parseDoubleFromXMLTag(Node node) {
        return Double.parseDouble(node.getTextContent().replace(',', '.'));
    }

    private CurrencyRate getCurrencyRate(Node nodeBuyingRate, Node nodeSellingRate) {
        if (checkTagsNames(nodeBuyingRate, nodeSellingRate)) {
            double averageBuyingRate = parseDoubleFromXMLTag(nodeBuyingRate);
            double averageSellingRate = parseDoubleFromXMLTag(nodeSellingRate);
            return new CurrencyRate(averageBuyingRate, averageSellingRate);
        } else {
            return null;
        }
    }

    private boolean checkTagsNames(Node nodeBuyingRate, Node nodeSellingRate) {
        return nodeBuyingRate.getNodeName().equals("kurs_kupna") && nodeSellingRate.getNodeName().equals("kurs_sprzedazy");
    }

    private Document parsePositions(InputStream xml) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        dbf.setValidating(false);
        dbf.setNamespaceAware(true);
        dbf.setFeature("http://xml.org/sax/features/namespaces", false);
        dbf.setFeature("http://xml.org/sax/features/validation", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
        dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

        DocumentBuilder db = dbf.newDocumentBuilder();
        return db.parse(xml);
    }

    private NodeList getPositionNodes(InputStream xml) throws Exception {
        Document doc = parsePositions(xml);
        return doc.getElementsByTagName("pozycja");
    }
}
