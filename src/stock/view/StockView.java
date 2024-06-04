package stock.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface StockView {
  void printWelcomeScreen();

  void printMainMenu();

  void printMessage(String message);

  void printViewPortfolios(List<String> portfolios);

  void printManagePortfolio(Map<String, Integer> stocks, String name);

  void printXDayCrossovers(String ticker, LocalDate date, int days, List<LocalDate> dates);

  void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain);

  void printStockAverage(String ticker, LocalDate endDate, int days, double average);
}
