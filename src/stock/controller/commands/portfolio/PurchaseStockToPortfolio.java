package stock.controller.commands.portfolio;

import java.util.Scanner;

import stock.controller.commands.stock.AddStock;
import stock.controller.commands.stock.AddStockWithDate;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.StockView;

public class PurchaseStockToPortfolio extends PortfolioCommand {
  protected final String portfolioName;

  /**
   * Constructs a create portfolio command with a stock's
   * view, model, and source of input.
   *
   * @param view    the view of the stock program.
   * @param model   the model of the stock program.
   * @param scanner the input of the stock program.
   */
  public PurchaseStockToPortfolio(StockView view, StockModel model, Scanner scanner, String portfolioName) {
    super(view, model, scanner);
    this.portfolioName = portfolioName;
  }

  /**
   * Executes the command.
   */
  @Override
  public void apply() {
    view.printMessage("What is the name of the portfolio you would like to add the stock to?");
    String name = scanner.nextLine().toUpperCase();

    if (!model.getPortfolios().contains(name)) {
      view.printMessage("A portfolio with that name doesn't exists, create the portfolio first!");
      return;
    }
//    AddStockWithDate stockWDate = new AddStockWithDate(view, model, scanner);
//    stockWDate.apply();

//   model.addStockToPortfolio(String name, String ticker, int shares, LocalDate date);

  }
}
