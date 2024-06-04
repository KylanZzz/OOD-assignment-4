package stock.view;

import java.util.List;

/**
 * Represents all the possible menu options for the terminal UI of a stock view. By using static
 * methods, this design allows for easy extension and customization in the future. Subclasses can
 * override these methods to provide different sets of menu options as needed, or add different
 * menus entirely.
 */
public class BasicMenuOptions {
  public static String exitKeyword() {
    return "EXIT";
  }

  public static List<String> mainMenu() {
    return List.of("Get the gain/loss of stock over period of time",
            "Get x-day moving average of a stock",
            "Get x-day crossovers for a stock",
            "Manage portfolios");
  }

  public static List<String> viewPortfolios() {
    return List.of("Create new portfolio",
            "Delete portfolio",
            "Rename portfolio");
  }

  public static List<String> managePortfolio() {
    return List.of("Calculate portfolio value",
            "Add stock to portfolio",
            "Remove stock from portfolio");
  }
}
