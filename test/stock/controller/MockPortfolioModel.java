package stock.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import stock.model.PortfolioStockModel;

final class MockPortfolioModel implements PortfolioStockModel {
  private StringBuilder log;
  private boolean throwIOException;
  private BasicStockControllerTest.MockModel mockModelHelper;

  public MockPortfolioModel(StringBuilder log, boolean throwIOException) {
    this.log = log;
    this.throwIOException = throwIOException;
    this.mockModelHelper = new BasicStockControllerTest.MockModel(log, throwIOException);

  }

  @Override
  public Map<String, Double> getPortfolioContentsDecimal(String name, LocalDate date)
          throws IllegalArgumentException {

    return Map.of("AAPL", 5.5, "AMZN", 10.5, "NFLX", 15.5);
  }

  @Override
  public void addStockToPortfolio(String name, String ticker, int shares, LocalDate date)
          throws IOException, IllegalArgumentException {
    if (throwIOException) {
      throw new IOException("buyIOExceptionMessage");
    }
    log.append("addStockToPortfolio").append(name).append(ticker).append(shares)
            .append(date).append("\n");
  }


  //similar test in other controller test
  @Override
  public void sellStockFromPortfolio(String name, String ticker, int shares, LocalDate date)
          throws IOException, IllegalArgumentException {
    if (throwIOException) {
      throw new IOException("sellIOExceptionMessage");
    }
    log.append("sellStockFromPortfolio").append(name).append(ticker).append(shares)
            .append(date).append("\n");

  }


  @Override
  public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker)
          throws IOException {
    if (throwIOException) {
      log.append("getGainOverTimeIOException\n");
      throw new IOException("gainIOExceptionMessage");
    }
    return 100;
  }


  @Override
  public double getMovingDayAverage(LocalDate endDate, int days, String ticker)
          throws IOException {
    if (throwIOException) {
      log.append("getMovingDayAverageIOException\n");
      throw new IOException("averageIOExceptionMessage");
    }
    return 200;
  }

  @Override
  public List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker)
          throws IOException {
    if (throwIOException) {
      log.append("getCrossoverIOException\n");
      throw new IOException("crossoverIOExceptionMessage");
    }
    log.append("getCrossover").append(endDate).append(days).append(ticker).append("\n");
    return List.of(LocalDate.of(1, 1, 1),
            LocalDate.of(2, 2, 2));
  }

  @Override
  public void createNewPortfolio(String name) {
    mockModelHelper.createNewPortfolio(name);
  }

  @Override
  public void deletePortfolio(String name) {
    mockModelHelper.deletePortfolio(name);

  }

  @Override
  public void renamePortfolio(String oldName, String newName) {
    mockModelHelper.renamePortfolio(oldName, newName);

  }

  @Override
  public List<String> getPortfolios() {
    return List.of("S&P500", "NASDAQ");
  }

  @Override
  public double getPortfolioValue(String name, LocalDate date)
          throws IOException, IllegalArgumentException {
    if (throwIOException) {
      log.append("getPortfolioValueIOException\n");
      throw new IOException("portfolioValueIOExceptionMessage");
    }
    return 400;
  }

  @Override
  public boolean stockExists(String ticker) throws IOException {
    return List.of("AAPL", "NFLX", "AMZN", "TSLA").contains(ticker);
  }

  @Override
  public Map<String, Double> getPortfolioDistribution(String name, LocalDate date)
          throws IOException, IllegalArgumentException {
    if (throwIOException) {
      log.append("getPortfolioDistributionIOException\n");
      throw new IOException("getPortfolioDistributionIOExceptionMessage");
    }
    log.append("getPortfolioDistribution").append(name).append(date).append("\n");
    return Map.of("AMZN", 0.25, "GOOG", 0.25, "AAPL", 0.25);
  }

  @Override
  public List<String> getPortfolioSaves(String name)
          throws IllegalArgumentException, IOException {
    if (throwIOException) {
      log.append("getPortfolioSavesIOException\n");
      throw new IOException("getPortfolioSavesIOExceptionMessage");
    }
    log.append("getPortfolioSaves").append(name).append("\n");
    return List.of("AA", "BB", "CC");
  }

  @Override
  public void loadPortfolioSave(String fileSaveName) throws IOException,
          IllegalArgumentException {
    if (throwIOException) {
      throw new IOException("loadPortfolioSaveIOExceptionMessage");
    }
    log.append("loadPortfolioSave").append(fileSaveName).append("\n");
  }

  @Override
  public void createNewPortfolioSave(String name) throws IOException, IllegalArgumentException {
    if (throwIOException) {
      throw new IOException("createNewPortfolioSaveIOExceptionMessage");
    }
    log.append("createNewPortfolioSave").append(name).append("\n");
  }

  @Override
  public void rebalancePortfolio(String name, LocalDate date, Map<String, Double> proportions)
          throws IOException, IllegalArgumentException {
    if (throwIOException) {
      throw new IOException("rebalancePortfolioIOExceptionMessage");
    }
    log.append("rebalancePortfolio").append(name).append(date).append(proportions).append("\n");
  }

  @Override
  public Map<LocalDate, Double> getPortfolioPerformance(String name,
                                                        LocalDate startDate,
                                                        LocalDate endDate)
          throws IllegalArgumentException, IOException {
    if (throwIOException) {
      log.append("getPortfolioPerformanceIOException\n");
      throw new IOException("getPortfolioPerformanceIOExceptionMessage");
    }
    log.append("getPortfolioPerformance").append(name).append(startDate)
            .append(endDate).append("\n");
    return Map.of(LocalDate.of(2024, 1, 1), 10.0,
            LocalDate.of(2024, 1, 2), 15.0,
            LocalDate.of(2024, 1, 3), 20.0,
            LocalDate.of(2024, 1, 4), 15.0,
            LocalDate.of(2024, 1, 5), 25.0,
            LocalDate.of(2024, 1, 6), 5.0);
  }
}
