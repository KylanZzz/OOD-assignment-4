package stock.model.portfolio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Portfolio class represents a collection of transactions involving various stocks.
 * It provides functionality to buy and sell stocks, rebalance the portfolio, and retrieve
 * the portfolio's composition, value, and value distribution at a specific date.
 */
public class Portfolio {
  private final List<Transaction> transactions;
  private String name;
  private final String FILE_EXTENSION = ".txt";

  /**
   * Constructs a Portfolio with the specified name.
   *
   * @param name the name of the portfolio
   */
  public Portfolio(String name) {
    this.transactions = new ArrayList<>();
    this.name = name;
  }

  /**
   * Gets the name of the portfolio.
   *
   * @return the name of the portfolio
   */
  public String getName() {
    return name;
  }

  /**
   * Renames the portfolio to the specified new name.
   *
   * @param newName the new name for the portfolio
   */
  public void rename(String newName) {
    this.name = newName;
  }

  /**
   * Adds a buy transaction to the portfolio.
   *
   * @param ticker the stock ticker symbol
   * @param date   the date of the buy transaction
   * @param shares the number of shares to buy
   */
  public void buyStock(String ticker, LocalDate date, double shares) {
    transactions.add(new BuyTransaction(date, shares, ticker));
  }

  /**
   * Adds a sell transaction to the portfolio.
   *
   * @param ticker the stock ticker symbol
   * @param date   the date of the sell transaction
   * @param shares the number of shares to sell
   * @throws IllegalArgumentException if there are not enough shares to sell or if the stock was
   *                                  not previously bought
   */
  public void sellStock(String ticker, LocalDate date, double shares) throws
          IllegalArgumentException {
    var composition = getComposition(date);

    if (!composition.containsKey(ticker)) {
      throw new IllegalArgumentException("You cannot sell a stock you have not bought before"
              + " that time.");
    }

    if (composition.get(ticker) < shares) {
      throw new IllegalArgumentException("You have not bought enough of that stock up until that "
              + "date to sell that many shares.");
    }

    transactions.add(new SellTransaction(date, shares, ticker));
  }

  /**
   * Rebalances the portfolio on a given date with specified stock prices and proportions.
   *
   * @param date        the date of the rebalance transaction
   * @param prices      a map of stock tickers to their prices
   * @param proportions a map of stock tickers to their desired proportions in the portfolio
   * @throws IllegalArgumentException if the proportions do not match the current stocks in the
   *                                  portfolio
   */
  public void rebalance(LocalDate date, Map<String, Double> prices,
                        Map<String, Double> proportions) throws IllegalArgumentException {

    var composition = getComposition(date);
    if (!composition.keySet().equals(proportions.keySet())) {
      throw new IllegalArgumentException("The stocks in proportions is not the same as the stocks"
              + " in prices.");
    }

    transactions.add(new RebalanceTransaction(date, new HashMap<>(prices),
            new HashMap<>(proportions)));
  }

  /**
   * Gets the composition of the portfolio on a specific date.
   *
   * @param date the date to get the composition at
   * @return a map of stock tickers to the number of shares held
   * @throws IllegalArgumentException if the date is invalid
   */
  public Map<String, Double> getComposition(LocalDate date) throws IllegalArgumentException {
    Map<String, Double> res = new HashMap<>();

    for (var tran : transactions) {
      if (tran.getDate().isAfter(date)) {
        continue;
      }
      res = tran.apply(res);
    }

    return res;
  }

  /**
   * Gets the value of the portfolio on a specific date.
   *
   * @param date   the date to get the value at
   * @param prices a map of stock tickers to their prices on the specified date
   * @return the total value of the portfolio
   * @throws IllegalArgumentException if the prices map does not contain all the necessary stocks
   */
  public Double getValue(LocalDate date, Map<String, Double> prices) throws
          IllegalArgumentException {
    var composition = getComposition(date);

    // prices map DOESNT have all the stocks that portfolio does
    if (!prices.keySet().containsAll(composition.keySet())) {
      throw new IllegalArgumentException("Prices does not contain all the necessary stocks!");
    }

    double res = 0;
    for (String ticker : composition.keySet()) {
      res += composition.get(ticker) * prices.get(ticker);
    }
    return res;
  }

  /**
   * Gets the value distribution of the portfolio on a specific date.
   *
   * @param date   the date to get the value distribution at
   * @param prices a map of stock tickers to their prices on the specified date
   * @return a map of stock tickers to their value in the portfolio
   * @throws IllegalArgumentException if the prices map does not contain all the necessary stocks
   */
  public Map<String, Double> getDistribution(LocalDate date, Map<String, Double> prices) throws
          IllegalArgumentException {
    var composition = getComposition(date);
    // prices map DOESNT have all the stocks that portfolio does
    if (!prices.keySet().containsAll(composition.keySet())) {
      throw new IllegalArgumentException("Prices does not contain all the necessary stocks!");
    }

    var res = new HashMap<String, Double>();
    for (String ticker : composition.keySet()) {
      res.put(ticker, prices.get(ticker) * composition.get(ticker));
    }
    return res;
  }

  /**
   * Gets all save files for this portfolio in the specified folder.
   *
   * @param folderName the name of the folder to search for save files
   * @return a list of save file names without the file extension
   * @throws IOException if an error occurs while reading the folder
   */
  public List<String> getAllSaves(String folderName) throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(folderName),
            name + "*")) {
      List<String> output = new ArrayList<>();

      for (Path entry : stream) {
        // KEEP file extension
        output.add(entry.getFileName().toString());
      }

      return output;
    } catch (IOException e) {
      throw new IOException(
              "An error occurred while trying to read all saves from " + folderName + ".", e);
    }
  }

  /**
   * Creates a save file for this portfolio in the specified folder with the specified file name.
   *
   * @param folderName the name of the folder to save the file in
   * @param fileName   the name of the save file
   * @throws IOException if an error occurs while creating or writing to the file
   */
  public void createSave(String folderName, String fileName) throws IOException {
    StringBuilder data = new StringBuilder();

    for (var tran : transactions) {
      data.append(tran.save()).append(System.lineSeparator());
    }

    try {
      Path folderPath = Paths.get(folderName);
      Path filePath = folderPath.resolve(fileName + FILE_EXTENSION);

      Files.createDirectories(folderPath);
      Files.write(filePath, data.toString().getBytes());

    } catch (IOException e) {
      throw new IOException("An error occurred while trying to save: " + fileName + ".", e);
    }
  }

  /**
   * Loads a save file into this portfolio from the specified folder with the specified file name.
   *
   * @param folderName the name of the folder containing the save file
   * @param fileName   the name of the save file
   * @throws IOException if an error occurs while reading the file or if the file is incorrectly
   *                     formatted
   */
  public void loadSave(String folderName, String fileName) throws IOException {
    Path filePath = Paths.get(folderName, fileName);

    List<String> lines = Files.readAllLines(filePath);

    transactions.clear();
    for (String line : lines) {
      transactions.add(parseTransaction(line));
    }

  }

  private Transaction parseTransaction(String line) throws IOException {
    try {
      String identifier = line.split(":")[0];

      switch (identifier) {
        case "BUY":
          return new BuyTransaction(line);
        case "SELL":
          return new SellTransaction(line);
        case "REBALANCE":
          return new RebalanceTransaction(line);
        default:
          throw new IllegalArgumentException("Incorrect identifier");
      }
    } catch (Exception e) {
      throw new IOException("Error while loading save from file: Incorrect format.");
    }
  }
}
