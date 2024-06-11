package stock.model.portfolio;

import java.time.LocalDate;
import java.util.Map;

public class BuyTransaction extends Transaction {
  private final double shares;
  private final String ticker;

  protected BuyTransaction(LocalDate date, double shares, String ticker) {
    super(date);
    this.shares = shares;
    this.ticker = ticker;
  }

  @Override
  Map<String, double> apply(Map<String, double> res) {
    if (!res.containsKey(ticker)) {
      res.put(ticker, shares);
    } else {
      res.put(ticker, res.get(ticker) + shares);
    }
    return res;
  }
}
