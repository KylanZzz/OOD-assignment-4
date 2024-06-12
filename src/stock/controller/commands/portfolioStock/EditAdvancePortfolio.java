package stock.controller.commands.portfolioStock;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.portfolio.EditPortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.AddStock;
import stock.controller.commands.portfolioStock.AdvancePortfolio.DistributionWithDate;
import stock.controller.commands.portfolioStock.AdvancePortfolio.LoadPortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.PerformanceOverTime;
import stock.controller.commands.portfolioStock.AdvancePortfolio.PortfolioValue;
import stock.controller.commands.portfolioStock.AdvancePortfolio.PortfolioValueWithDate;
import stock.controller.commands.portfolioStock.AdvancePortfolio.PurchaseStockWithDate;
import stock.controller.commands.portfolioStock.AdvancePortfolio.RebalancePortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.RemoveStock;
import stock.controller.commands.portfolioStock.AdvancePortfolio.SavePortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.SellStockWithDate;
import stock.controller.commands.portfolioStock.AdvancePortfolio.StockPortfolioCommand;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.view.BasicMenuOptions;
import stock.view.BasicPortfolioMenuOptions;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class EditAdvancePortfolio extends PortfolioCommand {
  protected final String portfolioName;
  private final Map<String, StockPortfolioCommand> commands;

  /**
   * Constructs a new edit portfolio command.
   *
   * @param portfolioView          the view of the stock program.
   * @param portfolioModel         the model of the stock program.
   * @param scanner       the input of the stock program.
   * @param portfolioName name of the portfolio to edit.
   */
  public EditAdvancePortfolio(PortfolioStockView portfolioView, PortfolioStockModel portfolioModel, Scanner scanner, String portfolioName) {
    super(portfolioView, portfolioModel, scanner);
    this.portfolioName = portfolioName;
    commands = new HashMap<>();
  }
  /**
   * Initializes different commands that can be performed on the portfolio.
   */
  protected void initializeCommands() {
    commands.put("1", new PortfolioValue(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("2", new AddStock(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("3", new RemoveStock(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("4", new PurchaseStockWithDate(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("5", new SellStockWithDate(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("6", new PortfolioValueWithDate(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("7", new DistributionWithDate(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("8", new SavePortfolio(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("9", new LoadPortfolio(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("10", new RebalancePortfolio(portfolioView, portfolioModel, scanner, portfolioName));
    commands.put("11", new PerformanceOverTime(portfolioView, portfolioModel, scanner, portfolioName));
  }

  /**
   * Repeatedly asks the user for the command they want to perform on the
   * portfolio until the user inputs the exit keyword.
   */
  @Override
  public void apply() {
    initializeCommands();
    String choice = "";

    while (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {
      portfolioView.printMessage("Enter the date that you add the stocks: ");
      LocalDate date = getDateFromUser();

      portfolioView.printManagePortfolioDouble(portfolioModel.getPortfolioContentsDecimal(portfolioName, date), portfolioName, date);
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicPortfolioMenuOptions.exitKeyword())) {
        portfolioView.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicPortfolioMenuOptions.managePortfolio().size() + ") or "
                + BasicPortfolioMenuOptions.exitKeyword()
                + " to go back.");
      }
    }
  }

}
