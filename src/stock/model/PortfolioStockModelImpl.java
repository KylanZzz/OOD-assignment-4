package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import stock.model.portfolio.Portfolio;

public class PortfolioStockModelImpl implements PortfolioStockModel {

  private final DataSource dataSource;
  private final StockModel simpleModel;

  public PortfolioStockModelImpl(DataSource dataSource) {
    this.dataSource = dataSource;
    simpleModel = new BasicStockModel(dataSource);
  }

  @Override
  public Map<String, Double> getPortfolioContentsDecimal(String name) throws
          IllegalArgumentException {
    return null;
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
  public Map<String, Double> getPortfolioDistribution(String name, LocalDate date) throws
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
  public Map<LocalDate, Double> getPortfolioPerformance(String name, LocalDate startDate,
                                                        LocalDate endDate) throws
          IllegalArgumentException, IOException {
    return null;
  }
}
