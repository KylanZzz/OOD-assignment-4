package stock.controller;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.StockView;

public class BasicStockController implements StockController {
  private final StockView view;
  private final StockModel model;
  private final Scanner scanner;
  private State state;

  protected enum State {
    WELCOME, MAIN_MENU, VIEW_PORTFOLIOS, MANAGE_PORTFOLIO, EXIT
  }

  public BasicStockController(StockView view, StockModel model, InputStream in) {
    scanner = new Scanner(in);
    this.view = view;
    this.model = model;
    state = State.MAIN_MENU;
  }

  /**
   * 1. Get the gain/loss of stock over period of time
   * 2. Get x-day moving average of a stock
   * 3. Get x-day crossovers for a stock
   * 4. Manage portfolios
   * 5. Exit
   *
   * Type 4...
   *
   * 1. Create new portfolio
   * 2. Delete portfolio
   * 3. Rename portfolio
   * 4. Portfolio 1...
   * 5. Portfolio 2...
   * 6. Portfolio 3...
   *
   * Type 4...
   *
   * // Display portfolio contents
   *
   * 1. Calculate portfolio value
   * 2. Add stock to portfolio
   * 3. Remove stock from portfolio
   */

  @Override
  public void run() {
    while (true) {
      switch (state) {
        case WELCOME:
          handleWelcomeState();
          break;
        case MAIN_MENU:
          handleMainMenuState();
          break;
        case VIEW_PORTFOLIOS:
          handleViewPortfoliosState();
          break;
        case MANAGE_PORTFOLIO:
          handleManagePortfolioState();
          break;
        case EXIT:
          handleExitState();
          return;
        default:
      }
    }
  }

  private void handleWelcomeState() {
    view.printWelcomeScreen();
    view.printMainMenu();
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
        view.printViewPortfolios(model.getPortfolios());
        state = State.MANAGE_PORTFOLIO;
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
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the starting date (inclusive) in the format MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    view.printMessage("Please enter the ending date (inclusive) in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    double gain = model.getGainOverTime(startDate, endDate, ticker);
    view.printStockGain(ticker, startDate, endDate, gain);
  }

  private void handleAverage() {
    view.printMessage("Please enter the ticker of the stock that you would like to know about:");
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the ending date in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    view.printMessage("Please enter the number of days in the format MM/DD/YYYY");
    int days = getPositiveFromUser(Integer.MAX_VALUE);

    double average = model.getMovingDayAverage(endDate, days, ticker);
    view.printStockAverage(ticker, endDate, days, average);
  }

  private void handleCrossover() {

  }

  private void handleViewPortfoliosState() {
    int choice = getPositiveFromUser(BasicMenuOptions.viewPortfolios().size());

    switch (choice) {
      case 1:
        break;
      case 2:
        break;
      case 3:
        break;
      default:
        break;
    }
  }

  private void handleManagePortfolioState() {

  }

  private void handleExitState() {
    view.printMessage("Exiting application...");
  }

  private int getPositiveFromUser(int max) {
    int choice = -1;
    while (choice == -1) {
      try {
        choice = scanner.nextInt();

        if (choice > max || choice < 1) {
          throw new IllegalArgumentException();
        }
      } catch (InputMismatchException e) {
        view.printMessage("Invalid input: not an integer, please try again.");
      } catch (IllegalArgumentException e) {
        view.printMessage("Invalid input: not a valid number. Please enter a number from 1 to " +
                max);
      }
    }

    return choice;
  }

  private String getTickerFromUser() {
    String ticker = scanner.nextLine();

    while (!model.stockExists(ticker)) {
      view.printMessage("Invalid input: stock does not exist for such a ticker. Please try again.");
      ticker = scanner.nextLine();
    }

    return ticker;
  }

  private LocalDate getDateFromUser() {
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
}
