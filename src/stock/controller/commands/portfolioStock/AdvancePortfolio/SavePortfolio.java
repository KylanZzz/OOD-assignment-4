package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.util.Scanner;

import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class SavePortfolio extends StockPortfolioCommand {
  public SavePortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    portfolioView.printMessage("Please input the name of the portfolio that you want to save: ");
    String portfolioName = getPortfolioNameFromUser();

    try {
      portfolioModel.createNewPortfolioSave(portfolioName);
      portfolioView.printMessage("You have successfully saved the portfolio!");
    } catch (IOException e) {
      portfolioView.printMessage(String.format("Failed to save %s", portfolioName));
    }

  }
}
