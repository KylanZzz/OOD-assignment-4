package stock;

import java.io.InputStreamReader;

import stock.controller.BasicStockController;
import stock.controller.StockController;
import stock.model.AlphaVantageDataSource;
import stock.model.PortfolioStockModelImpl;
import stock.model.StockModel;
import stock.view.BasicPortfolioStockView;
import stock.view.StockView;

/**
 * The {@code StockProgram} class serves as the entry point for the stock application.
 * It initializes the components necessary for the MVC (Model-View-Controller) architecture
 * and starts the application.
 */
public class StockProgram {

  /**
   * The main method to run the stock portfolio application.
   *
   * <p>This method initializes the view, model, and controller components of the application
   * and starts the application by calling the controller's run method.</p>
   *
   * <p>The view is implemented by BasicStockView, which outputs to the system output stream.
   * The model is implemented by BasicStockModel, which can be initialized,
   * with different data sources.
   * Currently, it is initialized with AlphaVantageDataSource.</p>
   *
   * @param args command line arguments (not used).
   */
  public static void main(String[] args) {

    //    StockView view = new BasicPortfolioStockView(System.out) {
    //    };
    //    StockView view= new BasicPortfolioStockView(System.out);
    //    StockModel model = new BasicStockModel(new AlphaVantageDataSource());
    //    StockController controller = new BasicStockController(view, model,
    //            new InputStreamReader(System.in));
    //
    //     StockModel model = new BasicStockModel(new CSVDataSource("res/CSVData"));
    StockView view = new BasicPortfolioStockView(System.out);
    StockModel model = new PortfolioStockModelImpl(new AlphaVantageDataSource(), "res/portfolio");
    StockController controller = new BasicStockController(view, model,
            new InputStreamReader(System.in));

    controller.run();
  }
}
