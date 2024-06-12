package stock.model;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class PortfolioStockModelImplTest extends BasicStockModelTest {

  @Override
  public void setup() {
    mockDataSource = new MockDataSource();
    model = new PortfolioStockModelImpl(mockDataSource);
  }

  @Override
  public void testAddStockToPortfolio() {

  }

  @Override
  public void testAddStockToPortfolioFailed() {

  }

  @Override
  public void testRemoveStockFromPortfolioFaildTicker() {

  }

  @Override
  public void getPortfolioValue() {

  }

  @Override
  public void testGetPortfolioContents() {

  }

  @Override
  public void testGetPortfolioContentsFailed() {

  }

  @Override
  public void testRemoveStockFromPortfolioFaildName() {

  }

  @Override
  public void testRemoveStockFromPortfolio() {

  }

  @Override
  public void getPortfolioValueNoTicker() throws IOException {

  }
}