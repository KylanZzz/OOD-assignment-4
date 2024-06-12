package stock.model.portfolio;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

public abstract class Transaction {
  private final LocalDate date;

  protected Transaction(LocalDate date) {
    this.date = date;
  }

  protected Transaction(String data) throws IOException {
    try {
      var dateString = data.split(",")[0].split(":")[1];
      var dateSplit = dateString.split("/");
      this.date = LocalDate.of(Integer.parseInt(dateSplit[2]), Integer.parseInt(dateSplit[0]),
              Integer.parseInt(dateSplit[1]));
    } catch (Exception e) {
      throw new IOException("Error reading data from file; File is formatted incorrectly.");
    }
  }

  public final LocalDate getDate() {
    return date;
  }

  // Takes in the result map of tickers to shares, then returns that same map after changing it
  abstract Map<String, Double> apply(Map<String, Double> res);

  // Save the transaction as a single-line string
  abstract String save();
}
