package stock.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class CalculateCrossover extends Command {
  public CalculateCrossover(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  @Override
  public void apply() {
    try {
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

      var crossOvers = model.getCrossover(endDate, days, ticker);
      view.printXDayCrossovers(ticker, endDate, days, crossOvers);
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}