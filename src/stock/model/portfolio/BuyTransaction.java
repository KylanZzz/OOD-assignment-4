package stock.model.portfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Represents a buy transaction within a portfolio. This transaction adds the specified number
 * of shares of a stock to the portfolio on a given date.
 */
public class BuyTransaction extends Transaction {
  private final double shares;
  private final String ticker;

  /**
   * Constructs a BuyTransaction with the specified date, number of shares, and stock ticker.
   *
   * @param date   the date of the buy transaction
   * @param shares the number of shares to buy
   * @param ticker the stock ticker symbol
   * @throws IllegalArgumentException if the number of shares is less than 0
   */
  protected BuyTransaction(LocalDate date, double shares, String ticker) {
    super(date);
    if (shares < 0) {
      throw new IllegalArgumentException("Cannot buy less than 0 shares");
    }
    this.shares = shares;
    this.ticker = ticker;
  }

  /**
   * Constructs a BuyTransaction from a string representation of the transaction data.
   *
   * @param data the string representation of the buy transaction data
   * @throws IOException if there is an error reading the data or if the data is formatted
   *                     incorrectly
   */
  protected BuyTransaction(String data) throws IOException {
    super(data);
    var split = data.split(",");
    this.shares = Double.parseDouble(split[1]);
    this.ticker = split[2];
  }

  /**
   * Applies the buy transaction to the provided map of tickers to shares, adding the specified
   * number of shares to the stock in the portfolio.
   *
   * @param res the map of tickers to shares
   * @return the updated map after applying the buy transaction
   */
  @Override
  Map<String, Double> apply(Map<String, Double> res) {
    if (!res.containsKey(ticker)) {
      res.put(ticker, shares);
    } else {
      res.put(ticker, res.get(ticker) + shares);
    }
    return res;
  }

  /**
   * Saves the buy transaction as a single-line string.
   *
   * @return the string representation of the buy transaction
   */
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
