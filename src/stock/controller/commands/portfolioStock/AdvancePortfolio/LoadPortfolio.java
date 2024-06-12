package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class LoadPortfolio extends StockPortfolioCommand {
  public LoadPortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(portfolioView, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    try {
      portfolioView.printFileSaveName(portfolioModel.getPortfolioSaves(portfolio));

    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
     String fileSaveName = getPortfolioFileSaveName();
    try {
      portfolioModel.loadPortfolioSave(portfolio, fileSaveName);
      portfolioView.printMessage("Successfully load the saved file!");
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
