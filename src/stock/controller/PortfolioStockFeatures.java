package stock.controller;

public interface PortfolioStockFeatures {
  void createPortfolio(String name);

  void loadPortfolio(String filePath);

  void choosePortfolio(String name);

  void buyStock(String portfolio, String ticker, int shares, int month, int day, int year);

  void sellStock(String portfolio, String ticker, int shares, int month, int day, int year);

  void getComposition(String portfolio, int month, int day, int year);

  void getValue(String portfolio, int month, int day, int year);

  void savePortfolio(String portfolio);
}
