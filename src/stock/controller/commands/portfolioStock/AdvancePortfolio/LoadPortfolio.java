package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class LoadPortfolio extends StockPortfolioCommand {
  public LoadPortfolio(StockView view, PortfolioStockModel portfolioModel, Scanner scanner, String portfolio) {
    super(view, portfolioModel, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    view.printMessage("Here is the list of the portfolios that you have saved: ");
    String name = getPortfolioFileSaveName();
    try {
      view.printFileSaveName(portfolioModel.getPortfolioSaves(name));
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
    String fileSaveName = getFileSaves();
    try {
      portfolioModel.loadPortfolioSave(name, fileSaveName);
      view.printMessage("Successfully load the saved file!");
    } catch (IOException e) {
      view.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
