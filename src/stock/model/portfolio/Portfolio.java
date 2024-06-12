package stock.model.portfolio;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Portfolio {
  private final List<Transaction> transactions;
  private String name;
  private final String FILE_EXTENSION = ".txt";

  public Portfolio(String name) {
    this.transactions = new ArrayList<>();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void rename(String newName) {
    this.name = newName;
  }

  public void buyStock(String ticker, LocalDate date, double shares) {
    transactions.add(new BuyTransaction(date, shares, ticker));
  }

  // throws illegal argument if there isn't enough stocks bought to sell that many
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

  // hashmap of all the stocks in the portfolio with their closing prices at provided date
  // throws illegalArgument if hashmap doesn't have all the stocks
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

  // get the stocks and shares
  public Map<String, Double> getComposition(LocalDate date) throws IllegalArgumentException {
    Map<String, Double> res = new HashMap<>();

    for (var tran : transactions) {
      if (tran.getDate().isAfter(date)) continue;
      res = tran.apply(res);
    }

    return res;
  }

  // hashmap of all the stocks in the portfolio with their closing prices at provided date
  // throws illegalArgument if hashmap doesn't have all the stocks
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

  // get value distribution (shares and value)
  // throws illegalArgument if hashmap doesn't have all the stocks
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

  public List<String> getAllSaves(String folderName) throws IOException {
    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(folderName),
            name + "*")) {
      List<String> output = new ArrayList<>();

      for (Path entry : stream) {
        // remove file extension
        output.add(entry.getFileName().toString().replace(FILE_EXTENSION, ""));
      }

      return output;
    } catch (IOException e) {
      throw new IOException(
              "An error occurred while trying to read all saves from " + folderName + ".", e);
    }
  }

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

  public void loadSave(String folderName, String fileName) throws IOException {
    Path filePath = Paths.get(folderName, fileName + FILE_EXTENSION);

    try {
      List<String> lines = Files.readAllLines(filePath);

      transactions.clear();
      for (String line : lines) {
        transactions.add(parseTransaction(line));
      }

    } catch (IOException e) {
      throw new IOException("An error occurred while trying to load save: " + fileName + ".", e);
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
