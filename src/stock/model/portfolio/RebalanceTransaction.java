package stock.model.portfolio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class RebalanceTransaction extends Transaction {
  private final Map<String, Double> prices;
  private final Map<String, Double> proportions;
  private static final double epsilon = 0.01;

  // stock, price at date
  protected RebalanceTransaction(LocalDate date, Map<String, Double> prices,
                                 Map<String, Double> proportions) {
    super(date);
    this.proportions = proportions;
    this.prices = prices;
  }

  // stock, shares
  @Override
  Map<String, Double> apply(Map<String, Double> res) {
    // stock, value
    Map<String, Double> expected = new HashMap<>();

    // calculate current total price
    double total = 0;
    double percentage = 0;
    for (var key: proportions.keySet()) {
      percentage += proportions.get(key);
      total += res.get(key) * prices.get(key);
    }

    if (!proportions.isEmpty() && Math.abs(1.0 - percentage) > epsilon) {
      throw new RuntimeException("The proportions map does not add up to 100% (1.00)!");
    }

    // calculate the expected value for each stock
    for (var key: proportions.keySet()) {
      double val = proportions.get(key) * total;
      expected.put(key, val);
    }

    // calculate new shares based on the expected value / price
    for (var key: expected.keySet()) {
      res.put(key, expected.get(key) / prices.get(key));
    }

    return res;
  }
}
