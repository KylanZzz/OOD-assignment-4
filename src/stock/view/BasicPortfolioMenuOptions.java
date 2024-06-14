package stock.view;

import java.util.List;

public class BasicPortfolioMenuOptions extends BasicMenuOptions {
  public static List<String> managePortfolio() {
    return List.of(
            "Buy stock to portfolio",
            "Sell stock from portfolio",
            "Calculate portfolio value on specific date",
            "Check the distribution of value on specific date",
            "Save portfolio",
            "Load the saved portfolio",
            "Rebalance the portfolio",
            "Performance chart for the portfolio",
            "Composition of the portfolio");
  }

}
