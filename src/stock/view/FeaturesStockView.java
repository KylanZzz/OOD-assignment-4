package stock.view;

import java.util.List;
import java.util.Map;

import stock.controller.PortfolioStockFeatures;

public interface FeaturesStockView {
  void addFeatures(PortfolioStockFeatures features);

  void displayComposition(Map<String, Double> composition);

  void displayValue(double value);

  void displayBoughtStock(String ticker, double shares);

  void displaySoldStock(String ticker, double shares);

  void displayPortfolios(List<String> names);

  void displayCreatedSave(String name, String filepath);

  void displayCreatedPortfolio(String portfolio);

  void displayLoadedPortfolio(String portfolio);

  void displayEditPortfolio(String portfolio);

  void displayErrorMessage(String message);
}
