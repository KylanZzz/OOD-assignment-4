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

In order to run the program in GUI mode:

        $java -jar assignment-4.jar

In order to run the program in TUI mode:

        $java -jar assignment-4.jar --text


--Valid Stocks--
The list of all stocks that this program supports is listed in 'res/stocksData/tickerList.txt'.
This list was pulled from the AlphaVantage API on 06/03/2024. The dates that our program supports
is all the valid stock data dates that are available on the AlphaVantage API. The program will work
for other dates as well, but will simply return 0.