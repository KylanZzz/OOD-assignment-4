package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BasicStockModel implements StockModel {
  private DataSource dataSource;
  private Map<String, Map<String, Integer>> portfolios;

  private boolean checkInitialization = false;

  private final List<String> CONTENT = List.of("Calculate portfolio value" , "Add stock to portfolio", "Add stock to portfolio");

  public BasicStockModel(DataSource ds)  {
    this.dataSource = ds;
    this.portfolios = new HashMap<>();
  }

  @Override
  public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker) throws IOException {
    double total = 0;
    LocalDate currentDate = startDate;

    while (!currentDate.isAfter(endDate.minusDays(1))) {
      double todayPrice = dataSource.getClosingPrice(currentDate, ticker);
      double nextDayPrice = dataSource.getClosingPrice(currentDate.plusDays(1), ticker);

      double dailyChange = nextDayPrice - todayPrice;
      total += dailyChange;
      currentDate = currentDate.plusDays(1);
    }
    return total;
  }


  @Override
  public double getMovingDayAverage(LocalDate endDate, int days, String ticker) throws IOException {
    //might occur error if the data is insufficient
    double total = 0;
    int counter = 0;
    LocalDate date = endDate;

    int count = 0;
    while (count < days && !(date.equals(LocalDate.of(1,1,1)))) {
      if (dataSource.stockExistsAtDate(date, ticker)) {
        total += dataSource.getClosingPrice(date, ticker);
        count++;
      }
      date = date.minusDays(1);
    }

    return total / days;
  }

  @Override
  public List<LocalDate> getCrossover(LocalDate endDate, int days, String ticker) throws IOException {
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
    portfolios.put(name, new HashMap<String, Integer>());
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
   public Map<String, Integer> getPortfolioContents(String name) {
    return portfolios.get(name);
  }

  @Override
  public List<String> getPortfolios() {
    List<String> portfoliosList = new ArrayList<>();
    for (Map.Entry<String, Map<String, Integer>> entry : portfolios.entrySet()) {
      portfoliosList.add(entry.getKey());
    }
    return portfoliosList;
  }

 // multi
  @Override
  public double getPortfolioValue(String name, LocalDate date) throws IOException {
    List<String> stocks = new ArrayList<>(portfolios.get(name).keySet());
    double value = 0;
    for (int i = 0; i < stocks.size(); i++) {
      LocalDate dates = date;
      while (!dataSource.stockExistsAtDate(date, stocks.get(i))) {
        dates = dates.minusDays(1);
      }
      value += dataSource.getClosingPrice(date, stocks.get(i));
    }
    return value;
  }

  @Override
  public void addStockToPortfolio(String name, String ticker, int quantity) {
    if (portfolios.get(name).containsKey(ticker)) {
      int newQuantity = portfolios.get(name).get(ticker) + quantity;
      portfolios.get(name).remove(ticker);
      portfolios.get(name).put(ticker, newQuantity);
    }

    portfolios.get(name).put(ticker, quantity);
  }

  @Override
  public void removeStockFromPortfolio(String name, String ticker) {
    portfolios.get(name).remove(ticker);
  }

  @Override
  public boolean stockExists(String ticker) throws IOException {
    return dataSource.stockInDataSource(ticker);
  }

}
