package pl.parser.nbp.model.finance;

/**
 * Created by Wojtek on 2017-04-22.
 */
public class CurrencyRate {
    private double buyingRate;
    private double sellingRate;

    public CurrencyRate(double buyingRate, double sellingRate) {
        this.buyingRate = buyingRate;
        this.sellingRate = sellingRate;
    }

    public double getBuyingRate() {
        return buyingRate;
    }

    public double getSellingRate() {
        return sellingRate;
    }

}
