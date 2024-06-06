***Program Instructions***


--Setup--
Before you can run this program, there MUST be a res folder with this file structure in the
same directory as assignment-4.jar:
                        |--assignment-4.jar
                        |--res/
                            |--APIData/
                            |--CSVData/
                            |--stocksData/
                                |--tickersList.txt
                            ...

Note: This should be the default as soon as you unzipped the files, but if any of the files/folders
are missing, please create the directories and .txt file with those EXACT names. Then, copy the
contents of tickerList.txt from:

https://github.com/KylanZzz/OOD-assignment-4/blob/main/res/stocksData/tickerList.txt


--Running the Program--

This section contains instructions on running the Stocks Program, detailing examples of creating a
portfolio with three different stocks and a second portfolio with two different stocks, as well as
querying their values on a specific date.

1. Run the program
    $java -jar assignment-4.jar
2. Select manage portfolios
    $4
3. Create the first portfolio
    $1
    $S&P500
4. Edit the first portfolio
    $4
4. Add first stock to the first portfolio
    $2
    $AAPL
    $17
5. Add second stock to the first portfolio
    $2
    $AMZN
    $28
6. Add third stock to the first portfolio
    $2
    $GOOG
    $3
7. Calculate the value of the first portfolio on 01/20/2023
    $1
    $01/20/2023
8. Return to the portfolio manager
    $EXIT
9. Create a second portfolio
    $1
    $NASDAQ
10. Edit the second portfolio
    $4
11. Add first stock to the second portfolio
    $2
    $NFLX
    $50
11. Add second stock to the second portfolio
    $2
    $NVDA
    $200
12. Calculate value of second portfolio on 04/20/2013
    $1
    $04/20/2013
13. Exit the portfolio editor
    $EXIT
14. Exit the portfolio manager
    $EXIT
15. Exit the application
    $EXIT

--Valid Stocks--
The list of all stocks that this program supports is listed in 'res/stocksData/tickerList.txt'.
This list was pulled from the AlphaVantage API on 06/03/2024. The dates that our program supports
is all the valid stock data dates that are available on the AlphaVantage API. The program will work
for other dates as well, but will simply return 0.