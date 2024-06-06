package stock.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * A data source implementation that reads stock data from CSV files.
 * This class is responsible for loading stock data from CSV files,
 * and providing access to specific data points such as
 * closing prices on given dates.
 */
public class CSVDataSource implements DataSource {
  protected Map<String, Map<LocalDate, Double>> stocks;

  /**
   * Constructs a CSVDataSource with a specific directory path.
   * This constructor initializes the stock data from the CSV files
   * located in the specified directory.
   *
   * @param directoryPath The path to the directory containing stock data CSV files.
   */
  public CSVDataSource(String directoryPath) {
    stocks = new HashMap<>();
    loadAllStockData(directoryPath);
  }

  /**
   * Default constructor that initializes an empty data source without preloading data.
   */
  public CSVDataSource() {
    stocks = new HashMap<>();
  }

  // Check the format of the CSV and load all stock data from the directory
  protected void loadAllStockData(String directoryPath) {
    try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
      paths.filter(Files::isRegularFile)
              .filter(path -> path.toString().endsWith(".csv"))
              .forEach(this::loadStockDataFromCSV);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // Load stock data from a single CSV file
  protected void loadStockDataFromCSV(Path filePath) {
    String ticker = filePath.getFileName().toString().replace(".csv", "");
    stocks.put(ticker, new HashMap<>());

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
      String line = reader.readLine();
      int timeIndex = -1;
      int closeIndex = -1;
      String[] headers = line.split(",");
      for (int i = 0; i < headers.length; i++) {
        if (headers[i].trim().equalsIgnoreCase("timestamp")) {
          timeIndex = i;
        }
        if (headers[i].trim().equalsIgnoreCase("adjusted_close")) {
          closeIndex = i;
        }
      }
      if (timeIndex == -1 || closeIndex == -1) {
        throw new IllegalArgumentException("CSV file does not have required "
                + "'timestamp' or 'adjusted_close' columns.");
      }

      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        LocalDate date = LocalDate.parse(parts[timeIndex], DateTimeFormatter.ISO_LOCAL_DATE);
        double closePrice = Double.parseDouble(parts[closeIndex]);
        stocks.get(ticker).put(date, closePrice);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public double getClosingPrice(LocalDate date, String ticker) throws IOException {
    if (!stockInDataSource(ticker)) {
      throw new IllegalArgumentException("Stock is not in Data Source.");
    } else if (!stockExistsAtDate(date, ticker)) {
      return 0;
    } else {
      return stocks.get(ticker).get(date);
    }
  }

  @Override
  public boolean stockExistsAtDate(LocalDate date, String ticker) throws IOException {
    if (!stockInDataSource(ticker)) {
      throw new IllegalArgumentException("Invalid ticker: Stock is not in data source.");
    }
    return stocks.get(ticker).containsKey(date);
  }

  @Override
  public boolean stockInDataSource(String ticker) throws IOException {
    return stocks.containsKey(ticker);
  }
}

