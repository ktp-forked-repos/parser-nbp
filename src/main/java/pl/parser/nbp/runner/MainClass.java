package pl.parser.nbp.runner;

import pl.parser.nbp.services.CurrencyRatesService;
import pl.parser.nbp.utils.Input;
import pl.parser.nbp.utils.InputValidator;

public class MainClass {
    private static final String NBP_URI = "http://www.nbp.pl/kursy/xml/";

    public static void main(String[] args) throws Exception {
        if (args.length == 3) {
            InputValidator inputValidator = new InputValidator(args[0], args[1], args[2]);
            if (inputValidator.validate()) {
                Input input = inputValidator.getInput();
                CurrencyRatesService currencyRatesService = new CurrencyRatesService(input, NBP_URI);
                currencyRatesService.computeStatistic();
                currencyRatesService.printStatistics();
            } else {
                System.out.print("Invalid args! Example input: EUR 2015-01-04 2016-01-06");
            }
        } else {
            System.out.print("Incorrect number of arguments");
        }
    }
}