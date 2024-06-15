package stock.model.portfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Represents a rebalance transaction within a portfolio. This transaction adjusts the portfolio
 * to ensure that the specified stocks meet the given proportions at the specified prices.
 */
public class RebalanceTransaction extends Transaction {
  private final Map<String, Double> prices;
  private final Map<String, Double> proportions;
  private static final double epsilon = 0.01;

  /**
   * Constructs a RebalanceTransaction with the specified date, stock prices, and proportions.
   *
   * @param date        the date of the rebalance transaction
   * @param prices      the map of stock tickers to their prices
   * @param proportions the map of stock tickers to their proportions in the portfolio
   */
  protected RebalanceTransaction(LocalDate date, Map<String, Double> prices,
                                 Map<String, Double> proportions) {
    super(date);
    this.proportions = proportions;
    this.prices = prices;
  }

  /**
   * Constructs a RebalanceTransaction from a string representation of the transaction data.
   *
   * @param data the string representation of the rebalance transaction data
   * @throws IOException if there is an error reading the data or if the data is formatted
   * incorrectly
   */
  protected RebalanceTransaction(String data) throws IOException {
    super(data);
    var split = data.split(",");
    this.prices = parseMap(split[1]);
    this.proportions = parseMap(split[2]);
  }

  private Map<String, Double> parseMap(String data) {
    Map<String, Double> res = new HashMap<>();

    var split = data.split(";");
    for (String s : split) {
      insertElement(res, s);
    }

    return res;
  }

  private void insertElement(Map<String, Double> map, String data) {
    var split = data.split("=>");
    map.put(split[0], Double.parseDouble(split[1]));
  }

  /**
   * Applies the rebalance transaction to the provided map of tickers to shares, adjusting the
   * number of shares to meet the specified proportions.
   *
   * @param res the map of tickers to shares
   * @return the updated map after applying the rebalance transaction
   * @throws RuntimeException if the proportions do not add up to 100% (1.00)
   */
  @Override
  Map<String, Double> apply(Map<String, Double> res) {
    // stock, value
    Map<String, Double> expected = new HashMap<>();

    // calculate current total price
    double total = 0;
    double percentage = 0;
    for (var key : proportions.keySet()) {
      percentage += proportions.get(key);
      total += res.get(key) * prices.get(key);
    }

    if (!proportions.isEmpty() && Math.abs(1.0 - percentage) > epsilon) {
      throw new RuntimeException("The proportions map does not add up to 100% (1.00)!");
    }

    // calculate the expected value for each stock
    for (var key : proportions.keySet()) {
      double val = proportions.get(key) * total;
      expected.put(key, val);
    }

    // calculate new shares based on the expected value / price
    for (var key : expected.keySet()) {
      res.put(key, expected.get(key) / prices.get(key));
    }

    return res;
  }

  /**
   * Saves the rebalance transaction as a single-line string.
   *
   * @return the string representation of the rebalance transaction
   */
  @Override
  String save() {
    // append the name qualifier and date
    StringBuilder out = new StringBuilder("REBALANCE:" + String.join(",",
            String.format("%02d/%02d/%04d",
                    getDate().getMonthValue(),
                    getDate().getDayOfMonth(),
                    getDate().getYear()
            ))
    );

    out.append(",");

    // append prices map
    for (var key : prices.keySet().stream().sorted().collect(Collectors.toList())) {
      out.append(key).append("=>").append(prices.get(key)).append(";");
    }
    // remove last element separator
    out.deleteCharAt(out.length() - 1);

    out.append(",");

    // append cost map
    for (var key : proportions.keySet().stream().sorted().collect(Collectors.toList())) {
      out.append(key).append("=>").append(proportions.get(key)).append(";");
    }
    // remove last element separator
    out.deleteCharAt(out.length() - 1);

    return out.toString();
  }
}
