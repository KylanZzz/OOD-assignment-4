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
    portfolioView.printMessage("Here is the list of the portfolios that you have saved: ");
    String name = getPortfolioFileSaveName();
    try {
      portfolioView.printFileSaveName(portfolioModel.getPortfolioSaves(name));
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
    String fileSaveName = getFileSaves();
    try {
      portfolioModel.loadPortfolioSave(name, fileSaveName);
      portfolioView.printMessage("Successfully load the saved file!");
    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
