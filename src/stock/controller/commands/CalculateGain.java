package stock.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

import stock.model.StockModel;
import stock.view.StockView;

public class CalculateGain extends Command {
  public CalculateGain(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
  }

  @Override
  public void apply() {
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
}
