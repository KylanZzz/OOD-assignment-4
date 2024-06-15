package stock.view;

import java.util.List;

/**
 * Extends the BasicMenuOptions to provide specific menu options for managing a stock portfolio.
 * This class contains methods that return lists of options related to various portfolio management
 * tasks.
 */
public class BasicPortfolioMenuOptions extends BasicMenuOptions {

  /**
   * Provides a list of menu options for portfolio management activities.
   * The options include buying and selling stocks, calculating portfolio values, and more.
   *
   * @return a List of Strings, where each string is a
   *          descriptive menu option for portfolio management:
   *         <ul>
   *         <li>Buy stock to portfolio - Adds stocks to the portfolio.</li>
   *         <li>Sell stock from portfolio - Removes stocks from the portfolio.</li>
   *         <li>Calculate portfolio value on specific date - Computes the total value of the
   *         portfolio on a given date.</li>
   *         <li>Check the distribution of value on specific date - Analyzes how the portfolio's
   *         value is distributed across different stocks on a specific date.</li>
   *         <li>Save portfolio - Saves the current state of the portfolio.</li>
   *         <li>Load the saved portfolio - Loads a previously saved state of the portfolio.</li>
   *         <li>Rebalance the portfolio - Adjusts the portfolio to maintain a desired asset
   *         allocation.</li>
   *         <li>Performance chart for the portfolio - Displays a chart showing the performance
   *         of the portfolio over time.</li>
   *         <li>Composition of the portfolio - Shows the makeup of the portfolio in terms of
   *         various stocks.</li>
   *         </ul>
   */
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
