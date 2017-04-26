package pl.parser.nbp.model.finance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Wojtek on 2017-04-23.
 */
public class Statistic {
    List<Double> buyingRates = Collections.synchronizedList(new ArrayList());
    List<Double> sellingRates = Collections.synchronizedList(new ArrayList());

    public double calculateMean(List<Double> series) {
        double sum = 0;
        for (Double i : series) {
            sum = sum + i;
        }
        return sum / series.size();
    }

    public double getVariance(List<Double> series) {
        double mean = calculateMean(series);
        double temp = 0;
        for (double a : series)
            temp += (a - mean) * (a - mean);
        return temp / series.size();
    }

    public double getStdDev(List<Double> series) {
        return Math.sqrt(getVariance(series));
    }

    public void addCurrencyRate(CurrencyRate currencyRate) {
        buyingRates.add(currencyRate.getBuyingRate());
        sellingRates.add(currencyRate.getSellingRate());
    }

    private void printStatistic(double value) {
        System.out.format("%.4f\n", value);
    }

    public void printBuyingMean() {
        printStatistic(calculateMean(buyingRates));
    }

    public void printSellingStdDev() {
        printStatistic(getStdDev(sellingRates));
    }
}
