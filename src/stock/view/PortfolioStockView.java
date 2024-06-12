package stock.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface PortfolioStockView extends StockView {
  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the fractional quantity of the stocks.
   * @param name the name of the portfolio.
   */
  void printManagePortfolioDouble(Map<String, Double> stocks, String name, LocalDate date);

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param stocks the ticker of the stocks and the fractional quantity of the stocks.
   * @param name the name of the portfolio.
   */
  void printDistribution(Map<String, Double> stocks, String name, LocalDate date);

  /**
   * Display the list of commands that the user can ask for.
   *
   * @param fileName the name of the saved files.
   */
  void printFileSaveName(List<String> fileName);
}
