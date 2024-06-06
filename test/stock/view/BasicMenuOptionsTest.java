package stock.view;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;


/**
 * Test the functionalities of displaying the menu, portfolios, options within the portfolios.
 */
public class BasicMenuOptionsTest {


  @Test
  public void testExitKeyword() {
    assertEquals("EXIT", BasicMenuOptions.exitKeyword());
  }

  @Test
  public void testMainMenu() {
    List<String> expectedMainMenu = List.of(
            "Get the gain/loss of stock over period of time",
            "Get x-day moving average of a stock",
            "Get x-day crossovers for a stock",
            "Manage portfolios"
    );
    assertEquals(expectedMainMenu, BasicMenuOptions.mainMenu());
  }

  @Test
  public void testViewPortfolios() {
    List<String> expectedViewPortfolios = List.of(
            "Create new portfolio",
            "Delete portfolio",
            "Rename portfolio"
    );
    assertEquals(expectedViewPortfolios, BasicMenuOptions.viewPortfolios());
  }

  @Test
  public void testManagePortfolio() {
    List<String> expectedManagePortfolio = List.of(
            "Calculate portfolio value",
            "Add stock to portfolio",
            "Remove stock from portfolio"
    );
    assertEquals(expectedManagePortfolio, BasicMenuOptions.managePortfolio());
  }

}