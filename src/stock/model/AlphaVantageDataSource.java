package stock.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * A data source class that retrieves the data from AlphaVantage API.
 * This class handles initialization of the stock data, manages ticker symbols, and
 * facilitates downloading and processing stock data from the API.
 */
public class AlphaVantageDataSource extends CSVDataSource {
  private static final String API_KEY = "W0M1JOKC82EZEQA8";

  //  3FKL0E8WUDB1EOMS

  private Set<String> tickerList = new HashSet<>();
  String folderName = "res/APIData";

  private boolean checkInitialization = false;

  /**
   * Initialized the class by inherent the CSVDataSource class.
   */
  // throw IOException
  public AlphaVantageDataSource() {
    super();
  }

  protected void init() throws IOException {
    File folder = new File(folderName);
    deleteFolder(folder);
    folder.mkdirs();
    generateTickerList(new File("res/stocksData"));
  }

  /**
   * User can upload more than one stock files to let the program get the ticker,
   * this function will help to combine to list of tickers.
   *
   * @param directory The directory of the files.
   * @throws IOException when the directory cannot find any files.
   */
  protected void generateTickerList(File directory) throws IOException {
    File[] files = directory.listFiles((dir, name) -> name.endsWith(".txt"));
    if (files == null || files.length == 0) {
      throw new IOException("There is no TXT file in the folder");
    }

    for (File file : files) {
      try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
        String line;
        while ((line = reader.readLine()) != null) {
          String[] parts = line.split(",");
          for (int i = 1; i < parts.length; i++) {
            tickerList.add(parts[i].trim());
          }
        }
      } catch (IOException e) {
        throw new IOException("Failed to read from file: " + file.getPath());
      }
    }
  }

  protected Set<String> getTickerList() {
    return tickerList;
  }

  /**
   * @param folder the file of a certain CSV of the stock.
   * @param ticker the ticker of the stock.
   * @throws IOException when the user reached the maximum of request, the API needs time to load,
   *                     when the link is malformed, when the link cannot be read,
   *                     or any kind of error that the API encounters.
   */
  protected void generateStockCSV(File folder, String ticker) throws IOException {
    URL url;
    HttpURLConnection connection = null;
    try {
      // Constructing the URL
      // url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED"
      //        + "&outputsize=full"
      //        + "&symbol=" + ticker
      //        + "&apikey=" + API_KEY
      //        + "&datatype=csv");
      url = createStockDataURL(ticker);
      connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");

      int responseCode = connection.getResponseCode();
      if (responseCode == 429) {
        throw new IOException("API rate limit exceeded for " + ticker);
      } else if (responseCode != 200) {
        throw new IOException("Failed to download data for " + ticker + ". HTTP response code: " + responseCode);
      }

      // Opening a stream from the URL
      try (InputStream in = url.openStream();
        BufferedInputStream bis = new BufferedInputStream(in);
        FileOutputStream fos = new FileOutputStream(new File(folder, ticker + ".csv"))) {

        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1) {
          fos.write(dataBuffer, 0, bytesRead);
        }
      }
    } catch (MalformedURLException e) {
      throw new IOException("The URL is malformed, please check the API endpoint and parameters: " + e.getMessage(), e);
    } catch (IOException e) {
      throw new IOException("Failed to download or write data for " + ticker + ": " + e.getMessage(), e);
    } catch (Exception e) {
      throw new IOException("An unexpected error occurred while accessing the API for " + ticker + ": " + e.getMessage(), e);
    }
  }


  protected URL createStockDataURL(String ticker) throws MalformedURLException {
    return new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
            + "&outputsize=full"
            + "&symbol=" + ticker
            + "&apikey=" + API_KEY
            + "&datatype=csv");
  }

  @Override
  public boolean stockExistsAtDate(LocalDate date, String ticker) throws IOException {
    if (!checkInitialization) {
      init();
      checkInitialization = true;
    }

    // Invalid ticker
    if (!stockInDataSource(ticker)) {
      throw new IllegalArgumentException("Invalid ticker: Stock is not in data source.");
    }

    // Stock has not been read yet
    if (!stocks.containsKey(ticker)) {
      generateStockCSV(new File(folderName), ticker);
      loadAllStockData("res/APIData");
    }

    return stocks.get(ticker).containsKey(date);
  }

  @Override
  public double getClosingPrice(LocalDate date, String ticker) throws IOException {
    if (!checkInitialization) {
      init();
      checkInitialization = true;
    }
    if (!stockExistsAtDate(date, ticker)) {
      return 0;
    } else {
      return stocks.get(ticker).get(date);
    }
  }

  @Override
  public boolean stockInDataSource(String ticker) throws IOException {
    if (!checkInitialization) {
      init();
      checkInitialization = true;
    }
    return tickerList.contains(ticker);
  }

  protected void deleteFolder(File folder) {
    File[] files = folder.listFiles();
    if (files != null) {
      for (File f : files) {
        if (f.isDirectory()) {
          deleteFolder(f);
        } else {
          f.delete();
        }
      }
    }
    folder.delete();
  }
}
