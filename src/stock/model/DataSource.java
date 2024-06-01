package stock.model;

import java.time.LocalDate;

/**
 * Represents a data source for Stock data.
 */
public interface DataSource {

  /**
   *
   * @param date
   * @param ticker
   * @return the price of a certain date.
   */
  double getClosingPrice(LocalDate date, String ticker);

  /**
   *
   * @param date
   * @param ticker
   * @return whether the date contains the stock price
   */
  boolean stockExistsAtDate(LocalDate date, String ticker);

  /**
   *
   * @param ticker
   * @return
   */
  boolean stockInDataSource(String ticker);
}
