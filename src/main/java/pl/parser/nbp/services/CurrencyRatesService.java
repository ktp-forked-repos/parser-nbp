package pl.parser.nbp.services;

import pl.parser.nbp.model.finance.CurrencyRate;
import pl.parser.nbp.model.finance.Statistic;
import pl.parser.nbp.web.HttpClient;
import pl.parser.nbp.utils.Input;
import pl.parser.nbp.utils.XMLPositionParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Created by Wojtek on 2017-04-23.
 */
public class CurrencyRatesService {
    private String currencyCode;
    private LocalDate startDate;
    private LocalDate endDate;
    private Statistic statistic;
    private String uri;

    public CurrencyRatesService(Input input, String uri) {
        this.statistic = new Statistic();
        this.currencyCode = input.getCurrencyCode();
        this.startDate = input.getStartDate();
        this.endDate = input.getEndDate();
        this.uri = uri;
    }

    public void computeStatistic() throws Exception {
        ExecutorService fileNamesService = Executors.newWorkStealingPool();
        List<String> fileNames = createFileNamesList();
        List<Callable<Void>> requestFileName = fileNames.stream()
                .map(this::prepareFileNamesRequests)
                .collect(Collectors.toList());

        callAllFileNamesRequests(fileNamesService, requestFileName);
    }

    public void printStatistics() {
        statistic.printBuyingMean();
        statistic.printSellingStdDev();
    }

    private List<String> createFileNamesList() {
        int years = endDate.getYear() - startDate.getYear();
        List<String> uris = new ArrayList<>();
        for (int year = 0; year <= years; year++) {
            int currentYear = startDate.getYear() + year;
            if (currentYear == 2017) {
                uris.add("dir.txt");
            } else {
                uris.add("dir" + currentYear + ".txt");
            }
        }
        return uris;
    }

    private Callable<Void> prepareFileNamesRequests(String fileName) {
        return () -> {
            try {
                ExecutorService currencyRatesService = Executors.newWorkStealingPool();
                HttpClient NBPhttpClient = new HttpClient(uri);
                InputStream fileNames = NBPhttpClient.getFile(fileName);

                List<Callable<CurrencyRate>> currencyRatesRequests = prepareCurrencyRatesRequest(fileNames);
                callAllCurrencyRatesRequests(currencyRatesService, currencyRatesRequests);
                return null;
            } catch (Exception e) {
                e.printStackTrace();
                throw new InterruptedException();
            }
        };
    }

    private void callAllFileNamesRequests(ExecutorService fileNamesService, List<Callable<Void>> requestFileName) throws Exception {
        fileNamesService.invokeAll(requestFileName)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException();
                    }
                });
    }

    private List<Callable<CurrencyRate>> prepareCurrencyRatesRequest(InputStream fileNames) throws IOException {
        List<Callable<CurrencyRate>> currencyRatesRequests = new ArrayList<>();
        BufferedReader in = new BufferedReader(new InputStreamReader(fileNames));
        String line;
        while ((line = in.readLine()) != null) {
            if (checkFileName(line)) {
                final String fileName = line;
                currencyRatesRequests.add(() -> getCurrencyRate(fileName));
            }
        }
        return currencyRatesRequests;
    }

    private void callAllCurrencyRatesRequests(ExecutorService currencyRatesService, List<Callable<CurrencyRate>> currencyRatesRequests) throws InterruptedException {
        currencyRatesService.invokeAll(currencyRatesRequests)
                .stream()
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception e) {
                        throw new IllegalStateException(e);
                    }
                })
                .forEach(statistic::addCurrencyRate);
    }

    private CurrencyRate getCurrencyRate(String fileName) {
        try {
            HttpClient NBPhttpClient = new HttpClient(uri);
            String currencyRatesXML = fileName + ".xml";
            XMLPositionParser xmlPositionParser = new XMLPositionParser(NBPhttpClient.getFile(currencyRatesXML), currencyCode);
            return xmlPositionParser.retrieveCurrencyRates();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean checkFileName(String fileName) {
        return isFileContainingBuyingAndSellingRates(fileName);
    }

    private boolean isFileContainingBuyingAndSellingRates(String fileName) {
        // see http://www.nbp.pl/home.aspx?f=/kursy/instrukcja_pobierania_kursow_walut.html
        if (fileName.charAt(0) == 'c') {
            LocalDate currentDate = getCurrentDate(fileName.split("z")[1]);
            if (isDateInRange(currentDate)) {
                return true;
            }
        }
        return false;
    }

    private LocalDate getCurrentDate(String fileName) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMdd", Locale.ENGLISH);
        return LocalDate.parse(fileName, formatter);
    }

    private boolean isDateInRange(LocalDate currentDate) {
        return currentDate.isEqual(startDate) || currentDate.isEqual(endDate) ||
                (currentDate.isAfter(startDate) && currentDate.isBefore(endDate));
    }

}
