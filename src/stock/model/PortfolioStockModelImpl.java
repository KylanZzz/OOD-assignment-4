package stock.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import stock.model.portfolio.Portfolio;

public class PortfolioStockModelImpl implements PortfolioStockModel {

  private final DataSource dataSource;
  private final StockModel simpleModel;
  private final List<Portfolio> portfolios;
  // "res/portfolio"
  private final String portfoliosDirectory;

  public PortfolioStockModelImpl(DataSource dataSource, String portfoliosDirectory) {
    this.dataSource = dataSource;
    simpleModel = new BasicStockModel(dataSource);
    portfolios = new ArrayList<>();
    this.portfoliosDirectory = portfoliosDirectory;
  }

  @Override
  public Map<String, Double> getPortfolioContentsDecimal(String name, LocalDate date) throws
          IllegalArgumentException {
    return getPortfolio(name).getComposition(date);
  }

  @Override
  public void addStockToPortfolio(String name, String ticker, int shares, LocalDate date) throws
          IOException, IllegalArgumentException {
    getPortfolio(name).buyStock(ticker, date, shares);
  }

  @Override
  public void sellStockFromPortfolio(String name, String ticker, int shares, LocalDate date) throws
          IOException, IllegalArgumentException {
    getPortfolio(name).sellStock(ticker, date, shares);
  }

  @Override
  public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker) throws
          IOException {
    return simpleModel.getGainOverTime(startDate, endDate, ticker);
  }

  @Override
  public double getMovingDayAverage(LocalDate endDate, int days, String ticker) throws IOException {
    return simpleModel.getMovingDayAverage(endDate, days, ticker);
  }

  @Override
  public List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker) throws
          IOException {
    return simpleModel.getCrossover(endDate, days, ticker);
  }

  @Override
  public void createNewPortfolio(String name) {
    if (getPortfolioNames().contains(name)) {
      throw new IllegalArgumentException("A portfolio with that name already exists!");
    }
    portfolios.add(new Portfolio(name));
  }

  @Override
  public void deletePortfolio(String name) {
    portfolios.remove(getPortfolio(name));
  }

  @Override
  public void renamePortfolio(String oldName, String newName) throws IllegalArgumentException {
    if (getPortfolioNames().contains(newName)) {
      throw new IllegalArgumentException("A portfolio with newName already exists!");
    }

    getPortfolio(oldName).rename(newName);
  }

  @Override
  public List<String> getPortfolios() {
    return getPortfolioNames();
  }

  @Override
  public double getPortfolioValue(String name, LocalDate date) throws IOException,
          IllegalArgumentException {
    var port = getPortfolio(name);

    var prices = new HashMap<String, Double>();
    for (var key : port.getComposition(date).keySet()) {
      var dt = date;
      while (!dataSource.stockExistsAtDate(dt, key) && !dt.equals(LocalDate.of(1990,1,
              1))) dt = dt.minusDays(1);
      prices.put(key, dataSource.getClosingPrice(dt, key));
    }

    return port.getValue(date, prices);
  }

  @Override
  public boolean stockExists(String ticker) throws IOException {
    return dataSource.stockInDataSource(ticker);
  }

  @Override
  public Map<String, Double> getPortfolioDistribution(String name, LocalDate date) throws
          IOException, IllegalArgumentException {
    var port = getPortfolio(name);

    var prices = new HashMap<String, Double>();
    for (var key : port.getComposition(date).keySet()) {
      prices.put(key, dataSource.getClosingPrice(date, key));
    }

    return getPortfolio(name).getDistribution(date, prices);
  }

  @Override
  public List<String> getPortfolioSaves(String name) throws IllegalArgumentException, IOException {
    return getPortfolio(name).getAllSaves(portfoliosDirectory);
  }

  @Override
  public void loadPortfolioSave(String name, String fileSaveName) throws IOException,
          IllegalArgumentException {
    getPortfolio(name).loadSave(portfoliosDirectory, fileSaveName);
  }

  @Override
  public void createNewPortfolioSave(String name) throws IOException, IllegalArgumentException {
//    Format: [name]_[YYYY]-[MM]-[DD]T[HH]-[mm]-[ss]
    String currTime = LocalDateTime.now().toString();
    currTime = currTime.split("\\.")[0];
    currTime = currTime.replace(':', '-');
    String fileName = name + "_" + currTime;

    getPortfolio(name).createSave(portfoliosDirectory, fileName);
  }

  @Override
  public void rebalancePortfolio(String name, LocalDate date,
                                 Map<String, Double> proportions) throws IOException,
          IllegalArgumentException {
    var prices = getPrices(name, date);

    getPortfolio(name).rebalance(date, prices, proportions);
  }

  @Override
  public Map<LocalDate, Double> getPortfolioPerformance(String name, LocalDate startDate,
                                                        LocalDate endDate) throws
          IllegalArgumentException, IOException {

    var res = new HashMap<LocalDate, Double>();

    for (var date = startDate; !date.equals(endDate); date = date.plusDays(1)) {
      res.put(date, getPortfolioValue(name, date));
    }

    return res;
  }

  protected final HashMap<String, Double> getPrices(String name, LocalDate date) throws
          IOException {
    var port = getPortfolio(name);

    var prices = new HashMap<String, Double>();
    for (var key : port.getComposition(date).keySet()) {
      prices.put(key, dataSource.getClosingPrice(date, key));
    }
    return prices;
  }

  protected final Portfolio getPortfolio(String name) {
    for (var port : portfolios) {
      if (port.getName().equals(name)) {
        return port;
      }
    }
    throw new IllegalArgumentException("Name of that portfolio doesn't exist");
  }

  protected final List<String> getPortfolioNames() {
    return portfolios.stream().map(Portfolio::getName).collect(Collectors.toList());
  }
}
