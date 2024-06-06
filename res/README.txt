***Stock Program***

Features:
--Examine Gain/Loss For a Stock--
The application allows users to calculate the gain or loss of a stock over a specified period based
on its closing prices. All calculations use the closing price, not the adjusted closing price.
Users are prompted to input the ticker symbol of the stock and the start and end dates for the
calculation.
Note: If the start date has no data, but data is available for some later date up to and including
the end date, the calculation will begin from the first available date.
Note: If both the start date and the end date have data, the gain or loss is calculated over this
period.
Note: If neither the start date nor the end date have data, but data is available in between,
the program will return 0 because the end date is required for comparison.

--Calculate x-Day Moving Average of a Stock--
Users can calculate the x-day moving average of a stock, which is the average of the closing prices
over the last x days from a specified date. The application prompts the user to enter the ticker
symbol, the end date, and the number of days for the moving average calculation.
Note: The x-day moving average is calculated based on the last x days the stock market was open.
Note: If there are not enough data points available for the specified x days from the end date
(meaning there is less historical data available for the stock), the calculation uses all the
available closing prices up to the earliest available date.

--Calculate x-Day Crossovers for a Stock--
The application can identify days when the stock's closing price was greater than the x-day moving
average, signaling potential buy opportunities. Users are asked to input the ticker symbol,
the end date, and the number of days for the crossover calculation.
Note: The x-day crossover is based on the last x days the stock market was open.
Note: If there are not enough data points available for the specified x days from the end date
(meaning there is less historical data available for the stock), the calculation uses all the
available data points up to the earliest available date.

--Manage Portfolios--
Users can create, delete, rename, and edit investment portfolios. When creating a new portfolio,
users must provide a unique name. When editing a portfolio, users can add or remove stocks by
specifying the ticker symbol and the number of shares (whole numbers only, fractional shares are
not supported).
Note: Inputting a unique ticker (a stock that is not already in a portfolio) when using 'Add Stock'
will add a new stock with the inputted amount of shares in the portfolio. If the stock already
exists in the portfolio, the input amount of shares will be added to the existing amount of shares.
Note: When renaming a portfolio, all its contents (stocks and # of shares) are kept

--Calculate Portfolio Value--
Users can calculate the total value of their investment portfolio on a specific date based on the
stock prices on that date. The application uses the closing price for these calculations. Users are
prompted to enter the portfolio name and the date for which they want to calculate the portfolio
value.
Note: If the closing price on the inputted date cannot be found, then the next earliest available
closing price is used instead. If no other earlier closing price can be found, then 0 is assumed.

--Configuration--
The application is currently configured to use the AlphaVantage API to retrieve stock data, with
our own free key of the API being used. Keep in mind there is a limit of 25 API calls per day with
this key.

This configuration is set in the main function of the BasicStockController class and can be
changed by altering the constructor of the model. Another data source that has been implemented
is to directly support CSV files. This works by replacing this line in stock program:
    ${StockModel model = new BasicStockModel(new AlphaVantageDataSource());}
with this line:
    ${StockModel model = new BasicStockModel(new CSVDataSource("res/CSVData"));}

This secondary data source will instead retrieve data from CSV files on the user's local computer.
All stock data using this method must be stored in the res/CSVData folder as .csv files, and named
in the following format:
    [Ticker Symbol in all UPPERCASE LETTERS].csv
        IE: "AAPL.csv" for Apple Inc. "AMZN.csv" for Amazon.com Inc
The CSV files themselves should have the first row as:
    "timestamp,open,high,low,close,volume"
and each subsequent row should contain the corresponding data values in the same order.
Note: The first row must be formatted exactly as specified above, and each row must have all
fields present with no extra or missing fields.

--Share Purchasing Restrictions--
One important restriction is that the program does not allow users to purchase fractional shares.
This design choice reflects the real-world limitation where most brokers do not support fractional
share purchases. Users can only buy whole numbers of shares, ensuring that the simulation remains
realistic.

--Valid Stocks--
The list of all stocks that this program supports is listed in 'res/stocksData/tickerList.txt'.
This list was pulled from the AlphaVantage API on 06/03/2024.