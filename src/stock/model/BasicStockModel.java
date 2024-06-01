package stock.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BasicStockModel implements StockModel{
  private DataSource dataSource;
  private HashMap<String, List<String>> portfolio;

  private final List<String> CONTENT = List.of("Calculate portfolio value" , "Add stock to portfolio", "Add stock to portfolio");


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
  public double getCrossover(LocalDate shortStartDate, LocalDate shortEndDate, LocalDate longStartDate, LocalDate longEndDate, int days, String ticker) {
    return 0;
  }

  @Override
  public void createNewPortfolio(String name) {
<<<<<<< HEAD
    portfolio.put(name, null);
=======
    portfolio.put(name, new ArrayList<>());
>>>>>>> 7b4750a189661dc238938828a429274aa5cae3a4
  }

  @Override
  public void deletePortfolio(String name) {
<<<<<<< HEAD
    portfolio.remove(name, portfolio.get(name));
=======
    portfolio.remove(name);
>>>>>>> 7b4750a189661dc238938828a429274aa5cae3a4
  }

  @Override
  public void renamePortfolio(String oldName, String newName) {
    portfolio.put(newName, portfolio.get(oldName));
<<<<<<< HEAD
    portfolio.remove(oldName, portfolio.get(oldName));
=======
    deletePortfolio(oldName);
>>>>>>> 7b4750a189661dc238938828a429274aa5cae3a4
  }

  @Override
  public List<String> getPortfolioContents(String name) {
    return portfolio.get(name);
  }

  @Override
  public List<String> getPortfolios() {
<<<<<<< HEAD
    List<String> portfoliosList = new ArrayList<>();
    for (Map.Entry<String, List<String>> entry : portfolio.entrySet()) {
      portfoliosList.add(entry.getKey());
    }
    return portfoliosList;
=======
    return new ArrayList<>(portfolio.keySet());
>>>>>>> 7b4750a189661dc238938828a429274aa5cae3a4
  }

  @Override
  public double getPortfolioValue(String name, LocalDate date) {
    return 0;
  }

  @Override
  public void addStockToPortfolio(String name, String ticker) {
    portfolio.get(name).add(ticker);
  }

  @Override
  public void removeStockFromPortfolio(String name, String ticker) {
    portfolio.get(name).remove(ticker);
  }

  @Override
  public boolean stockExists(String ticker) {
    return dataSource.stockInDataSource(ticker);
  }

}
