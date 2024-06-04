package stock.controller.commands;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Map;

import stock.controller.commands.Command;
import stock.controller.commands.portfolio.CreatePortfolio;
import stock.controller.commands.portfolio.DeletePortfolio;
import stock.controller.commands.portfolio.EditPortfolio;
import stock.controller.commands.portfolio.PortfolioCommand;
import stock.controller.commands.portfolio.RenamePortfolio;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.StockView;

public class ViewPortfolios extends Command {

  private final Map<String, PortfolioCommand> commands;

  public ViewPortfolios(StockView view, StockModel model, Scanner scanner) {
    super(view, model, scanner);
    commands = new HashMap<>();
  }

  private void initializeCommands() {
    commands.put("1", new CreatePortfolio(view, model, scanner));
    commands.put("2", new DeletePortfolio(view, model, scanner));
    commands.put("3", new RenamePortfolio(view, model, scanner));

    int numOptions = BasicMenuOptions.viewPortfolios().size();
    for (int i = 0; i < model.getPortfolios().size(); i++) {
      commands.put(Integer.toString(i + numOptions + 1),
              new EditPortfolio(view, model, scanner, model.getPortfolios().get(i)));
    }
  }

  @Override
  public void apply() {
    String choice = "";

    while (!choice.equals(BasicMenuOptions.exitKeyword())) {
      initializeCommands();
      view.printViewPortfolios(model.getPortfolios());
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicMenuOptions.exitKeyword())) {
        view.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicMenuOptions.viewPortfolios().size() + ") or " + BasicMenuOptions.exitKeyword()
                + " to go back.");
      }
    }
  }
}
