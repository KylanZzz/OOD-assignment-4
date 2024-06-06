package stock;

import java.io.InputStreamReader;

import stock.controller.BasicStockController;
import stock.controller.StockController;
import stock.model.AlphaVantageDataSource;
import stock.model.BasicStockModel;
import stock.model.StockModel;
import stock.view.BasicStockView;
import stock.view.StockView;

/**
 * The {@code StockProgram} class serves as the entry point for the stock application.
 * It initializes the components necessary for the MVC (Model-View-Controller) architecture
 * and starts the application.
 */
public class StockProgram {

  /**
   * The main method that sets up the stock market application. It creates instances
   * of the view, model, and controller, and then starts the application.
   *
   * The method initializes:
   * - A {@link StockView} that outputs to {@code System.out}, represented by a {@code BasicStockView}.
   * - A {@link StockModel} that can be switched between a CSV data source and an AlphaVantage data source.
   *   This example uses {@code AlphaVantageDataSource} for live stock data.
   * - A {@link StockController} that manages the interaction between the model and view,
   *   taking input from {@code System.in}.
   *
   * @param args the command-line arguments, not used in this application.
   */
  public static void main(String[] args) {

    StockView view = new BasicStockView(System.out);
    // StockModel model = new BasicStockModel(new CSVDataSource("res/CSVData"));
    StockModel model = new BasicStockModel(new AlphaVantageDataSource());
    StockController controller = new BasicStockController(view, model, new InputStreamReader(System.in));
    controller.run();
  }
}
