package stock.controller.commands.portfolio;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.AdvancePortfolio.DisplayPortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.DistributionWithDate;
import stock.controller.commands.portfolioStock.AdvancePortfolio.LoadPortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.PerformanceOverTime;
import stock.controller.commands.portfolioStock.AdvancePortfolio.PortfolioValueWithDate;
import stock.controller.commands.portfolioStock.AdvancePortfolio.PurchaseStockWithDate;
import stock.controller.commands.portfolioStock.AdvancePortfolio.RebalancePortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.SavePortfolio;
import stock.controller.commands.portfolioStock.AdvancePortfolio.SellStockWithDate;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.PortfolioStockView;
import stock.view.StockView;

/**
 * Command that prompts the user for how they want
 * to edit a portfolio.
 */
public class EditPortfolio extends PortfolioCommand {
  protected final String portfolioName;
  private final Map<String, StockCommand> commands;

  /**
   * Constructs a new edit portfolio command.
   *
   * @param view          the view of the stock program.
   * @param model         the model of the stock program.
   * @param scanner       the input of the stock program.
   * @param portfolioName name of the portfolio to edit.
   */
  public EditPortfolio(StockView view, StockModel model, Scanner scanner, String portfolioName) {
    super(view, model, scanner);
    this.portfolioName = portfolioName;
    commands = new HashMap<>();
  }

  /**
   * Initializes different commands that can be performed on the portfolio.
   */
  protected void initializeCommands() {
    // commands.put("1", new PortfolioValue(view, model, scanner, portfolioName));
    // commands.put("2", new AddStock(view, model, scanner, portfolioName));
    // commands.put("3", new RemoveStock(view, model, scanner, portfolioName));
    commands.put("1", new PurchaseStockWithDate(view, model, scanner, portfolioName));
    commands.put("2", new SellStockWithDate(view, model, scanner, portfolioName));
    commands.put("3", new PortfolioValueWithDate(view, model, scanner, portfolioName));
    commands.put("4", new DistributionWithDate(view, model, scanner, portfolioName));
    commands.put("5", new SavePortfolio(view, model, scanner, portfolioName));
    commands.put("6", new LoadPortfolio(view, model, scanner, portfolioName));
    commands.put("7", new RebalancePortfolio(view, model, scanner, portfolioName));
    commands.put("8", new PerformanceOverTime(view, model, scanner, portfolioName));
    commands.put("9", new DisplayPortfolio(view, model, scanner, portfolioName));
  }

  /**
   * Repeatedly asks the user for the command they want to perform on the
   * portfolio until the user inputs the exit keyword.
   */
  @Override
  public void apply() {
    initializeCommands();
    String choice = "";

    while (!choice.equals(BasicMenuOptions.exitKeyword())) {
//      if (!(model instanceof PortfolioStockModel)) {
//        view.printMessage("model is not compatible.");
//        return;
//      }
      PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
      PortfolioStockView portfolioView = (PortfolioStockView) view;


      portfolioView.printPortfolioOption();
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicMenuOptions.exitKeyword())) {
        portfolioView.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicMenuOptions.managePortfolio().size() + ") or "
                + BasicMenuOptions.exitKeyword()
                + " to go back.");
      }
    }
  }
}
