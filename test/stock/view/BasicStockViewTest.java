package stock.view;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test the functionalities of printing the welcome message, the options for the users,
 * getting crossovers, getting gain and loss value, moving average, and portfolios.
 */
public class BasicStockViewTest {
  StockView view;
  StringBuilder sb;

  /**
   * Prepares the test environment before each test execution.
   * This method initializes a StringBuilder to capture the output,
   * from the BasicStockView, and creates an instance of BasicStockView with this StringBuilder,
   * as its Appendable output target.
   * This setup is crucial for testing the output functionality of,
   * the BasicStockView without interacting, with more complex I/O operations.
   */
  @Before
  public void setup() {
    sb = new StringBuilder();
    view = new BasicStockView(sb);
  }

  @Test
  public void printWelcomeScreenWorks() {
    view.printWelcomeScreen();
    assertEquals("Welcome to OOD Assignment 4 for Stocks part 1!\n\n", sb.toString());
  }

  @Test
  public void printMainMenuWorks() {
    view.printMainMenu();
    String expectedOutput = "Please type the number that corresponds with the choice you would "
            + "like to pick, or type EXIT to return/exit\n"
            + "1. Get the gain/loss of stock over period of time\n"
            + "2. Get x-day moving average of a stock\n"
            + "3. Get x-day crossovers for a stock\n"
            + "4. Manage portfolios\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printXDayCrossoversWorks() {
    LocalDate date = LocalDate.of(2023, 6, 1);
    List<LocalDate> crossovers = List.of(
            LocalDate.of(2023, 5, 20),
            LocalDate.of(2023, 5, 25)
    );
    view.printXDayCrossovers("AAPL", date, 5, crossovers);
    String expectedOutput = "Here are all the 5-day crossovers for the AAPL stock "
            + "starting at 2023-06-01:\n\n"
            + "2023-05-20\n"
            + "2023-05-25\n\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printStockGainWorks() {
    LocalDate startDate = LocalDate.of(2023, 1, 1);
    LocalDate endDate = LocalDate.of(2023, 6, 1);
    view.printStockGain("AAPL", startDate, endDate, 150.75);
    String expectedOutput = "The gain for stock AAPL from 2023-01-01 "
            + "to 2023-06-01 was $150.75.\n\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printStockAverageWorks() {
    LocalDate endDate = LocalDate.of(2023, 6, 1);
    view.printStockAverage("AAPL", endDate, 30, 135.50);
    String expectedOutput = "The average for stock AAPL on 2023-06-01 "
            + "for 30 days was $135.50.\n\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printMessageWorks() {
    view.printMessage("Test message.");
    String expectedOutput = "Test message.\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printViewPortfoliosWorksWithNonEmptyList() {
    List<String> portfolios = List.of("S&P500", "NASDAQ");
    view.printViewPortfolios(portfolios);

    String expectedOutput = "Please type the number that corresponds "
            + "with the choice you would like to pick, or type EXIT to return/exit\n"
            + "1. Create new portfolio\n"
            + "2. Delete portfolio\n"
            + "3. Rename portfolio\n"
            + "4. View/Edit: S&P500\n"
            + "5. View/Edit: NASDAQ\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printViewPortfolioWorksWithEmptyList() {
    List<String> portfolios = List.of();
    view.printViewPortfolios(portfolios);

    String expectedOutput = "Please type the number that corresponds with the choice "
            + "you would like to pick, or type EXIT to return/exit\n"
            + "1. Create new portfolio\n"
            + "2. Delete portfolio\n"
            + "3. Rename portfolio\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printManagePortfolioWorksWithNonEmptyList() {
    Map<String, Integer> stocks = Map.of("AAPL", 5, "AMZN", 10, "NFLX", 15);
    view.printManagePortfolio(stocks, "S&P500");

    String expectedOutput = "Here are all the stocks in the S&P500 portfolio:\n\n"
            + "Stock                          Shares\n"
            + "AAPL                           5\n"
            + "AMZN                           10\n"
            + "NFLX                           15\n\n"
            + "Please type the number that corresponds w"
            + "ith the choice you would like to pick, or type EXIT to return/exit\n"
            + "1. Calculate portfolio value\n"
            + "2. Add stock to portfolio\n"
            + "3. Remove stock from portfolio\n";
    assertEquals(expectedOutput, sb.toString());
  }

  @Test
  public void printManagePortfolioWorksWithEmptyList() {
    Map<String, Integer> stocks = Map.of();
    view.printManagePortfolio(stocks, "S&P500");

    String expectedOutput = "Here are all the stocks in the S&P500 portfolio:\n\n"
            + "Stock                          Shares\n\n"
            + "Please type the number that corresponds with the choice you "
            + "would like to pick, or type EXIT to return/exit\n"
            + "1. Calculate portfolio value\n"
            + "2. Add stock to portfolio\n"
            + "3. Remove stock from portfolio\n";
    assertEquals(expectedOutput, sb.toString());
  }
}