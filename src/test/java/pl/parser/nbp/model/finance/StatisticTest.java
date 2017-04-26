package pl.parser.nbp.model.finance;

import org.junit.Before;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static org.junit.Assert.*;

/**
 * Created by Wojtek on 2017-04-24.
 */
public class StatisticTest {
    private Statistic statistic;
    private static final double DELTA = 1e-15;

    @Before
    public void before() {
        statistic = new Statistic();
        CurrencyRate[] currencyRates = {
                new CurrencyRate(1.0, 1.5),
                new CurrencyRate(2.0, 1.2),
                new CurrencyRate(3, 2.3)
        };

        Arrays.stream(currencyRates).forEach(statistic::addCurrencyRate);
    }

    @Test
    public void shouldCalculateMean() throws Exception {
        //given
        List<Double> series = statistic.buyingRates;

        //when
        double mean = statistic.calculateMean(series);

        //then
        assertEquals(2., mean, DELTA);
    }

    @Test
    public void shouldGetVariance() throws Exception {
        //given
        List<Double> series = statistic.buyingRates;

        //when
        double variance = statistic.getVariance(series);

        //then
        assertEquals(0.6666666666666666, variance, DELTA);
    }

    @Test
    public void shouldGetStdDev() throws Exception {
        //given
        List<Double> series = statistic.buyingRates;

        //when
        double stdDev = statistic.getStdDev(series);

        //then
        assertEquals(0.816496580927726, stdDev, DELTA);
    }

    @Test
    public void shouldAddToListInMultiThreads() throws Exception {
        //given
        ExecutorService executor = Executors.newWorkStealingPool();
        CurrencyRate[] currencyRates = {
                new CurrencyRate(1, 1),
                new CurrencyRate(2, 1),
                new CurrencyRate(1, 3)
        };

        List<Callable<Void>> callables = Arrays.asList(
                () -> {
                    statistic.addCurrencyRate(currencyRates[0]);
                    return null;
                },
                () -> {
                    statistic.addCurrencyRate(currencyRates[1]);
                    return null;
                },
                () -> {
                    statistic.addCurrencyRate(currencyRates[2]);
                    return null;
                });

        //when
        executor.invokeAll(callables);

        //then
        assertNotNull(statistic.buyingRates.get(3));
        assertNotNull(statistic.buyingRates.get(4));
        assertNotNull(statistic.buyingRates.get(5));
        assertNotNull(statistic.sellingRates.get(3));
        assertNotNull(statistic.sellingRates.get(4));
        assertNotNull(statistic.sellingRates.get(5));
    }

}