package stock.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

  public static List<String> generateCSVData(int days, double noDataProbability) {
    List<String> lines = new ArrayList<>();
    LocalDate startDate = LocalDate.of(2022, 1, 1);
    Random random = new Random();

    lines.add("timestamp,open,high,low,close,volume");

    for (int i = 0; i < days; i++) {
      if (random.nextDouble() > noDataProbability) {
        LocalDate date = startDate.plusDays(i);
        double open = 100 + random.nextDouble() * 20;
        double high = open + random.nextDouble() * 10;
        double low = open - random.nextDouble() * 10;
        double close = low + random.nextDouble() * (high - low);
        int volume = 9000 + random.nextInt(6000);

        String line = String.format("%s,%.2f,%.2f,%.2f,%.2f,%d",
                date.format(DateTimeFormatter.ISO_LOCAL_DATE), open, high, low, close, volume);
        lines.add(line);
      }
    }

    return lines;
  }
}
