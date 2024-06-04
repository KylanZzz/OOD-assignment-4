package stock.model;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Represents a data source for Stock data.
 */
public interface DataSource {

  /**
   * Gets the closing price for a stock.
   *
   * @param date the date to get the price at.
   * @param ticker the ticker of the stock.
   * @return the price of a certain date.
   *
   * @throws IOException if an I/O error occurs during data fetching.
   */
  double getClosingPrice(LocalDate date, String ticker) throws IOException;

  /**
   * Determines whether the data source contains a log of the stock at a certain date (If it
   * doesn't this may be because the stock market was closed at that date, the data source doesn't
   * have data on that day, or the company did not exist yet).
   *
   * @param date the date.
   * @param ticker the ticker of the stock.
   * @return whether the date contains the stock price
   */
  boolean stockExistsAtDate(LocalDate date, String ticker) throws IOException;

  /**
   * Determines whether a stock exists in the data source (This could either be because the stock
   * doesn't exist, or the stock exists and is missing from the data source).
   *
   * @param ticker the ticker of the stock.
   * @return whether the stock exist sin the data source.
   */
  boolean stockInDataSource(String ticker) throws IOException;
}
