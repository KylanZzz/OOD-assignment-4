package stock.controller;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import stock.model.AlphaVantageDataSource;
import stock.model.BasicStockModel;
import stock.model.DataSource;
import stock.model.StockModel;
import stock.view.BasicStockView;
import stock.view.StockView;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static stock.controller.Interactions.inputs;
import static stock.controller.Interactions.prints;

/**
 * Test for the BasicStock program that tests the entire MVC
 * architecture by simulating user interaction.
 */
public class BasicStockControllerIntegrationTest {

  private boolean runTest(Interaction... interactions) {
    StringBuilder expectedViewLog = new StringBuilder();
    StringBuilder fakeInput = new StringBuilder();

    for (var inter : interactions) {
      inter.apply(fakeInput, expectedViewLog, new StringBuilder());
    }

    Reader in = new StringReader(fakeInput.toString());
    StringBuilder viewLog = new StringBuilder();
    DataSource dataSource = new AlphaVantageDataSource();
    StockModel model = new BasicStockModel(dataSource);
    StockView view = new BasicStockView(viewLog);
    StockController controller = new BasicStockController(view, model, in);
    controller.run();

    return expectedViewLog.toString().equals(viewLog.toString());
  }

  String mainMenu =
          "Please type the number that corresponds with the choice you would like to pick, or "
                  + "type EXIT to return/exit\n"
                  + "1. Get the gain/loss of stock over period of time\n"
                  + "2. Get x-day moving average of a stock\n"
                  + "3. Get x-day crossovers for a stock\n"
                  + "4. Manage portfolios";
  String managePortfoliosMenu =
          "Please type the number that corresponds with the choice you would like to pick, or "
                  + "type EXIT to return/exit\n"
                  + "1. Create new portfolio\n"
                  + "2. Delete portfolio\n"
                  + "3. Rename portfolio";
  String tickerPrompt = "Please enter the ticker of the stock that you would like to know about:";
  String startDatePrompt = "Please enter the starting date (inclusive) in the format MM/DD/YYYY:";
  String endDatePrompt = "Please enter the ending date (inclusive) in the format MM/DD/YYYY:";
  String tickerIncorrect = "That stock does not exist! Please try again.";
  String invalidDateFormat = "Incorrect format: Please enter the date in the format MM/DD/YYYY.";
  String invalidInputInteger = "Invalid input: not an integer, please try again.";
  String invalidDate = "Invalid date: Please enter a valid date.";
  String futureDateError =
          "Invalid date: Date has not passed yet, please enter a "
                  + "date before or equal to today.";
  String invalidInputMessage =
          "Invalid input. Please enter a valid choice (a number from 1 through 4) "
                  + "or EXIT to exit the application.";
  String daysPrompt = "Please enter the number of days.";
  String datePrompt = "Please enter the ending date in the format MM/DD/YYYY:";
  String portfolioNamePrompt = "What is the name of the portfolio you would like to create?";
  String portfolioCreatedMessage = "Successfully created portfolio ";
  String duplicatePortfolioMessage = "A portfolio with that name already exists!";
  String portfolioDeletedMessage = "Successfully deleted portfolio ";
  String sharesPrompt =
          "Please enter the number of shares you would like to purchase (you cannot buy "
                  + "fractional number of stocks): ";
  String viewEditPortfolioMenu =
          "Please type the number that corresponds with the choice you would like to pick, or "
                  + "type EXIT to return/exit\n"
                  + "1. Calculate portfolio value\n"
                  + "2. Add stock to portfolio\n"
                  + "3. Remove stock from portfolio";
  String removeStockPrompt =
          "Please enter the ticker of the stock that you would like to remove from portfolio ";
  String portfolioValuePrompt =
          "What date would you like to know the value of portfolio NASDAQ at? Please enter the "
                  + "date in the format MM/DD/YYYY.";
  String portfolioValuePromptSMP =
          "What date would you like to know the value of portfolio S&P500 at? Please enter the "
                  + "date in the format MM/DD/YYYY.";
  String deletePortfolioPrompt = "What portfolio would you like to delete?";
  String portfolioDoesNotExistMessage = "A portfolio with that name does not exist!";
  String renamePortfolioPrompt = "What would you like to rename this portfolio to?";

  @Test
  public void programRunsAndExits() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void getGainWorksForOne() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("4/20/2013"),

            prints(endDatePrompt),
            inputs("4/20/2024"),

            prints("The gain for stock AAPL from 2013-04-20 to 2024-04-20 was $152.56.\n"),
            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void getGainWorksForMultiple() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("4/20/2013"),

            prints(endDatePrompt),
            inputs("4/20/2024"),

            prints("The gain for stock AAPL from 2013-04-20 to 2024-04-20 was $152.56.\n"),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AMZN"),

            prints(startDatePrompt),
            inputs("1/1/2018"),

            prints(endDatePrompt),
            inputs("3/20/2024"),

            prints("The gain for stock AMZN from 2018-01-01 to 2024-03-20 was $118.70.\n"),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("4/20/2013"),

            prints(endDatePrompt),
            inputs("4/20/2024"),

            prints("The gain for stock AAPL from 2013-04-20 to 2024-04-20 was $152.56.\n"),
            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void inputHandlesIncorrectTicker() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAAAAAA"),

            prints(tickerIncorrect),
            inputs("LMNZ"),

            prints(tickerIncorrect),
            inputs("FGHJ"),

            prints(tickerIncorrect),
            inputs("TYUIO"),

            prints(tickerIncorrect),
            inputs(")(HDA"),

            prints(tickerIncorrect),
            inputs("*(Y(*HY"),

            prints(tickerIncorrect),
            inputs("5678"),

            prints(tickerIncorrect),
            inputs("87yhgbn"),

            prints(tickerIncorrect),
            inputs("567yh"),

            prints(tickerIncorrect),
            inputs("rtfghy"),

            prints(tickerIncorrect),
            inputs("apple"),

            prints(tickerIncorrect),
            inputs("google"),

            prints(tickerIncorrect),
            inputs("amazon"),

            prints(tickerIncorrect),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("04/20/2005"),

            prints(endDatePrompt),
            inputs("04/20/2013"),

            prints("The gain for stock AAPL from 2005-04-20 to 2013-04-20 was $10.89.\n"),
            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void inputTickerIsCaseInsensitive() {
    String gainMessage = "The gain for stock AAPL from 2005-04-20 to 2013-04-20 was $10.89.\n";

    assertTrue(runTest(
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("aAPl"),

            prints(startDatePrompt),
            inputs("04/20/2005"),

            prints(endDatePrompt),
            inputs("04/20/2013"),

            prints(gainMessage),
            prints(mainMenu),
            inputs("EXIT")
    ));

    assertTrue(runTest(
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("aapl"),

            prints(startDatePrompt),
            inputs("04/20/2005"),

            prints(endDatePrompt),
            inputs("04/20/2013"),

            prints(gainMessage),
            prints(mainMenu),
            inputs("EXIT")
    ));

  }

  @Test
  public void inputHandlesIncorrectDate() {
    LocalDate today = LocalDate.now();
    String todayString = today.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    String tomorrowString = today.plusDays(1).format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

    assertTrue(runTest(
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("04/20/2024/2"),

            prints(invalidDateFormat),
            inputs("/1/04/20/2024"),

            prints(invalidDateFormat),
            inputs("///4202024"),

            prints(invalidDateFormat),
            inputs("/420/2024/"),

            prints(invalidInputInteger),
            inputs("a/3/2024"),

            prints(invalidInputInteger),
            inputs("^&*/1/3"),

            prints(invalidInputInteger),
            inputs("APRIL 4th 2024"),

            prints(invalidDateFormat),
            inputs("20/4/2024"),

            prints(invalidDate),
            inputs("13/4/2024"),

            prints(invalidDate),
            inputs("0/4/2024"),

            prints(invalidDate),
            inputs("3/32/2024"),

            prints(invalidDate),
            inputs("3/0/2024"),

            prints(invalidDate),
            inputs("1/1/2024"),

            prints(endDatePrompt),
            inputs("1/31/2024"),

            prints("The gain for stock AAPL from 2024-01-01 to 2024-01-31 was $-1.24.\n"),
            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void menuHandlesInvalidOptionAndEndDate() {
    String invalidInputDateOrder = "Invalid input: The end date must be after the start date.";
    assertTrue(runTest(
            prints(mainMenu),
            inputs("0"),
            prints(invalidInputMessage),
            prints(mainMenu),
            inputs("-1"),
            prints(invalidInputMessage),
            prints(mainMenu),
            inputs("5"),
            prints(invalidInputMessage),
            prints(mainMenu),
            inputs("6"),
            prints(invalidInputMessage),
            prints(mainMenu),
            inputs("4"),
            prints(managePortfoliosMenu),
            inputs("EXIT"),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("04/20/2024"),

            prints(endDatePrompt),
            inputs("4/19/2024"),
            prints(invalidInputDateOrder),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("04/20/2024"),

            prints(endDatePrompt),
            inputs("4/20/2024"),
            prints(invalidInputDateOrder),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            inputs("04/20/2024"),

            prints(endDatePrompt),
            inputs("04/21/2024"),
            prints("The gain for stock AAPL from 2024-04-20 to 2024-04-21 was $0.00.\n"),

            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void getMovingAverageWorks() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("2"),

            prints(tickerPrompt),
            inputs("GOOG"),

            prints(datePrompt),
            inputs("6/1/2021"),

            prints(daysPrompt),
            inputs("100"),

            prints("The average for stock GOOG on 2021-06-01 for 100 days was $106.70.\n"),
            prints(mainMenu),
            inputs("2"),

            prints(tickerPrompt),
            inputs("ALLL"),

            prints(tickerIncorrect),
            inputs("AMZN"),

            prints(datePrompt),
            inputs("04/21/2011"),

            prints(daysPrompt),
            inputs("10"),

            prints("The average for stock AMZN on 2011-04-21 for 10 days was $9.10.\n"),
            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void testXDayCrossoversFlow() {
    String amznCrossoversMessage =
            "Here are all the 100-day crossovers for the AMZN stock "
                    + "starting at 2021-06-01:\n\n"
                    + "2021-05-27\n2021-05-26\n2021-05-25\n2021-05-"
                    + "24\n2021-05-20\n2021-05-19\n"
                    + "2021-05-18\n2021-05-17\n2021-05-11\n2021"
                    + "-05-07\n2021-05-06\n2021-05-05\n"
                    + "2021-05-04\n2021-05-03\n2021-04-30\n2021-"
                    + "04-29\n2021-04-28\n2021-04-27\n"
                    + "2021-04-26\n2021-04-23\n2021-04-22\n"
                    + "2021-04-21\n2021-04-20\n2021-04-19\n"
                    + "2021-04-16\n2021-04-15\n2021-04-14\n2021-"
                    + "04-13\n2021-04-12\n2021-04-09\n"
                    + "2021-04-08\n2021-04-07\n2021-04-"
                    + "06\n2021-04-05\n";
    String aaplCrossoversMessage =
            "Here are all the 30-day crossovers for the AAPL stock sta"
                    + "rting at 2021-01-20:\n\n"
                    + "2021-01-20\n2021-01-14\n2021-01-13\n2"
                    + "021-01-12\n2021-01-11\n2021-01-08\n"
                    + "2021-01-07\n2021-01-05\n2021-01-04\n20"
                    + "20-12-31\n2020-12-30\n2020-12-29\n"
                    + "2020-12-28\n2020-12-24\n2020-12-23\n202"
                    + "0-12-22\n";

    assertTrue(runTest(
            prints(mainMenu),
            inputs("3"),

            prints(tickerPrompt),
            inputs("AMZN"),

            prints(datePrompt),
            inputs("06/1/2021"),

            prints(daysPrompt),
            inputs("100"),

            prints(amznCrossoversMessage),
            prints(mainMenu),
            inputs("3"),

            prints(tickerPrompt),
            inputs("APPL"),

            prints(tickerIncorrect),
            inputs("AAPL"),

            prints(datePrompt),
            inputs("01/20/2021"),

            prints(daysPrompt),
            inputs("30"),

            prints(aaplCrossoversMessage),
            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void createAndDeletePortfolios() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"),

            prints(managePortfoliosMenu),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("S&P500"),

            prints(portfolioCreatedMessage + "S&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("NASDAQ"),

            prints(portfolioCreatedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ\n5. View/Edit: S&P500"),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("KYLAN'S PORTFOLIO"),

            prints(portfolioCreatedMessage + "KYLAN'S PORTFOLIO."),
            prints(managePortfoliosMenu
                    + "\n4. View/Edit: NASDAQ\n5. View/Edit: S&P500\n6. "
                    + "View/Edit: KYLAN'S "
                    + "PORTFOLIO"),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("nasdaq"),

            prints(duplicatePortfolioMessage),
            prints(managePortfoliosMenu
                    + "\n4. View/Edit: NASDAQ\n5. View/Edit: "
                    + "S&P500\n6. View/Edit: KYLAN'S "
                    + "PORTFOLIO"),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("s&P500"),

            prints(duplicatePortfolioMessage),
            prints(managePortfoliosMenu
                    + "\n4. View/Edit: NASDAQ\n5. View/Edit: "
                    + "S&P500\n6. View/Edit: KYLAN'S "
                    + "PORTFOLIO"),
            inputs("6"),

            prints("Here are all the stocks in the "
                    + "KYLAN'S PORTFOLIO portfol"
                    + "io:\n\nStock         "
                    + "                 Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu
                    + "\n4. View/Edit: NASDAQ\n5. View/Edit: "
                    + "S&P500\n6. View/Edit: KYLAN'S "
                    + "PORTFOLIO"),
            inputs("2"),

            prints("What portfolio would you like to delete?"),
            inputs("NASDAQ"),

            prints(portfolioDeletedMessage + "NASDAQ."),
            prints(managePortfoliosMenu
                    + "\n4. View/Edit: S&P500\n5. View/E"
                    + "dit: KYLAN'S PORTFOLIO"),
            inputs("2"),

            prints("What portfolio would you like to delete?"),
            inputs("KYLAN'S PORTFOLIO"),

            prints(portfolioDeletedMessage + "KY"
                    + "LAN'S PORTFOLIO."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("2"),

            prints("What portfolio would you like to delete?"),
            inputs("S&P500"),

            prints(portfolioDeletedMessage + "S&P500."),
            prints(managePortfoliosMenu),
            inputs("EXTI"),

            prints("Invalid input. Please enter a valid c"
                    + "hoice (a number from 1 through 3) or "
                    + "EXIT to go back."),
            prints(managePortfoliosMenu),
            inputs("EXIT"),

            prints(mainMenu),
            inputs("EXIT")
    ));
  }

  @Test
  public void testManagePortfoliosWithInvalidInputs() {
    String invalidInputMessage =
            "Invalid input. Please enter a valid choice (a number "
                    + "from 1 through 3) or EXIT to go"
                    + " back.";
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"),

            prints(managePortfoliosMenu),
            inputs("4"),
            prints(invalidInputMessage),
            prints(managePortfoliosMenu),
            inputs("0"),
            prints(invalidInputMessage),
            prints(managePortfoliosMenu),
            inputs("-1"),
            prints(invalidInputMessage),
            prints(managePortfoliosMenu),
            inputs("5"),
            prints(invalidInputMessage),
            prints(managePortfoliosMenu),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("NASDAQ"),
            prints(portfolioCreatedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("4"),

            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    +
                    "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("EXIT"),

            prints(mainMenu),
            inputs("EXIT")
    ));

  }

  @Test
  public void testOnePortfolio() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"),

            prints(managePortfoliosMenu),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("NASDAQ"),
            prints(portfolioCreatedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("4"),

            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would "
                    + "like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AAPL"),

            prints(sharesPrompt),
            inputs("50"),
            prints("Successfully purchased 50 number of AAPL "
                    + "stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ "
                    + "portfolio:\n\nStock                    "
                    + "      Shares\nAAPL                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that "
                    + "you would like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AMZN"),

            prints(sharesPrompt),
            inputs("500"),
            prints("Successfully purchased 500 number of "
                    + "AMZN stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you "
                    + "would like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AAPL"),

            prints(sharesPrompt),
            inputs("500"),
            prints("Successfully purchased 500 number of "
                    + "AAPL stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           550\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("3"),

            prints(removeStockPrompt + "NASDAQ."),
            inputs("AAPL"),
            prints("Successfully removed stock AAPL from portfolio NASDAQ."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you "
                    + "would like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AAPL"),

            prints(sharesPrompt),
            inputs("60"),
            prints("Successfully purchased 60 number of AAPL stocks "
                    + "in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           60\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints(portfolioValuePrompt),
            inputs("04/20/2013"),
            prints("The value of the portfolio NASDAQ at 2013-04-20 is 7225.88."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           60\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("2"),

            prints("What portfolio would you like to delete?"),
            inputs("NASDAQ"),
            prints(portfolioDeletedMessage + "NASDAQ."),
            prints(managePortfoliosMenu),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("NASDAQ"),
            prints(portfolioCreatedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("4"),

            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would "
                    + "like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AMZN"),

            prints(sharesPrompt),
            inputs("10"),
            prints("Successfully purchased 10 number of AMZN "
                    + "stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AMZN                           10\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would "
                    + "like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AAPL"),

            prints(sharesPrompt),
            inputs("10"),
            prints("Successfully purchased 10 number of AAPL "
                    + "stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\nA"
                    + "APL                           10\n"
                    + "AMZN                           10\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints(portfolioValuePrompt),
            inputs("04/20/2024"),
            prints("The value of the portfolio NASDAQ "
                    + "at 2024-04-20 is 3394.05."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           10\n"
                    + "AMZN                           10\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("EXIT"),

            prints(mainMenu),
            inputs("EXIT")
    ));

  }

  @Test
  public void testMultiplePortfolios() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"),

            prints(managePortfoliosMenu),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("NASDAQ"),
            prints(portfolioCreatedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("4"),

            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you "
                    + "would like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AAPL"),

            prints(sharesPrompt),
            inputs("50"),
            prints("Successfully purchased 50 number of AAPL "
                    + "stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that "
                    + "you would like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AMZN"),

            prints(sharesPrompt),
            inputs("500"),
            prints("Successfully purchased 500 number "
                    + "of AMZN stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that "
                    + "you would like to add to portfolio "
                    + "NASDAQ:"),
            inputs("AAA"),

            prints(sharesPrompt),
            inputs("100"),
            prints("Successfully purchased 100 number of AAA "
                    + "stocks in the NASDAQ portfolio."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            100\n"
                    + "AAPL                           50\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("1"),

            prints(portfolioNamePrompt),
            inputs("S&P500"),
            prints(portfolioCreatedMessage + "S&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: "
                    + "NASDAQ\n5. View/Edit: S&P500"),
            inputs("4"),

            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            100\n"
                    + "AAPL                           50\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ\n5. "
                    + "View/Edit: S&P500"),
            inputs("5"),

            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would "
                    + "like to add to portfolio "
                    + "S&P500:"),
            inputs("AAA"),

            prints(sharesPrompt),
            inputs("10000"),
            prints("Successfully purchased 10000 number of "
                    + "AAA stocks in the S&P500 portfolio."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            10000\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you "
                    + "would like to add to portfolio "
                    + "S&P500:"),
            inputs("AMZN"),

            prints(sharesPrompt),
            inputs("300"),
            prints("Successfully purchased 300 number of "
                    + "AMZN stocks in the S&P500 portfolio."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            10000\n"
                    + "AMZN                           300\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints(portfolioValuePromptSMP),
            inputs("4/20/2013"),
            prints("The value of the portfolio S&P500 at "
                    + "2013-04-20 is 3904.80."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            10000\n"
                    + "AMZN                           300\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints(portfolioValuePromptSMP),
            inputs("4/20/2013"),
            prints("The value of the portfolio S&P500 at "
                    + "2013-04-20 is 3904.80."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            10000\n"
                    + "AMZN                           300\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ\n5. "
                    + "View/Edit: S&P500"),
            inputs("4"),

            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            100\n"
                    + "AAPL                           50\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints(portfolioValuePrompt),
            inputs("4/20/2013"),
            prints("The value of the portfolio NASDAQ at 2013-04-20 "
                    + "is 7106.23."),
            prints("Here are all the stocks in the NASDAQ portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAA                            100\n"
                    + "AAPL                           50\n"
                    + "AMZN                           500\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ\n5. "
                    + "View/Edit: S&P500"),
            inputs("EXIT"),

            prints(mainMenu),
            inputs("EXIT")
    ));

  }

  @Test
  public void renamePortfolioWorksForDifferentScenarios() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"),

            prints(managePortfoliosMenu),
            inputs("1"),

            prints("What is the name of the portfolio you would like to create?"),
            inputs("S&P500"),
            prints("Successfully created portfolio S&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("1"),

            prints("What is the name of the portfolio you would like to create?"),
            inputs("NASDAQ"),
            prints("Successfully created portfolio NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ\n5. View/Edit: S&P500"),
            inputs("3"),

            prints("What portfolio would you like to rename? (Please enter the name)."),
            inputs("NASDAQ"),
            prints(renamePortfolioPrompt),
            inputs("s&p500"),
            prints("A portfolio with that name already exists!"),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ\n5. View/Edit: S&P500"),
            inputs("3"),

            prints("What portfolio would you like to rename? (Please enter the name)."),
            inputs("nasdaq"),
            prints(renamePortfolioPrompt),
            inputs("othernasdaq"),
            prints("Successfully renamed portfolio NASDAQ to OTHERNASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERNASDAQ\n5. View/Edit: S&P500"),
            inputs("3"),

            prints("What portfolio would you like to rename? (Please enter the name)."),
            inputs("nasdaq"),
            prints(portfolioDoesNotExistMessage),
            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERNASDAQ\n5. View/Edit: S&P500"),
            inputs("2"),

            prints(deletePortfolioPrompt),
            inputs("nasdaq"),
            prints(portfolioDoesNotExistMessage),
            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERNASDAQ\n5. View/Edit: S&P500"),
            inputs("2"),

            prints(deletePortfolioPrompt),
            inputs("othernasdaq"),
            prints("Successfully deleted portfolio OTHERNASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("3"),

            prints("What portfolio would you like to rename? (Please enter the name)."),
            inputs("s&p500"),
            prints(renamePortfolioPrompt),
            inputs("others&p500"),
            prints("Successfully renamed portfolio S&P500 to OTHERS&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERS&P500"),
            inputs("2"),

            prints(deletePortfolioPrompt),
            inputs("s&p500"),
            prints(portfolioDoesNotExistMessage),
            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERS&P500"),
            inputs("1"),

            prints("What is the name of the portfolio you would like to create?"),
            inputs("s&p500"),
            prints("Successfully created portfolio S&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERS&P500\n5. View/Edit: S&P500"),
            inputs("5"),

            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would like to add to portfolio "
                    + "S&P500:"),
            inputs("AAPL"),

            prints(sharesPrompt),
            inputs("50"),
            prints("Successfully purchased 50 number of AAPL stocks in the S&P500 portfolio."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERS&P500\n5. View/Edit: S&P500"),
            inputs("3"),

            prints("What portfolio would you like to rename? (Please enter the name)."),
            inputs("s&p500"),
            prints(renamePortfolioPrompt),
            inputs("thirds&p500"),
            prints("Successfully renamed portfolio S&P500 to THIRDS&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERS&P500\n5. View/Edit: THIRDS&P500"),
            inputs("5"),

            prints("Here are all the stocks in the THIRDS&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERS&P500\n5. View/Edit: THIRDS&P500"),
            inputs("4"),

            prints("Here are all the stocks in the OTHERS&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: OTHERS&P500\n5. View/Edit: THIRDS&P500"),
            inputs("EXIT"),

            prints(mainMenu),
            inputs("EXIT")
    ));

  }

  @Test
  public void deletePortfolioWorksInDifferentScenarios() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"),

            prints(managePortfoliosMenu),
            inputs("1"),

            prints("What is the name of the portfolio you would like to create?"),
            inputs("S&P500"),
            prints("Successfully created portfolio S&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("2"),

            prints(deletePortfolioPrompt),
            inputs("NASDAQ"),
            prints(portfolioDoesNotExistMessage),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("2"),

            prints(deletePortfolioPrompt),
            inputs("S&P50"),
            prints(portfolioDoesNotExistMessage),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("EXIT"),

            prints(mainMenu),
            inputs("EXIT")
    ));

  }

  @Test
  public void addingAndRemovingStockFromPortfolioChangesValue() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"),

            prints(managePortfoliosMenu),
            inputs("1"),

            prints("What is the name of the portfolio you would like to create?"),
            inputs("S&P500"),
            prints("Successfully created portfolio S&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("4"),

            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would like to add to portfolio "
                    + "S&P500:"),
            inputs("AAPL"),

            prints(sharesPrompt),
            inputs("50"),
            prints("Successfully purchased 50 number of AAPL stocks in the S&P500 portfolio."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would like to add to portfolio "
                    + "S&P500:"),
            inputs("GOOG"),

            prints(sharesPrompt),
            inputs("50"),
            prints("Successfully purchased 50 number of GOOG stocks in the S&P500 portfolio."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"
                    + "GOOG                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints("What date would you like to know the value of portfolio S&P500 at? Please "
                    + "enter the date in the format MM/DD/YYYY."),
            inputs("04/20/2024"),
            prints("The value of the portfolio S&P500 at 2024-04-20 is 16024.75."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"
                    + "GOOG                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("2"),

            prints("Please enter the ticker of the stock that you would like to add to portfolio "
                    + "S&P500:"),
            inputs("AMZN"),

            prints(sharesPrompt),
            inputs("50"),
            prints("Successfully purchased 50 number of AMZN stocks in the S&P500 portfolio."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"
                    + "AMZN                           50\n"
                    + "GOOG                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints("What date would you like to know the value of portfolio S&P500 at? Please "
                    + "enter the date in the format MM/DD/YYYY."),
            inputs("04/20/2024"),
            prints("The value of the portfolio S&P500 at 2024-04-20 is 24756.25."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"
                    + "AMZN                           50\n"
                    + "GOOG                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("4"),

            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AAPL                           50\n"
                    + "AMZN                           50\n"
                    + "GOOG                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("3"),

            prints("Please enter the ticker of the stock that you would like to remove from "
                    + "portfolio S&P500."),
            inputs("AAPL"),
            prints("Successfully removed stock AAPL from portfolio S&P500."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AMZN                           50\n"
                    + "GOOG                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("1"),

            prints("What date would you like to know the value of portfolio S&P500 at? Please "
                    + "enter the date in the format MM/DD/YYYY."),
            inputs("04/20/2024"),
            prints("The value of the portfolio S&P500 at 2024-04-20 is 16517.50."),
            prints("Here are all the stocks in the S&P500 portfolio:\n\n"
                    + "Stock                          Shares\n"
                    + "AMZN                           50\n"
                    + "GOOG                           50\n"),
            prints(viewEditPortfolioMenu),
            inputs("EXIT"),

            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),
            inputs("EXIT"),

            prints(mainMenu),
            inputs("EXIT")
    ));

  }

}