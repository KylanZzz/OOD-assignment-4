package stock.model;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CSVDataSource implements DataSource {
    private Map<String, Map<LocalDate, Double>> stocks = new HashMap<>();
    private String directoryPath;

    public CSVDataSource(String directoryPath) {
        stocks = new HashMap<>();
        this.directoryPath = directoryPath;
        loadAllStockData(directoryPath);
    }

    public CSVDataSource() {
        stocks = new HashMap<>();
    }

    // check the format of the csv
    protected void loadAllStockData(String directoryPath) {
        try (Stream<Path> paths = Files.walk(Paths.get(directoryPath))) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".csv"))
                    .forEach(this::loadStockDataFromCSV);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    protected void loadStockDataFromCSV(Path filePath) {
        String ticker = filePath.getFileName().toString().replace(".csv", "");
        stocks.put(ticker, new HashMap<>());
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath.toFile()))) {
            String line = reader.readLine();
            int timeIndex = -1;
            int closeIndex = -1;
            String[] headers = line.split(",");
            for (int i = 0; i < headers.length; i++) {
                if (headers[i].trim().equalsIgnoreCase("timestamp")) {
                    timeIndex = i;
                }
                if (headers[i].trim().equalsIgnoreCase("close")) {
                    closeIndex = i;
                }
            }
            if (timeIndex == -1 || closeIndex == -1) {
                throw new IllegalArgumentException("CSV file does not have required 'timestamp' or 'closing' columns.");
            }

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                LocalDate date = LocalDate.parse(parts[0], DateTimeFormatter.ISO_LOCAL_DATE);
                double closePrice = Double.parseDouble(parts[5]);
                stocks.get(ticker).put(date,closePrice);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public double getClosingPrice(LocalDate date, String ticker) throws IOException {
        if (!stockInDataSource(ticker)) {
            throw new IllegalArgumentException("Stock is not in Data Source.");
        } else if (!stockExistsAtDate(date, ticker)) {
            return 0;
        } else {
            return stocks.get(ticker).get(date);
        }
    }

    // Assumes that the stock exists in the function
    // Throws illegalargumentexception of stock doesn't exist
    @Override
    public boolean stockExistsAtDate(LocalDate date, String ticker) throws IOException {
        if (!stockInDataSource(ticker)) {
            throw new IllegalArgumentException("Stock is not in Data Source.");
        }
        return stocks.get(ticker).containsKey(date);
    }

    @Override
    public boolean stockInDataSource(String ticker) throws IOException {
        return stocks.containsKey(ticker);
    }

}
