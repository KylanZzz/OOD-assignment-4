package stock.controller;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import stock.model.PortfolioStockModel;
import stock.model.StockModel;
import stock.view.FeaturesStockView;
import stock.view.StockView;

import static org.junit.Assert.*;
import static stock.controller.Interactions.inputs;
import static stock.controller.Interactions.modelLog;
import static stock.controller.Interactions.prints;

public class FeaturesStockControllerTest {
  protected final class MockFeaturesView implements FeaturesStockView {
    private final StringBuilder log;
    private Scanner scanner;

    public MockFeaturesView(StringBuilder log, Readable in) {
      this.log = log;
      this.scanner = new Scanner(in);
    }

    @Override
    public void addFeatures(PortfolioStockFeatures features) {
      while (scanner.hasNextLine()) {
        String line = scanner.nextLine();
        String[] split = line.split(":");
        String func = split[0];
        String[] param = split.length > 1 ? split[1].split(",") : new String[]{" "};
        switch (func) {
          case "createPortfolio":
            features.createPortfolio(param[0]);
            break;
          case "loadPortfolio":
            features.loadPortfolio(param[0]);
            break;
          case "choosePortfolio":
            features.choosePortfolio(param[0]);
            break;
          case "buyStock":
            features.buyStock(param[0], param[1], param[2], param[3], param[4], param[5]);
            break;
          case "sellStock":
            features.sellStock(param[0], param[1], param[2], param[3], param[4], param[5]);
            break;
          case "getComposition":
            features.getComposition(param[0], param[1], param[2], param[3], param[4], param[5]);
            break;
          case "getValue":
            features.getValue(param[0], param[1], param[2], param[3], param[4], param[5]);
            break;
          case "savePortfolio":
            features.savePortfolio(param[0]);
            break;
        }
      }
    }

    @Override
    public void displayComposition(Map<String, Double> composition) {
      log.append("displayComposition");
      for (var kv: composition.entrySet()) {
        log.append(kv.getKey()).append(kv.getValue());
      }
      log.append(System.lineSeparator());
    }

    @Override
    public void displayValue(double value) {
      log.append("displayValue").append(value).append(System.lineSeparator());
    }

    @Override
    public void displayBoughtStock(String ticker, double shares) {
      log.append("displayBoughtStock").append(ticker).append(shares).append(System.lineSeparator());
    }

    @Override
    public void displaySoldStock(String ticker, double shares) {
      log.append("displaySoldStock").append(ticker).append(shares).append(System.lineSeparator());
    }

    @Override
    public void displayPortfolios(List<String> names) {
      log.append("displayPortfolios");
      for (String name: names) {
        log.append(name);
      }
      log.append(System.lineSeparator());
    }

    @Override
    public void displayCreatedSave(String name) {
      log.append("displayCreatedSave").append(name).append(System.lineSeparator());
    }

    @Override
    public void displayCreatedPortfolio(String portfolio) {
      log.append("displayCreatedPortfolio").append(portfolio).append(System.lineSeparator());
    }

    @Override
    public void displayLoadedPortfolio(String portfolio) {
      log.append("displayLoadedPortfolio").append(portfolio).append(System.lineSeparator());
    }

    @Override
    public void displayEditPortfolio(String portfolio) {
      log.append("displayEditPortfolio").append(portfolio).append(System.lineSeparator());
    }

    @Override
    public void displayErrorMessage(String message) {
      log.append("displayErrorMessage").append(message).append(System.lineSeparator());
    }
  }


  private boolean runTest(boolean throwException, Interaction... interactions) {
    StringBuilder expectedViewLog = new StringBuilder();
    StringBuilder expectedModelLog = new StringBuilder();
    StringBuilder fakeInput = new StringBuilder();

    for (var inter : interactions) {
      inter.apply(fakeInput, expectedViewLog, expectedModelLog);
    }

    Reader in = new StringReader(fakeInput.toString());
    StringBuilder viewLog = new StringBuilder();
    StringBuilder modelLog = new StringBuilder();

    FeaturesStockView view = new MockFeaturesView(viewLog, in);
    PortfolioStockModel model = new MockPortfolioModel(modelLog, throwException);

    // Constructing a controller automatically processes input
    PortfolioStockFeatures controller = new FeaturesStockController(view, model);

    assertEquals(expectedViewLog.toString(), viewLog.toString());
    assertEquals(expectedModelLog.toString(), modelLog.toString());

    return expectedViewLog.toString().equals(viewLog.toString()) && expectedModelLog.toString()
            .equals(modelLog.toString());
  }

  private final String namelessPortfolio = "Cannot create a nameless portfolio!";
  private final String portfolioExists = "Portfolio name already exists!";


  @Test
  public void createNewPortfolioWorks() {
    assertTrue(runTest(false,
            inputs("createPortfolio:abc"),
            modelLog("createNewPortfolioABC"),
            prints("displayCreatedPortfolioABC"),
            prints("displayPortfoliosS&P500NASDAQ")
    ));
  }

  @Test
  public void createNewPortfolioHandlesInvalidInput() {
    assertTrue(runTest(false,
            inputs("createPortfolio:s&p500"),
            prints("displayErrorMessage" + portfolioExists),
            inputs("createPortfolio:  "),
            prints("displayErrorMessage" + namelessPortfolio),
            inputs("createPortfolio:"),
            prints("displayErrorMessage" + namelessPortfolio)
    ));
  }

  public void loadPortfolioWorksWithInvalidInput() {
    assertTrue(runTest(false,
            inputs("loadPortfolio:"),
            prints("displayErrorMessage")
    ));
  }
}