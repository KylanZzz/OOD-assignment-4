package stock.controller.commands.portfolioStock;

import java.util.HashMap;
import java.util.Scanner;

import stock.controller.BasicStockController;
import stock.controller.commands.CalculateAverage;
import stock.controller.commands.CalculateCrossover;
import stock.controller.commands.CalculateGain;
import stock.controller.commands.ViewPortfolios;
import stock.model.PortfolioStockModel;
import stock.view.StockView;

public class BasicPortfolioStockController extends BasicStockController {

  public BasicPortfolioStockController(StockView view, PortfolioStockModel portfolioModel, Readable in) {
    super(view, portfolioModel, in);
  }


}
