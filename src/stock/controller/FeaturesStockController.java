package stock.controller;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import stock.model.AlphaVantageDataSource;
import stock.model.PortfolioStockModel;
import stock.model.PortfolioStockModelImpl;
import stock.view.FeaturesStockView;
import stock.view.PortfolioStockView;
import stock.view.SimpleFeaturesStockView;

public class FeaturesStockController implements PortfolioStockFeatures {
  FeaturesStockView view;
  PortfolioStockModel model;

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
    if (!name.isEmpty()) view.displayEditPortfolio(name);
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
        model.sellStockFromPortfolio(portfolio, ticker, sh, getValidDate(month, day, year));
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
    if (date == null) return;

    if (portfolio.isEmpty()) {
      view.displayErrorMessage("Portfolio cannot be an empty string!");
      return;
    }

//    if (!share.isEmpty() || !ticker.isEmpty()) {
//      view.displayErrorMessage("Ticker and share must be empty in order to get composition.");
//      return;
//    }

    view.displayComposition(model.getPortfolioContentsDecimal(portfolio, date));
  }

  @Override
  public void getValue(String portfolio, String month, String day, String year, String share,
                       String ticker) {
    LocalDate date = getValidDate(month, day, year);
    if (date == null) return;

    if (portfolio.isEmpty()) {
      view.displayErrorMessage("Portfolio cannot be an empty string!");
      return;
    }

//    if (!share.isEmpty() || !ticker.isEmpty()) {
//      view.displayErrorMessage("Ticker and share must be empty in order to calculate value.");
//      return;
//    }

    try {
      view.displayValue(model.getPortfolioValue(portfolio, date));
    } catch (IOException e) {
      view.displayErrorMessage("IO Error occurred: " + e.getMessage());
    }
  }

  @Override
  public void savePortfolio(String portfolio) {
    portfolio = portfolio.toUpperCase();

    try {
      model.createNewPortfolioSave(portfolio);
      view.displayCreatedSave(portfolio);
    } catch (Exception e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  private boolean isValidInput(String portfolio, String ticker, String shares, String month,
                               String day, String year) {
    portfolio = portfolio.toUpperCase();
    if (!model.getPortfolios().contains(portfolio)) {
      view.displayErrorMessage("Portfolio with that name does not exist.");
      return false;
    }

    if (portfolio.isEmpty()) {
      view.displayErrorMessage("Portfolio cannot be an empty string!");
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
      return LocalDate.of(y, m, d);
    } catch (DateTimeException e) {
      view.displayErrorMessage("Invalid date! Please enter a positive, valid date.");
      return null;
    }
  }

  public static void main(String[] args) {
    SimpleFeaturesStockView view = new SimpleFeaturesStockView("Stock Program");
    PortfolioStockModel model = new PortfolioStockModelImpl(new AlphaVantageDataSource(), "res"
            + "/portfolio");
    FeaturesStockController controller = new FeaturesStockController(view, model);
  }
}
