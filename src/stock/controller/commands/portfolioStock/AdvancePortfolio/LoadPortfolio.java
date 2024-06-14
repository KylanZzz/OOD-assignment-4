package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class LoadPortfolio extends StockPortfolioCommand {
  public LoadPortfolio(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    try {
      if (portfolioModel.getPortfolioSaves(portfolio).isEmpty()) {
        return;
      }
      portfolioView.printFileSaveName(portfolioModel.getPortfolioSaves(portfolio));

    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }

     String fileSaveName = getPortfolioFileSaveName();
    try {
      portfolioModel.loadPortfolioSave(portfolio, fileSaveName);
      portfolioView.printMessage("Successfully load the saved file!");
      portfolioView.printMessage("");

    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
