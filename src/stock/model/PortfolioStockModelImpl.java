package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    for (var port: portfolios) {
      if (port.getName().equals(name)) {
        return port.getComposition(date);
      }
    }

    throw new IllegalArgumentException("Name of that portfolio doesn't exist");
  }

  @Override
  public void addStockToPortfolio(String name, String ticker, int shares, LocalDate date) throws
          IOException, IllegalArgumentException {

  }

  @Override
  public void sellStockFromPortfolio(String name, String ticker, int shares, LocalDate date) throws
          IOException, IllegalArgumentException {

  }

  @Override
  public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker) throws
          IOException {
    return 0;
  }

  @Override
  public double getMovingDayAverage(LocalDate endDate, int days, String ticker) throws IOException {
    return 0;
  }

  @Override
  public List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker) throws
          IOException {
    return null;
  }

  @Override
  public void createNewPortfolio(String name) {

  }

  @Override
  public void deletePortfolio(String name) {

  }

  @Override
  public void renamePortfolio(String oldName, String newName) {

  }

  @Override
  public List<String> getPortfolios() {
    return null;
  }

  @Override
  public double getPortfolioValue(String name, LocalDate date) throws IOException,
          IllegalArgumentException {
    return 0;
  }

  @Override
  public void removeStockFromPortfolio(String name, String ticker) {

  }

  @Override
  public boolean stockExists(String ticker) throws IOException {
    return false;
  }

  @Override
  public Map<String, double> getPortfolioDistribution(String name, LocalDate date) throws
          IOException, IllegalArgumentException {
    return null;
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

  }


  @Override
  public Map<LocalDate, double> getPortfolioPerformance(String name, LocalDate startDate,
                                                        LocalDate endDate) throws
          IllegalArgumentException, IOException {
    return null;
  }
}
