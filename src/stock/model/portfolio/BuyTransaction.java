package stock.model.portfolio;

import java.io.IOException;
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

  protected BuyTransaction(String data) throws IOException {
    super(data);
    var split = data.split(",");
    this.shares = Double.parseDouble(split[1]);
    this.ticker = split[2];
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

  @Override
  String save() {

    return "BUY:" + String.join(",",
            String.format("%02d/%02d/%04d",
                    getDate().getMonthValue(),
                    getDate().getDayOfMonth(),
                    getDate().getYear()) + ","
                    + shares + ","
                    + ticker);
  }
}
