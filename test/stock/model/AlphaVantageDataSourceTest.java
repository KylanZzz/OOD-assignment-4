package stock.model;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.io.File;


import static org.junit.Assert.*;

public class AlphaVantageDataSourceTest {
  private static Path tempDirectory;

  @BeforeClass
  public static void setUp() throws IOException {
    tempDirectory = Files.createTempDirectory("test/stock/model");
    Path csvFile = tempDirectory.resolve("AAPL.csv");
    List<String> lines = DataGenerator.generateCSVData(30, 0.2);
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
    assertTrue("Stock should exist in data source.", dataSource.stockInDataSource("AAPL"));
    assertTrue(dataSource.stockExistsAtDate(LocalDate.of(2022, 1, 1), "AAPL"));
    assertEquals(105.0, dataSource.getClosingPrice(LocalDate.of(2022, 1, 1), "AAPL"), 0.01);
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
}
