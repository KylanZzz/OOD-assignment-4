package stock.model.portfolio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Portfolio {
  private final Set<Transaction> transactions;
  private String name;

  public Portfolio(String name) {
    this.transactions = new TreeSet<>();
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void rename(String newName) {
    this.name = newName;
  }

  public void buyStock(String ticker, LocalDate date, double shares) {
    transactions.add(new BuyTransaction(date, shares, ticker));
  }

  // throws illegal argument if there isn't enough stocks bought to sell that many
  public void sellStock(String ticker, LocalDate date, double shares) throws IllegalArgumentException {
    var composition = getComposition(date);

    if (!composition.containsKey(ticker)) {
      throw new IllegalArgumentException("You cannot sell a stock you have not bought before"
              + " that time.");
    }

    if (composition.get(ticker) < shares) {
      throw new IllegalArgumentException("You have not bought enough of that stock up until that "
              + "date to sell that many shares.");
    }

    transactions.add(new SellTransaction(date, shares, ticker));
  }

  // hashmap of all the stocks in the portfolio with their closing prices at provided date
  // throws illegalArgument if hashmap doesn't have all the stocks
  public void rebalance(LocalDate date, HashMap<String, Double> prices) throws IllegalArgumentException {
    transactions.add(new RebalanceTransaction(date, new HashMap<String, Double>(prices)));
  }

  // get the stocks and shares
  public Map<String, Double> getComposition(LocalDate date) throws IllegalArgumentException {
    Map<String, Double> res = new HashMap<String, Double>();

    for (var tran : transactions) {
      if (tran.getDate().isAfter(date)) break;
      res = tran.apply(res);
    }

    return res;
  }

  // hashmap of all the stocks in the portfolio with their closing prices at provided date
  // throws illegalArgument if hashmap doesn't have all the stocks
  public Double getValue(LocalDate date, HashMap<String, Double> prices) throws IllegalArgumentException {
    var composition = getComposition(date);
    // prices map DOESNT have all the stocks that portfolio does
    if (!prices.keySet().containsAll(composition.keySet())) {
      throw new IllegalArgumentException("Prices does not contain all the necessary stocks!");
    }

    double res = 0;
    for (String ticker : composition.keySet()) {
      res += composition.get(ticker) * prices.get(ticker);
    }
    return res;
  }

  // get value distribution (shares and value)
  // throws illegalArgument if hashmap doesn't have all the stocks
  public Map<String, Double> getDistribution(LocalDate date, Map<String, Double> prices) throws IllegalArgumentException {
    var composition = getComposition(date);
    // prices map DOESNT have all the stocks that portfolio does
    if (!prices.keySet().containsAll(composition.keySet())) {
      throw new IllegalArgumentException("Prices does not contain all the necessary stocks!");
    }

    var res = new HashMap<String, Double>();
    for (String ticker : composition.keySet()) {
      res.put(ticker, prices.get(ticker) * composition.get(ticker));
    }
    return res;
  }

  public void createSave(String folderName) {

  }

  public void loadSave(String fileName) {

  }
}
