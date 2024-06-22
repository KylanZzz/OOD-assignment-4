package stock.controller;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

import stock.model.PortfolioStockModel;
import stock.view.FeaturesStockView;

/**
 * A simple implementation of a features stock controller. This controller allows for
 * view-independent input processing that works with both text-based and graphics-based UIs.
 */
public class FeaturesStockController implements PortfolioStockFeatures {
  FeaturesStockView view;
  PortfolioStockModel model;

  /**
   * Constructs a features stock controller.
   *
   * @param view the view of the stock program.
   * @param model the controller of the stock program.
   */
  public FeaturesStockController(FeaturesStockView view, PortfolioStockModel model) {
    this.view = view;
    this.model = model;
    view.addFeatures(this);
  }

  @Override
  public void createPortfolio(String name) {
    name = name.stripTrailing().stripLeading();

    if (name.isEmpty()) {
      view.displayErrorMessage("Cannot create a nameless portfolio!");
      return;
    }

    name = name.toUpperCase();
    if (model.getPortfolios().contains(name)) {
      view.displayErrorMessage("Portfolio name already exists!");
    } else {
      model.createNewPortfolio(name);
      view.displayCreatedPortfolio(name);
      view.displayPortfolios(model.getPortfolios());
    }
  }

  @Override
  public void loadPortfolio(String filePath) {
    filePath = filePath.stripLeading().stripTrailing();

    if (filePath.isEmpty()) {
      view.displayErrorMessage("Please choose a file path to load!");
      return;
    }

    try {
      model.loadPortfolioSave(filePath);
      view.displayLoadedPortfolio(filePath);
      view.displayPortfolios(model.getPortfolios());
    } catch (IOException e) {
      view.displayErrorMessage("Error while loading save: " + e.getMessage());
    }
  }

  @Override
  public void choosePortfolio(String name) {
    if (name != null && !name.isBlank() && model.getPortfolios().contains(name)) {
      view.displayEditPortfolio(name);
    } else {
      view.displayErrorMessage("Please create a new portfolio or load one from a save first.");
    }
  }

  @Override
  public void buyStock(String portfolio, String ticker, String shares, String month, String day,
                       String year) {
    ticker = ticker.toUpperCase();
    portfolio = portfolio.toUpperCase();

    if (isValidInput(portfolio, ticker, shares, month, day, year)) {
      int sh = Integer.parseInt(shares);

      try {
        model.addStockToPortfolio(portfolio, ticker, sh, getValidDate(month, day, year));
        view.displayBoughtStock(ticker, sh);
      } catch (Exception e) {
        view.displayErrorMessage(e.getMessage());
      }
    }
  }


  @Override
  public void sellStock(String portfolio, String ticker, String shares, String month, String day,
                        String year) {
    ticker = ticker.toUpperCase();
    portfolio = portfolio.toUpperCase();

    if (isValidInput(portfolio, ticker, shares, month, day, year)) {
      int sh = Integer.parseInt(shares);

      try {
        var date = getValidDate(month, day, year);
        if (model.getPortfolioContentsDecimal(portfolio, date).get(ticker) < sh) {
          view.displayErrorMessage("Not enough shares bought to sell!");
          return;
        }

        model.sellStockFromPortfolio(portfolio, ticker, sh, date);
        view.displaySoldStock(ticker, sh);
      } catch (Exception e) {
        view.displayErrorMessage(e.getMessage());
      }
    }
  }

  @Override
  public void getComposition(String portfolio, String month, String day, String year, String share,
                             String ticker) {
    ticker = ticker.toUpperCase();
    portfolio = portfolio.toUpperCase();

    LocalDate date = getValidDate(month, day, year);
    if (date == null) {
      return;
    }

    if (portfolio.isBlank()) {
      view.displayErrorMessage("Portfolio cannot be an empty string!");
      return;
    }

    if (!model.getPortfolios().contains(portfolio)) {
      view.displayErrorMessage("Portfolio does not exist.");
      return;
    }

    view.displayComposition(model.getPortfolioContentsDecimal(portfolio, date));
  }

  @Override
  public void getValue(String portfolio, String month, String day, String year, String share,
                       String ticker) {
    LocalDate date = getValidDate(month, day, year);
    if (date == null) {
      return;
    }

    if (portfolio.isBlank()) {
      view.displayErrorMessage("Portfolio cannot be an empty string!");
      return;
    }

    if (!model.getPortfolios().contains(portfolio)) {
      view.displayErrorMessage("Portfolio does not exist.");
      return;
    }

    try {
      view.displayValue(model.getPortfolioValue(portfolio, date));
    } catch (IOException e) {
      view.displayErrorMessage("IO Error occurred: " + e.getMessage());
    }
  }

  @Override
  public void savePortfolio(String portfolio) {
    portfolio = portfolio.toUpperCase();

    if (!model.getPortfolios().contains(portfolio)) {
      view.displayErrorMessage("Portfolio does not exist.");
      return;
    }

    try {
      model.createNewPortfolioSave(portfolio);
      view.displayCreatedSave(portfolio);
    } catch (Exception e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  private boolean isValidInput(String portfolio, String ticker, String shares, String month,
                               String day, String year) {
    portfolio = portfolio.toUpperCase().stripLeading().stripTrailing();
    shares = shares.toUpperCase().stripLeading().stripTrailing();
    month = month.toUpperCase().stripLeading().stripTrailing();
    day = day.toUpperCase().stripLeading().stripTrailing();
    year = year.toUpperCase().stripLeading().stripTrailing();

    if (!model.getPortfolios().contains(portfolio)) {
      view.displayErrorMessage("Portfolio with that name does not exist.");
      return false;
    }

    if (portfolio.isEmpty() || ticker.isEmpty() || shares.isEmpty() || month.isEmpty()
            || day.isEmpty()) {
      view.displayErrorMessage("Make sure all fields are filled out!");
      return false;
    }

    LocalDate date = getValidDate(month, day, year);
    if (date == null) {
      return false;
    }

    int sh;
    try {
      sh = Integer.parseInt(shares);
    } catch (Exception e) {
      view.displayErrorMessage("Number of shares must be an Integer.");
      return false;
    }

    if (sh < 0) {
      view.displayErrorMessage("Number of shares must be a positive.");
      return false;
    }

    try {
      if (!model.stockExists(ticker)) {
        view.displayErrorMessage("Ticker does not exist.");
        return false;
      }
    } catch (Exception e) {
      view.displayErrorMessage(e.getMessage());
    }

    return true;
  }

  private LocalDate getValidDate(String month, String day, String year) {
    int y;
    int m;
    int d;
    try {
      y = Integer.parseInt(year);
      m = Integer.parseInt(month);
      d = Integer.parseInt(day);
    } catch (Exception e) {
      view.displayErrorMessage("Dates entered must be Integers only!");
      return null;
    }

    try {
      LocalDate date =  LocalDate.of(y, m, d);
      if (date.isAfter(LocalDate.now())) {
        view.displayErrorMessage("Date must be before today!");
        return null;
      }
      return date;
    } catch (DateTimeException e) {
      view.displayErrorMessage("Invalid date! Please enter a positive, valid date.");
      return null;
    }
  }
}
