package stock.model;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the StockModel interface,
 * that provides the functionality for the user to calculate the stocks,
 * including the ability to calculate portfolio values,
 * track stock gains over time, and manage multiple portfolios.
 */
public class BasicStockModel implements StockModel {
  private DataSource dataSource;
  private Map<String, Map<String, Integer>> portfolios;

  public BasicStockModel(DataSource ds)  {
    this.dataSource = ds;
    this.portfolios = new HashMap<>();
  }


  /**
   * 1) When the startDate didn't have a stock but someday in the middle starts,
   * to have and the endDate exists,
   * the program will calculate from the date that the stocks starts to exist.
   * 2) When the startDate did exist and the endDate also exists,
   * the program will calculate the gain and loss over the time.
   * 3) When the startDate and the endDate both missing, only the middle part exists,
   * will return 0 because cannot find the endDate to compare.
   * 4) the startDate is after the endDate, throws illegalArgumentException.
   * @param startDate the start date for calculating the gain/loss.
   * @param endDate   the end date for calculating the cost.
   * @param ticker    the ticker of the Stock.
   * @return the gain/loss over the time.
   * @throws IOException when the date doesn't exist with the stock.
   */
  // Need to write a good documentation for this function
  // Explain to the user how we calculate it
  @Override
  public double getGainOverTime(LocalDate startDate, LocalDate endDate, String ticker)
          throws IOException {
    double total = 0;
    LocalDate currentDate = startDate;

    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date is after the end date");
    }
    while (!dataSource.stockExistsAtDate(currentDate, ticker) && !currentDate.isAfter(endDate)) {
      currentDate = currentDate.plusDays(1);
    }
    if (currentDate.isAfter(endDate)) {
      return 0;
    }

    LocalDate nextDate = currentDate.plusDays(1);

    while (!currentDate.isAfter(endDate)) {
      if (dataSource.stockExistsAtDate(currentDate, ticker)) {
        double todayPrice = dataSource.getClosingPrice(currentDate, ticker);
        while (!dataSource.stockExistsAtDate(nextDate, ticker) && !nextDate.isAfter(endDate)) {
          nextDate = nextDate.plusDays(1);
        }
        if (!nextDate.isAfter(endDate)) {
          double nextDayPrice = dataSource.getClosingPrice(nextDate, ticker);
          total += nextDayPrice - todayPrice;
        }
        currentDate = nextDate;
        nextDate = nextDate.plusDays(1);
      } else {
        currentDate = currentDate.plusDays(1);
        nextDate = currentDate.plusDays(1);
      }
    }

    return total;
  }

  @Override
  public double getMovingDayAverage(LocalDate endDate, int days, String ticker)
          throws IOException {
    //might occur error if the data is insufficient
    double total = 0;
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
    if (portfolios.containsKey(name)) {
      throw new IllegalArgumentException("The name of the portfolio exists");
    }
    portfolios.put(name, new HashMap<String, Integer>());
  }

  @Override
  public void deletePortfolio(String name) {
    if (!portfolios.containsKey(name)) {
      throw new IllegalArgumentException("There is no such name");
    }
    portfolios.remove(name);
  }

  @Override
  public void renamePortfolio(String oldName, String newName) {
    if (!portfolios.containsKey(oldName)) {
      throw new IllegalArgumentException("There is no such name");
    }
    portfolios.put(newName, portfolios.get(oldName));
    deletePortfolio(oldName);
  }

  @Override
   public Map<String, Integer> getPortfolioContents(String name) {
    if (!portfolios.containsKey(name)) {
      throw new IllegalArgumentException("There is no such name");
    }
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
      LocalDate currDate = date;
      while (!dataSource.stockExistsAtDate(currDate, stocks.get(i))
              && !currDate.equals(LocalDate.of(1,1,1))) {
        currDate = currDate.minusDays(1);
      }
      value += dataSource.getClosingPrice(currDate, stocks.get(i))
              * portfolios.get(name).get(stocks.get(i));
    }
    return value;
  }

  @Override
  public void addStockToPortfolio(String name, String ticker, int quantity) {
    if (!portfolios.containsKey(name)) {
      throw new IllegalArgumentException("No such name");
    }

    if (portfolios.get(name).containsKey(ticker)) {
      int newQuantity = portfolios.get(name).get(ticker) + quantity;
      portfolios.get(name).put(ticker, newQuantity);
    } else {
      portfolios.get(name).put(ticker, quantity);
    }
  }

  @Override
  public void removeStockFromPortfolio(String name, String ticker) {
    if (!portfolios.containsKey(name)) {
      throw new IllegalArgumentException("No such name");
    }

    if (!portfolios.get(name).containsKey(ticker)) {
      throw new IllegalArgumentException("No such ticker");
    }
    portfolios.get(name).remove(ticker);
  }

  @Override
  public boolean stockExists(String ticker) throws IOException {
    return dataSource.stockInDataSource(ticker);
  }

}
