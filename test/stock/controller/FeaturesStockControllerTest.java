package stock.controller;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

import stock.model.PortfolioStockModel;
import stock.view.FeaturesStockView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static stock.controller.Interactions.inputs;
import static stock.controller.Interactions.modelLog;
import static stock.controller.Interactions.prints;

/**
 * A test for features stock controller. It tests whether information is correctly passed from
 * view to controller to model and then back to view. It also tests whether the controller properly
 * handles invalid inputs.
 */
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
      for (var key: composition.keySet().stream().sorted().collect(Collectors.toList())) {
        log.append(key).append(composition.get(key));
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
  private final String noFileName = "Please choose a file path to load!";
  private final String portfolioDoesntExist = "Portfolio with that name does not exist.";
  private final String createOrLoadPort = "Please create a new portfolio or load one from a save "
          + "first.";
  private final String emptyField = "Make sure all fields are filled out!";
  private final String notAnInt = "Number of shares must be an Integer.";
  private final String notPositive = "Number of shares must be a positive.";
  private final String tickerDoesntExist = "Ticker does not exist.";
  private final String dateNotInteger = "Dates entered must be Integers only!";
  private final String dateTooLate = "Date must be before today!";
  private final String invalidDate = "Invalid date! Please enter a positive, valid date.";
  private final String notEnoughShares = "Not enough shares bought to sell!";


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

  @Test
  public void loadPortfolioWorksWithNotYetCreatedPortfolio() {
    assertTrue(runTest(false,
            inputs("loadPortfolio:DD"),
            prints("displayLoadedPortfolioDD"),
            prints("displayPortfoliosS&P500NASDAQ"),
            modelLog("loadPortfolioSaveDD")
    ));
  }

  @Test
  public void loadPortfolioWorksWithOverwriteExistingPortfolio() {
    assertTrue(runTest(false,
            inputs("loadPortfolio:S&P500_111"),
            modelLog("loadPortfolioSaveS&P500_111"),
            prints("displayLoadedPortfolioS&P500_111"),
            prints("displayPortfoliosS&P500NASDAQ")
    ));
  }

  @Test
  public void loadPortfolioWorksWithInvalidInput() {
    assertTrue(runTest(false,
            inputs("loadPortfolio:   "),
            prints("displayErrorMessage" + noFileName)
    ));
  }

  @Test
  public void loadPortfolioWorksWithIOError() {
    assertTrue(runTest(true,
            inputs("loadPortfolio:DDD"),
            prints("displayErrorMessageError while loading save: loadPortfolioSaveIOExceptionMessage")
    ));
  }

  @Test
  public void choosePortfolioWorksWhenNameExists() {
    assertTrue(runTest(false,
            inputs("choosePortfolio:S&P500"),
            prints("displayEditPortfolioS&P500")
    ));
  }

  @Test
  public void choosePortfolioWorksWhenNameDoesntExists() {
    assertTrue(runTest(false,
            inputs("choosePortfolio:AAAAA"),
            prints("displayErrorMessage" + createOrLoadPort)
    ));
  }

  @Test
  public void choosePortfolioWorksWithBlankName() {
    assertTrue(runTest(false,
            inputs("choosePortfolio:    "),
            prints("displayErrorMessage" + createOrLoadPort),
            inputs("choosePortfolio:"),
            prints("displayErrorMessage" + createOrLoadPort)
    ));
  }

  @Test
  public void buyStockWorksWithInvalidInput() {
    assertTrue(runTest(false,
            inputs("buyStock:ZZZZZZ,AAPL,10,4,20,2020"),
            prints("displayErrorMessage" + portfolioDoesntExist),

            inputs("buyStock: ,AAPL,10,4,20,2020"),
            prints("displayErrorMessage" + portfolioDoesntExist),

            inputs("buyStock:S&P500,AAPL, ,4,20,2020"),
            prints("displayErrorMessage" + emptyField),

            inputs("buyStock:S&P500,AAPL,10, ,20,2020"),
            prints("displayErrorMessage" + emptyField),

            inputs("buyStock:S&P500,AAPL,10,4, ,2020"),
            prints("displayErrorMessage" + emptyField),

            inputs("buyStock:S&P500,AAPL,10A,4,20,2020"),
            prints("displayErrorMessage" + notAnInt),

            inputs("buyStock:S&P500,AAPL,10.0,4,20,2020"),
            prints("displayErrorMessage" + notAnInt),

            inputs("buyStock:S&P500,AAPL,-10,4,20,2020"),
            prints("displayErrorMessage" + notPositive),

            inputs("buyStock:S&P500,SDFGHJK,10,4,20,2020"),
            prints("displayErrorMessage" + tickerDoesntExist),

            inputs("buyStock:S&P500,AAPL,10,4,20,2020"),
            modelLog("addStockToPortfolioS&P500AAPL102020-04-20"),
            prints("displayBoughtStockAAPL10.0")
    ));
  }

  @Test
  public void buyStockHandlesException() {
    assertTrue(runTest(true,
            inputs("buyStock:S&P500,AAPL,10,4,20,2020"),
            prints("displayErrorMessagebuyIOExceptionMessage")
    ));
  }

  @Test
  public void buyStockWorksWithMultiple() {
    assertTrue(runTest(false,
            inputs("buyStock:S&P500,AApl,10,4,20,2020"), //Case Insensitive
            modelLog("addStockToPortfolioS&P500AAPL102020-04-20"),
            prints("displayBoughtStockAAPL10.0"),

            inputs("buyStock:S&P500,AAPL,10,4,20,2020"), //Case Insensitive
            modelLog("addStockToPortfolioS&P500AAPL102020-04-20"),
            prints("displayBoughtStockAAPL10.0"),

            inputs("buyStock:S&P500,amzn,500,3,25,2022"), //Case Insensitive
            modelLog("addStockToPortfolioS&P500AMZN5002022-03-25"),
            prints("displayBoughtStockAMZN500.0")
    ));
  }

  @Test
  public void controllerHandlesInvalidDate() {
    LocalDate tomorrow = LocalDate.now().plusDays(1);

    assertTrue(runTest(false,
            inputs("buyStock:S&P500,AAPL,10,4,-20,2020"),
            prints("displayErrorMessage" + invalidDate),

            inputs("buyStock:S&P500,AAPL,10,4,200,2020"),
            prints("displayErrorMessage" + invalidDate),

            inputs("buyStock:S&P500,AAPL,10,4,20.0,2020"),
            prints("displayErrorMessage" + dateNotInteger),

            inputs("buyStock:S&P500,AAPL,10,4,A20,2020"),
            prints("displayErrorMessage" + dateNotInteger),

            inputs(String.format("buyStock:S&P500,AAPL,10,%d,%d,%d", tomorrow.getMonthValue(),
                    tomorrow.getDayOfMonth(), tomorrow.getYear())),
            prints("displayErrorMessage" + dateTooLate)
    ));
  }

  @Test
  public void sellStockWorksWithInvalidInput() {
    assertTrue(runTest(false,
            inputs("buyStock:S&P500,AAPL,10,4,20,2019"), //Buy stock so we have enough to sell
            modelLog("addStockToPortfolioS&P500AAPL102019-04-20"),
            prints("displayBoughtStockAAPL10.0"),

            inputs("sellStock:ZZZZZZ,AAPL,10,4,20,2020"),
            prints("displayErrorMessage" + portfolioDoesntExist),

            inputs("sellStock: ,AAPL,10,4,20,2020"),
            prints("displayErrorMessage" + portfolioDoesntExist),

            inputs("sellStock:S&P500,AAPL, ,4,20,2020"),
            prints("displayErrorMessage" + emptyField),

            inputs("sellStock:S&P500,AAPL,10, ,20,2020"),
            prints("displayErrorMessage" + emptyField),

            inputs("sellStock:S&P500,AAPL,10,4, ,2020"),
            prints("displayErrorMessage" + emptyField),

            inputs("sellStock:S&P500,AAPL,10A,4,20,2020"),
            prints("displayErrorMessage" + notAnInt),

            inputs("sellStock:S&P500,AAPL,10.0,4,20,2020"),
            prints("displayErrorMessage" + notAnInt),

            inputs("sellStock:S&P500,AAPL,-10,4,20,2020"),
            prints("displayErrorMessage" + notPositive),

            inputs("sellStock:S&P500,SDFGHJK,10,4,20,2020"),
            prints("displayErrorMessage" + tickerDoesntExist),

            inputs("sellStock:S&P500,AAPL,5,4,20,2020"),
            modelLog("sellStockFromPortfolioS&P500AAPL52020-04-20"),
            prints("displaySoldStockAAPL5.0")
    ));
  }

  @Test
  public void sellStockWithoutEnoughShares() {
    assertTrue(runTest(false,
            inputs("sellStock:S&P500,AAPL,10,4,20,2020"), // Trying to sell before buying
            prints("displayErrorMessage" + notEnoughShares)
    ));

    assertTrue(runTest(false,
            inputs("buyStock:S&P500,AAPL,10,4,20,2019"), //Buy stock
            modelLog("addStockToPortfolioS&P500AAPL102019-04-20"),
            prints("displayBoughtStockAAPL10.0"),

            inputs("sellStock:S&P500,AAPL,20,4,20,2020"), // not enough shares, 10 < 20
            prints("displayErrorMessage" + notEnoughShares)
    ));

    assertTrue(runTest(false,
            inputs("buyStock:S&P500,AAPL,20,4,20,2021"), // Buy stock
            modelLog("addStockToPortfolioS&P500AAPL202021-04-20"),
            prints("displayBoughtStockAAPL20.0"),

            inputs("sellStock:S&P500,AAPL,10,4,20,2020"), // Trying to sell before buy date
            prints("displayErrorMessage" + notEnoughShares)
    ));
  }

  @Test
  public void getCompositionHandlesInvalidInput() {
    assertTrue(runTest(false,
            inputs("getComposition:AAAAAA,4,20,2024,20,AAPL"), // Portfolio does not exist
            prints("displayErrorMessagePortfolio does not exist.")
    ));

    assertTrue(runTest(false,
            inputs("getComposition:   ,4,20,2024,20,AAPL"), // Portfolio name is empty
            prints("displayErrorMessagePortfolio cannot be an empty string!")
    ));

    assertTrue(runTest(false,
            inputs("getComposition:S&P500,4,20,2024,20,AAPL"), // Portfolio name is empty
            prints("displayCompositionAAPL5.5AMZN10.5NFLX15.5")
    ));
  }

  @Test
  public void getCompositionIgnoresTickerAndShares() {
    assertTrue(runTest(false,
            inputs("getComposition:S&P500,4,20,2024,20,YFCGVABLOIUJFHAI"), // Ticker is ignored
            prints("displayCompositionAAPL5.5AMZN10.5NFLX15.5")
    ));

    assertTrue(runTest(false,
            inputs("getComposition:S&P500,4,20,2024,09a87f90a(*Y(*G,AAPl"), // Shares ignored
            prints("displayCompositionAAPL5.5AMZN10.5NFLX15.5")
    ));
  }

  @Test
  public void getCompositionWorks() {
    assertTrue(runTest(false,
            inputs("getComposition:S&P500,4,20,2024, , "),
            prints("displayCompositionAAPL5.5AMZN10.5NFLX15.5")
    ));
  }

  @Test
  public void getValueHandlesInvalidInput() {
    assertTrue(runTest(false,
            inputs("getValue:AAAAAA,4,20,2024,20,AAPL"), // Portfolio does not exist
            prints("displayErrorMessagePortfolio does not exist.")
    ));

    assertTrue(runTest(false,
            inputs("getValue:   ,4,20,2024,20,AAPL"), // Portfolio name is empty
            prints("displayErrorMessagePortfolio cannot be an empty string!")
    ));

    assertTrue(runTest(false,
            inputs("getValue:S&P500,4,20,2024,20,AAPL"), // Portfolio name is empty
            prints("displayValue400.0")
    ));
  }

  @Test
  public void getValueIgnoresTickerAndShares() {
    assertTrue(runTest(false,
            inputs("getValue:S&P500,4,20,2024,20,YFCGVABLOIUJFHAI"), // Ticker is ignored
            prints("displayValue400.0")
    ));

    assertTrue(runTest(false,
            inputs("getValue:S&P500,4,20,2024,09a87f90a(*Y(*G,AAPl"), // Shares ignored
            prints("displayValue400.0")
    ));
  }

  @Test
  public void getValueWorks() {
    assertTrue(runTest(false,
            inputs("getValue:S&P500,4,20,2024, , "),
            prints("displayValue400.0")
    ));
  }

  @Test
  public void getValueHandlesException() {
    assertTrue(runTest(true,
            inputs("getValue:S&P500,10,4,20,2020,AAPL"),
            prints("displayErrorMessageIO Error occurred: portfolioValueIOExceptionMessage"),
            modelLog("getPortfolioValueIOException")
    ));
  }

  @Test
  public void savePortfolioHandlesException() {
    assertTrue(runTest(true,
            inputs("savePortfolio:S&P500"),
            prints("displayErrorMessagecreateNewPortfolioSaveIOExceptionMessage")
    ));
  }

  @Test
  public void savePortfolioHandlesNonexistentPortfolio() {
    assertTrue(runTest(false,
            inputs("savePortfolio:SDFGHJDFGHJ"),
            prints("displayErrorMessage" + "Portfolio does not exist.")
    ));
  }

  @Test
  public void savePortfolioWorks() {
    assertTrue(runTest(false,
            inputs("savePortfolio:S&P500"),
            modelLog("createNewPortfolioSaveS&P500"),
            prints("displayCreatedSaveS&P500")
    ));
  }

}