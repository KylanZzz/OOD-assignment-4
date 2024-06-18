package stock.controller;

public interface PortfolioStockFeatures {
  void createPortfolio(String name);

  void loadPortfolio(String filePath);

  void choosePortfolio(String name);

  void buyStock(String portfolio, String ticker, String shares, String month, String day, String year);

  void sellStock(String portfolio, String ticker, String shares, String month, String day, String year);

  void getComposition(String portfolio, String month, String day, String year, String share, String ticker);

  void getValue(String portfolio, String month, String day, String year, String share, String ticker);

  void savePortfolio(String portfolio);
}
