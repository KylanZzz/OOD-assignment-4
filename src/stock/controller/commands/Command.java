package stock.controller.commands;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

import stock.view.StockView;
import stock.model.StockModel;

/**
 * Represents a single command in the stock application. This abstract class provides some utility
 * functions, such as for handling user input.
 */
public abstract class Command {
  protected StockView view;
  protected StockModel model;
  protected final Scanner scanner;

  /**
   * Constructs a command with a stock's view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public Command(StockView view, StockModel model, Scanner scanner) {
    this.view = view;
    this.model = model;
    this.scanner = scanner;
  }


  /**
   * Executes the command.
   */
  public abstract void apply();

  // Some helper functions below

  protected final int getPositiveFromUser(int max) {
    int choice = -1;
    while (choice == -1) {
      try {
        choice = scanner.nextInt();
        if (choice > max || choice < 1) {
          throw new IllegalArgumentException();
        }
        scanner.nextLine();
      } catch (InputMismatchException e) {
        view.printMessage("Invalid input: not an integer, please try again.");
        scanner.nextLine();
      } catch (IllegalArgumentException e) {
        view.printMessage("Invalid input: not a valid number. Please enter a number from 1 to "
                + max);
        choice = -1;
        scanner.nextLine();
      }
    }

    return choice;
  }

  protected String getTickerFromUser() {
    while (true) {
      String ticker = scanner.nextLine().toUpperCase();
      try {
        if (model.stockExists(ticker)) {
          return ticker;
        } else {
          view.printMessage("That stock does not exist! Please try again.");
        }
      } catch (IOException e) {
        view.printMessage("Error while fetching data: " + e.getMessage());
      }
    }
  }

  protected final LocalDate getDateFromUser() {
    LocalDate date = null;

    while (date == null) {
      view.printMessage("Please input the year: ");
      String yearInput = scanner.nextLine();

      view.printMessage("Please input the month: ");
      String monthInput = scanner.nextLine();

      view.printMessage("Please input the day: ");
      String dayInput = scanner.nextLine();

      try {
        date = LocalDate.of(Integer.parseInt(yearInput), Integer.parseInt(monthInput),
                Integer.parseInt(dayInput));
        if (date.isAfter(LocalDate.now())) {
          date = null;
          throw new InputMismatchException();
        }
      } catch (NumberFormatException e) {
        view.printMessage("Invalid input: not an integer, please try again.");
      } catch (DateTimeException e) {
        view.printMessage("Invalid date: Please enter a valid date.");
      } catch (InputMismatchException e) {
        view.printMessage("Invalid date: Date has not passed yet, please enter a date before or "
                + "equal to today.");
      } catch (Exception e) {
        view.printMessage("Incorrect format: Please enter the date in the format MM/DD/YYYY.");
      }
    }
    return date;
  }
}
