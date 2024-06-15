package stock.model.portfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

/**
 * Represents a transaction (on a specific date) on a portfolio, IE: buying, selling rebalancing,
 * etc.
 */
public abstract class Transaction {
  private final LocalDate date;

  /**
   * Construct a transaction with a specific date.
   *
   * @param date the date of the transaction.
   */
  protected Transaction(LocalDate date) {
    this.date = date;
  }

  /**
   * Construct a transaction using data from a string, likely parsed/read from a file.
   *
   * @param data the string file; Must be formatted in the format that is specified in README.txt.
   * @throws IOException if an error occurs during string reading.
   */
  protected Transaction(String data) throws IOException {
    try {
      var dateString = data.split(",")[0].split(":")[1];
      var dateSplit = dateString.split("/");
      this.date = LocalDate.of(Integer.parseInt(dateSplit[2]), Integer.parseInt(dateSplit[0]),
              Integer.parseInt(dateSplit[1]));
    } catch (Exception e) {
      throw new IOException("Error reading data from file; File is formatted incorrectly.");
    }
  }

  /**
   * Get the date of the transaction.
   *
   * @return the date of the transaction.
   */
  public final LocalDate getDate() {
    return date;
  }

  /**
   * Applies the transaction to the provided map of tickers to shares and returns the updated map.
   *
   * @param res the map of tickers to shares
   * @return the updated map after applying the transaction
   */
  abstract Map<String, Double> apply(Map<String, Double> res);

  /**
   * Saves the transaction as a single-line string.
   *
   * @return the string representation of the transaction
   */
  abstract String save();
}
