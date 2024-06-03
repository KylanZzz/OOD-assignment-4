package stock.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.model.AlphaVantageDataSource;
import stock.model.BasicStockModel;
import stock.model.CSVDataSource;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.BasicStockView;
import stock.view.StockView;

public class BasicStockController implements StockController {

  private final StockView view;
  private final StockModel model;
  private final Scanner scanner;
  private State state;
  private String currentPortfolioName;

  protected enum State {
    WELCOME, MAIN_MENU, VIEW_PORTFOLIOS, MANAGE_PORTFOLIO, EXIT
  }

  public BasicStockController(StockView view, StockModel model, InputStream in) {
    scanner = new Scanner(in);
    this.view = view;
    this.model = model;
    state = State.WELCOME;
    currentPortfolioName = "";
  }

  @Override
  public void run() {
    boolean exitFlag = false;

    while (!exitFlag) {
      switch (state) {
        case WELCOME:
          view.printWelcomeScreen();
          handleWelcomeState();
          break;
        case MAIN_MENU:
          view.printMainMenu();
          handleMainMenuState();
          break;
        case VIEW_PORTFOLIOS:
          view.printViewPortfolios(model.getPortfolios());
          handleViewPortfoliosState();
          break;
        case MANAGE_PORTFOLIO:
          view.printManagePortfolio(model.getPortfolioContents(currentPortfolioName),
                  currentPortfolioName);
          handleManagePortfolioState();
          break;
        case EXIT:
          handleExitState();
          exitFlag = true;
          break;
        default:
      }
    }
  }

  private void handleWelcomeState() {
    state = State.MAIN_MENU;
  }

  private void handleMainMenuState() {
    int choice = getPositiveFromUser(BasicMenuOptions.mainMenu().size());

    switch (choice) {
      case 1:
        handleGain();
        break;
      case 2:
        handleAverage();
        break;
      case 3:
        handleCrossover();
        break;
      case 4:
        state = State.VIEW_PORTFOLIOS;
        break;
      case 5:
        state = State.EXIT;
        break;
      default:
        throw new IllegalStateException("Invalid choice.");
    }
  }

  private void handleGain() {
    view.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = scanner.nextLine().toUpperCase();
    if (!model.stockExists(ticker)) {
      view.printMessage("That stock does not exist!");
      return;
    }

    view.printMessage("Please enter the starting date (inclusive) in the format MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    view.printMessage("Please enter the ending date (inclusive) in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    try {
      double gain = model.getGainOverTime(startDate, endDate, ticker);
      view.printStockGain(ticker, startDate, endDate, gain);
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }

  private void handleAverage() {
    view.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = scanner.nextLine().toUpperCase();
    if (!model.stockExists(ticker)) {
      view.printMessage("That stock does not exist!");
      return;
    }

    view.printMessage("Please enter the ending date in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    view.printMessage("Please enter the number of days.");
    int days = getPositiveFromUser(Integer.MAX_VALUE);

    try {
      double average = model.getMovingDayAverage(endDate, days, ticker);
      view.printStockAverage(ticker, endDate, days, average);
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }

  private void handleCrossover() {
    view.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = scanner.nextLine().toUpperCase();
    if (!model.stockExists(ticker)) {
      view.printMessage("That stock does not exist!");
      return;
    }

    view.printMessage("Please enter the ending date in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    view.printMessage("Please enter the number of days.");
    int days = getPositiveFromUser(Integer.MAX_VALUE);

    try {
      var crossOvers = model.getCrossover(endDate, days, ticker);
      view.printXDayCrossovers(ticker, endDate, days, crossOvers);
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }

  private void handleViewPortfoliosState() {
    int choice = getPositiveFromUser(BasicMenuOptions.viewPortfolios().size() +
            model.getPortfolios().size());

    switch (choice) {
      case 1:
        handleCreatePortfolio();
        break;
      case 2:
        handleDeletePortfolio();
        break;
      case 3:
        handleRenamePortfolio();
        break;
      case 4:
        handleGoBackMain();
        break;
      default:
        handleChosePortfolio(choice);
        break;
    }
  }

  private void handleGoBackMain() {
    state = State.MAIN_MENU;
  }

  private void handleChosePortfolio(int choice) {
    int idx = choice - BasicMenuOptions.viewPortfolios().size() - 1;
    currentPortfolioName = model.getPortfolios().get(idx);
    state = State.MANAGE_PORTFOLIO;
  }

  private void handleRenamePortfolio() {
    view.printMessage("What portfolio would you like to rename? (Please enter the name).");
    String oldName = scanner.nextLine().toUpperCase();
    if (!model.getPortfolios().contains(oldName)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    view.printMessage("What would you like to rename this portfolio to?");
    String newName = scanner.nextLine().toUpperCase();
    if (!model.getPortfolios().contains(oldName)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    if (model.getPortfolios().contains(newName)) {
      view.printMessage("A portfolio with that name already exists!");
    } else {
      model.renamePortfolio(oldName, newName);
      view.printMessage(String.format("Successfully renamed portfolio %s to %s.", oldName, newName));
    }
  }

  private void handleDeletePortfolio() {
    view.printMessage("What portfolio would you like to delete?");
    String name = scanner.nextLine().toUpperCase();
    if (!model.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name does not exist!");
      return;
    }

    model.deletePortfolio(name);
    view.printMessage(String.format("Successfully deleted portfolio %s.", name));
  }

  private void handleCreatePortfolio() {
    view.printMessage("What is the name of the portfolio you would like to create?");
    String name = scanner.nextLine().toUpperCase();
    if (model.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name already exists!");
    }

    model.createNewPortfolio(name);
    view.printMessage(String.format("Successfully created portfolio %s.", name));
  }

  private void handleManagePortfolioState() {
    int choice = getPositiveFromUser(BasicMenuOptions.viewPortfolios().size() +
            model.getPortfolios().size());

    switch (choice) {
      case 1:
        handlePortfolioValue();
        break;
      case 2:
        handleAddStockPortfolio();
        break;
      case 3:
        handleRemoveStockPortfolio();
        break;
      case 4:
        handleGoBackViewPortfolio();
        break;
      default:
        throw new IllegalArgumentException("Invalid input.");
    }
  }

  private void handleAddStockPortfolio() {
    view.printMessage(String.format("Please enter the ticker of the stock " +
            "that you would like to add to portfolio %s.", currentPortfolioName));
    String ticker = scanner.nextLine().toUpperCase();
    if (!model.stockExists(ticker)) {
      view.printMessage("That stock does not exist!");
      return;
    }

    model.addStockToPortfolio(currentPortfolioName, ticker);
    view.printMessage(String.format("Successfully added stock %s to portfolio %s.", ticker,
            currentPortfolioName));
  }

  private void handleRemoveStockPortfolio() {
    view.printMessage(String.format("Please enter the ticker of the stock " +
            "that you would like to remove from portfolio %s.", currentPortfolioName));
    String ticker = scanner.nextLine().toUpperCase();
    if (!model.stockExists(ticker)) {
      view.printMessage("That stock does not exist!");
      return;
    }

    if (!model.getPortfolioContents(currentPortfolioName).contains(ticker)) {
      view.printMessage("That stock is not in the portfolio.");
      return;
    }

    model.removeStockFromPortfolio(currentPortfolioName, ticker);
    view.printMessage(String.format("Successfully added stock %s to portfolio %s.", ticker,
            currentPortfolioName));
  }

  private void handlePortfolioValue() {
    view.printMessage(String.format("What date would you like to know the value of portfolio %s " +
            "at? Please enter the date in the format MM/DD/YYYY.", currentPortfolioName));
    LocalDate date = getDateFromUser();

    try {
      double value = model.getPortfolioValue(currentPortfolioName, date);
      view.printMessage(String.format("The value of the portfolio %s at %s is %.2f.",
              currentPortfolioName, date, value));
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }

  private void handleGoBackViewPortfolio() {
    currentPortfolioName = "";
    state = State.VIEW_PORTFOLIOS;
  }

  private void handleExitState() {
    view.printMessage("Exiting application... (test)");
  }

  // Helper functions below

//  protected final String getPortfolioName() {
//    String output = scanner.nextLine().toUpperCase();
//    while (!model.getPortfolios().contains(output) && !output.equals(EXIT_KEYWORD)) {
//      view.printMessage("A portfolio with that name does not exist! Please try again.");
//      output = scanner.nextLine().toUpperCase();
//    }
//
//    return output;
//  }

  protected final int getPositiveFromUser(int max) {
    int choice = -1;
    while (choice == -1) {
      try {
        choice = scanner.nextInt();
        scanner.nextLine();
        if (choice > max || choice < 1) {
          throw new IllegalArgumentException();
        }
      } catch (InputMismatchException e) {
        view.printMessage("Invalid input: not an integer, please try again.");
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.printMessage("Invalid input: not a valid number. Please enter a number from 1 to " +
                max);
        choice = -1;
        scanner.nextLine();
      }
    }

    return choice;
  }

  protected final LocalDate getDateFromUser() {
    LocalDate date = null;
    while (date == null) {
      try {
        String input = scanner.nextLine();
        String[] split = input.split("/");

        if (split.length != 3) {
          throw new IllegalArgumentException();
        }

        int month = Integer.parseInt(split[0]);
        int day = Integer.parseInt(split[1]);
        int year = Integer.parseInt(split[2]);

        date = LocalDate.of(year, month, day);
      } catch (NumberFormatException e) {
        view.printMessage("Invalid input: not an integer, please try again.");
      } catch (DateTimeException e) {
        view.printMessage("Invalid date: Please enter a valid date.");
      } catch (Exception e) {
        view.printMessage("Incorrect format: Please enter the date in the format MM/DD/YYYY.");
      }
    }

    return date;
  }

  public static void main(String[] args) {

    StockView view = new BasicStockView(System.out);
//    StockModel model = new BasicStockModel(new CSVDataSource("res/CSVData"));
    StockModel model = new BasicStockModel(new AlphaVantageDataSource());
    StockController controller = new BasicStockController(view, model, System.in);
    controller.run();
  }
}
