package stock.model;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.io.BufferedReader;
import java.util.stream.Stream;

public class CSVDataSource implements DataSource {
    private final Map<String, Map<LocalDate, Double>> stocks = new HashMap<>();

    public CSVDataSource(String directoryPath) {
        loadAllStockData(directoryPath);
    }

    private void loadAllStockData(String directoryPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(this::loadStockDataFromCSV);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadStockDataFromCSV(Path filePath) {
        String ticker = filePath.getFileName().toString().replace(".csv", "");
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE);
                double closePrice = Double.parseDouble(parts[4]);
                stocks.computeIfAbsent(ticker, n -> new HashMap<>()).put(date, closePrice);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public double getClosingPrice(LocalDate date, String ticker) {
        return stocks.get(ticker).get(date);
    }

    @Override
    public boolean stockExistsAtDate(LocalDate date, String ticker) {
        return stocks.get(ticker).containsKey(date);
    }

    @Override
    public boolean stockInDataSource(String ticker) {
        return stocks.containsKey(ticker);
    }
}
