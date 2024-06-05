package stock.view;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.List;

public class BasicStockView implements StockView {
  private final Appendable out;

  public BasicStockView(Appendable out) {
    this.out = out;
  }

  private void println(String message) {
    try {
      out.append(message).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Unable to append message: " + message + " to view.");
    }
  }

  private void printOptionsPrompt() {
    println("Please type the number that corresponds with the choice you would like to pick, " +
            "or type " + BasicMenuOptions.exitKeyword() + " to return/exit");
  }

  private void printMenu(List<String> options) {
    for (int i = 0; i < options.size(); i++) {
      println(String.format("%d. %s", i + 1, options.get(i)));
    }
  }

  private void printList(List<String> list) {
    for (String item : list) {
      println(item);
    }
  }

  @Override
  public void printWelcomeScreen() {
    println("Welcome to OOD Assignment 4 for Stocks part 1!\n");
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
  public void printManagePortfolio(Map<String, Integer> stocks, String name) {
    println(String.format("Here are all the stocks in the %s portfolio:\n", name));
    var list = stocks.keySet().stream().map(it -> String.format("%-30s %d", it, stocks.get(it)))
            .collect(Collectors.toList());
    list.add(0, String.format("%-30s %s", "Stock", "Shares"));
    printList(list);
    println("");

    printOptionsPrompt();
    printMenu(BasicMenuOptions.managePortfolio());
  }

  @Override
  public void printXDayCrossovers(String ticker, LocalDate date, int days, List<LocalDate> dates) {
    println(String.format("Here are all the %d-day crossovers for the %s stock starting at %s:\n",
            days, ticker, date.toString()));
    printList(dates.stream().map(LocalDate::toString).collect(Collectors.toList()));
    println("");
  }

  @Override
  public void printStockGain(String ticker, LocalDate startDate, LocalDate endDate, double gain) {
    println(String.format("The gain for stock %s from %s to %s was $%.2f.\n",
            ticker, startDate, endDate, gain));
  }

  @Override
  public void printStockAverage(String ticker, LocalDate endDate, int days, double average) {
    println(String.format("The average for stock %s on %s for %d days was $%.2f.\n",
            ticker, endDate, days, average));
  }

  @Override
  public void printMessage(String message) {
    println(message);
  }

  public static void main(String[] args) {
  }
}
