package stock.controller.commands.portfolio;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import stock.controller.commands.stock.AddStock;
import stock.controller.commands.stock.PortfolioValue;
import stock.controller.commands.stock.RemoveStock;
import stock.controller.commands.stock.StockCommand;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.StockView;

public class EditPortfolio extends PortfolioCommand {
  protected final String portfolioName;
  private final Map<String, StockCommand> commands;

  public EditPortfolio(StockView view, StockModel model, Scanner scanner, String portfolioName) {
    super(view, model, scanner);
    this.portfolioName = portfolioName;
    commands = new HashMap<>();
  }

  private void initializeCommands() {
    commands.put("1", new PortfolioValue(view, model, scanner, portfolioName));
    commands.put("2", new AddStock(view, model, scanner, portfolioName));
    commands.put("3", new RemoveStock(view, model, scanner, portfolioName));
  }

  @Override
  public void apply() {
    initializeCommands();
    String choice = "";

    while (!choice.equals(BasicMenuOptions.EXIT_KEYWORD)) {
      view.printManagePortfolio(model.getPortfolioContents(portfolioName), portfolioName);
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicMenuOptions.EXIT_KEYWORD)){
        view.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicMenuOptions.managePortfolio().size() + ") or " + BasicMenuOptions.EXIT_KEYWORD
                + " to go back.");
      }
    }
  }
}
