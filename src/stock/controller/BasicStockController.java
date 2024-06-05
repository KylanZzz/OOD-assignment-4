package stock.controller;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Scanner;

import stock.controller.commands.CalculateAverage;
import stock.controller.commands.CalculateCrossover;
import stock.controller.commands.CalculateGain;
import stock.controller.commands.Command;
import stock.controller.commands.ViewPortfolios;
import stock.model.AlphaVantageDataSource;
import stock.model.BasicStockModel;
import stock.model.StockModel;
import stock.view.BasicMenuOptions;
import stock.view.BasicStockView;
import stock.view.StockView;
import java.util.Map;

public class BasicStockController implements StockController {

  private final StockView view;
  private final StockModel model;
  private final Scanner scanner;
  private final Map<String, Command> commands;

  public BasicStockController(StockView view, StockModel model, Readable in) {
    scanner = new Scanner(in);
    this.view = view;
    this.model = model;
    commands = new HashMap<>();
    initializeCommands();
  }

  protected void initializeCommands() {
    commands.put("1", new CalculateGain(view, model, scanner));
    commands.put("2", new CalculateAverage(view, model, scanner));
    commands.put("3", new CalculateCrossover(view, model, scanner));
    commands.put("4", new ViewPortfolios(view, model, scanner));
  }

  @Override
  public void run() {
    String choice = "";

    while (!choice.equals(BasicMenuOptions.exitKeyword())) {
      view.printMainMenu();
      choice = scanner.nextLine();

      if (commands.containsKey(choice)) {
        commands.get(choice).apply();
      } else if (!choice.equals(BasicMenuOptions.exitKeyword())){
        view.printMessage("Invalid input. Please enter a valid choice (a number from 1 through "
                + BasicMenuOptions.mainMenu().size() + ") or " + BasicMenuOptions.exitKeyword()
        + " to exit the application.");
      }
    }
  }

  public static void main(String[] args) {

    StockView view = new BasicStockView(System.out);
//    StockModel model = new BasicStockModel(new CSVDataSource("res/CSVData"));
    StockModel model = new BasicStockModel(new AlphaVantageDataSource());
    StockController controller = new BasicStockController(view, model, new InputStreamReader(System.in));
    controller.run();
  }
}
