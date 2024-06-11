package stock.model.portfolio;

import java.time.LocalDate;
import java.util.Map;

public class SellTransaction extends Transaction {
  private final double shares;
  private final String ticker;

  protected SellTransaction(LocalDate date, double shares, String ticker) {
    super(date);
    this.shares = shares;
    this.ticker = ticker;
  }

  @Override
  Map<String, Double> apply(Map<String, Double> res) {
    if (res.containsKey(ticker)) {
      throw new RuntimeException("The ticker does not exist in the portfolio at the time of this "
              + "transaction.");
    }
    if (res.get(ticker) < shares) {
      throw new RuntimeException("There are enough shares of this stock at this time to sell.");
    }

    res.put(ticker, res.get(ticker) - shares);

    return res;
  }
}
