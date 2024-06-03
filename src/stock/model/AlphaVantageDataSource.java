package stock.model;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class AlphaVantageDataSource extends CSVDataSource {
  private static final String API_KEY = "W0M1JOKC82EZEQA8";
  //  3FKL0E8WUDB1EOMS

  private List<String> tickerList = new ArrayList();

  // throw IOException
  public AlphaVantageDataSource() {
    super();
    File folder = new File("res/APIData");
    deleteFolder(folder);
    folder.mkdirs();

  }
  private void generateStockCSV(File folder, String ticker) {
    URL url;
    try {
      url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY_ADJUSTED"
              + "&outputsize=full"
              + "&symbol=" + ticker
              + "&apikey=" + API_KEY
              + "&datatype=csv");

      InputStream in = url.openStream();
      File file = new File(folder, ticker + ".csv");

      try (BufferedInputStream bis = new BufferedInputStream(in);
           FileOutputStream fos = new FileOutputStream(file)) {
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1) {
          fos.write(dataBuffer, 0, bytesRead);
        }
      } catch (IOException e) {
        System.err.println("Failed to write data for " + ticker);
      }
    } catch (IOException e) {
      throw new IllegalArgumentException("No price data found for " + ticker);
    }
  }

  @Override
  public boolean stockInDataSource(String ticker) {
    String filePath = "res/stocksData/stocksList.csv";

    try {
      BufferedReader reader = new BufferedReader(new FileReader(filePath));
      String line = reader.readLine();
      int symbolIndex = -1;
      int statusIndex = -1;
      String[] headers = line.split(",");
      for (int i = 0; i < headers.length; i++) {
        if (headers[i].trim().equalsIgnoreCase("Symbol")) {
          symbolIndex = i;
        }
        if (headers[i].trim().equalsIgnoreCase("Status")) {
          statusIndex = i;
        }
      }

      if (symbolIndex == -1 || statusIndex == -1) {
        throw new IllegalArgumentException("CSV file does not have required 'Symbol' or 'Status' columns.");
      }

      boolean isFound = false;
      while ((line = reader.readLine()) != null) {
        String[] parts = line.split(",");
        if (parts[symbolIndex].trim().equalsIgnoreCase(ticker)) {
          isFound = true;
          break;
        }
      }

      if (!isFound) {
        return false;
      }

      reader.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    if (!tickerList.contains(ticker)) {
      generateStockCSV(new File("res/APIData"), ticker);
      loadAllStockData("res/APIData");
      tickerList.add(ticker);
    }

    return true;
  }

  //  public void initialize() {
//    File folder = new File("res/APIData");
//    generateStockCSV(folder, "A");
//    loadAllStockData("res/APIData");
//  }


//  @Override
//  protected void loadAllStockData(String directoryPath) {
//    try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
//      paths.filter(Files::isRegularFile)
//              .filter(path -> path.toString().endsWith(".csv"))
//              .forEach(this::addAllStocksToList);
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

//  }

//  private void addAllStocksToList(Path filePath) {
//    try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
//      String line = reader.readLine();
//      while ((line = reader.readLine()) != null) {
//        String[] parts = line.split(",");
//        String ticker = parts[0];
//        stockSymbols.add(ticker);
//      }
//    } catch (IOException e) {
//      e.printStackTrace();
//    }
//  }

//  private void generateStockCSV(File folder) {
//    for (String stockSymbol : STOCK_SYMBOLS) {
//      URL url;
//      try {
//        url = new URL("https://www.alphavantage.co/query?function=TIME_SERIES_DAILY"
//                + "&outputsize=full"
//                + "&symbol=" + STOCK_SYMBOLS
//                + "&apikey=" + API_KEY
//                + "&datatype=csv");
//
//        InputStream in = url.openStream();
//        File file = new File(folder, STOCK_SYMBOLS + ".csv");
//
//        try (BufferedInputStream bis = new BufferedInputStream(in);
//             FileOutputStream fos = new FileOutputStream(file)) {
//          byte[] dataBuffer = new byte[1024];
//          int bytesRead;
//          while ((bytesRead = bis.read(dataBuffer, 0, 1024)) != -1) {
//            fos.write(dataBuffer, 0, bytesRead);
//          }
//        } catch (IOException e) {
//          System.err.println("Failed to write data for " + stockSymbol);
//        }
//        System.out.println("Data for " + stockSymbol + " has been saved to " + file.getAbsolutePath());
//      } catch (IOException e) {
//        throw new IllegalArgumentException("No price data found for " + stockSymbol);
//      }
//    }
//  }

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
