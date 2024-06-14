package stock.controller.commands.portfolioStock.AdvancePortfolio;

import java.time.LocalDate;
import java.util.Scanner;

import stock.controller.commands.portfolioStock.StockPortfolioCommand;
import stock.controller.commands.stock.StockCommand;
import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.PortfolioStockView;
import stock.view.StockView;

public class DisplayPortfolio extends StockPortfolioCommand {
  public DisplayPortfolio(StockView view, StockModel model, Scanner scanner, String portfolio) {
    super(view, model, scanner, portfolio);
  }

  @Override
  public void apply() {
    PortfolioStockModel portfolioModel = (PortfolioStockModel) model;
    PortfolioStockView portfolioView = (PortfolioStockView) view;

    portfolioView.printMessage(String.format("What date would you like to know the composition of portfolio %s"
            + " at? Please enter the date in the format MM/DD/YYYY ", portfolio));
    LocalDate date = getDateFromUser();

    portfolioView.printManagePortfolioDouble(portfolioModel.getPortfolioContentsDecimal(portfolio, date), portfolio);

  }
}
