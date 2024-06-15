package stock.model.portfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Represents a sell transaction of a specific stock within a portfolio. If the shares of a stock
 * reach 0 (less than epsilon to account for floating point precision errors), then the stock
 * will be removed from the portfolio.
 */
public class SellTransaction extends Transaction {
  private final double shares;
  private final String ticker;
  private static final double epsilon = 0.01;

  /**
   * Constructs a SellTransaction with the specified date, number of shares, and stock ticker.
   *
   * @param date   the date of the sell transaction
   * @param shares the number of shares to sell
   * @param ticker the stock ticker
   * @throws IllegalArgumentException if the number of shares is less than 0
   */
  protected SellTransaction(LocalDate date, double shares, String ticker) {
    super(date);
    if (shares < 0) {
      throw new IllegalArgumentException("Cannot buy less than 0 shares");
    }
    this.shares = shares;
    this.ticker = ticker;
  }

  /**
   * Constructs a SellTransaction from a string representation of the transaction data.
   *
   * @param data the string representation of the sell transaction data
   * @throws IOException if there is an error reading the data or if the data is formatted
   * incorrectly
   */
  protected SellTransaction(String data) throws IOException {
    super(data);
    var split = data.split(",");
    this.shares = Double.parseDouble(split[1]);
    this.ticker = split[2];
  }

  /**
   * Applies the sell transaction to the provided map of tickers to shares, updating the number
   * of shares accordingly. If the number of shares of a stock falls below epsilon, the stock
   * will be removed from the portfolio.
   *
   * @param res the map of tickers to shares
   * @return the updated map after applying the sell transaction
   * @throws RuntimeException if the stock does not exist in the portfolio or if there are not
   *                          enough shares to sell
   */
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

  /**
   * Saves the sell transaction as a single-line string. In the format:
   * SELL:MM/DD/YYYY,[shares],[ticker]
   *
   * @return the string representation of the sell transaction
   */
  @Override
  String save() {

    return "SELL:" + String.join(",",
            String.format("%02d/%02d/%04d",
                    getDate().getMonthValue(),
                    getDate().getDayOfMonth(),
                    getDate().getYear()) + ","
                    + shares + ","
                    + ticker);
  }
}
