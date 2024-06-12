package stock.model.portfolio;

import java.time.LocalDate;
import java.util.Map;

/**
 * If the shares of a stock reach 0 (less than epsilon to account for floating point precision
 * errors), then the stock will be removed from the portfolio.
 */
public class SellTransaction extends Transaction {
  private final double shares;
  private final String ticker;
  private static final double epsilon = 0.01;

  protected SellTransaction(LocalDate date, double shares, String ticker) {
    super(date);
    if (shares < 0) {
      throw new IllegalArgumentException("Cannot buy less than 0 shares");
    }
    this.shares = shares;
    this.ticker = ticker;
  }

  @Override
  Map<String, Double> apply(Map<String, Double> res) {
    if (!res.containsKey(ticker)) {
      throw new RuntimeException("The ticker does not exist in the portfolio at the time of this "
              + "transaction.");
    }
    if (res.get(ticker) < shares) {
      throw new RuntimeException("There are enough shares of this stock at this time to sell.");
    }

    res.put(ticker, res.get(ticker) - shares);
    if (res.get(ticker) < epsilon) {
      res.remove(ticker);
    }

    return res;
  }
}
