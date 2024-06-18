package stock.controller;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
    public void buyStock(String portfolio, String ticker, String shares, String month, String day, String year) {

    }

    @Override
    public void sellStock(String portfolio, String ticker, String shares, String month, String day, String year) {

    }

  @Override
  public void getComposition(String portfolio, String month, String day, String year, String share, String ticker) {
    LocalDate date = getValidDate(month, day, year);
    if (date == null) return;

    view.displayComposition(model.getPortfolioContentsDecimal(portfolio, date));
  }

  @Override
  public void getValue(String portfolio, String month, String day, String year, String share, String ticker) {
    LocalDate date = getValidDate(month, day, year);
    if (date == null) return;

    try {
      view.displayValue(model.getPortfolioValue(portfolio, date));
    } catch (IOException e) {
      view.displayErrorMessage("IO Error occurred: " + e.getMessage());
    }
  }

  @Override
  public void savePortfolio(String portfolio) {

  }

  private LocalDate getValidDate(int month, int day, int year) {
    try {
      return LocalDate.of(year, month, day);
    } catch (DateTimeException e) {
      view.displayErrorMessage("Invalid date! Please enter a positive, valid integer in all "
              + "fields.");
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
