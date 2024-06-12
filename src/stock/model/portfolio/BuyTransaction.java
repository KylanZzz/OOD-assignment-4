package stock.model.portfolio;

import java.time.LocalDate;
import java.util.Map;

public class BuyTransaction extends Transaction {
  private final double shares;
  private final String ticker;

  protected BuyTransaction(LocalDate date, double shares, String ticker) {
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
      res.put(ticker, shares);
    } else {
      res.put(ticker, res.get(ticker) + shares);
    }
    return res;
  }
}
