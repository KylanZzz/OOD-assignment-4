package stock.view;

import java.time.LocalDate;
import java.util.List;

public interface StockView {
  void printMainMenu();

  void printMessage(String message);

  void printViewPortfolios(List<String> portfolios);

  void printManagePortfolio(List<String> tickers, String name);

  void printXDayCrossovers(String ticker, LocalDate date, int days, List<LocalDate> dates);

  void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain);

  void printStockAverage(String ticker, LocalDate endDate, int days, double average);
}
