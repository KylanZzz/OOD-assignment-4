package stock.model;

public class AlphaVantageDataSource extends CSVDataSource {
  // throw IOException
  public AlphaVantageDataSource(String directoryPath) {
    super(directoryPath);
    String apiKey = "3FKL0E8WUDB1EOMS";
  }


}
