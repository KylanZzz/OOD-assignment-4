***Design***

--Overview--
The Stock Program is designed using the Model-View-Controller (MVC) architecture. This design
separates the application into three independent components which allows for modularity and
extensibility.

--Model--
At the highest level, a stock model performs tasks such as retrieving stock data, calculating
gain/loss, moving averages, and crossovers, which is defined by the StockModel interface.

The BasicStockModel class implements the StockModel interface and provides simple implementations
for these functionalities. It interacts with data sources to fetch stock data and perform
calculations.

The BasicModelModel constructor takes in a DataSource, which is our representation of a data stream
for stock data. Currently, there are two data sources implemented:
- AlphaVantageDataSource: This implementation uses the AlphaVantage API to retrieve real-time and
historical stock data. It requires an API key for access and retrieves data over the internet.
- CSVDataSource: This implementation reads stock data from CSV files stored locally on the user's
computer. The CSV files must be placed in the res/CSVData directory and named according to the
stock ticker symbol (e.g., AAPL.csv for Apple Inc.). There is no example CSV data shown; the user
must download and get this data on their own.

--Controller--
The controller performs two main tasks: Handling user input and orchestrating information and logic
flow between the model and view.

The BasicStockController implements the StockController interface and has a single function: run().
This is the starting point of the program and must be called in order for the program to begin.
It also utilizes the Command design interface, which defines certain Commands that the controller
calls upon receiving user input.

The Command class is an abstract class that defines the apply() method, which is implemented by
various concrete command classes. Each concrete command class (e.g., CalculateGain,
CalculateAverage, CalculateCrossover, ViewPortfolios) handles a specific user request. The reason
an abstract class was used instead of an interface here is because there are several helper methods
that all commands are able to use (mainly for parsing input) that are placed in the parent Command
class.

There are several commands that also act as "menus", which keep looping until a specific user input
is detected ("EXIT" in this case).

--View--
The view is in charge of displaying data to the user.

The BasicStockView implements the StockView interface and displays this data by printing text to
the terminal. It simply receives input from the controller and then displays that information,
 formatted in a visually appealing way.

--Testing--
The Model was tested...

The Controller was unit tested by mocking both the model and view simultaneously.
MockModel simulates the behavior of the StockModel interface and logs method calls, which allows
us to verify interactions without needing actual stock data. MockView simulates the StockView
interface and logs messages sent to the view. These mocks help isolate the controller's behavior,
making sure that it handles user input properly, relays information from the model to the view
correctly, and handles exceptions from the model accordingly.

The View was tested straightforwardly by measuring it's actual output with the expected output.

We also wrote an integration test for our Stock Program at
test/stock/controller/BasicStockControllerIntegrationTest
This test simulates user interactions with the stock program, including user inputs and navigation
through the application. It tests the entire MVC architecture, making real API calls and generating
actual output. This ensures that all components work together seamlessly and the program functions
as intended.












