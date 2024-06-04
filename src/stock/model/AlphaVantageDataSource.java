package stock.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class AlphaVantageDataSource extends CSVDataSource {
  private static final String API_KEY = "W0M1JOKC82EZEQA8";
  //  3FKL0E8WUDB1EOMS

  private String Stockticker = "";

  private Set<String> tickerList = new HashSet<>();

  private boolean checkInitialization = false;

  // throw IOException
  public AlphaVantageDataSource() {
    super();
  }

  private void init() throws IOException {
    File folder = new File("res/APIData");
    deleteFolder(folder);
    folder.mkdirs();

    try {
      generateTickerList(new File("res/stocksData"));
      getTickerList();
      generateStockCSV(folder, Stockticker);
    }
    catch (IOException e) {
      throw new IOException("The API contains issue, please try again later");
    }
    loadAllStockData("res/APIData");
  }

  public void generateTickerList(File directory) throws IOException {
    File[] files = directory.listFiles((dir, name) -> name.endsWith(".csv"));  // Filter for CSV files
    if (files != null) {
      for (File file : files) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
          String line = reader.readLine();
          while ((line = reader.readLine()) != null) {
            String[] parts = line.split(",");
            if (parts.length > 0) {
              String symbol = parts[0].trim();
              tickerList.add(symbol);
            }
          }
        }
      }
    } else {
      throw new IOException("No CSV files found or access denied in directory: " + directory.getPath());
    }
  }


  public Set<String> getTickerList() {
    return tickerList;
  }

  private void generateStockCSV(File folder, String ticker) throws IOException {
    URL url;
    try {
      // Constructing the URL
      url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED"
              + "&outputsize=full"
              + "&symbol=" + ticker
              + "&apikey=" + API_KEY
              + "&datatype=csv");

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

  @Override
  public boolean stockInDataSource(String ticker) {
    Stockticker = ticker;
    if (!checkInitialization) {
      try {
        init();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
      checkInitialization = true;
    }
    return tickerList.contains(ticker);
  }

  private static void deleteFolder(File folder) {
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
