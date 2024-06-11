package stock.model.portfolio;
import java.time.LocalDate;
import java.util.Map;

public abstract class Transaction implements Comparable<Transaction> {
  private final LocalDate date;

  protected Transaction(LocalDate date) {
    this.date = date;
  }

  public final LocalDate getDate() {
    return date;
  }

  // Takes in the result map of tickers to shares, then returns that same map after changing it
  abstract Map<String, double> apply(Map<String, double> res);

  @Override
  public int compareTo(Transaction o) {
    return this.date.compareTo(o.date);
  }
}
