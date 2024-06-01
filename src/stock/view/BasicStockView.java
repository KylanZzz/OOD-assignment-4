package stock.view;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

public class BasicStockView implements StockView {
  private final PrintStream out;

  public BasicStockView(PrintStream out) {
    this.out = out;
  }

  private void printOptionsPrompt() {
    out.println("Please type the number that corresponds with the choice you would like to pick.");
  }

  private void printMenu(List<String> options) {
    for (int i = 0; i < options.size(); i++) {
      out.printf("%d. %s\n", i + 1, options.get(i));
    }
  }

  private void printList(List<String> list) {
    for (String item: list) {
      out.println(item);
    }
  }

  @Override
  public void printWelcomeScreen() {
    out.println("Welcome to OOD Assignment 4 for Stocks part 1!\n");
  }

  @Override
  public void printMainMenu() {
    printOptionsPrompt();
    printMenu(BasicMenuOptions.mainMenu());
  }

  @Override
  public void printViewPortfolios(List<String> portfolios) {
    var updatedPortfolioOptions = new ArrayList<>(BasicMenuOptions.viewPortfolios());
    portfolios.forEach(it -> updatedPortfolioOptions.add("View/Edit: " + it));

    printOptionsPrompt();
    printMenu(updatedPortfolioOptions);
  }

  @Override
  public void printManagePortfolio(List<String> tickers, String name) {
    out.printf("Here are all the stocks in the %s portfolio:\n\n", name);
    printList(tickers);
    out.println();

    printOptionsPrompt();
    printMenu(BasicMenuOptions.managePortfolio());
  }

  @Override
  public void printXDayCrossovers(String ticker, LocalDate date, int days, List<LocalDate> dates) {
    out.printf("Here are all the %d-day crossovers for the %s stock starting at %s:\n\n",
            days, ticker, date.toString());
    printList(dates.stream().map(LocalDate::toString).collect(Collectors.toList()));
    out.println();
  }

  @Override
  public void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain) {
    out.printf("The gain for stock %s from %s to %s was $%.2f.\n",
            ticker, startDate, endDate, gain);
  }

  @Override
  public void printStockAverage(String ticker, LocalDate endDate, int days, double average) {
    out.printf("The average for stock %s on %s for %d days was $%.2f.\n",
            ticker, endDate, days, average);
  }

  @Override
  public void printMessage(String message) {
    out.println(message);
  }

  public static void main(String[] args) {
  }
}
