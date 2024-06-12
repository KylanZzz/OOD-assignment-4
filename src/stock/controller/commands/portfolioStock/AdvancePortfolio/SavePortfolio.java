package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.util.Scanner;

import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class SavePortfolio extends StockPortfolioCommand {
  public SavePortfolio(StockView view, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(view, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    view.printMessage("Please input the name of the portfolio that you want to save: ");
    String portfolioName = getPortfolioNameFromUser();

    try {
      portfolioModel.createNewPortfolioSave(portfolioName);
      view.printMessage("You have successfully saved the portfolio!");
    } catch (IOException e) {
      view.printMessage(String.format("Failed to save %s", portfolioName));
    }

  }
}
