package stock.view;

import java.util.List;

/**
 * Represents all the possible menu options for the terminal UI of a stock view. By using static
 * methods, this design allows for easy extension and customization in the future. Subclasses can
 * override these methods to provide different sets of menu options as needed, or add different
 * menus entirely.
 */
public class BasicMenuOptions {


  /**
   * Provides the exit keywords and helps when the user needs to exist the program.
   *
   * @return "EXIT" when the user needs to quit the program.
   */
  public static String exitKeyword() {
    return "EXIT";
  }


  /**
   * Provides a menu when the user enter the program.
   *
   * @return A list that represents the menu of the commands that user can ask for.
   */
  public static List<String> mainMenu() {
    return List.of("Get the gain/loss of stock over period of time",
            "Get x-day moving average of a stock",
            "Get x-day crossovers for a stock",
            "Manage portfolios");
  }

  /**
   * Provides a list when the user wants to give any kind of changes to the portfolio itself.
   *
   * @return A list of commands that helps the user to manipulate the portfolios.
   */
  public static List<String> viewPortfolios() {
    return List.of("Create new portfolio",
            "Delete portfolio",
            "Rename portfolio",
            "Purchases the stocks and add to the portfolio");
  }

  /**
   * Provides the options to the user when they need to do something within the portfolio.
   *
   * @return a list of commands that helps user to manipulate the items in the portfolios.
   */
  public static List<String> managePortfolio() {
    return List.of("Calculate portfolio value",
            "Add stock to portfolio",
            "Remove stock from portfolio");
  }
}
