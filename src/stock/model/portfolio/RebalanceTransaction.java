package stock.model.portfolio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RebalanceTransaction extends Transaction {
  private final Map<String, Double> prices;

  // stock, price at date
  protected RebalanceTransaction(LocalDate date, Map<String, Double> prices) {
    super(date);
    this.prices = prices;
  }

  // stock, shares
  @Override
  Map<String, Double> apply(Map<String, Double> res) {
    // stock, shares
    var diff = new HashMap<String, Double>();

    int numStocks = res.keySet().size();
    double total = 0;
    for (var key: res.keySet()) total += res.get(key) * prices.get(key);

    double expected = total / numStocks;

    for (var key: res.keySet()) {
      diff.put(key, expected - res.get(key));
    }

    res.replaceAll((k, v) -> res.get(k) + diff.get(k));

    return res;
  }
}