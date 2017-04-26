package pl.parser.nbp.utils;

import java.time.LocalDate;

/**
 * Created by Wojtek on 2017-04-23.
 */
public class Input {
    private String currencyCode;
    private LocalDate startDate;
    private LocalDate endDate;

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
