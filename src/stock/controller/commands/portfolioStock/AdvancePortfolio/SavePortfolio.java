package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class SavePortfolio extends StockPortfolioCommand {
  public SavePortfolio(StockView view, StockModel model, Scanner scanner, String portfolio) {
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
      portfolioModel.createNewPortfolioSave(portfolio);
      portfolioView.printMessage("You have successfully saved the portfolio!");
    } catch (IOException e) {
      portfolioView.printMessage(String.format("Failed to save %s", portfolio));
    }

  }
}
