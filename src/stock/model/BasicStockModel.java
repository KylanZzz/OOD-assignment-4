package stock.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class BasicStockModel implements StockModel{
  private DataSource dataSource;
  private HashMap<String, List<String>> portfolio;


  @Override
  public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker) {
    double total = 0;
    LocalDate currentDate = startDate;
    while (!currentDate.equals(endDate)) {
      total += dataSource.getClosingPrice(currentDate, ticker);
      currentDate = currentDate.plusDays(1);
    }

    return total;
  }

  @Override
  public double getMovingDayAverage(LocalDate endDate, int days, String ticker) {
    double total = dataSource.getClosingPrice(endDate, ticker);
    for (int i = 0; i < days; i++) {
      total += dataSource.getClosingPrice(endDate.minusDays(1), ticker);
    }
    return total / (double)days;
  }

  @Override
  public double getCrossover(LocalDate startDate, LocalDate endDate, int days, String ticker) {
    return 0;
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
  public List<String> getPortfolioContents(String name) {
    return null;
  }

  @Override
  public List<String> getPortfolios() {
    return null;
  }

  @Override
  public double getPortfolioValue(String name, LocalDate date) {
    return 0;
  }

  @Override
  public void addStockToPortfolio(String name, String ticker) {

  }

  @Override
  public void removeStockFromPortfolio(String name, String ticker) {

  }

  @Override
  public boolean stockExists(String ticker) {
    return false;
  }
}
