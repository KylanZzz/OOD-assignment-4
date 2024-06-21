***Stock Program***


Features:
--IMPORTANT--
All input (Ticker symbols, portfolio names, etc.) are CASE INSENSITIVE!
A tag such as [GUI-ONLY] indicates that a section of the readme is ONLY applicable the GUI portion
of the program, and [TUI-ONLY] indicates that it is ONLY application to the text-based interface.

--TUI/GUI choice--
In our program, it is possible to run the program either using a text-based interface (TUI) or a
graphical user interface (GUI). The default is the GUI; in order to select the TUI, run the program
jar file with the '--text' flag. The TUI has several more functions than the GUI (IE: Rebalancing
portfolios, viewing the performance graph)


[TUI-ONLY]
--Examine Gain/Loss For a Stock--
The application allows users to calculate the gain or loss of a stock over a specified period based
on its closing prices. All calculations use the adjusted closing price. Users are prompted to input
the ticker symbol of the stock and the start and end dates for the calculation.
Note: If the start date has no data, but data is available for some later date up to and including
the end date, the calculation will begin from the first available date.
Note: If both the start date and the end date have data, the gain or loss is calculated over this
period.
Note: If neither the start date nor the end date have data, but data is available in between,
the program will return 0 because the end date is required for comparison.


[TUI-ONLY]
--Calculate x-Day Moving Average of a Stock--
Users can calculate the x-day moving average of a stock, which is the average of the closing prices
over the last x days from a specified date. The application prompts the user to enter the ticker
symbol, the end date, and the number of days for the moving average calculation.
Note: The x-day moving average is calculated based on the last x days the stock market was open.
Note: If there are not enough data points available for the specified x days from the end date
(meaning there is less historical data available for the stock), the calculation uses all the
available closing prices up to the earliest available date.


[TUI-ONLY]
--Calculate x-Day Crossovers for a Stock--
The application can identify days when the stock's closing price was greater than the x-day moving
average, signaling potential buy opportunities. Users are asked to input the ticker symbol,
the end date, and the number of days for the crossover calculation.
Note: The x-day crossover is based on the last x days the stock market was open.
Note: If there are not enough data points available for the specified x days from the end date
(meaning there is less historical data available for the stock), the calculation uses all the
available data points up to the earliest available date.


--Manage Portfolio--
Users can create, delete, rename, edit portfolios, etc. When creating a new portfolio,
users must provide a unique name. When editing a portfolio, users can add or remove stocks by
specifying the ticker symbol and the number of shares (whole numbers only, fractional shares are
not supported directly through buying/selling, but can be achieved through rebalancing).
Note: When renaming a portfolio, all its contents (stocks and # of shares) are kept


--Buying Stock--
Users can buy stock to a portfolio. This is similar to adding stock in the previous Basic stock
program, but with the added benefit that users can now specify a date of the purchase. This
transaction is time-independent, meaning the user can buy stock at any time, whenever they want.
They can choose to buy a stock after all previous transactions, after all other transactions, or
even in between transactions (in terms of date). The portfolio composition and value will be
updated accordingly.


--Selling Stock--
Users can sell stock from a portfolio. Similar to removing stock in the previous Basic Stock Program,
but like the buy transaction, users can also specify a date. This transaction is also time-
independent, the meaning of which is described above. However, it will not allow users to sell
a stock if there is not enough shares of that stock in the portfolio at the time of selling (or the
stock simply doesn't exist in the portfolio). After selling all shares of a stock (shares reaches 0)
it will no longer be shown in the portfolio composition until it is bought again.


[TUI-ONLY]
--Rebalance--
Users can also choose to rebalance a portfolio. This means buying shares of some stocks and selling
shares of others so that the proportion of value of each stock matches a user-inputted proportion.
For example, if a portfolio started with 10 shares of stock A worth $5 each ($50 total), and 10
shares of stock B worth $10 each ($100), rebalancing the portfolio to 50% stock A and 50% stock B
would mean selling $25 of stock B (100 - 25 = 75 now) and buying $25 of stock A (50 + 25 = 75 now).
This transaction is also time-independent, meaning it can be called at any time, on any date. Keep
in mind that rebalancing will only allow you to give proportions to stocks that are valid on the date
of rebalancing. For example, if in the previous example there was a Stock C that was bought after
the date of rebalancing was called, it would NOT be included in the proportions for rebalancing. This
is to ensure consistency.
Note: Rebalancing does NOT change the value of a portfolio; the value of a portfolio before and after
rebalancing should and will remain the same.
Note: This change allows for fractional stocks in a portfolio, as sometimes rebalancing will not
lead to whole numbers.


--Composition of Portfolio--
Users can find the composition of the portfolio (all the stocks and the number of shares of each
stock) at a specific date. This will IGNORE all transactions (buy, sell, rebalance) AFTER the
specified date.


[TUI-ONLY]
--Calculate Portfolio Distribution--
Users can calculate the value of each stock (shares * value) at a date. The value of each stock at
any given date should be equivalent to the total value of the portfolio.


--Calculate Portfolio Value--
Users can calculate the total value of their investment portfolio on a specific date based on the
stock prices on that date. The application uses the closing price for these calculations. Users are
prompted to enter the portfolio name and the date for which they want to calculate the portfolio
value.
Note: If the closing price on the inputted date cannot be found, then the next earliest available
closing price is used instead. If no other earlier closing price can be found, then 0 is assumed.


[TUI-ONLY]
--Calculate Portfolio Performance--
Users can calculate the performance of a portfolio between two dates. The performance of a portfolio
is simply the value of the portfolio at various times. This performance is displayed on a graph
of portfolio value versus time (date). Keep in mind that this should only be performed between two
dates where NO TRANSACTIONS have occurred. This is because transactions such as buying and selling
would affect the actual value of the portfolio but not the performance (IE: increasing shares
would make it seem like the portfolio value has increased, but is not reflective of high performance
because that increase may only be a result of the increased shares that were bought)


--Saving a Portfolio--
Users can also save a portfolio to disk. This is done by logging all transactions, then writing to
a .txt file. The name of the file is saved as such:
    [name of portfolio]_[year]-[month]-[day]T[hour]-[minutes]-[seconds].txt
where the date/time used is the date/time that the save was created. This naming is AUTOMATICALLY
DONE and CANNOT BE CHANGED.
If you would like to create your own saves, use this format:
    [name of portfolio]_[your own custom identifier].txt
Only save files that start with the name of the portfolio followed by an underscore '_' will be
recognized as a valid save. Furthermore, each line in a save file represents a transaction that was
made to the portfolio; The order of these lines doesn't matter (you can put the earliest transaction
at the top, bottom, or anywhere in between). Here is how to format each transaction in a save file:

    BUY:MM/DD/YYYY,[shares],[ticker]
            IE: BUY:04/20/2005,200.0,AAPL

    SELL:MM/DD/YYYY,[shares],[ticker]
            IE: SELL:04/20/2005,100.0,AAPL

    REBALANCE:MM/DD/YYYY,[ticker1]=>[price1];[ticker2]=>[price2],[ticker1]=>[proportion1],[ticker2]=>[proportion2]
        Where ticker#=>price# is the ticker of a stock and the price of that stock at the given date
        and ticker#=>proportion# is the relative value proportion of a stock to rebalance to.
            IE: REBALANCE:04/21/2005,AAPL=>1.1213207210798;AMZN=>1.6835,AAPL=>0.5;AMZN=>0.5


[TUI-ONLY]
--Loading a Portfolio Text-based--
After creating a save (whether through the program or manually written), users can choose to load
a portfolio save into an existing portfolio. The contents of a save can only be loaded into a
portfolio with the same name as the save (case insensitive). For example, if I created a save with a
portfolio named S&P500, restarted the program, I would have to create a portfolio named S&P500 again
in order to load the save that I created before. The reason this was implemented this way was to
safeguard against accidental data corruption from the users side. While it may seem inconvenient at
first, designing loading this way ensures that all saves correspond to the correct portfolio and
no portfolio can be "crossed" with another.


[GUI-ONLY]
--Loading a Portfolio GUI--
Loading contents into the GUI has been changed. Users may pick ANY file with the format following
naming format:
    [portfolio name]_[custom identifier (can be anything non-empty)]
When the file is loaded, if the portfolio with that name already exists, it's data be OVERWRITTEN.
If a portfolio with that name does NOT exist, then it will be CREATED.


--Configuration--
The application is currently configured to use the AlphaVantage API to retrieve stock data, with
the unlimited key that was provided to us by the professor.

A secondary data source can be configured so that the program will instead retrieve data from CSV
files on the user's local computer. All stock data using this method must be stored in the
res/CSVData folder as .csv files, and named in the following format:
    [Ticker Symbol in all UPPERCASE LETTERS].csv
        IE: "AAPL.csv" for Apple Inc. "AMZN.csv" for Amazon.com Inc
The CSV files themselves should have the first row as:
    "timestamp,open,high,low,close,volume"
and each subsequent row should contain the corresponding data values in the same order.
Note: The first row must be formatted exactly as specified above, and each row must have all
fields present with no extra or missing fields.


[GUI-ONLY]
--GUI--
All the features in it have the same mechanism as the text-based one.

The GUI has its own controller called FeaturesStockController.java. This GUI will first lead the
user to create a new portfolio or let the user upload their own txt file. The way it uploads the file
is through user's computer.

Once the user clicks in to the edit, all the features will show in the other GUI window. There are
five text fields that allow the user to input the shares, ticker, month, day, and year respectively.
After the information been given from the user, they can choose the button that they want
to manipulate. They can either buy stock, sell stock, get composition, or get value of this
portfolio. At any point of their operation, user are able to save the portfolio with the button
that's at the bottom of the window.

Note: Get composition and Get value wouldn't be affected by the shares and ticker that are currently
in the text fields.
