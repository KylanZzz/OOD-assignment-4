package stock.model;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.*;

public class CSVDataSourceTest {
  private static Path tempDirectory;

  @BeforeClass
  public static void setUp() throws IOException {
    tempDirectory = Files.createTempDirectory("testCsvData");
    Path csvFile = tempDirectory.resolve("AAPL.csv");
    List<String> lines = Arrays.asList(
            "timestamp,open,high,low,close,volume",
            "2022-01-01,100,110,90,100,100",
            "2022-01-02,105,115,95,110,200",
            "2022-01-03,105,115,95,120,300",
            "2022-01-04,105,115,95,110,400",
            "2022-01-05,105,115,95,115,500",
            "2022-01-10,105,115,95,120,600",
            "2022-01-13,105,115,95,100,700",
            "2022-01-14,105,115,95,110,800",
            "2022-01-15,105,115,95,120,900"
            );
    Files.write(csvFile, lines);
  }

  @AfterClass
  public static void tearDown() throws IOException {
    Files.walk(tempDirectory)
            .sorted(Comparator.reverseOrder())
            .map(Path::toFile)
            .forEach(File::delete);
  }

  @Test
  public void testLoadStockDataFromCSV() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    assertTrue(dataSource.stockInDataSource("AAPL"));
    assertTrue(dataSource.stockExistsAtDate(LocalDate.of(2022, 1, 1), "AAPL"));
    assertEquals(100.0, dataSource.getClosingPrice(LocalDate.of(2022, 1, 1), "AAPL"), 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadIncorrectFormatCSV() throws IOException {
    Path incorrectCsv = tempDirectory.resolve("Bad.csv");
    List<String> badLines = Arrays.asList(
            "date,open,high,low,close,volume",
            "2022-01-01,100,110,90,105,10000"
    );
    Files.write(incorrectCsv, badLines);

    new CSVDataSource(tempDirectory.toString());
  }

  @Test
  public void testGetClosingPrice() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    double closingPrice = dataSource.getClosingPrice(LocalDate.of(2022,1,1), "AAPL");

    assertEquals(100, closingPrice, 0.001);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGetClosingPriceFailed() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    double closingPrice = dataSource.getClosingPrice(LocalDate.of(2022,1,1), "GOOG");
  }

  @Test
  public void testGetClosingPriceNotExists() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    double closingPrice = dataSource.getClosingPrice(LocalDate.of(2022,2,1), "AAPL");
    assertEquals(0, closingPrice, 0.001);
  }

//  @Test
//  public void testAddExistsAtDate() throws IOException {
//    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
//    assertTrue(dataSource.stockExistsAtDate(LocalDate.of(2022, 1, 5), "AAPL"));
//  }

  @Test
  public void testAddExistsAtDateFalse() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    assertFalse(dataSource.stockExistsAtDate(LocalDate.of(2023, 1, 4), "AAPL"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAddExistsAtDateFailed() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    assertTrue(dataSource.stockExistsAtDate(LocalDate.of(2023, 1, 4), "A"));
  }

  @Test
  public void testStockInDataSource() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    assertTrue(dataSource.stockInDataSource("AAPL"));
  }

  @Test
  public void testStockNotInDataSource() throws IOException {
    CSVDataSource dataSource = new CSVDataSource(tempDirectory.toString());
    assertFalse(dataSource.stockInDataSource("GOOG"));
  }
}