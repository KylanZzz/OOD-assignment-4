package stock.controller.commands.portfoliostock.advanceportfolio;

import java.io.IOException;
import java.util.Scanner;
import stock.controller.commands.portfoliostock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Command class responsible for loading a saved stock portfolio from a file.
 * It extends the StockPortfolioCommand which provides basic command structure.
 * This class handles user interaction to load the contents of
 * a previously saved portfolio from persistent storage.
 */
public class LoadPortfolio extends StockPortfolioCommand {

  /**
   * Constructs a DisplayPortfolio command object.
   *
   * @param view     The view used to interact with the user.
   * @param model    The model used for portfolio data manipulation.
   * @param scanner  The scanner to read user input.
   * @param portfolio The name of the portfolio to display.
   */
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
      portfolioModel.loadPortfolioSave(fileSaveName);
      portfolioView.printMessage("Successfully load the saved file!");
      portfolioView.printMessage("");

    } catch (IOException e) {
      portfolioView.printMessage("Error occurred while fetching data: " + e.getMessage());
    }
  }
}
