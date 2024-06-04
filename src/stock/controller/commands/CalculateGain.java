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
    String ticker = getTickerFromUser();

    view.printMessage("Please enter the starting date (inclusive) in the format MM/DD/YYYY:");
    LocalDate startDate = getDateFromUser();

    view.printMessage("Please enter the ending date (inclusive) in the format MM/DD/YYYY:");
    LocalDate endDate = getDateFromUser();

    if (!endDate.isAfter(startDate)) {
      view.printMessage("Invalid input: The end date must be after the start date.");
      return;
    }

    try {
      double gain = model.getGainOverTime(startDate, endDate, ticker);
      view.printStockGain(ticker, startDate, endDate, gain);
    } catch (IOException e) {
      view.printMessage("Error while fetching data: " + e.getMessage());
    }
  }
}
