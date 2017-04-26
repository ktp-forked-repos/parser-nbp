package pl.parser.nbp.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

/**
 * Created by Wojtek on 2017-04-23.
 */
public class InputValidator {
    private Input input;

    private enum CurrencyCodes {
        USD, EUR, CHF, GBP;
    }

    public InputValidator(String currencyCode, String startDate, String endDate) {
        this.input = new Input();
        input.setCurrencyCode(currencyCode);
        parseDate(startDate, endDate);
    }

    public boolean validate() throws IllegalArgumentException {
        return validateCurrencyCode() && validateDate();
    }

    private boolean validateCurrencyCode() throws IllegalArgumentException {
        CurrencyCodes.valueOf(input.getCurrencyCode());
        return true;
    }

    private void parseDate(String stringStartDate, String stringEndDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        try {
            input.setStartDate(LocalDate.parse(stringStartDate, formatter));
            input.setEndDate(LocalDate.parse(stringEndDate, formatter));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Incorrect date format");
        }
    }

    private boolean validateDate() {
        // nbp does not expose statistics older than 2002
        LocalDate currentDate = LocalDateTime.now().toLocalDate();
        return !(!input.getEndDate().isAfter(input.getStartDate()) || input.getStartDate().getYear() < 2002 ||
                input.getEndDate().isAfter(currentDate));
    }

    public Input getInput() {
        return input;
    }
}
