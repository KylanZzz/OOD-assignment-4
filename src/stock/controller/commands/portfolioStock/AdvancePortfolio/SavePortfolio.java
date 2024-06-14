package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.io.IOException;
import java.util.Scanner;
import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Command class responsible for saving the state of a specified stock portfolio.
 * This command allows the user to save the current state of a portfolio,
 * which can be reloaded later.
 */
public class SavePortfolio extends StockPortfolioCommand {
  /**
   * Constructs a DisplayPortfolio command object.
   *
   * @param view     The view used to interact with the user.
   * @param model    The model used for portfolio data manipulation.
   * @param scanner  The scanner to read user input.
   * @param portfolio The name of the portfolio to display.
   */
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
      portfolioView.printMessage("");

    } catch (IOException e) {
      portfolioView.printMessage(String.format("Failed to save %s", portfolio));
    }
  }
}
