package stock.view;

import java.util.List;

public class BasicPortfolioMenuOptions extends BasicMenuOptions {
  public static List<String> managePortfolio() {
    return List.of("Calculate portfolio value",
            "Add stock to portfolio",
            "Remove stock from portfolio",
            "Add stock to portfolio with specific date",
            "Remove stock from portfolio on specific date",
            "Calculate portfolio value on specific date",
            "Check the distribution of value on specific date",
            "Save portfolio",
            "Load the saved portfolio",
            "Rebalance the portfolio",
            "Performance chart for the portfolio");
  }
}
