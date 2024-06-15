***Program Instructions***


--Setup--
Before you can run this program, there MUST be a res folder with this file structure in the
same directory as assignment-4.jar:
                        |--assignment-4.jar
                        |--res/
                            |--APIData/
                            |--portfolio/
                            |--stocksData/
                                |--tickersList.txt
                            ...

Note: This should be the default as soon as you unzipped the files, but if any of the files/folders
are missing, please create the directories and .txt file with those EXACT names. Then, copy the
contents of tickerList.txt from:
    https://github.com/KylanZzz/OOD-assignment-4/blob/main/res/stocksData/tickerList.txt


--Running the Program--

This section contains instructions on running the Stocks Program, detailing an example of how to
create a portfolio, add three unique stocks to it at three distinct dates, then query the value of
the portfolio at two separate dates.

1. Run the program
    $java -jar assignment-4.jar
2. Select manage portfolios
    $4
3. Create the portfolio
    $1
    $S&P500
4. Edit the portfolio
    $4
5. Add first stock
    $1
    $AAPL
    $100
    $04/20/2024
6. Add second stock
    $1
    $AMZN
    $200
    $04/22/2024
7. Add third stock
    $1
    $NFLX
    $250
    $04/24/2024
8. Calculate value of portfolio at first date
    $3
    $04/25/2024
9. Calculate value of portfolio at second date
    $3
    $04/27/2024
10. Exit application
    $EXIT
    $EXIT
    $EXIT

--Valid Stocks--
The list of all stocks that this program supports is listed in 'res/stocksData/tickerList.txt'.
This list was pulled from the AlphaVantage API on 06/03/2024. The dates that our program supports
is all the valid stock data dates that are available on the AlphaVantage API. The program will work
for other dates as well, but will simply return 0.