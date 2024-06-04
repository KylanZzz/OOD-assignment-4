package stock.controller.commands.stock;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class PortfolioValue extends StockCommand {
  public PortfolioValue(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    view.printMessage(String.format("What date would you like to know the value of portfolio %s " +
            "at? Please enter the date in the format MM/DD/YYYY.", portfolio));
    LocalDate date = getDateFromUser();

    try {
      double value = model.getPortfolioValue(portfolio, date);
      view.printMessage(String.format("The value of the portfolio %s at %s is %.2f.",
              portfolio, date, value));
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
