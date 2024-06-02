package stock.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicStockModel implements StockModel{
  private DataSource dataSource;
  private HashMap<String, List<String>> portfolios;

  private final List<String> CONTENT = List.of("Calculate portfolio value" , "Add stock to portfolio", "Add stock to portfolio");


  @Override
  public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker) {
    double total = 0;
    LocalDate currentDate = startDate;
    while (!currentDate.isAfter(endDate)) {
      total += dataSource.getClosingPrice(currentDate, ticker);
      currentDate = currentDate.plusDays(1);
    }

    return total;
  }

  @Override
  public double getMovingDayAverage(LocalDate endDate, int days, String ticker) {
    //might occur error if the data is insufficient
    double total = 0;
    LocalDate date = endDate;

    int count = 0;
    while (count < days) {
      if (dataSource.stockExistsAtDate(date, ticker)) {
        total += dataSource.getClosingPrice(date, ticker);
        count++;
      }
      date = date.minusDays(1);
    }

    return total / days;
  }

  @Override
  public List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker) {
    double movingDayAvg = getMovingDayAverage(endDate, days, ticker);
    List<LocalDate> crossOvers = new ArrayList<>();
    LocalDate date = endDate;

    int count = 0;
    while (count < days) {
      if (dataSource.stockExistsAtDate(date, ticker)
              && (dataSource.getClosingPrice(date, ticker) > movingDayAvg)) {
        crossOvers.add(date);
      }
      count++;
      date = date.minusDays(1);
    }
    return crossOvers;
  }

  @Override
  public void createNewPortfolio(String name) {
    portfolios.put(name, new ArrayList<>());
  }

  @Override
  public void deletePortfolio(String name) {
    portfolios.remove(name);
  }

  @Override
  public void renamePortfolio(String oldName, String newName) {
    portfolios.put(newName, portfolios.get(oldName));
    deletePortfolio(oldName);
  }

  @Override
  public List<String> getPortfolioContents(String name) {
    return portfolios.get(name);
  }

  @Override
  public List<String> getPortfolios() {
    List<String> portfoliosList = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : portfolios.entrySet()) {
      portfoliosList.add(entry.getKey());
    }
    return portfoliosList;
  }

  @Override
  public double getPortfolioValue(String name, LocalDate date) {
    List<String> stocks = new ArrayList<>(portfolios.get(name));
    double value = 0;
    for (int i = 0; i < stocks.size(); i++) {
      value += dataSource.getClosingPrice(date, stocks.get(i));
    }
    return value;
  }

  @Override
  public void addStockToPortfolio(String name, String ticker) {
    portfolios.get(name).add(ticker);
  }

  @Override
  public void removeStockFromPortfolio(String name, String ticker) {
    portfolios.get(name).remove(ticker);
  }

  @Override
  public boolean stockExists(String ticker) {
    return dataSource.stockInDataSource(ticker);
  }

}
