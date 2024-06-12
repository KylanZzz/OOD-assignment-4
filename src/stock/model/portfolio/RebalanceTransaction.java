package stock.model.portfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


// REBALANCE:MM/DD/YYYY,AAPL=>400.0;AMZN=>100.0;NFLX=>250.0,AAPL=>0.3;AMZN=>0.4;NFLX=>0.3
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

  // stock, shares
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
    for (var key: prices.keySet().stream().sorted().toList()) {
      out.append(key).append("=>").append(prices.get(key)).append(";");
    }
    // remove last element separator
    out.deleteCharAt(out.length() - 1);

    out.append(",");

    // append cost map
    for (var key: proportions.keySet().stream().sorted().toList()) {
      out.append(key).append("=>").append(proportions.get(key)).append(";");
    }
    // remove last element separator
    out.deleteCharAt(out.length() - 1);

    return out.toString();
  }
}
