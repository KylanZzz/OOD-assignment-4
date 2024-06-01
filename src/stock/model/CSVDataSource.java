package stock.model;

import java.time.LocalDate;

public class CSVDataSource implements DataSource {

    public CSVDataSource() {

    }

    @Override
    public double getClosingPrice(LocalDate date, String ticker) {
        return 0;
    }

    @Override
    public boolean stockExistsAtDate(LocalDate date, String ticker) {
        return false;
    }

    @Override
    public boolean stockInDataSource(String ticker) {
        return false;
    }
}
