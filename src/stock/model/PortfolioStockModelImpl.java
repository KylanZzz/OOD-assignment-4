package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import stock.model.portfolio.Portfolio;

public class PortfolioStockModelImpl implements PortfolioStockModel {

  private final DataSource dataSource;
  private final StockModel simpleModel;
  private final List<Portfolio> portfolios;

  public PortfolioStockModelImpl(DataSource dataSource) {
    this.dataSource = dataSource;
    simpleModel = new BasicStockModel(dataSource);
    portfolios = new ArrayList<>();
  }


  @Override
  public Map<String, double> getPortfolioContentsDecimal(String name, LocalDate date) throws
          IllegalArgumentException {
    return getPortfolio(name).getComposition(date);
  }

  @Override
  public void addStockToPortfolio(String name, String ticker, int shares, LocalDate date) throws
          IOException, IllegalArgumentException {
    getPortfolio(name).buyStock(ticker, date ,shares);
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
    portfolios.add(new Portfolio(name));
  }

  @Override
  public void deletePortfolio(String name) {
    portfolios.remove(getPortfolio(name));
  }

  @Override
  public void renamePortfolio(String oldName, String newName) {
    getPortfolio(oldName).rename(newName);
  }

  @Override
  public List<String> getPortfolios() {
    return portfolios.stream().map(Portfolio::getName).collect(Collectors.toList());
  }

  @Override
  public double getPortfolioValue(String name, LocalDate date) throws IOException,
          IllegalArgumentException {
    var port = getPortfolio(name);

    var prices = new HashMap<String, double>();
    for (var key: port.getComposition(date).keySet()) {
      prices.put(key, dataSource.getClosingPrice(date, key));
    }

    return port.getValue(date, prices);
  }

  @Override
  public boolean stockExists(String ticker) throws IOException {
    return dataSource.stockInDataSource(ticker);
  }

  @Override
  public Map<String, double> getPortfolioDistribution(String name, LocalDate date) throws
          IOException, IllegalArgumentException {
    var port = getPortfolio(name);

    var prices = new HashMap<String, double>();
    for (var key: port.getComposition(date).keySet()) {
      prices.put(key, dataSource.getClosingPrice(date, key));
    }

    return getPortfolio(name).getDistribution(date, prices);
  }

  @Override
  public List<String> getPortfolioSaves(String name) throws IllegalArgumentException {
    return null;
  }

  @Override
  public void loadPortfolioSave(String name, String fileSaveName) throws IOException,
          IllegalArgumentException {

  }

  @Override
  public void createNewPortfolioSave(String name) throws IOException, IllegalArgumentException {

  }

  @Override
  public void rebalancePortfolio(String name, LocalDate date) throws IOException,
          IllegalArgumentException {
    var prices = getPrices(name, date);

    getPortfolio(name).rebalance(date, prices);
  }

  @Override
  public Map<LocalDate, double> getPortfolioPerformance(String name, LocalDate startDate,
                                                        LocalDate endDate) throws
          IllegalArgumentException, IOException {

    var res = new HashMap<LocalDate, double>();

    for (var date = startDate; !date.equals(endDate); date = date.plusDays(1)) {
      res.put(date, getPortfolioValue(name, date));
    }

    return res;
  }

  protected final HashMap<String, double> getPrices(String name, LocalDate date) throws IOException {
    var port = getPortfolio(name);

    var prices = new HashMap<String, double>();
    for (var key: port.getComposition(date).keySet()) {
      prices.put(key, dataSource.getClosingPrice(date, key));
    }
    return prices;
  }

  protected final Portfolio getPortfolio(String name) {
    for (var port: portfolios) {
      if (port.getName().equals(name)) {
        return port;
      }
    }
    throw new IllegalArgumentException("Name of that portfolio doesn't exist");
  }
}
