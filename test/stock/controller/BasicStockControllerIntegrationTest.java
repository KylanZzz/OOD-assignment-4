package stock.controller;

import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import stock.model.AlphaVantageDataSource;
import stock.model.DataSource;
import stock.model.PortfolioStockModelImpl;
import stock.model.StockModel;
import stock.view.BasicPortfolioStockView;
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
    StockModel model = new PortfolioStockModelImpl(dataSource, "res/portfolio");
    StockView view = new BasicPortfolioStockView(viewLog);
    StockController controller = new BasicStockController(view, model, in);
    controller.run();
    assertEquals(expectedViewLog.toString(), viewLog.toString());
    return expectedViewLog.toString().equals(viewLog.toString());
  }

  String mainMenu =
          "Please type the number that corresponds with the choice you would like to pick, or "
                  + "type 0 to return/exit\n"
                  + "1. Get the gain/loss of stock over period of time\n"
                  + "2. Get x-day moving average of a stock\n"
                  + "3. Get x-day crossovers for a stock\n"
                  + "4. Manage portfolios";
  String managePortfoliosMenu =
          "Please type the number that corresponds with the choice you would like to pick, or "
                  + "type 0 to return/exit\n"
                  + "1. Create new portfolio\n"
                  + "2. Delete portfolio\n"
                  + "3. Rename portfolio";
  String tickerPrompt = "Please enter the ticker of the stock that you would like to know about:";
  String startDatePrompt = "Please enter the starting date (inclusive)!";
  String endDatePrompt = "Please enter the ending date (inclusive)!";
  String yearPrompt = "Please input the year: ";
  String monthPrompt = "Please input the month: ";
  String dayPrompt = "Please input the day: ";
  String tickerIncorrect = "That stock does not exist! Please try again.";
  String invalidDateFormat = "Invalid date: Please enter a valid date.";
  String invalidInputInteger = "Invalid input: not an integer, please try again.";
  String invalidDate = "Invalid date: Please enter a valid date.";
  String futureDateError =
          "Invalid date: Date has not passed yet, please enter a "
                  + "date before or equal to today.";
  String invalidInputMessage =
          "Invalid input. Please enter a valid choice (a number from 1 through 4) "
                  + "or 0 to exit the application.";
  String daysPrompt = "Please enter the number of days.";
  String datePrompt = "Please enter the ending date!";
  String portfolioNamePrompt = "What is the name of the portfolio you would like to create?";
  String portfolioCreatedMessage = "Successfully created portfolio ";
  String duplicatePortfolioMessage = "A portfolio with that name already exists!";
  String portfolioDeletedMessage = "Successfully deleted portfolio ";
  String sharesPrompt =
          "Please enter the number of shares you would like to purchase (you cannot buy "
                  + "fractional number of stocks): ";
  String viewEditPortfolioMenu =
          "Please type the number that corresponds with the choice you would like to pick, or "
                  + "type 0 to return/exit\n"
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
            inputs("0")
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

            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),


            prints("The gain for stock AAPL from 2013-04-20 to 2024-04-20 was $152.56.\n"),
            prints(mainMenu),
            inputs("0")
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
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints("The gain for stock AAPL from 2013-04-20 to 2024-04-20 was $152.56.\n"),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AMZN"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2018"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("3"),

            prints(dayPrompt),
            inputs("20"),

            prints("The gain for stock AMZN from 2018-01-01 to 2024-03-20 was $118.70.\n"),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints("The gain for stock AAPL from 2013-04-20 to 2024-04-20 was $152.56.\n"),
            prints(mainMenu),
            inputs("0")
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
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints("The gain for stock AAPL from 2005-04-20 to 2013-04-20 was $10.89.\n"),
            prints(mainMenu),
            inputs("0")
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
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(gainMessage),
            prints(mainMenu),
            inputs("0")
    ));

    assertTrue(runTest(
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("aapl"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2005"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),
            prints(gainMessage),
            prints(mainMenu),
            inputs("0")
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
            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("a"),

            prints(dayPrompt),
            inputs("20"),

            prints(invalidInputInteger),

            prints(yearPrompt),
            inputs("2013"),

            prints(monthPrompt),
            inputs("^&*"),

            prints(dayPrompt),
            inputs("20"),

            prints(invalidInputInteger),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("APRIL"),

            prints(dayPrompt),
            inputs("4th"),

            prints(invalidInputInteger),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("20"),

            prints(dayPrompt),
            inputs("4"),

            prints(invalidDateFormat),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("13"),

            prints(dayPrompt),
            inputs("4"),

            prints(invalidDate),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("0"),

            prints(dayPrompt),
            inputs("4"),

            prints(invalidDate),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("3"),

            prints(dayPrompt),
            inputs("32"),

            prints(invalidDate),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("3"),

            prints(dayPrompt),
            inputs("0"),

            prints(invalidDate),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("1"),

            prints(endDatePrompt),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("31"),

            prints("The gain for stock AAPL from 2024-01-01 to 2024-01-31 was $-1.24.\n"),
            prints(mainMenu),
            inputs("0")
    ));
  }

  @Test
  public void menuHandlesInvalidOptionAndEndDate() {
    String invalidInputDateOrder = "Invalid input: The end date must be after the start date.";
    assertTrue(runTest(
            prints(mainMenu),
            inputs("-1"),
            prints("Invalid input. Please enter a valid choice or 0 to exit the application."),
            prints(mainMenu),
            inputs("5"),
            prints("Invalid input. Please enter a valid choice or 0 to exit the application."),
            prints(mainMenu),
            inputs("6"),
            prints("Invalid input. Please enter a valid choice or 0 to exit the application."),
            prints(mainMenu),
            inputs("4"),
            prints(managePortfoliosMenu),
            inputs("0"),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("19"),

            prints(invalidInputDateOrder),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(invalidInputDateOrder),
            prints(mainMenu),
            inputs("1"),

            prints(tickerPrompt),
            inputs("AAPL"),

            prints(startDatePrompt),
            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("20"),

            prints(endDatePrompt),

            prints(yearPrompt),
            inputs("2024"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("21"),

            prints("The gain for stock AAPL from 2024-04-20 to 2024-04-21 was $0.00.\n"),

            prints(mainMenu),
            inputs("0")
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
            prints(yearPrompt),
            inputs("2021"),

            prints(monthPrompt),
            inputs("6"),

            prints(dayPrompt),
            inputs("1"),

            prints(daysPrompt),
            inputs("100"),

            prints("The average for stock GOOG on 2021-06-01 for 100 days was $106.58.\n"),
            prints(mainMenu),
            inputs("2"),

            prints(tickerPrompt),
            inputs("ALLL"),

            prints(tickerIncorrect),
            inputs("AMZN"),

            prints(datePrompt),
            prints(yearPrompt),
            inputs("2011"),

            prints(monthPrompt),
            inputs("4"),

            prints(dayPrompt),
            inputs("21"),

            prints(daysPrompt),
            inputs("10"),

            prints("The average for stock AMZN on 2011-04-21 for 10 days was $9.10.\n"),
            prints(mainMenu),
            inputs("0")
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
            prints(yearPrompt),
            inputs("2021"),

            prints(monthPrompt),
            inputs("6"),

            prints(dayPrompt),
            inputs("1"),

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
            prints(yearPrompt),
            inputs("2021"),

            prints(monthPrompt),
            inputs("1"),

            prints(dayPrompt),
            inputs("20"),

            prints(daysPrompt),
            inputs("30"),

            prints(aaplCrossoversMessage),
            prints(mainMenu),
            inputs("0")
    ));
  }

  @Test
  public void createAndDeletePortfolios() {
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"), // Manage portfolios

            // Creating first portfolio
            prints(managePortfoliosMenu),
            inputs("1"), // Select 'Create new portfolio'
            prints(portfolioNamePrompt),
            inputs("S&P500"),
            prints(portfolioCreatedMessage + "S&P500."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),

            // Creating second portfolio
            inputs("1"), // Select 'Create new portfolio'
            prints(portfolioNamePrompt),
            inputs("NASDAQ"),
            prints(portfolioCreatedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500\n5. "
                    + "View/Edit: NASDAQ"),

            // Creating third portfolio
            inputs("1"), // Select 'Create new portfolio'
            prints(portfolioNamePrompt),
            inputs("KYLAN'S PORTFOLIO"),
            prints(portfolioCreatedMessage + "KYLAN'S PORTFOLIO."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500\n5. "
                    + "View/Edit: NASDAQ\n6. View/Edit: KYLAN'S PORTFOLIO"),

            // Attempt to create a duplicate portfolio "NASDAQ"
            inputs("1"),
            prints(portfolioNamePrompt),
            inputs("NASDAQ"),
            prints(duplicatePortfolioMessage),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500\n5. "
                    + "View/Edit: NASDAQ\n6. "
                    + "View/Edit: KYLAN'S PORTFOLIO"),

            // Attempt to create a duplicate portfolio "S&P500"
            inputs("1"),
            prints(portfolioNamePrompt),
            inputs("S&P500"),
            prints(duplicatePortfolioMessage),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500\n5. "
                    + "View/Edit: NASDAQ\n6. "
                    + "View/Edit: KYLAN'S PORTFOLIO"),

            // Deleting NASDAQ portfolio
            inputs("2"),
            prints("What portfolio would you like to delete?"),
            inputs("NASDAQ"),
            prints(portfolioDeletedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500\n5. "
                    + "View/Edit: "
                    + "KYLAN'S PORTFOLIO"),

            // Deleting KYLAN'S PORTFOLIO
            inputs("2"),
            prints("What portfolio would you like to delete?"),
            inputs("KYLAN'S PORTFOLIO"),
            prints(portfolioDeletedMessage + "KYLAN'S PORTFOLIO."),
            prints(managePortfoliosMenu + "\n4. View/Edit: S&P500"),

            // Deleting S&P500 portfolio
            inputs("2"),
            prints("What portfolio would you like to delete?"),
            inputs("S&P500"),
            prints(portfolioDeletedMessage + "S&P500."),
            prints(managePortfoliosMenu),

            // Exiting the application
            inputs("0"),
            prints(mainMenu),
            inputs("0")
    ));
  }

  @Test
  public void testManagePortfoliosWithInvalidInputs() {
    String invalidInputMessage =
            "Invalid input. Please enter a valid choice or 0 to go back.";
    assertTrue(runTest(
            prints(mainMenu),
            inputs("4"), // Access manage portfolios

            // Test invalid inputs for manage portfolios menu
            prints(managePortfoliosMenu),
            inputs("4"), // This input is invalid based on your expected range
            prints(invalidInputMessage),
            prints(managePortfoliosMenu),
            inputs("-1"), // Test negative number invalid input
            prints(invalidInputMessage),
            prints(managePortfoliosMenu),
            inputs("5"), // Test upper boundary invalid input
            prints(invalidInputMessage),
            prints(managePortfoliosMenu),

            // Now, proceed with valid input to create a portfolio
            inputs("1"),
            prints(portfolioNamePrompt),
            inputs("NASDAQ"),
            prints(portfolioCreatedMessage + "NASDAQ."),
            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),

            // Access the newly created portfolio
            inputs("4"),
            prints("Please type the number that corresponds with the choice "
                    + "you would like to pick, or type 0 to return/exit"),
            prints("1. Buy stock to portfolio\n"
                    + "2. Sell stock from portfolio\n"
                    + "3. Calculate portfolio value on specific date\n"
                    + "4. Check the distribution of value on specific date\n"
                    + "5. Save portfolio\n"
                    + "6. Load the saved portfolio\n"
                    + "7. Rebalance the portfolio\n"
                    + "8. Performance chart for the portfolio\n"
                    + "9. Composition of the portfolio"),
            inputs("0"), // Exit from view/edit NASDAQ portfolio

            prints(managePortfoliosMenu + "\n4. View/Edit: NASDAQ"),
            inputs("0"), // Exit from manage portfolios

            prints(mainMenu),
            inputs("0") // Exit from main menu
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
            inputs("0"),

            prints(mainMenu),
            inputs("0")
    ));

  }
}
