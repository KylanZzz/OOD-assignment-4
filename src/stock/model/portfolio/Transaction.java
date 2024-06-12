package stock.model.portfolio;
import java.time.LocalDate;
import java.util.Map;

public abstract class Transaction {
  private final LocalDate date;

  protected Transaction(LocalDate date) {
    this.date = date;
  }

  public final LocalDate getDate() {
    return date;
  }

  // Takes in the result map of tickers to shares, then returns that same map after changing it
  abstract Map<String, Double> apply(Map<String, Double> res);
}
